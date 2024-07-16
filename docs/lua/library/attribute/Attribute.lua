---@meta

---@class Attribute
---@field key string

---Key definitions are not guaranteed to be an exhaustive list of all possible types.
---<br>
---See [Minecraft Wiki](https://minecraft.fandom.com/wiki/Attribute#Attributes) for the complete list
---in the current version.
---<br><br>
---Version: **Minecraft: 1.20.1**
---@type {[string]:Attribute}
local lib = {}
_G.Attribute = lib

lib["generic.max_health"] = nil
lib["generic.follow_range"] = nil
lib["generic.knockback_resistance"] = nil
lib["generic.movement_speed"] = nil
lib["generic.flying_speed"] = nil
lib["generic.attack_damage"] = nil
lib["generic.attack_knockback"] = nil
lib["generic.attack_speed"] = nil
lib["generic.armor"] = nil
lib["generic.armor_toughness"] = nil
lib["generic.fall_damage_multiplier"] = nil
lib["generic.luck"] = nil
lib["generic.max_absorption"] = nil
lib["generic.safe_fall_distance"] = nil
lib["generic.scale"] = nil
lib["generic.step_height"] = nil
lib["generic.gravity"] = nil
lib["generic.jump_strength"] = nil
lib["generic.burning_time"] = nil
lib["generic.explosion_knockback_resistance"] = nil
lib["generic.movement_efficiency"] = nil
lib["generic.oxygen_bonus"] = nil
lib["generic.water_movement_efficiency"] = nil
lib["player.block_interaction_range"] = nil
lib["player.entity_interaction_range"] = nil
lib["player.block_break_speed"] = nil
lib["player.mining_efficiency"] = nil
lib["player.sneaking_speed"] = nil
lib["player.submerged_mining_speed"] = nil
lib["player.sweeping_damage_ratio"] = nil
lib["zombie.spawn_reinforcements"] = nil