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
        event.registerTypeHandler(itemStackLib, ItemStack.class);

        var itemMetaLib = new ItemMetaLib();
        event.registerLib(itemMetaLib);
        event.registerTypeHandler(itemMetaLib, ItemMeta.class);

        var attributeLib = new AttributeLib();
        event.registerLib(attributeLib);
        event.registerTypeHandler(attributeLib, AttributeInstance.class);

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

        event.registerTypeHandler(new EntityHandler<>(Entity.class), Entity.class);
        event.registerTypeHandler(new LivingEntityHandler<>(LivingEntity.class), LivingEntity.class);
        event.registerTypeHandler(new HumanEntityHandler<>(HumanEntity.class), HumanEntity.class);
        event.registerTypeHandler(new PlayerHandler(), Player.class);
        event.registerTypeHandler(new ItemEntityHandler(), Item.class);
        event.registerTypeHandler(new DisplayHandler<>(Display.class), Display.class);
        event.registerTypeHandler(new TextDisplayHandler(), TextDisplay.class);

        event.registerTypeHandler(new InventoryHandler<>(Inventory.class), Inventory.class);
        event.registerTypeHandler(new PlayerInventoryHandler(), PlayerInventory.class);

        //noinspection UnstableApiUsage
        event.registerTypeHandler(new DamageSourceHandler(), DamageSource.class);
        event.registerTypeHandler(new EventHandler(), Event.class);

        var bossBarLib = new BossBarLib();
        event.registerLib(bossBarLib);
        event.registerTypeHandler(bossBarLib, BossBar.class);
    }

    @org.bukkit.event.EventHandler
    public void onInstancedLibraries(RegisterLocalLibraryEvent event) {
        Game instance = event.getInstance();

        event.registerLib(new GameLib(instance));
        event.registerLib(new DataStoreLib(instance));
        event.registerLib(new WorldLib(instance));
        event.registerLib(new EntityLib(instance));
        event.registerLib(new PlayerLib(instance));
    }
}

