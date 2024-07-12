package net.malfact.gamecore.lua.event.handler;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.event.GameEvent;
import net.malfact.gamecore.event.player.PlayerConnectEvent;
import net.malfact.gamecore.event.player.PlayerDisconnectEvent;
import net.malfact.gamecore.event.player.PlayerGameEvent;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.luaj.vm2.*;

public final class GameEventHandler implements TypeHandler<GameEvent> {

    private final LuaFunction func_index = LuaUtil.toFunction(this::onIndex);
    private final LuaFunction func_newindex = LuaUtil.toFunction(this::onNewIndex);
    private final LuaFunction func_toString = LuaUtil.toFunction(this::onToString);

    @Override
    public LuaValue getUserdataOf(GameEvent event) {
        if (event instanceof PlayerGameEvent) {
            LuaTable meta = new LuaTable();
            meta.set(LuaConstant.MetaTag.INDEX,     func_index);
            meta.set(LuaConstant.MetaTag.NEWINDEX,  func_newindex);
            meta.set(LuaConstant.MetaTag.TOSTRING,  func_toString);

            return new LuaUserdata(event, meta);
        }

        return LuaConstant.NIL;
    }

    private LuaValue onIndex(LuaValue self, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        PlayerGameEvent event = self.checkuserdata(PlayerGameEvent.class);

        switch (key.tojstring()) {
            case "name":
                return LuaApi.valueOf(event.getEventName());
            case "player":
                return LuaApi.userdataOf(event.getPlayer());
            case "spawnLocation":
                if (event instanceof PlayerConnectEvent e)
                    return LuaApi.userdataOf(e.getSpawnLocation());
            case "quitReason":
                if (event instanceof PlayerDisconnectEvent e)
                    return LuaApi.userdataOf(e.getQuitReason());
        }

        return LuaConstant.NIL;
    }

    private void onNewIndex(LuaValue self, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return;

        PlayerGameEvent event = self.checkuserdata(PlayerGameEvent.class);

        if (key.tojstring().equals("spawnLocation") && event instanceof PlayerConnectEvent e) {
            e.setSpawnLocation(value.checkuserdata(Location.class));
        }
    }

    private LuaValue onToString(LuaValue self) {
        return LuaValue.valueOf(self.checkuserdata(Event.class).getEventName());
    }
}
