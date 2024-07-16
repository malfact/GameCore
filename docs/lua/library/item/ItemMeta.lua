---@meta

---@alias ItemRarity "common" | "uncommon" | "rare" | "epic"

---@class ItemMeta
---@field unbreakable true
---@field itemName string | nil
---@field displayName string | nil
---@field maxStackSize integer | nil
---@field fireResistant boolean
---@field rarity ItemRarity | nil 
---@field lore string[] | nil
---@field asComponentString string ***(read-only)***
local ItemMeta = {}