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
        dispatch.clearListeners();

        var bindings = (Map<Class<?>, Map<Object, List<Method>>>) bindingField.get(dispatch);
        assert(bindings.isEmpty());
    }
}
