---@meta

---@type {[string] : Material}
local lib = {}
_G.Material = lib

---@class (exact) Material : userdata
---@field maxStackSize integer
---@field maxDurability number
---@field isBlock boolean
---@field isRecord boolean
---@field isSolid boolean
---@field isAir boolean
---@field isFlammable boolean
---@field isBurnable boolean
---@field isFuel boolean
---@field isOccluding boolean
---@field hasGravity boolean
---@field isItem boolean
---@field hardness number | nil
---@field slipperiness number | nil
---@field isCompostable boolean
---@field compostChance number | nil
local Material = {}