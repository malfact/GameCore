---@meta

---@class Inventory
---@field size integer
---@field maxStackSize integer
---@field contents ItemStack[]
---@field storageContents ItemStack[]
---@field isEmtpty boolean
---@field location nil | Location
local Inventory = {}

---Stores the given ItemStacks in the inventory.
---@param item ItemStack
---@return ItemStack[]
function Inventory:addItem(item) end

---Removes the given ItemStacks from the storage contents of the inventory.
---@param ... ItemStack
---@return ItemStack[]
function Inventory:removeItem(...) end

---Checks if the inventory contains any ItemStacks with the given material, 
---adding to at least the minimum amount specified.
---@param item ItemStack the item
---@param amount? integer in range `[1,...)`
function Inventory:contains(item, amount) end

---Finds the first slot in the inventory containing an ItemStack with the given material.
---@param item ItemStack | Material
---@return integer
function Inventory:first(item) end

---Returns the first empty Slot.
---@return integer
function Inventory:firstEmpty() end

---Clears the inventory
function Inventory:clear() end

---Removes all stacks in the inventory matching the given stack.
---@param item ItemStack | Material
function Inventory:remove(item) end

---Closes the inventory for all viewers.
function Inventory:close() end

---@class PlayerInventory : Inventory
---@field mainHand ItemStack | nil
---@field offHand ItemStack | nil
---@field heldItemSlot integer
---@field head ItemStack | nil
---@field chest ItemStack | nil
---@field legs ItemStack | nil
---@field feet ItemStack | nil
local PlayerInventory = {}