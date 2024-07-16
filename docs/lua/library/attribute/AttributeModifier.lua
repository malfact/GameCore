---@meta

---@alias Operation
---| '"add_number"' Adds (or subtracts) the specified amount to the base value.
---| '"add_scalar"' Adds this scalar of amount to the base value.
---| '"multiply_scalar_1' Multiply amount by this value, after adding 1 to it.

---@alias SlotGroup
---| 'any'
---| 'mainhand'
---| 'offhand'
---| 'hand'
---| 'feet'
---| 'legs'
---| 'chest'
---| 'head'
---| 'armor'
---| 'body'

---@class AttributeModifier : userdata
---@field name string
---@field amount number
---@field operation Operation
---@field slotGroup SlotGroup

local lib = {}
_G.AttributeModifier = lib

---Creates a new attribute modifier.
---@param key string
---@param amount number
---@param operation Operation
---@param slotGroup? SlotGroup
function lib.new(key, amount, operation, slotGroup) end