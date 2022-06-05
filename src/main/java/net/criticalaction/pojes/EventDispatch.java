package net.criticalaction.pojes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class EventDispatch {

    private static final Logger logger = Logger.getLogger(EventDispatch.class.getName());

    Map<Class<?>, Map<Object, List<Method>>> bindings = new HashMap<>();

    public void registerListener(Object object) {
        var objectMethods = object.getClass().getDeclaredMethods();
        for (Method method : objectMethods) {
            var annotation = method.getAnnotation(EventReceiver.class);
            var invalid = false;

            if (annotation == null) {
                continue;
            }

            if(!Modifier.isPublic(method.getModifiers())) {
                logger.warning("Method \"" + method.getName() + "\" needs to be public to be a valid reciever");
                invalid = true;
            }

            if(method.getReturnType() != void.class) {
                logger.warning("Method \"" + method.getName() + "\" needs to return void to be a valid receiver");
                invalid = true;
            }

            if (method.getParameterCount() != 1) {
                logger.warning("Method \"" + method.getName() + "\" needs to have exactly one parameter that implements IEvent to be a valid receiver");
                invalid = true;
            }

            var param = method.getParameters()[0];

            if (!IEvent.class.isAssignableFrom(param.getType())) {
                logger.warning("Method \"" + method.getName() + "\"'s parameter needs to implement IEvent to be a valid receiver");
                invalid = true;
            }

            if (invalid) {
                continue;
            }

            var classOfParam = param.getType();

            var classMap = bindings.computeIfAbsent(classOfParam, s -> new HashMap<>());

            var objectList = classMap.computeIfAbsent(object, s -> new ArrayList<>());

            if(objectList.contains(method)) {
                logger.warning("Method \"" + method.getName() + "\" already is registered.");
                continue;
            }

            objectList.add(method);
        }
    }

    public <T extends IEvent> T fireEvent(T event) {
        var objMap = bindings.get(event.getClass());
        // No handlers
        if (objMap == null) {
            logger.fine("Event \"" +  event.getClass().getSimpleName() + "\" fired with no handlers");
            return event;
        }

        for(Map.Entry<Object, List<Method>> entry : objMap.entrySet()) {
            for(Method method : entry.getValue()) {
                try {
                    method.invoke(entry.getKey(), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return event;
    }

    public void unregisterListener(Object object) {
        for (Map<Object, List<Method>> map : bindings.values()) {
            map.remove(object);
        }
    }

    public void clearListeners() {
        bindings = new HashMap<>();
    }

}
