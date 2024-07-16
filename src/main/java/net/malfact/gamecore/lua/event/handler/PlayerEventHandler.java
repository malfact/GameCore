package net.malfact.gamecore.lua.event.handler;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.player.PlayerProxy;
import net.malfact.gamecore.lua.event.EventClass;
import net.malfact.gamecore.lua.event.EventMethod;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.luaj.vm2.*;

public class PlayerEventHandler implements TypeHandler<PlayerEvent> {

    private final LuaFunction func_index = LuaUtil.toFunction(this::onIndex);
    private final LuaFunction func_newindex = LuaUtil.toFunction(this::onNewIndex);
    private final LuaFunction func_toString = LuaUtil.toFunction(this::onToString);

    @Override
    public LuaValue getUserdataOf(PlayerEvent event) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX,     func_index);
        meta.set(LuaConstant.MetaTag.NEWINDEX,  func_newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING,  func_toString);
        return new LuaUserdata(event, meta);
    }

    private LuaValue onIndex(LuaValue self, LuaValue key) {
        if (!key.isstring())
            return LuaConstant.NIL;

        PlayerEvent event = self.checkuserdata(PlayerEvent.class);
        PlayerProxy player = GameCore.gameManager().getPlayerProxy(event.getPlayer().getUniqueId());

        String stringKey = key.tojstring();
        if (stringKey.equals("player"))
            return LuaApi.userdataOf(player);

        if (stringKey.equals("canceled") && event instanceof Cancellable cancellable)
            return LuaApi.valueOf(cancellable.isCancelled());

        EventClass eventClass = EventClass.of(event.getClass());
        EventMethod method = eventClass.getMethod(stringKey);
        return method != null ? getFunction(method, event, player.getGame()) : LuaConstant.NIL;
    }

    private void onNewIndex(LuaValue self, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return;

        PlayerEvent event = self.checkuserdata(PlayerEvent.class);

        String stringKey = key.tojstring();
        if (stringKey.equals("canceled") && event instanceof Cancellable cancellable)
            cancellable.setCancelled(value.checkboolean());
    }

    private LuaValue onToString(LuaValue self) {
        return LuaValue.valueOf(self.checkuserdata(Event.class).getEventName());
    }

    private LuaFunction getFunction(EventMethod method, PlayerEvent event, Game instance) {
        return LuaUtil.toVarargFunction(args -> {
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

            return LuaApi.userdataOf(method.invokeMethod(event, objArgs), instance);
        });
    }
}
