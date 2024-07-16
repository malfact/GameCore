package net.malfact.gamecore.lua.event.handler;

import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.lua.event.EventClass;
import net.malfact.gamecore.lua.event.EventUserdata;
import org.bukkit.event.Event;
import org.luaj.vm2.LuaValue;

public class EventHandler implements TypeHandler<Event> {

    @Override
    public LuaValue getUserdataOf(Event object, Game instance) {
        return new EventUserdata(instance, EventClass.of(object.getClass()), object);
    }
}
