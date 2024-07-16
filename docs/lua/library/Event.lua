---@meta

---This is a definition-only utility method. 
---<br>
---**It is not present in the runtime API.**
---@generic T : Event
---@param type `T`
---@return fun(callback: fun(event: T))
function Callback(type) end

---@class Event
---@field canceled boolean | nil *(will be `nil` if the event does not implement Cancelable)*

---@class GameEvent
---@field name string

---@class PlayerEvent : Event
---@field player Player

---@class PlayerConnectEvent : PlayerEvent
---@field spawnLocation Location

---@class PlayerDisconnectEvent : PlayerEvent
---@field quitReason "disconnected" | "kicked" | "timed_out" | "erroneous_state" ***(read-only)***

---@class PlayerTriggerEvent : PlayerEvent
---@field trigger string ***(read-only)***

---@type PlayerDisconnectEvent
local a = nil