package net.malfact.gamecore.api.event;

import org.bukkit.event.Event;

import java.lang.reflect.Modifier;
import java.util.*;

final class EventClass {

    private static final Map<Class<? extends Event>, EventClass> EVENT_CLASSES = new HashMap<>();

    static EventClass of(Class<? extends Event> clazz) {
        return EVENT_CLASSES.computeIfAbsent(clazz, k -> new EventClass(clazz));
    }

    public final Class<? extends Event> rawClass;
    private Map<String, EventMethod> classMethods;

    private EventClass(Class<? extends Event> eventClazz) {
        this.rawClass = eventClazz;
    }

    private static final String[] IGNORED_METHODS = {
        "getHandlers", "callEvent",
        "toString", "getClass", "hashCode", "equals", "clone", "notify", "notifyAll", "wait", "finalize"
    };

    EventMethod getMethod(String key) {
        if (classMethods == null) {
            Map<String, List<EventMethod>> methodLists = new HashMap<>();

            var methods = rawClass.getMethods();
            for (var method : methods) {
                // Ignore Static methods and Deprecated Methods
                if (Modifier.isStatic(method.getModifiers()) || method.getAnnotation(Deprecated.class) != null)
                    continue;

                String name = method.getName();

                // Ignore Methods
                if (Arrays.asList(IGNORED_METHODS).contains(name))
                    continue;

                List<EventMethod> eventMethods = methodLists.computeIfAbsent(name, k -> new ArrayList<>());
                eventMethods.add(EventMethod.of(method));
            }

            classMethods = new HashMap<>();

            for (var entry : methodLists.entrySet()) {
                List<EventMethod> list = entry.getValue();
                if (list.isEmpty())
                    continue;

                classMethods.put(entry.getKey(), EventMethod.of(list.toArray(new EventMethod[0])));
            }
        }

        return classMethods.get(key);
    }

}
