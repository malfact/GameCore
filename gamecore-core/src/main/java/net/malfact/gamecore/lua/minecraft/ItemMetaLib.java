package net.malfact.gamecore.lua.minecraft;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import net.malfact.gamecore.api.LuaLib;
import net.malfact.gamecore.api.LuaUtil;
import net.malfact.gamecore.api.TypeHandler;
import net.malfact.gamecore.lua.minecraft.meta.*;
import org.bukkit.inventory.meta.*;
import org.luaj.vm2.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemMetaLib implements LuaLib, TypeHandler<ItemMeta> {

    /**
     * This is temporary until the new component api is available for paper
     */
    public static final Map<Class<? extends ItemMeta>, ItemMetaHandler<? extends ItemMeta>> HANDLERS = new HashMap<>();
    public static final ItemMetaHandler<ItemMeta> DEFAULT_HANDLER = new ItemMetaHandler<>(ItemMeta.class);
    static {
        HANDLERS.put(ArmorMeta.class, new ArmorMetaHandler());
        HANDLERS.put(ArmorStandMeta.class, new ArmorStandMetaHandler());
        HANDLERS.put(AxolotlBucketMeta.class, new AxolotlBucketMetaHandler());
        HANDLERS.put(BannerMeta.class, new BannerMetaHandler());
        HANDLERS.put(BookMeta.class, new BookMetaHandler());
        //noinspection UnstableApiUsage
        HANDLERS.put(BundleMeta.class, new BundleMetaHandler());
        HANDLERS.put(CompassMeta.class, new CompassMetaHandler());
        HANDLERS.put(CrossbowMeta.class, new CrossbowMetaHandler());
        HANDLERS.put(Damageable.class, new DamageableHandler());
        HANDLERS.put(EnchantmentStorageMeta.class, new EnchantmentStorageMetaHandler());
        HANDLERS.put(LeatherArmorMeta.class, new LeatherArmorMetaHandler());
        HANDLERS.put(MusicInstrumentMeta.class, new MusicInstrumentMetaHandler());
        HANDLERS.put(OminousBottleMeta.class, new OminousBottleMetaHandler());
        HANDLERS.put(Repairable.class, new RepairableHandler());
        HANDLERS.put(SkullMeta.class, new SkullMetaHandler());
        HANDLERS.put(TropicalFishBucketMeta.class, new TropicalFishBucketMetaHandler());
        HANDLERS.put(WritableBookMeta.class, new WritableBookMetaHandler());
        // ToDo: Firework Effects
        // ToDo: Firework Effects
        // ToDo: Potion Data
        // ToDo: Suspicious Strew
        // ToDo: Spawn Egg
        // ToDo: Potion Effects
        // MapMeta??

    }

    public static List<ItemMetaHandler<?>> getHandlers(Class<? extends ItemMeta> itemMetaClass) {
        Class<?>[] interfaces = itemMetaClass.getInterfaces();
        List<Class<?>> itemMetaInterfaces = new ArrayList<>();
        for (var i : interfaces) {
            if (ItemMeta.class.isAssignableFrom(i))
                itemMetaInterfaces.add(i);
        }

        List<ItemMetaHandler<?>> itemMetaHandlers = new ArrayList<>();
        for (var i : itemMetaInterfaces) {
            var handler = HANDLERS.get(i);
            if (handler != null)
                itemMetaHandlers.add(handler);
        }

        return itemMetaHandlers;
    }

    private static final LuaFunction func_index =       LuaUtil.toFunction(ItemMetaLib::onIndex);
    private static final LuaFunction func_newindex =    LuaUtil.toFunction(ItemMetaLib::onNewIndex);
    private static final LuaFunction func_tostring =    LuaUtil.toFunction(ItemMetaLib::onToString);

    private static LuaValue onIndex(LuaValue arg1, LuaValue arg2) {
        ItemMeta meta = arg1.checkuserdata(ItemMeta.class);
        String key = arg2.checkjstring();
        List<ItemMetaHandler<?>> handlers = getHandlers(meta.getClass());

        var value = DEFAULT_HANDLER.get(meta, key);
        if (value != null)
            return value;

        for (var handler : handlers) {
            var val = handler.getRaw(meta, key);
            if (val != null)
                return val;
        }

        return LuaConstant.NIL;
    }

    private static void onNewIndex(LuaValue arg1, LuaValue arg2, LuaValue value) {
        ItemMeta meta = arg1.checkuserdata(ItemMeta.class);
        String key = arg2.checkjstring();
        List<ItemMetaHandler<?>> handlers = getHandlers(meta.getClass());

        if (DEFAULT_HANDLER.setRaw(meta, key, value))
            return;

        for (var handler : handlers) {
            if(handler.setRaw(meta, key, value))
                return;
        }
    }

    private static LuaValue onToString() {
        return LuaValue.valueOf("ItemMeta");
    }

    public static LuaValue userdataOf(ItemMeta itemMeta) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX,     func_index);
        meta.set(LuaConstant.MetaTag.NEWINDEX,  func_newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING,  func_tostring);

        meta.set("__userdata_type__", "item_meta");

        meta.set(LuaConstant.MetaTag.METATABLE, LuaConstant.FALSE);

        return new LuaUserdata(itemMeta, meta);
    }

    @Override
    public void load(LuaValue env) {

    }

    @Override
    public LuaValue getUserdataOf(ItemMeta itemMeta) {
        return userdataOf(itemMeta);
    }
}
