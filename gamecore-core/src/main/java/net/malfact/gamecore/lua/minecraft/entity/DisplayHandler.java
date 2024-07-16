package net.malfact.gamecore.lua.minecraft.entity;

import net.malfact.gamecore.Vector3;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.game.Game;
import org.bukkit.DyeColor;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class DisplayHandler<T extends Display> extends EntityHandler<T> {

    public DisplayHandler(Class<T> entityClass) {
        super(entityClass);
    }

    private final LuaFunction func_setTransformation = LuaUtil.toVarargFunction(this::setTransformation);
    private final LuaFunction func_getBrightness = LuaUtil.toVarargFunction(this::getBrightness);
    private final LuaFunction func_setBrightness = LuaUtil.toFunction(this::setBrightness);

    @Override
    protected LuaValue get(Game instance, T display, String key) {
        return switch (key) {
            case "interpolationDuration" -> LuaApi.valueOf(display.getInterpolationDuration());
            case "teleportDuration" ->      LuaApi.valueOf(display.getTeleportDuration());
            case "viewRange" ->             LuaApi.valueOf(display.getViewRange());
            case "shadowRadius" ->          LuaApi.valueOf(display.getShadowRadius());
            case "shadowStrength" ->        LuaApi.valueOf(display.getShadowStrength());
            case "displayWidth" ->          LuaApi.valueOf(display.getDisplayWidth());
            case "displayHeight" ->         LuaApi.valueOf(display.getDisplayHeight());
            case "interpolationDelay" ->    LuaApi.valueOf(display.getInterpolationDelay());
            case "billboard" ->             LuaApi.valueOf(display.getBillboard());
            case "glowColorOverride" ->     LuaApi.valueOf(display.getGlowColorOverride().toString());

            // Functions
            case "getTransformation" ->     new getTransformation();
            case "setTransformation" ->     func_setTransformation;
            case "getBrightness" ->         func_getBrightness;
            case "setBrightness" ->         func_setBrightness;

            default -> super.get(instance, display, key);
        };
    }

    @Override
    protected void set(Game instance, T display, String key, LuaValue value) {
        switch (key) {
            case "interpolationDuration" -> display.setInterpolationDuration(value.checkint());
            case "teleportDuration" ->      display.setTeleportDuration(value.checkint());
            case "viewRange" ->             display.setViewRange((float) value.checkdouble());
            case "shadowRadius" ->          display.setShadowRadius((float) value.checkdouble());
            case "shadowStrength" ->        display.setShadowStrength((float) value.checkdouble());
            case "displayWidth" ->          display.setDisplayWidth((float) value.checkdouble());
            case "displayHeight" ->         display.setDisplayHeight((float) value.checkdouble());
            case "interpolationDelay" ->    display.setInterpolationDelay(value.checkint());
            case "billboard" ->             display.setBillboard(LuaUtil.toEnum(value, Display.Billboard.class));
            case "glowColorOverride" ->     display.setGlowColorOverride(LuaUtil.toEnum(value, DyeColor.class).getColor());

            default -> super.set(instance, display, key, value);
        }
    }


    private void setTransformation(Varargs args) {
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
    }

    private Varargs getBrightness(Varargs args) {
        Display display = args.arg1().checkuserdata(Display.class);
        var brightness = display.getBrightness();
        return LuaValue.varargsOf(LuaValue.valueOf(brightness.getBlockLight()), LuaValue.valueOf(brightness.getSkyLight()));
    }

    private void setBrightness(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        Display display = arg1.checkuserdata(Display.class);
        display.setBrightness(new Display.Brightness(arg2.checkint(), arg3.checkint()));
    }

    private static class getTransformation extends VarArgFunction {

        @Override
        public Varargs invoke(Varargs args) {
            Display display = args.arg1().checkuserdata(Display.class);
            var transform = display.getTransformation();
            var translate = transform.getTranslation();
            var vec3Translation = LuaApi.userdataOf(new Vector3(translate.x,translate.y,translate.z));
            var leftRot = valueOf(transform.getLeftRotation().angle());
            var scale = transform.getScale();
            var vec3Scale = LuaApi.userdataOf(new Vector3(scale.x, scale.y, scale.z));
            var rightRot = valueOf(transform.getRightRotation().angle());

            return varargsOf(new LuaValue[]{vec3Translation, leftRot, vec3Scale, rightRot});
        }
    }

    @Override
    protected String type() {
        return "display";
    }
}
