---@meta

---@class Enchantment : userdata
---@field maxLevel integer in range `[1,...)`
---@field startLevel integer in range `[1,...)`
---@field isTradleable boolean
---@field isDiscoverable boolean
---@field anvilCost integer in range `[0,...)`
local Enchantment = {}

---Check if this enchantment conflicts with another enchantment
---@param other Enchantment
---@return boolean
function Enchantment:conflicstWith(other) end

---Checks if this Enchantment may be applied to the given ItemStack.
---@param item ItemStack
---@return boolean
function Enchantment:canEnchantItem(item) end

---Get the name of the enchantment with its applied level.
---@param level? integer
---@return string
function Enchantment:displayName(level) end


---Key definitions are not guaranteed to be an exhaustive list of all possible entity types.
---<br>
---See [Minecraft Wiki](https://minecraft.fandom.com/wiki/Java_Edition_data_values#Enchantments) for the complete list
---in the current version.
---<br><br>
---Version: **Minecraft: 1.20.1**
---@type {[string] : Enchantment}
local lib = {}
_G.Enchantment = lib

lib.protection = nil
lib.fire_protection = nil
lib.feather_falling = nil
lib.blast_protection = nil
lib.projectile_protection = nil
lib.respiration = nil
lib.aqua_affinity = nil
lib.thorns = nil
lib.depth_strider = nil
lib.frost_walker = nil
lib.binding_curse = nil
lib.sharpness = nil
lib.smite = nil
lib.bane_of_arthropods = nil
lib.knockback = nil
lib.fire_aspect = nil
lib.looting = nil
lib.sweeping_edge = nil
lib.efficiency = nil
lib.silk_touch = nil
lib.unbreaking = nil
lib.fortune = nil
lib.power = nil
lib.punch = nil
lib.flame = nil
lib.infinity = nil
lib.luck_of_the_sea = nil
lib.lure = nil
lib.loyalty = nil
lib.impaling = nil
lib.riptide = nil
lib.channeling = nil
lib.multishot = nil
lib.quick_charge = nil
lib.piercing = nil
lib.density = nil
lib.breach = nil
lib.wind_burst = nil
lib.mending = nil
lib.vanishing_curse = nil
lib.soul_speed = nil
lib.swift_sneak = nil