package net.malfact.gamecore.api.event;

import net.kyori.adventure.text.ComponentLike;
import net.malfact.gamecore.api.LuaApi;
import org.bukkit.event.Event;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

abstract class EventMethod {

    static EventMethod of(Method method) {
        return new SingleMethod(method);
    }

    static EventMethod of(EventMethod... methods) {
        return methods.length == 1
            ? methods[0]
            : new OverrideMethod(methods);
    }

    public final String name;

    protected EventMethod(String name) {
        this.name = name;
    }

    protected abstract Object invokeMethod(Event instance, Object[] args);

    protected int argCount() {
        return -1;
    }

    protected int score(Object[] args) {
        return -1;
    }

    private static final class SingleMethod extends EventMethod {

        private final Method method;
        private final int argCount;
        private final Class<?>[] argTypes;

        private SingleMethod(Method method) {
            super(method.getName());
            this.method = method;
            this.argCount = method.getParameterCount();
            this.argTypes = method.getParameterTypes();
        }

        private Object[] fitArgs(Object[] args) {
            if (args.length <= argCount)
                return args;

            return Arrays.copyOfRange(args, 0, argCount);
        }

        @Override
        protected int score(Object[] args) {
            // Score #Args first
            // Then pick based on args

            int len = args.length;
            int score = 0;
            // If #Args == #Params --> +1
            if (len == argCount)
                score++;
            // +min(#Arg,#Params)
            if (len <= argCount)
                score += Math.min(len, argCount);

            // For each Arg[i]: If typeOf(Arg[i]) == typeOf(Param[i]) --> +1
            for (int i = 0; i < Math.min(args.length, argCount); i++) {
                if (args[i] == null)
                    continue;

                if (argTypes[i].isInstance(args[i]))
                    score++;
            }

            return score;
        }

        @Override
        protected Object invokeMethod(Event instance, Object[] args) {
            // Account for components >:(
            for (int i = 0; i < Math.min(args.length, argCount); i++) {
                if (args[i] == null || !ComponentLike.class.isAssignableFrom(argTypes[i])) {
                    if (args[i] instanceof String str)
                        args[i] = LuaApi.toComponent(str);
                }
            }
            try {
                return method.invoke(instance, fitArgs(args));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final class OverrideMethod extends EventMethod {

        private final EventMethod[] methods;

        private OverrideMethod(EventMethod[] methods) {
            super(methods[0].name);
            // Sort Methods by parameter count
            Arrays.sort(methods, Comparator.comparingInt(EventMethod::argCount));
            this.methods = methods;
        }

        @Override
        protected Object invokeMethod(Event instance, Object[] args) {
            int score = -1;
            EventMethod best = null;
            for (var method : methods) {
                int s = method.score(args);
                if (s <= score)
                    continue;

                score = s;
                best = method;
            }

            return best.invokeMethod(instance, args);
        }
    }
}
