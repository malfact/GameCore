---@meta

DataStore = {}

---Gets the Player's data saved under `key`.
---@param player Player
---@return any
function DataStore.getPlayerData(player, key) end

---Sets the Player's data saved under `key` to `value`.
---<br>
---**Note:** *Use `DataStore.clearPlayerData` to clear the player's data.*
---@param player Player
---@param key string
---@param value any
function DataStore.setPlayerData(player, key, value) end

---Clears the Player's data saved under `key`.
---@param player Player
---@param key string
function DataStore.clearPlayerData(player, key) end

---Gets the Game's data saved under `key`.
---@param key string
function DataStore.getGameData(key) end

---Sets the Game's data saved under `key` to `value`.
---<br>
---**Note:** *Use `DataStore.clearGameData` to clear the Game's data.*
---@param key string
---@param value any
function DataStore.setGameData(key, value) end

---Clears the Game's data saved under `key`.
---@param key string
function DataStore.clearGameData(key) end