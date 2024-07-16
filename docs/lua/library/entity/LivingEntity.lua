---@meta

---@alias _PotionEffect string | PotionEffectType
---@alias _Attribute string | Attribute

---@class LivingEntity: Entity
---@field ai boolean
---@field air integer
---@field maxAir integer
---@field lastDamage number
---@field noDamageTicks integer
---@field noActionTicks integer
---@field canPickupItems boolean
---@field collidable boolean
---@field jumping boolean
---@field bodyYaw number
---@field health number
---@field eyeHeight number ***(read-only)***
---@field rawEyeHeight number ***(read-only)***
---@field eyeLocation Location ***(read-only)***
---@field leashed boolean ***(read-only)***
---@field swimming boolean ***(read-only)***
---@field sleeping boolean ***(read-only)***
---@field climbing boolean ***(read-only)***
---@field upwardsMovement number ***(read-only)***
---@field sidewaysMovement number ***(read-only)***
---@field forwardsMovement number ***(read-only)***
---@field canBreathUnderwater boolean ***(read-only)***
local LivingEntity = {}

---Adds a potion effect to the entity.
---@param effect _PotionEffect
---@param duration integer
---@param amplifier integer
---@param ambient? boolean
---@param particles? boolean
---@return boolean
function LivingEntity:addPotionEffect(effect, duration, amplifier, ambient, particles) end

---Chekcs if the entity has a potion effect.
---@param effect _PotionEffect
---@return boolean
function LivingEntity:hasPotionEffect(effect) end

---Clears a potion effect from the entity.
---@param effect  _PotionEffect
---@return nil | boolean
function LivingEntity:clearPotionEffect(effect) end

---Gets the attribute instance specified by attribute.
---@param attribute _Attribute
---@return nil | AttributeInstance
function LivingEntity:getAttribute(attribute) end