---@meta

---@class ItemStack : userdata
---@field type Material
---@field amount integer in range `[0,...)`
---@field isEmpty boolean
---@field meta ItemMeta
local ItemStack = {}

---Clones the ItemStack.
---@return ItemStack
function ItemStack:clone() end

function ItemStack:getEnchant(enchant) end
function ItemStack:addEnchant() end
function ItemStack:removeEnchant() end

local lib = {}
_G.lib = lib

---Creates a new ItemStack.
---@param material string | Material
---@param amount? integer in range `[0,...)`
---@return ItemStack
function lib.new(material, amount) end