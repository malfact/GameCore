package net.malfact.gamecore;

import net.malfact.gamecore.event.RegisterGlobalLibraryEvent;
import net.malfact.gamecore.event.RegisterLocalLibraryEvent;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.lua.DataStoreLib;
import net.malfact.gamecore.lua.EnvGlobalLib;
import net.malfact.gamecore.lua.GameLib;
import net.malfact.gamecore.lua.Vector3Lib;
import net.malfact.gamecore.lua.event.EventHandler;
import net.malfact.gamecore.lua.minecraft.*;
import net.malfact.gamecore.lua.minecraft.entity.*;
import net.malfact.gamecore.lua.minecraft.inventory.InventoryHandler;
import net.malfact.gamecore.lua.minecraft.inventory.PlayerInventoryHandler;
import net.malfact.gamecore.lua.minecraft.types.*;
import net.malfact.gamecore.util.CraftClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.boss.BossBar;
import org.bukkit.damage.DamageSource;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

class CoreApiPlugin implements Listener {

    @org.bukkit.event.EventHandler
    public void onLibraries(RegisterGlobalLibraryEvent event) {
        event.registerLib(new EnvGlobalLib());
        event.registerLib(new InventoryLib());

        var vector3Lib = new Vector3Lib();
        event.registerLib(vector3Lib);
        event.registerTypeHandler(vector3Lib, Vector3.class);

        var locationLib = new LocationLib();
        event.registerLib(locationLib);
        event.registerTypeHandler(locationLib, Location.class);

        var itemStackLib = new ItemStackLib();
        event.registerLib(itemStackLib);
        event.registerTypeHandler(itemStackLib, ItemStack.class, CraftClass.forName("inventory.CraftItemStack"));

        var itemMetaLib = new ItemMetaLib();
        event.registerLib(itemMetaLib);
        event.registerTypeHandler(itemMetaLib, ItemMeta.class, CraftClass.forName("inventory.CraftMetaItem"));

        var attributeLib = new AttributeLib();
        event.registerLib(attributeLib);
        event.registerTypeHandler(attributeLib, AttributeInstance.class, CraftClass.forName("attribute.CraftAttributeInstance"));

        var attributeModifierLib = new AttributeModifierLib();
        event.registerLib(attributeModifierLib);
        event.registerTypeHandler(attributeModifierLib, AttributeModifier.class);

        var attributeTypeLib = new AttributeTypeLib();
        event.registerLib(attributeTypeLib);
        event.registerTypeHandler(attributeTypeLib, Attribute.class);

        var enchantTypeLib = new EnchantTypeLib(); // Has To-Do
        event.registerLib(enchantTypeLib);
        event.registerTypeHandler(enchantTypeLib, Enchantment.class);

        var entityTypeLib = new EntityTypeLib();
        event.registerLib(entityTypeLib);
        event.registerTypeHandler(entityTypeLib, EntityType.class);

        var materialTypeLib = new MaterialTypeLib();
        event.registerLib(materialTypeLib);
        event.registerTypeHandler(materialTypeLib, Material.class);

        var potionTypeLib = new PotionTypeLib(); // Has To-Do
        event.registerLib(potionTypeLib);
        event.registerTypeHandler(potionTypeLib, PotionType.class);

        var potionEffectTypeLib = new PotionEffectTypeLib(); // Has To-Do
        event.registerLib(potionEffectTypeLib);
        event.registerTypeHandler(potionEffectTypeLib, PotionEffectType.class);

        var inventoryTypeLib = new InventoryTypeLib();
        event.registerLib(inventoryTypeLib);
        event.registerTypeHandler(inventoryTypeLib, InventoryType.class);

        event.registerTypeHandler(new EntityHandler<>(Entity.class), Entity.class, CraftClass.forName("entity.CraftEntity"));
        event.registerTypeHandler(new LivingEntityHandler<>(LivingEntity.class), LivingEntity.class, CraftClass.forName("entity.CraftLivingEntity"));
        event.registerTypeHandler(new HumanEntityHandler<>(HumanEntity.class), HumanEntity.class, CraftClass.forName("entity.CraftHumanEntity"));
        event.registerTypeHandler(new PlayerHandler(), Player.class, CraftClass.forName("entity.CraftPlayer"));
        event.registerTypeHandler(new ItemEntityHandler(), Item.class, CraftClass.forName("entity.CraftItem"));
        event.registerTypeHandler(new DisplayHandler<>(Display.class), Display.class, CraftClass.forName("entity.CraftDisplay"));
        event.registerTypeHandler(new TextDisplayHandler(), TextDisplay.class, CraftClass.forName("entity.CraftTextDisplay"));

        event.registerTypeHandler(new InventoryHandler<>(Inventory.class), Inventory.class, CraftClass.forName("inventory.CraftInventory"));
        event.registerTypeHandler(new PlayerInventoryHandler(), PlayerInventory.class, CraftClass.forName("inventory.CraftPlayerInventory"));

        //noinspection UnstableApiUsage
        event.registerTypeHandler(new DamageSourceHandler(), DamageSource.class, CraftClass.forName("damage.CraftDamageSource"));
        event.registerTypeHandler(new EventHandler(), Event.class);

        event.registerTypeHandler(BossBarLib.HANDLER, BossBar.class, CraftClass.forName("boss.CraftBossBar"));
    }

    @org.bukkit.event.EventHandler
    public void onInstancedLibraries(RegisterLocalLibraryEvent event) {
        Game instance = event.getInstance();

        event.registerLib(new GameLib(instance));
        event.registerLib(new DataStoreLib(instance));
        event.registerLib(new WorldLib(instance));
        event.registerLib(new EntityLib(instance));
        event.registerLib(new PlayerLib(instance));
        event.registerLib(new BossBarLib(instance));
    }
}

