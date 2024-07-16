---@meta

---The current Game instance.
---@class Game
---@field time integer **(read only)** the current running time of the game
---@field name string **(read only)** the registered name of the Game (usually the file name)
---@field displayName string the display name of the Game
Game = {}
_G.Game = Game

--- Get the players registered to the Game instance.
---@return table<Player>
function Game.getPlayers() end

--- Get the entities registered to the Game instance.
---@return table<Player>
function Game.getEntities() end

--- Force a player to leave the Gme instance.
---@param player Player the player
---@return boolean
function Game.leaveGame(player) end

--- Check if a player is registered to the Game instance.
---@param player Player the player
---@return boolean
function Game.isPlaying(player) end

---Stops the Game instance.
function Game.stopGame() end

Game.onGameStart =          Callback("GameEvent")
Game.onGameStop =           Callback("GameEvent")
Game.onPlayerJoin =         Callback("PlayerEvent")
Game.onPlayerLeave =        Callback("PlayerEvent")
Game.onPlayerConnect =      Callback("PlayerConnectEvent")
Game.onPlayerDisconnect =   Callback("PlayerDisconnectEvent")
Game.onPlayerSpawn =        Callback("PlayerEvent")
Game.onPlayerTrigger =      Callback("PlayerTriggerEvent")