package net.malfact.gamecore.lua.event;

import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.game.Game;
import org.bukkit.event.Event;
import org.luaj.vm2.LuaValue;

public class EventHandler implements TypeHandler<Event> {

    @Override
    public Class<Event> getTypeClass() {
        return Event.class;
    }

    @Override
    public LuaValue getUserdataOf(Event object, Game instance) {
        return new EventUserdata(instance, EventClass.of(object.getClass()), object);
    }
}
