package net.malfact.gamecore.lua.minecraft.entity;

import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.math.Position;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.TriState;
import net.malfact.gamecore.Vector3;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.*;

public class PlayerHandler extends HumanEntityHandler<Player> {

    private final LuaFunction func_sendMessageRaw =             LuaUtil.toVarargFunction(this::sendMessageRaw);
    private final LuaFunction func_sendMessage =                LuaUtil.toVarargFunction(this::sendMessage);
    private final LuaFunction func_sendActionBar =              LuaUtil.toVarargFunction(this::sendActionBar);
    private final LuaFunction func_setTime =                    LuaUtil.toFunction(this::setTime);
    private final LuaFunction func_getTime =                    LuaUtil.toFunction(this::getTime);
    private final LuaFunction func_setTimeOffset =              LuaUtil.toFunction(this::getTimeOffset);
    private final LuaFunction func_resetTime =                  LuaUtil.toFunction(this::resetTime);
    private final LuaFunction func_setWeather =                 LuaUtil.toFunction(this::setWeather);
    private final LuaFunction func_getWeather =                 LuaUtil.toFunction(this::getWeather);
    private final LuaFunction func_resetWeather =               LuaUtil.toFunction(this::resetWeather);
    private final LuaFunction func_giveExp =                    LuaUtil.toFunction(this::giveExp);
    private final LuaFunction func_giveExpLevels =              LuaUtil.toFunction(this::giveExpLevels);
    private final LuaFunction func_canSee =                     LuaUtil.toFunction(this::canSee);
    private final LuaFunction func_showTitle =                  LuaUtil.toFunction(this::showTitle);
    private final LuaFunction func_resetTitle =                 LuaUtil.toFunction(this::resetTitle);
    private final LuaFunction func_spawnParticle =              LuaUtil.toVarargFunction(this::spawnParticle);
    private final LuaFunction func_getCooledAttackStrength =    LuaUtil.toFunction(this::getCooledAttackStrength);
    private final LuaFunction func_resetCooldown =              LuaUtil.toFunction(this::setRotation);
    private final LuaFunction func_setRotation =                LuaUtil.toFunction(this::resetCooldown);
    private final LuaFunction func_lookAt =                     LuaUtil.toVarargFunction(this::lookAt);
    private final LuaFunction func_showElderGuardian =          LuaUtil.toFunction(this::showElderGuardian);
    private final LuaFunction func_resetIdleDuration =          LuaUtil.toFunction(this::resetIdleDuration);
    private final LuaFunction func_playSound =                  LuaUtil.toVarargFunction(this::playSound);
    private final LuaFunction func_stopSound =                  LuaUtil.toFunction(this::stopSound);

    public PlayerHandler() {
        super(Player.class);
    }

    @Override
    protected LuaValue get(Game instance, Player player, String key) {
        return switch (key) {
            // Overrides
            case "isOnGround" -> LuaConstant.NIL;
            case "customName" -> LuaUtil.valueOf(player.customName());

            // Get & Set
            case "displayName" ->       LuaApi.valueOf(player.displayName());
            case "playerListName" ->    LuaApi.valueOf(player.playerListName());
            case "playerListHeader" ->  LuaApi.valueOf(player.playerListHeader());
            case "playerListFooter" ->  LuaApi.valueOf(player.playerListFooter());
            case "compassTarget" ->     LuaApi.userdataOf(player.getCompassTarget());
            case "sprinting" ->         LuaApi.valueOf(player.isSneaking());
            case "sleepingIgnored" ->   LuaApi.valueOf(player.isSleepingIgnored());
            case "respawnLocation" ->   LuaApi.userdataOf(player.getRespawnLocation());
            case "expCooldown" ->       LuaApi.valueOf(player.getExpCooldown());
            case "exp" ->               LuaApi.valueOf(player.getExp());
            case "level" ->             LuaApi.valueOf(player.getLevel());
            case "totalExp" ->          LuaApi.valueOf(player.getTotalExperience());
            case "allowFlight" ->       LuaApi.valueOf(player.getAllowFlight());
            case "flyingFallDamage" -> {
                var fallDmg = player.hasFlyingFallDamage().toBoolean();
                yield (fallDmg == null ? LuaConstant.NIL : LuaValue.valueOf(fallDmg));
            }
            case "flying" -> LuaValue.valueOf(player.isFlying());
//            case "scoreboard" -> // ToDo: Scoreboards
            case "spectatorTarget" ->
                player.getGameMode() != GameMode.SPECTATOR ? LuaConstant.NIL : LuaApi.userdataOf(player.getSpectatorTarget());
            case "wardenWarningCooldown" -> LuaValue.valueOf(player.getWardenWarningCooldown());
            case "wardenTimeSinceLastWarning" -> LuaValue.valueOf(player.getWardenTimeSinceLastWarning());
            case "wardenWarningLevel" -> LuaValue.valueOf(player.getWardenWarningLevel());

            // Get Only
            case "isOnline" -> LuaValue.valueOf(player.isOnline());
            case "isTimeRelative" -> LuaValue.valueOf(player.isPlayerTimeRelative());
            case "totalExpPoints" -> LuaValue.valueOf(player.calculateTotalExperiencePoints());
            case "expForNextLevel" -> LuaValue.valueOf(player.getExperiencePointsNeededForNextLevel());
            case "flyspeed" -> LuaValue.valueOf(player.getFlySpeed());
            case "walkspeed" -> LuaValue.valueOf(player.getWalkSpeed());
            case "cooldownPeriod" -> LuaValue.valueOf(player.getCooldownPeriod());
            case "idleDuration" -> LuaValue.valueOf(player.getIdleDuration().toSeconds());
            case "inventory" -> LuaApi.userdataOf(player.getInventory(), instance);

            // Functions
            case "sendMessageRaw" ->          func_sendMessageRaw;
            case "sendMessage" ->             func_sendMessage;
            case "sendActionBar" ->           func_sendActionBar;
            case "setTime" ->                 func_setTime;
            case "getTime" ->                 func_getTime;
            case "setTimeOffset" ->           func_setTimeOffset;
            case "resetTime" ->               func_resetTime;
            case "setWeather" ->              func_setWeather;
            case "getWeather" ->              func_getWeather;
            case "resetWeather" ->            func_resetWeather;
            case "giveExp" ->                 func_giveExp;
            case "giveExpLevels" ->           func_giveExpLevels;
            case "canSee" ->                  func_canSee;
            case "showTitle" ->               func_showTitle;
            case "resetTitle" ->              func_resetTitle;
            case "spawnParticle" ->           func_spawnParticle;
            case "getCooledAttackStrength" -> func_getCooledAttackStrength;
            case "resetCooldown" ->           func_resetCooldown;
            case "setRotation" ->             func_setRotation;
            case "lookAt" ->                  func_lookAt;
            case "showElderGuardian" ->       func_showElderGuardian;
            case "resetIdleDuration" ->       func_resetIdleDuration;
            case "playSound" ->               func_playSound;
            case "stopSound" ->               func_stopSound;

            default -> super.get(instance, player, key);
        };
    }

    @Override
    protected void set(Game instance, Player player, String key, LuaValue value) {
        switch (key) {
            case "customName" -> {
            } // Override

            case "displayName" -> player.displayName(LuaUtil.toComponent(value));
            case "playerListName" -> player.playerListName(LuaUtil.toComponent(value));
            case "playerListHeader" -> player.sendPlayerListHeader(LuaUtil.toComponent(value));
            case "playerListFooter" -> player.sendPlayerListFooter(LuaUtil.toComponent(value));
            case "compassTarget" -> player.setCompassTarget(value.checkuserdata(Location.class));
            case "sprinting" -> player.setSprinting(value.checkboolean());
            case "sleepingIgnored" -> player.setSleepingIgnored(value.checkboolean());
            case "respawnLocation" -> player.setRespawnLocation(value.checkuserdata(Location.class), true);
            case "expCooldown" -> player.setExpCooldown(LuaUtil.toMinRange(value, 0));
            case "exp" -> player.setExp(LuaUtil.toRange(value, 0, 1));
            case "level" -> player.setLevel(LuaUtil.toMinRange(value, 0));
            case "totalExp" -> player.setTotalExperience(LuaUtil.toMinRange(value, 0));
            case "totalExpPoints" -> player.setExperienceLevelAndProgress(LuaUtil.toMinRange(value, 0));
            case "allowFlight" -> player.setAllowFlight(value.checkboolean());
            case "flyingFallDamage" ->
                player.setFlyingFallDamage(value.isnil() ? TriState.NOT_SET : TriState.byBoolean(value.checkboolean()));
            case "flying" -> player.setFlying(value.checkboolean());
            case "flySpeed" -> player.setFlySpeed((float) value.checkdouble());
            case "walkSpeed" -> player.setWalkSpeed((float) value.checkdouble());
//            case "scoreboard" -> // ToDo: Scoreboards
            case "spectatorTarget" -> {
                if (player.getGameMode() == GameMode.SPECTATOR)
                    player.setSpectatorTarget(value.checkuserdata(Entity.class));
            }
            case "wardenWarningCooldown" -> player.setWardenWarningCooldown(LuaUtil.toMinRange(value, 0));
            case "wardenTimeSinceLastWarning" -> player.setWardenTimeSinceLastWarning(LuaUtil.toMinRange(value, 0));
            case "wardenWarningLevel" -> player.setWardenWarningLevel(LuaUtil.toMinRange(value, 0));


            default -> super.set(instance, player, key, value);
        }
    }

    @Override
    protected LuaValue getUnregistered(Player player, String key) {
        if (key.equals("isOnline"))
            return LuaApi.valueOf(player.isOnline());
        else if (key.equals("gamemode"))
            return LuaApi.valueOf(player.getGameMode());
        else
            return super.getUnregistered(player, key);
    }

    @Override
    protected String toString(Player player) {
        return LuaUtil.fromComponent(player.name());
    }

    @Override
    protected String type() {
        return "player";
    }

    private void sendMessageRaw(Varargs args) {
        Varargs args1 = args.subargs(2);
        args.arg1().checkuserdata(Player.class).sendRawMessage(LuaUtil.toString(args1));
    }

    private void sendMessage(Varargs args) {
        args.arg1().checkuserdata(Player.class).sendMessage(LuaUtil.toComponent(LuaUtil.toString(args.subargs(2))));
    }

    private void playSound(Varargs args) {
        Player player = args.arg(1).checkuserdata(Player.class);
        World world = player.getWorld();

        int o = 0;

        Location location = LuaUtil.toLocation(args.arg(2));
        if (location == null) {
            var entity = args.arg(2).touserdata(Entity.class);
            if (entity == null) {
                location = player.getLocation();
                o = -1;
            } else {
                location = entity.getLocation();
            }
        }

        location.setWorld(world);

        var sound = LuaUtil.toSound(args.arg(3 + o));
        if (sound == null)
            LuaUtil.argumentError(3 + o, "Sound", args.arg(3 + o));

        float volume = (float) args.optdouble(4 + o, 1);
        float pitch = (float) args.optdouble(4 + o, 1);

        player.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

    private void stopSound(LuaValue arg1, LuaValue arg2) {
        Player player = arg1.checkuserdata(Player.class);
        if (arg2.isnil())
            player.stopAllSounds();
        else
            player.stopSound(LuaUtil.checkSound(arg2));
    }

    private void sendActionBar(Varargs args) {
        Player player = args.arg(1).checkuserdata(Player.class);
        String msg = LuaUtil.toString(args.subargs(2));
        player.sendActionBar(LuaUtil.toComponent(msg));
    }

    private void setTime(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        Player player = arg1.checkuserdata(Player.class);
        long time = arg2.checklong();
        boolean relative = arg3.optboolean(false);
        player.setPlayerTime(time, relative);
    }

    private LuaValue getTime(LuaValue arg) {
        return LuaValue.valueOf(arg.checkuserdata(Player.class).getPlayerTime());
    }

    private LuaValue getTimeOffset(LuaValue arg) {
        return LuaValue.valueOf(arg.checkuserdata(Player.class).getPlayerTimeOffset());
    }

    private void resetTime(LuaValue arg) {
        arg.checkuserdata(Player.class).resetPlayerTime();
    }

    private void setWeather(LuaValue arg1, LuaValue arg2) {
        arg1.checkuserdata(Player.class).setPlayerWeather(LuaUtil.checkEnum(arg2, WeatherType.class));
    }

    private LuaValue getWeather(LuaValue arg) {
        return LuaUtil.valueOf(arg.checkuserdata(Player.class).getPlayerWeather());
    }

    private void resetWeather(LuaValue arg) {
        arg.checkuserdata(Player.class).resetPlayerWeather();
    }

    private void giveExp(LuaValue arg1, LuaValue arg2) {
        arg1.checkuserdata(Player.class).giveExp(Math.max(1, arg2.optint(1)));
    }

    private void giveExpLevels(LuaValue arg1, LuaValue arg2) {
        arg1.checkuserdata(Player.class).giveExpLevels(Math.max(1, arg2.optint(1)));
    }

    private LuaValue canSee(LuaValue arg1, LuaValue arg2) {
        return LuaValue.valueOf(arg1.checkuserdata(Player.class).canSee(arg2.checkuserdata(Entity.class)));
    }

    private void showTitle(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        Player player = arg1.checkuserdata(Player.class);
        Component title = LuaUtil.toComponent(arg2);
        Component subtitle = LuaUtil.toComponent(arg3.optjstring(""));

        player.showTitle(Title.title(title, subtitle));
    }

    private void resetTitle(LuaValue arg) {
        arg.checkuserdata(Player.class).resetTitle();
    }

    private void spawnParticle(Varargs args) {
        Player player = args.arg1().checkuserdata(Player.class);
        Particle particle = LuaUtil.toParticle(args.arg(2).checkjstring());
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
    }

    private LuaValue getCooledAttackStrength(LuaValue arg1, LuaValue arg2) {
        Player player = arg1.checkuserdata(Player.class);
        float adjustTicks = (float) arg2.optdouble(0);
        return LuaValue.valueOf(player.getCooledAttackStrength(adjustTicks));
    }

    private void resetCooldown(LuaValue arg) {
        arg.checkuserdata(Player.class).resetCooldown();
    }

    private void setRotation(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        arg1.checkuserdata(Player.class).setRotation((float) arg2.checkdouble(), (float) arg3.checkdouble());
    }

    @SuppressWarnings("UnstableApiUsage")
    private void lookAt(Varargs args) {
        Player player = args.arg1().checkuserdata(Player.class);
        switch (args.narg()) {
            case 1:
            case 2:
                throw new LuaError("bad argument #" + (args.narg()+1)+ " (value expected, got nil)");
            case 3:
                Vector3 v = LuaUtil.checkVector3(args.arg(2));
                player.lookAt(Position.fine(v.x, v.y, v.z), LuaUtil.checkEnum(args.arg(3), LookAnchor.class));
                break;
            case 4:
                Entity e = args.arg(2).checkuserdata(Entity.class);
                LookAnchor playerAnchor = LuaUtil.checkEnum(args.arg(2), LookAnchor.class);
                LookAnchor entityAnchor = LuaUtil.checkEnum(args.arg(3), LookAnchor.class);
                player.lookAt(e, playerAnchor, entityAnchor);
                break;
            default:
                player.lookAt(
                    args.checkdouble(2),
                    args.checkdouble(3),
                    args.checkdouble(4),
                    LuaUtil.checkEnum(args.arg(5), LookAnchor.class)
                );
                break;
        }
    }

    private void showElderGuardian(LuaValue arg1, LuaValue arg2) {
        arg1.checkuserdata(Player.class).showElderGuardian(arg2.optboolean(false));
    }

    private void resetIdleDuration(LuaValue arg1) {
        arg1.checkuserdata(Player.class).resetIdleDuration();
    }
}