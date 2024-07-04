package net.malfact.gamecore.api.entity.handler;

import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.world.Vector3;
import net.malfact.gamecore.script.Instance;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class DisplayEntityHandler extends EntityHandler {

    @Override
    public boolean acceptsEntity(Entity entity) {
        return entity instanceof Display;
    }

    @Override
    public @NotNull LuaValue onIndex(Instance instance, Entity entity, LuaValue key) {
        if (!key.isstring())
            return NIL;

        Display display = checkEntity(instance, entity, Display.class);
        return switch (key.tojstring()) {
            case "interpolationDuration" -> LuaValue.valueOf(display.getInterpolationDuration());
            case "teleportDuration" -> LuaValue.valueOf(display.getTeleportDuration());
            case "viewRange" -> LuaValue.valueOf(display.getViewRange());
            case "shadowRadius" -> LuaValue.valueOf(display.getShadowRadius());
            case "shadowStrength" -> LuaValue.valueOf(display.getShadowStrength());
            case "displayWidth" -> LuaValue.valueOf(display.getDisplayWidth());
            case "displayHeight" -> LuaValue.valueOf(display.getDisplayHeight());
            case "interpolationDelay" -> LuaValue.valueOf(display.getInterpolationDelay());
            case "billboard" -> instance.getValueOf(display.getBillboard());
            case "glowColorOverride" -> LuaValue.valueOf(display.getGlowColorOverride().toString());
            case "location" -> instance.getValueOf(display.getLocation());
            case "getTransformation" -> getTransformation;
            case "setTransformation" -> setTransformation;
            case "getBrightness" -> getBrightness;
            case "setBrightness" -> setBrightness;

            default -> LuaConstant.NIL;
        };
    }

    @Override
    public boolean onNewIndex(Instance instance, Entity entity, LuaValue key, LuaValue value) {
        if (!key.isstring())
            return false;

        Display display = checkEntity(instance, entity, Display.class);
        switch (key.tojstring()) {
            case "interpolationDuration" -> display.setInterpolationDuration(value.checkint());
            case "teleportDuration" -> display.setTeleportDuration(value.checkint());
            case "viewRange" -> display.setViewRange((float) value.checkdouble());
            case "shadowRadius" -> display.setShadowRadius((float) value.checkdouble());
            case "shadowStrength" -> display.setShadowStrength((float) value.checkdouble());
            case "displayWidth" -> display.setDisplayWidth((float) value.checkdouble());
            case "displayHeight" -> display.setDisplayHeight((float) value.checkdouble());
            case "interpolationDelay" -> display.setInterpolationDelay(value.checkint());
            case "billboard" -> display.setBillboard(LuaApi.toEnum(value.checkjstring(), Display.Billboard.class));
            case "glowColorOverride" -> display.setGlowColorOverride(LuaApi.toEnum(value.checkjstring(), DyeColor.class).getColor());
            case "location" -> display.teleport(value.checkuserdata(Location.class));
            default -> {return false;}
        }

        return true;
    }

    private static final LuaFunction getTransformation = new VarArgFunction() {
        @Override
        public Varargs invoke(Varargs args) {
            Display display = args.arg1().checkuserdata(Display.class);
            var transform = display.getTransformation();
            var translate = transform.getTranslation();
            var vec3Translation = LuaApi.getValueOf(new Vector3(translate.x,translate.y,translate.z));
            var leftRot = valueOf(transform.getLeftRotation().angle());
            var scale = transform.getScale();
            var vec3Scale = LuaApi.getValueOf(new Vector3(scale.x, scale.y, scale.z));
            var rightRot = valueOf(transform.getRightRotation().angle());

            return varargsOf(new LuaValue[]{vec3Translation, leftRot, vec3Scale, rightRot});
        }
    };

    private static final LuaFunction setTransformation = new VarArgFunction() {
        @Override
        public Varargs invoke(Varargs args) {
            Display display = args.arg1().checkuserdata(Display.class);
            var t = args.arg(2).checkuserdata(Vector3.class);
            var left_rot = (float) args.arg(3).checkdouble();
            var s = args.arg(4).checkuserdata(Vector3.class);
            var right_rot = (float) args.arg(5).checkdouble();

            display.setTransformation(new Transformation(
                new Vector3f((float) t.x, (float) t.y, (float) t.z),
                new AxisAngle4f(left_rot, 1, 0 ,0),
                new Vector3f((float) s.x, (float) s.y, (float) s.z),
                new AxisAngle4f(right_rot, 0, 0, 1)
            ));

            return LuaConstant.NIL;
        }
    };

    private static final LuaFunction getBrightness = new VarArgFunction() {
        @Override
        public Varargs invoke(Varargs args) {
            Display display = args.arg1().checkuserdata(Display.class);
            var brightness = display.getBrightness();
            return varargsOf(valueOf(brightness.getBlockLight()), valueOf(brightness.getSkyLight()));
        }
    };

    private static final LuaFunction setBrightness = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            Display display = arg1.checkuserdata(Display.class);
            display.setBrightness(new Display.Brightness(arg2.checkint(), arg3.checkint()));
            return LuaConstant.NIL;
        }
    };
}
