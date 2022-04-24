package net.criticalaction.pojes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class EventDispatch {

    private static final Logger logger = Logger.getLogger(EventDispatch.class.getName());

    Map<Class<?>, Map<Object, List<Method>>> bindings = new HashMap<>();

    public void registerListener(Object object) {
        /*
        for (Method method : eventReceiver.getClass().getMethods()) {
            var annotation = method.getAnnotation(EventReceiver.class);

            if (annotation == null) {
                continue;
            }

        }
         */
    }

    public <T extends IEvent> T fireEvent(T event) {
        var handlers = bindings.get(event.getClass());
        // No handlers
        if (handlers == null) {
            logger.fine("Event \"" +  event.getClass().getSimpleName() + "\" fired with no handlers");
            return event;
        }
        /*
        for (EventReceiver handler : handlers) {
            handler.receive(event);
        }

         */
        return event;
    }

    public void unregisterListener(Object object) {

    }

    public void clearListeners() {

    }

}
