package net.malfact.gamecore.api.event;

import net.malfact.gamecore.script.Instance;
import org.bukkit.event.Event;
import org.luaj.vm2.*;

public class EventUserdata extends LuaUserdata {

    public static LuaValue of(Event event, Instance instance) {
        return new EventUserdata(event, EventClass.of(event.getClass()), instance);
    }

    private final EventClass eventClass;
    private final Instance instance;

    private EventUserdata(Event event, EventClass eventClass, Instance instance) {
        super(event);
        this.eventClass = eventClass;
        this.instance = instance;
    }

    @Override
    public String tojstring() {
        return eventClass.rawClass.getSimpleName();
    }

    @Override
    public LuaValue get(LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        EventMethod method = eventClass.getMethod(key.tojstring());

        if (method == null)
            return LuaConstant.NIL;

        return new InstancedMethod(instance, method);
    }

    @Override
    public void set(LuaValue key, LuaValue value) {}

    private static class InstancedMethod extends LuaFunction {
        private final Instance instance;
        private final EventMethod method;

        private InstancedMethod(Instance instance, EventMethod method) {
            this.instance = instance;
            this.method = method;
        }

        @Override
        public LuaValue call() {
            return error("Attempt to call " + method.name + " on nil");
        }

        @Override
        public LuaValue call(LuaValue arg) {
            return invoke(varargsOf(arg, LuaConstant.NONE)).arg1();
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return invoke(varargsOf(arg1, arg2)).arg1();
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            return invoke(varargsOf(arg1, arg2, arg3)).arg1();
        }

        @Override
        public Varargs invoke(Varargs args) {
            Event event = args.arg(1).checkuserdata(Event.class);
            args = args.subargs(2);
            Object[] objArgs = new Object[args.narg()];

            for (int i = 1; i <= args.narg(); i++) {
                objArgs[i-1] = switch (args.arg(i)) {
                    case LuaString arg -> arg.tojstring();
                    case LuaBoolean arg -> arg.toboolean();
                    case LuaInteger arg -> arg.toint();
                    case LuaDouble arg -> arg.todouble();
                    case LuaUserdata arg -> arg.touserdata();
                    default -> null;
                };
            }

            return instance.getValueOf(method.invokeMethod(event, objArgs));
        }
    }
}
