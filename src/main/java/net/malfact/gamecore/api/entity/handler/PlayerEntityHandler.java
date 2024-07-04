package net.malfact.gamecore.api.entity.handler;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.script.Instance;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class PlayerEntityHandler extends EntityHandler {

    @Override
    public boolean acceptsEntity(Entity entity) {
        return (entity instanceof Player);
    }

    @Override
    public LuaValue onIndex(Instance instance, Entity entity, LuaValue key) {
        if (!key.isstring())
            return NIL;

        Player player = checkEntity(instance, entity, Player.class);

        return switch (key.tojstring()) {
            // Get & Set
            case "displayName" ->       LuaValue.valueOf(LuaApi.fromComponent(player.displayName()));
            case "playerListName" ->    LuaValue.valueOf(LuaApi.fromComponent(player.playerListName()));
            case "playerListHeader" ->  LuaValue.valueOf(LuaApi.fromComponent(player.playerListHeader()));
            case "playerListFooter" ->  LuaValue.valueOf(LuaApi.fromComponent(player.playerListFooter()));

            case "sprinting" ->         LuaValue.valueOf(player.isSprinting());
            case "sleepingIgnored" ->   LuaValue.valueOf(player.isSleepingIgnored());
            case "allowFlight" ->       LuaValue.valueOf(player.getAllowFlight());
            case "flying" ->            LuaValue.valueOf(player.isFlying());

            case "compassTarget" ->     instance.getUserdataOf(player.getCompassTarget());
            case "respawnLocation" ->   instance.getUserdataOf(player.getRespawnLocation());

            case "walkspeed" ->         LuaValue.valueOf(player.getWalkSpeed());
            case "flyspeed" ->          LuaValue.valueOf(player.getFlySpeed());
            case "level" ->             LuaValue.valueOf(player.getLevel());
            case "experience" ->        LuaValue.valueOf(player.getExp());
            case "totalExperience" ->   LuaValue.valueOf(player.getTotalExperience());
            case "gamemode" ->          LuaValue.valueOf(player.getGameMode().toString());
            case "spectating" ->        instance.getUserdataOf(player.getSpectatorTarget());

            case "wardenAnger" ->               LuaValue.valueOf(player.getWardenWarningLevel());
            case "wardenAngerTimeCooldown" ->   LuaValue.valueOf(player.getWardenWarningCooldown());
            case "wardenAngerTimeElapsed" ->    LuaValue.valueOf(player.getWardenTimeSinceLastWarning());

            // Get Only
            case "idle" ->              LuaValue.valueOf(player.getIdleDuration().toSeconds());

            // -----------------
            // From Human Entity
            // -----------------
            // Get & Set
            case "exhaustion" ->            LuaValue.valueOf(player.getExhaustion());
            case "saturation" ->            LuaValue.valueOf(player.getSaturation());
            case "foodLevel" ->             LuaValue.valueOf(player.getFoodLevel());
            case "saturatedRegenRate" ->    LuaValue.valueOf(player.getSaturatedRegenRate());
            case "unsaturatedRegenRate" ->  LuaValue.valueOf(player.getUnsaturatedRegenRate());
            case "starvationRate" ->        LuaValue.valueOf(player.getStarvationRate());
            case "openInventory" ->         instance.getUserdataOf(player.getOpenInventory());

            // Get Only
            case "name" ->                  LuaValue.valueOf(LuaApi.fromComponent(player.name()));
            case "inventory" ->             instance.getUserdataOf(player.getInventory());
            case "enderchest" ->            instance.getUserdataOf(player.getEnderChest());
            case "itemOnCursor" ->          instance.getUserdataOf(player.getItemOnCursor());
            case "deepSleep" ->             LuaValue.valueOf(player.isDeeplySleeping());
            case "sleepTicks" ->            LuaValue.valueOf(player.getSleepTicks());
            case "blocking" ->              LuaValue.valueOf(player.isBlocking());
            case "attackCooldown" ->        LuaValue.valueOf(player.getAttackCooldown());
            case "deathLocation" ->         instance.getUserdataOf(player.getLastDeathLocation());

            // Functions
            case "messageRaw" ->    new MessageRaw();
            case "message" ->       new Message();
            case "setTime" ->       new SetTime();
            case "getTime" ->       new GetTime();
            case "getTimeOffset" -> new GetTimeOffset();
            case "setWeather" ->    new SetWeather();
            case "getWeather" ->    new GetWeather();
            case "giveExp" ->       new GiveExp();
            case "giveLevels" ->    new GiveLevels();
            case "spawnParticle" -> new SpawnParticle();
            case "spawn",
                 "remove" -> null;

            default -> NIL;
        };
    }

    @Override
    public boolean onNewIndex(Instance instance, Entity entity, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return false;

        Player player = checkEntity(instance, entity, Player.class);

        switch (key.tojstring()) {
            case "displayName" ->       player.displayName(LuaApi.toComponent(value.checkjstring()));
            case "playerListName" ->    player.playerListName(LuaApi.toComponent(value.checkjstring()));
            case "playerListHeader" ->  player.sendPlayerListHeader(LuaApi.toComponent(value.checkjstring()));
            case "playerListFooter" ->  player.sendPlayerListFooter(LuaApi.toComponent(value.checkjstring()));
            case "sprinting" ->         player.setSprinting(value.checkboolean());
            case "compassTarget" ->     player.setCompassTarget(value.checkuserdata(Location.class));
            case "sleepingIgnored" ->   player.setSleepingIgnored(value.checkboolean());
            case "respawnLocation" ->   player.setRespawnLocation(value.checkuserdata(Location.class), true);
            case "level" ->             player.setLevel(value.checkint());
            case "experience" ->        player.setExp((float) value.checkdouble());
            case "totalExperience" ->   player.setTotalExperience(value.checkint());
            case "allowFlight" ->       player.setAllowFlight(value.checkboolean());
            case "flying" ->            player.setFlying(value.checkboolean());
            case "flySpeed" ->          player.setFlySpeed((float) value.checkdouble());
            case "walkSpeed" ->         player.setWalkSpeed((float) value.checkdouble());
            case "spectating" ->        player.setSpectatorTarget(value.isnil() ? null : value.checkuserdata(Entity.class));
            case "wardenAnger" ->       player.setWardenWarningLevel(value.checkint());
            case "wardenAngerTimeCooldown" ->   player.setWardenWarningCooldown(value.checkint());
            case "wardenAngerTimeElapsed" ->    player.setWardenTimeSinceLastWarning(value.checkint());
            case "gamemode" ->          player.setGameMode(LuaApi.toEnum(value.checkjstring(), GameMode.class));

            // From HumanEntity
            case "exhaustion" ->        player.setExhaustion((float) value.checkdouble());
            case "saturation" ->        player.setSaturation((float) value.checkdouble());
            case "foodLevel" ->         player.setFoodLevel(value.checkint());
            case "saturatedRegenRate" ->    player.setSaturatedRegenRate(value.checkint());
            case "unsaturatedRegenRate" ->  player.setUnsaturatedRegenRate(value.checkint());
            case "starvationRate" ->    player.setStarvationRate(value.checkint());

            default -> {return false;}
        }

        return true;
    }

    private static class MessageRaw extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue msg) {
            data.checkuserdata(Player.class).sendRawMessage(msg.tojstring());
            return LuaConstant.NIL;
        }
    }

    private static class Message extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue msg) {
            data.checkuserdata(Player.class).sendMessage(LuaApi.toComponent(msg.tojstring()));
            return LuaConstant.NIL;
        }
    }

    private static class SetTime extends ThreeArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue time) {
            return call(data, time, LuaConstant.FALSE);
        }

        @Override
        public LuaValue call(LuaValue data, LuaValue time, LuaValue relative) {
            data.checkuserdata(Player.class).setPlayerTime(time.checklong(), relative.checkboolean());
            return LuaConstant.NIL;
        }
    }

    private static class GetTime extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue data) {
            return valueOf(data.checkuserdata(Player.class).getPlayerTime());
        }
    }

    private static class GetTimeOffset extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue data) {
            return valueOf(data.checkuserdata(Player.class).getPlayerTime());
        }
    }

    private static class SetWeather extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue weather) {
            data.checkuserdata(Player.class).setPlayerWeather(LuaApi.toEnum(weather.checkjstring(), WeatherType.class));
            return LuaConstant.NIL;
        }
    }

    private static class GetWeather extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue data) {
            return valueOf(data.checkuserdata(Player.class).getPlayerWeather().toString());
        }
    }

    private static class GiveExp extends ThreeArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue value) {
            return call(data, value, LuaConstant.FALSE);
        }

        @Override
        public LuaValue call(LuaValue data, LuaValue value, LuaValue mend) {
            data.checkuserdata(Player.class).giveExp(value.checkint(), mend.checkboolean());
            return LuaConstant.NIL;
        }
    }

    private static class GiveLevels extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue data, LuaValue value) {
            data.checkuserdata(Player.class).giveExpLevels(value.checkint());
            return LuaConstant.NIL;
        }
    }

    private static class SpawnParticle extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            Player player = args.arg1().checkuserdata(Player.class);
            Particle particle = LuaApi.toParticle(args.arg(2).checkjstring());
            int i = 3;
            double x, y, z;
            if (args.arg(3).isuserdata()) {
                Location location = args.arg(3).checkuserdata(Location.class);
                x = location.x();
                y = location.y();
                z = location.z();
                i++;
            } else {
                x = args.arg(i++).checkdouble();
                y = args.arg(i++).checkdouble();
                z = args.arg(i++).checkdouble();
            }

            int count = args.arg(i++).checkint();
            double offsetX = args.arg(i++).optdouble(0);
            double offsetY = args.arg(i++).optdouble(0);
            double offsetZ = args.arg(i++).optdouble(0);
            double speed = args.arg(i).optint(0);

            player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, speed);

            return LuaConstant.NIL;
        }
    }

    // ToDo: player:playSound(...)

    // Todo player:resetTitle()

    // ToDo player:lookAt(...)

    // ToDo AudienceStuff
}
