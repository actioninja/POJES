package net.criticalaction.pojes.test;


import net.criticalaction.pojes.EventDispatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventTests {
    static EventDispatch dispatch = new EventDispatch();
    static Field bindingField;

    @BeforeAll
    static void makePrivateFieldAccessible() {
        try {
            bindingField = EventDispatch.class.getDeclaredField("bindings");
        } catch (Exception e) {
            System.out.println(e);
        }

        bindingField.setAccessible(true);
    }

    @BeforeEach
    void clearDispatch() {
        dispatch.clearListeners();
    }

    @Test
    @DisplayName("Clearing dispatch should result in noop")
    void clearDispatchTest() throws IllegalAccessException {
        var listener = new TestReceiver();
        dispatch.registerListener(listener);

        dispatch.clearListeners();

        @SuppressWarnings("unchecked")
        var bindings = (Map<Class<?>, Map<Object, List<Method>>>) bindingField.get(dispatch);
        assertTrue(bindings.isEmpty());
    }

    @Test
    @DisplayName("Single receiver should receive the dispatched event")
    void eventDispatchTest() {
        var listener = new TestReceiver();
        dispatch.registerListener(listener);

        dispatch.fireEvent(new TestEvent());
        assertEquals(1, listener.count);
    }

    @Test
    @DisplayName("Multiple receiver should receive the dispatched event")
    void eventsDispatchTest() {
        var listener1 = new TestReceiver();
        var listener2 = new TestReceiver();

        dispatch.registerListener(listener1);
        dispatch.registerListener(listener2);

        var event = new TestEvent();
        dispatch.fireEvent(event);

        assertEquals(2, event.count);
    }

    @Test
    @DisplayName("Unregistering a receiver should remove only the one register")
    void eventUnregisterTest() {
        var listener1 = new TestReceiver();
        var listener2 = new TestReceiver();

        dispatch.registerListener(listener1);
        dispatch.registerListener(listener2);

        dispatch.unregisterListener(listener2);

        dispatch.fireEvent(new TestEvent());

        assertEquals(1, listener1.count);
        assertEquals(0, listener2.count);
    }
}
