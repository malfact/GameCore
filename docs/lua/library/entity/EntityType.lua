---@meta

---Key definitions are not guaranteed to be an exhaustive list of all possible entity types.
---<br>
---See [Minecraft Wiki](https://minecraft.fandom.com/wiki/Java_Edition_data_values#Entities) for the complete list
---in the current version.
---<br><br>
---Version: **Minecraft: 1.20.1**
local lib = {}
_G.EntityType = lib

---This is a data type class.
---@class (exact) EntityType<T>: {}
---@field key string
---@field isSpawnable boolean can the entity be spawned
---@field isAlive boolean is the entity alive
local EntityType = {}

---@type EntityType<Entity>
local Entity
---@type EntityType<LivingEntity>
local LivingEntity

local Item

---@type EntityType<Item>
lib.item = nil
lib.experience_orb =    Entity
lib.area_effect_cloud = Entity
lib.elder_guardian =    LivingEntity
lib.wither_skeleton =   LivingEntity
lib.stray =             LivingEntity
lib.egg =               Entity
lib.leash_knot =        Entity
lib.painting =          Entity
lib.arrow =             Entity
lib.snowball =          Entity
lib.fireball =          Entity
lib.small_fireball =    Entity
lib.ender_pearl =       Entity
lib.eye_of_ender =      Entity
lib.potion =            Entity
lib.experience_bottle = Entity
lib.item_frame =        Entity
lib.wither_skull =      Entity
lib.tnt =               Entity
lib.falling_block =     Entity
lib.firework_rocket =   Entity
lib.husk =              Entity
lib.spectral_arrow =    Entity
lib.shulker_bullet =    Entity
lib.dragon_fireball =   Entity
lib.zombie_villager =   LivingEntity
lib.skeleton_horse =    LivingEntity
lib.zombie_horse =      LivingEntity
lib.armor_stand =       Entity
lib.donkey =            LivingEntity
lib.mule =              LivingEntity
lib.evoker =            LivingEntity
lib.vex =               LivingEntity
lib.vindicator =        LivingEntity
lib.illusioner =        LivingEntity
lib.command_block_minecart = Entity
lib.boat =              Entity
lib.minecart =          Entity
lib.chest_minecart =    Entity
lib.furnace_minecart =  Entity
lib.tnt_minecart =      Entity
lib.hopper_minecart =   Entity
lib.spawner_minecart =  Entity
lib.creeper =           LivingEntity
lib.skeleton =          LivingEntity
lib.spider =            LivingEntity
lib.giant =             LivingEntity
lib.zombie =            LivingEntity
lib.slime =             LivingEntity
lib.ghast =             LivingEntity
lib.zombified_piglin =  LivingEntity
lib.enderman =          LivingEntity
lib.cave_spider =       LivingEntity
lib.silverfish =        LivingEntity
lib.blaze =             LivingEntity
lib.magma_cube =        LivingEntity
lib.ender_dragon =      LivingEntity
lib.wither =            LivingEntity
lib.bat =               LivingEntity
lib.witch =             LivingEntity
lib.endermite =         LivingEntity
lib.guardian =          LivingEntity
lib.shulker =           LivingEntity
lib.pig =               LivingEntity
lib.sheep =             LivingEntity
lib.cow =               LivingEntity
lib.chicken =           LivingEntity
lib.squid =             LivingEntity
lib.wolf =              LivingEntity
lib.mooshroom =         LivingEntity
lib.snow_golem =        LivingEntity
lib.ocelot =            LivingEntity
lib.iron_golem =        LivingEntity
lib.horse =             LivingEntity
lib.rabbit =            LivingEntity
lib.polar_bear =        LivingEntity
lib.llama =             LivingEntity
lib.llama_spit =        LivingEntity
lib.parrot =            LivingEntity
lib.villager =          LivingEntity
lib.end_crystal =       Entity
lib.turtle =            LivingEntity
lib.phantom =           LivingEntity
lib.trident =           Entity
lib.cod =               LivingEntity
lib.salmon =            LivingEntity
lib.pufferfish =        LivingEntity
lib.tropical_fish =     LivingEntity
lib.drowned =           LivingEntity
lib.dolphin =           LivingEntity
lib.cat =               LivingEntity
lib.panda =             LivingEntity
lib.pillager =          LivingEntity
lib.ravager =           LivingEntity
lib.trader_llama =      LivingEntity
lib.wandering_trader =  LivingEntity
lib.fox =               LivingEntity
lib.bee =               LivingEntity
lib.hoglin =            LivingEntity
lib.piglin =            LivingEntity
lib.strider =           LivingEntity
lib.zoglin =            LivingEntity
lib.piglin_brute =      LivingEntity
lib.axolotl =           LivingEntity
lib.glow_item_frame =   Entity
lib.glow_squid =        LivingEntity
lib.goat =              LivingEntity
lib.marker =            Entity
lib.allay =             LivingEntity
lib.chest_boat =        Entity
lib.frog =              LivingEntity
lib.tadpole =           LivingEntity
lib.warden =            LivingEntity
lib.camel =             LivingEntity
---@type EntityType<Display>
lib.block_display = nil
lib.Interaction =       Entity
---@type EntityType<Display>
lib.item_display = nil
lib.sniffer =           LivingEntity
---@type EntityType<TextDisplay>
lib.text_display = nil
lib.breeze  =           LivingEntity
lib.wind_charge =       Entity
lib.breeze_wind_charge =    Entity
lib.armadillo =         LivingEntity
lib.bogged =            LivingEntity
lib.ominous_item_spawner =  Entity
lib.fishing_bobber =    Entity
lib.lightning_bolt =    Entity
---@type EntityType<Player>
lib.player = nil

