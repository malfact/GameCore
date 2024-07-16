---@meta

---@alias BarColor "pink" | "blue" | "red" | "green" | "yellow" | "purple" | "white"
---@alias BarStyle "solid" | "segmented_6" | "segmented_10" | "segmented_12" | "segmented_20"

---@class BossBar
---@field title string
---@field color BarColor
---@field style BarStyle
---@field progress number in range `[0.0,1.0]`
---@field visible boolean
local BossBar = {}

---Shows the BossBar for a player.
function BossBar:addPlayer(player) end

---Hides the BossBar for a player.
function BossBar:removePlayer(player) end

---Hides the BossBar for all players.
function BossBar:removeAll(player) end

local lib = {}
_G.BossBar = lib

---Creates a new BossBar
---comment
---@param title? string
---@param color? BarColor
---@param style? BarStyle
---@return BossBar
function lib.new(title, color, style) end