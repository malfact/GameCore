---@meta

---@alias GameMode
---| '"creative'
---| '"survival'
---| '"adventure"'
---| '"spectator"'

---@class HumanEntity : LivingEntity
---@field openInventory Inventory
---@field gamemode GameMode
---@field exhaustion number
---@field saturation number
---@field foodLevel number
---@field saturatedRegenRate integer
---@field unsaturatedRegenRate integer
---@field starvationRate integer
---@field lastDeathLocation integer | nil
---@field inventory Inventory ***(read-only)***
---@field enderchest Inventory ***(read-only)***
---@field itemOnCursor ItemStack | nil ***(read-only)***
---@field isDeeplySleeping boolean ***(read-only)***
---@field sleepTicks integer ***(read-only)***
---@field bedLocation Location | nil ***(read-only)***
---@field isBlocking boolean ***(read-only)***
---@field expToLevel integer ***(read-only)***
---@field attackCooldown number ***(read-only)***
local HumanEntity = {}

---Does the entity have a cooldown for the material.
---@param material Material | string
---@return boolean
function HumanEntity:hasCooldown(material) end

---Set the entity's cooldown for the material.
---@param material Material | string
---@param value integer
function HumanEntity:setCooldown(material, value) end

---Attempt to make the entity sleep at a location.
---@param location Location | Vector3
---@param force? boolean
---@return boolean
function HumanEntity:sleep(location, force) end

---Cases the entity to wakeup if they are sleeping.
---@param setSpawnLocation boolean
function HumanEntity:wakeup(setSpawnLocation) end

---Forces the entity to drop the item in their hand.
---@param dropAll? any
---@return boolean
function HumanEntity:dropItem(dropAll) end