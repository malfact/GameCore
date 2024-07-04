package net.malfact.gamecore.api.inventory.meta;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import net.malfact.gamecore.api.LuaApi;
import org.bukkit.inventory.meta.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.HashMap;
import java.util.Map;

public class ItemMetaLib extends LuaFunction {

    private static Map<Class<? extends ItemMeta>, Index<? extends ItemMeta>> INDICES = new HashMap<>();

    static {
        INDICES.put(ItemMeta.class,                 new ItemMetaIndex());
        INDICES.put(Damageable.class,               new DamageableIndex());
        INDICES.put(ArmorStandMeta.class,           new ArmorStandMetaIndex());
        INDICES.put(ArmorMeta.class,                new ArmorMetaIndex());
        INDICES.put(AxolotlBucketMeta.class,        new AxolotlBucketMetaIndex());
        INDICES.put(BannerMeta.class,               new BannerMetaIndex());
        INDICES.put(BookMeta.class,                 new BookMetaIndex());
        INDICES.put(BundleMeta.class,               new BundleMetaIndex());
        INDICES.put(CompassMeta.class,              new CompassMetaIndex());
        INDICES.put(CrossbowMeta.class,             new CrossbowMetaIndex());
        INDICES.put(EnchantmentStorageMeta.class,   new EnchantmentStorageMetaIndex());
//        INDICES.put(FireworkEffectMeta.class,       new FireworkEffectMetaIndex());     // ToDo: Firework Effects
//        INDICES.put(FireworkMeta.class,             new FireworkMetaIndex());           // ToDo: Firework Effects
        INDICES.put(LeatherArmorMeta.class,         new LeatherArmorMetaIndex());
//        INDICES.put(MapMeta.class,                  new MapMetaIndex());                // Do I even
        INDICES.put(MusicInstrumentMeta.class,      new MusicInstrumentMetaIndex());
        INDICES.put(OminousBottleMeta.class,        new OminousBottleMetaIndex());
        INDICES.put(PotionMeta.class,               new PotionMetaIndex());             // ToDo: Potion Data
        INDICES.put(Repairable.class,               new RepairableIndex());
        INDICES.put(SkullMeta.class,                new SkullMetaIndex());
//        INDICES.put(SpawnEggMeta.class,             new SpawnEggMetaIndex());           // ToDo: IDEK
//        INDICES.put(SuspiciousStewMeta.class,       new SuspiciousStewMetaIndex());     // ToDo: Potion Effects
        INDICES.put(TropicalFishBucketMeta.class,   new TropicalFishBucketMetaIndex());
        INDICES.put(WritableBookMeta.class,         new WritableBookMetaIndex());
    }

    public static LuaValue userdataOf(ItemMeta metaData) {
        LuaTable meta = new LuaTable();
        meta.set(LuaConstant.MetaTag.INDEX, index);
        meta.set(LuaConstant.MetaTag.NEWINDEX, newindex);
        meta.set(LuaConstant.MetaTag.TOSTRING, tostring);
        return new LuaUserdata(metaData, meta);
    }

    public static final LuaFunction index = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue data, LuaValue arg) {
            ItemMeta meta = data.checkuserdata(ItemMeta.class);
            String key = arg.checkjstring();
            for (var index : INDICES.values()) {
                var val = index.safeGet(meta, key);
                if (val != null)
                    return LuaApi.getValueOf(val);
            }

            return LuaConstant.NIL;
        }
    };

    public static final LuaFunction newindex = new ThreeArgFunction() {
        @Override
        public LuaValue call(LuaValue data, LuaValue arg, LuaValue value) {
            ItemMeta meta = data.checkuserdata(ItemMeta.class);
            String key = arg.checkjstring();

            for (var index : INDICES.values()) {
                index.safeSet(meta, key, value);
            }

            return LuaConstant.NIL;
        }
    };

    public static final LuaFunction tostring = new ZeroArgFunction() {
        @Override
        public LuaValue call() {
            return valueOf("ItemMeta");
        }
    };

    static abstract class Index<T extends ItemMeta> {
        private final Class<T> clazz;

        Index(Class<T> clazz) {
            this.clazz = clazz;
        }

        public final Object safeGet(ItemMeta meta, String key) {
            if (clazz.isInstance(meta))
                return get(clazz.cast(meta), key);

            return null;
        }

        public final void safeSet(ItemMeta meta, String key, LuaValue value) {
            if (clazz.isInstance(meta))
                set(clazz.cast(meta), key, value);
        }

        public abstract Object get(T meta, String key);

        public void set(T meta, String key, LuaValue value) {}
    }
}
