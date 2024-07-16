---@meta

---@class PotionEffectType : userdata
---@field isInstant boolean
---@field category "beneficial" | "harmful" | "neutral"
---@field color string

---Key definitions are not guaranteed to be an exhaustive list of all possible types.
---<br>
---See [Minecraft Wiki](https://minecraft.fandom.com/wiki/Java_Edition_data_values#Effects) for the complete list
---in the current version.
---<br><br>
---Version: **Minecraft: 1.20.1**
---@type {[string] : PotionEffectType}
local lib = {}
_G.lib = lib

lib.speed = nil
lib.slowness = nil
lib.haste = nil
lib.mining_fatigue = nil
lib.strength = nil
lib.instant_health = nil
lib.instant_damage = nil
lib.jump_boost = nil
lib.nausea = nil
lib.regeneration = nil
lib.resistance = nil
lib.fire_resistance = nil
lib.water_breathing = nil
lib.invisibility = nil
lib.blindness = nil
lib.night_vision = nil
lib.hunger = nil
lib.weakness = nil
lib.poison = nil
lib.wither = nil
lib.health_boost = nil
lib.absorption = nil
lib.saturation = nil
lib.glowing = nil
lib.levitation = nil
lib.luck = nil
lib.unluck = nil
lib.slow_falling = nil
lib.conduit_power = nil
lib.dolphins_grace = nil
lib.bad_omen = nil
lib.hero_of_the_village = nil
lib.darkness = nil
lib.trial_omen = nil
lib.raid_omen = nil
lib.wind_charged = nil
lib.weaving = nil
lib.oozing = nil
lib.infested = nil

---@class PotionType : userdata
---@field effectType PotionEffectType[] ***Not yet implemented***
---@field isUpgradeable boolean
---@field isExtendable boolean
---@field maxLevel number

---Reflects and matches each potion state that can be obtained from the Creative mode
---<br>
---Key definitions are not guaranteed to be an exhaustive list of all possible types.
---<br>
---See [Minecraft Wiki](https://minecraft.fandom.com/wiki/Potion) for the complete list
---in the current version.
---<br><br>
---Version: **Minecraft: 1.20.1**
---@type {[string] : PotionType}
local lib = {}
_G.PotionType = lib
lib.water = nil
lib.mundane = nil
lib.thick = nil
lib.awkward = nil
lib.night_vision = nil
lib.long_night_vision = nil
lib.invisibility = nil
lib.long_invisibility = nil
lib.leaping = nil
lib.long_leaping = nil
lib.strong_leaping = nil
lib.fire_resistance = nil
lib.long_fire_resistance = nil
lib.swiftness = nil
lib.long_swiftness = nil
lib.strong_swiftness = nil
lib.slowness = nil
lib.long_slowness = nil
lib.strong_slowness = nil
lib.water_breathing = nil
lib.long_water_breathing = nil
lib.healing = nil
lib.strong_healing = nil
lib.harming = nil
lib.strong_harming = nil
lib.poison = nil
lib.long_poison = nil
lib.strong_poison = nil
lib.regeneration = nil
lib.long_regeneration = nil
lib.strong_regeneration = nil
lib.strength = nil
lib.long_strength = nil
lib.strong_strength = nil
lib.weakness = nil
lib.long_weakness = nil
lib.luck = nil
lib.turtle_master = nil
lib.long_turtle_master = nil
lib.strong_turtle_master = nil
lib.slow_falling = nil
lib.long_slow_falling = nil
lib.wind_charged = nil
lib.weaving = nil
lib.oozing = nil
lib.infested = nil