package net.malfact.gamecore.api.entity;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.inventory.LuaContainerInventory;
import net.malfact.gamecore.api.inventory.LuaItemStack;
import net.malfact.gamecore.api.inventory.LuaPlayerInventory;
import net.malfact.gamecore.api.world.LocationLib;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class PlayerLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue module, LuaValue env) {
        return env;
    }

    private static final LuaPlayerLib PLAYER_LIB = new LuaPlayerLib();

    public static LuaValue toUserdata(Player player) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, PLAYER_LIB.index);
        meta.set(LuaConstant.MetaTag.NEWINDEX, PLAYER_LIB.newindex);

        return new LuaUserdata(player, meta);
    }

    private static class LuaPlayerLib extends LivingEntityLib {

        private static final LuaFunction MESSAGE_RAW = new MessageRaw();
        private static final LuaFunction MESSAGE = new Message();
        private static final LuaFunction SET_TIME = new SetTime();
        private static final LuaFunction GET_TIME = new GetTime();
        private static final LuaFunction GET_TIME_OFFSET = new GetTimeOffset();
        private static final LuaFunction SET_WEATHER = new SetWeather();
        private static final LuaFunction GET_WEATHER = new GetWeather();
        private static final LuaFunction GIVE_EXP = new GiveExp();
        private static final LuaFunction GIVE_LEVELS = new GiveLevels();
        private static final LuaFunction SPAWN_PARTICLE = new SpawnParticle();

        @Override
        protected void remove(Entity entity) {}

        @Override
        protected boolean spawn(Entity entity) {
            return false;
        }

        @Override
        public boolean canHandle(Entity entity) {
            return entity instanceof Player;
        }

        @Override
        public LuaValue __index(LuaValue data, LuaValue key) {
            if (!key.isstring())
                return LuaConstant.NIL;

            Player player = data.checkuserdata(Player.class);

            return switch (key.tojstring()) {
                // Get & Set
                case "displayName" -> valueOf(player.displayName().toString());
                case "playerListName" -> valueOf(player.playerListName().toString());
                case "playerListHeader" -> valueOf(player.playerListHeader().toString());
                case "playerListFooter" -> valueOf(player.playerListFooter().toString());
                case "sprinting" -> valueOf(player.isSprinting());
                case "compassTarget" -> LocationLib.getValueOf(player.getCompassTarget());
                case "sleepingIgnored" -> valueOf(player.isSleepingIgnored());
                case "respawnLocation" -> LocationLib.getValueOf(player.getRespawnLocation());
                case "level" -> valueOf(player.getLevel());
                case "experience" -> valueOf(player.getExp());
                case "totalExperience" -> valueOf(player.getTotalExperience());
                case "allowFlight" -> valueOf(player.getAllowFlight());
                case "flying" -> valueOf(player.isFlying());
                case "flyspeed" -> valueOf(player.getFlySpeed());
                case "walkspeed" -> valueOf(player.getWalkSpeed());
                case "spectating" -> {
                    Entity target = player.getSpectatorTarget();
                    if (target == null)
                        yield LuaConstant.NIL;
                    else
                        yield EntityLib.getValueOf(target);

                }
                case "wardenAnger" -> valueOf(player.getWardenWarningLevel());
                case "wardenAngerTimeCooldown" -> valueOf(player.getWardenWarningCooldown());
                case "wardenAngerTimeElapsed" -> valueOf(player.getWardenTimeSinceLastWarning());
                case "gamemode" -> valueOf(player.getGameMode().toString());

                // Get Only
                case "ping" -> valueOf(player.getPing());
                case "idle" -> valueOf(player.getIdleDuration().toSeconds());

                // From HumanEntity
                case "exhaustion" -> valueOf(player.getExhaustion());
                case "saturation" -> valueOf(player.getSaturation());
                case "foodLevel" -> valueOf(player.getFoodLevel());
                case "saturatedRegenRate" -> valueOf(player.getSaturatedRegenRate());
                case "unsaturatedRegenRate" -> valueOf(player.getUnsaturatedRegenRate());
                case "starvationRate" -> valueOf(player.getStarvationRate());

                case "name" -> valueOf(player.getName());
                case "inventory" -> new LuaPlayerInventory(player.getInventory());
                case "enderchest" -> new LuaContainerInventory(player.getEnderChest());
//                case "openInventory" -> ...
                case "itemOnCursor" -> new LuaItemStack(player.getItemOnCursor());
                case "deepSleep" -> valueOf(player.isDeeplySleeping());
                case "sleepTicks" -> valueOf(player.getSleepTicks());
                case "blocking" -> valueOf(player.isBlocking());
                case "attackCooldown" -> valueOf(player.getAttackCooldown());
                case "deathLocation" -> LocationLib.getValueOf(player.getLastDeathLocation());

                // Functions
                case "messageRaw" -> MESSAGE_RAW;
                case "message" -> MESSAGE;
                case "setTime" -> SET_TIME;
                case "getTime" -> GET_TIME;
                case "getTimeOffset" -> GET_TIME_OFFSET;
                case "setWeather" -> SET_WEATHER;
                case "getWeather" -> GET_WEATHER;
                case "giveExp" -> GIVE_EXP;
                case "giveLevels" -> GIVE_LEVELS;
                case "spawnParticle" -> SPAWN_PARTICLE;

                default -> super.__index(data, key);
            };
        }

        @Override
        public LuaValue __newindex(LuaValue data, LuaValue key, LuaValue value) {
            if (!key.isstring())
                return LuaConstant.NIL;

            Player player = data.checkuserdata(Player.class);

            switch (key.tojstring()) {
                case "displayName" -> player.displayName(LuaApi.toComponent(value.checkjstring()));
                case "playerListName" -> player.playerListName(LuaApi.toComponent(value.checkjstring()));
                case "playerListHeader" -> player.sendPlayerListHeader(LuaApi.toComponent(value.checkjstring()));
                case "playerListFooter" -> player.sendPlayerListFooter(LuaApi.toComponent(value.checkjstring()));
                case "sprinting" -> player.setSprinting(value.checkboolean());
                case "compassTarget" -> player.setCompassTarget(value.checkuserdata(Location.class));
                case "sleepingIgnored" -> player.setSleepingIgnored(value.checkboolean());
                case "respawnLocation" -> player.setRespawnLocation(value.checkuserdata(Location.class), true);
                case "level" -> player.setLevel(value.checkint());
                case "experience" -> player.setExp((float) value.checkdouble());
                case "totalExperience" -> player.setTotalExperience(value.checkint());
                case "allowFlight" -> player.setAllowFlight(value.checkboolean());
                case "flying" -> player.setFlying(value.checkboolean());
                case "flySpeed" -> player.setFlySpeed((float) value.checkdouble());
                case "walkSpeed" -> player.setWalkSpeed((float) value.checkdouble());
                case "spectating" -> player.setSpectatorTarget(value.isnil() ? null : value.checkuserdata(Entity.class));
                case "wardenAnger" -> player.setWardenWarningLevel(value.checkint());
                case "wardenAngerTimeCooldown" -> player.setWardenWarningCooldown(value.checkint());
                case "wardenAngerTimeElapsed" -> player.setWardenTimeSinceLastWarning(value.checkint());
                case "gamemode" -> player.setGameMode(LuaApi.toEnum(value.checkjstring(), GameMode.class));

                // From HumanEntity
                case "exhaustion" -> player.setExhaustion((float) value.checkdouble());
                case "saturation" -> player.setSaturation((float) value.checkdouble());
                case "foodLevel" -> player.setFoodLevel(value.checkint());
                case "saturatedRegenRate" -> player.setSaturatedRegenRate(value.checkint());
                case "unsaturatedRegenRate" -> player.setUnsaturatedRegenRate(value.checkint());
                case "starvationRate" -> player.setStarvationRate(value.checkint());

                default -> super.__newindex(data, key, value);
            }

            return LuaConstant.NIL;
        }

        // ToDo: Player.get(name)
        // Instance PlayerLib with each game and only find players in that game

        private static class MessageRaw extends TwoArgFunction {

            @Override
            public LuaValue call(LuaValue data, LuaValue msg) {
                data.checkuserdata(Player.class).sendRawMessage(msg.checkjstring());
                return LuaConstant.NIL;
            }
        }

        private static class Message extends TwoArgFunction {

            @Override
            public LuaValue call(LuaValue data, LuaValue msg) {
                data.checkuserdata(Player.class).sendMessage(LuaApi.toComponent(msg.checkjstring()));
                return LuaConstant.NIL;
            }
        }

        // ToDo: player:playSound(...)

        private static class SetTime extends ThreeArgFunction {

            @Override
            public LuaValue call(LuaValue data, LuaValue time) {
                return call(data, time, LuaConstant.TRUE);
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

        // Todo player:resetTitle()

        // ToDo player:lookAt(...)

        // ToDo AudienceStuff
    }
}
