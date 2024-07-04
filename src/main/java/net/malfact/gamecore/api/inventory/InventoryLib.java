package net.malfact.gamecore.api.inventory;

import net.malfact.gamecore.api.InstancedLib;
import net.malfact.gamecore.api.userdata.UserdataProvider;
import net.malfact.gamecore.script.Instance;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.HashMap;
import java.util.Map;

public class InventoryLib extends InstancedLib implements UserdataProvider {

    public InventoryLib(Instance instance) {
        super(instance);
    }

    @Override
    public LuaValue call(LuaValue module, LuaValue env) {
        LuaTable lib = new LuaTable();
        lib.set("Inventory",lib);
        return env;
    }

    private static final Map<InventoryType, InventoryHandler<? extends Inventory>> HANDLERS = new HashMap<>();
    private static final InventoryHandler<Inventory> DEFAULT_HANDLER = new InventoryHandler<>(Inventory.class);

    public static <T extends Inventory> void registerHandler(InventoryType type, InventoryHandler<T> handler) {
        HANDLERS.put(type, handler);
    }

    static {
        registerHandler(InventoryType.PLAYER, new PlayerInventoryHandler());
    }

    public <H extends InventoryHandler<?>> LuaValue getUserdataOf(Inventory inventory, H handler) {
        LuaTable meta = new LuaTable();

        meta.set("HANDLER", new LuaUserdata(handler));
        meta.set(LuaConstant.MetaTag.INDEX, index);
        meta.set(LuaConstant.MetaTag.NEWINDEX, newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING, tostring);
        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);
        return new LuaUserdata(inventory, meta);
    }

    private static final LuaFunction tostring = new OneArgFunction() {
        @Override
        public LuaValue call(LuaValue arg) {
            InventoryHandler<?> handler = arg.getmetatable().get("HANDLER").checkuserdata(InventoryHandler.class);
            var inv = arg.checkuserdata(Inventory.class);
            return valueOf(handler.getType() + "<" + inv.getType() + ">");
        }
    };

    private final LuaFunction index = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            InventoryHandler<?> handler = arg1.getmetatable().get("HANDLER").checkuserdata(InventoryHandler.class);
            return handler.get(instance, arg1, arg2);
        }
    };

    private static final LuaFunction newindex = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            InventoryHandler<?> handler = arg1.getmetatable().get("HANDLER").checkuserdata(InventoryHandler.class);
            handler.set(arg1, arg2, arg3.checkuserdata(ItemStack.class));
            return LuaConstant.NIL;
        }
    };

    @Override
    public boolean accepts(Object o) {
        return o instanceof Inventory;
    }

    @Override
    public LuaValue getUserdataOf(Object o) {
        if (!accepts(o))
            return LuaConstant.NIL;

        Inventory inventory = (Inventory) o;

        InventoryHandler<?> handler = HANDLERS.getOrDefault(inventory.getType(), DEFAULT_HANDLER);
        return getUserdataOf(inventory, handler);
    }
}
