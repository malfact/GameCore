---@meta

---@class Player : HumanEntity
---@field isOnline boolean ***(read-only)***
---@field isOnGround nil
---@field customName nil
---@field displayName string
---@field playerListName string
---@field playerListHeader string
---@field playerListFooter string
---@field compassTarget Location | nil
---@field sprinting boolean
---@field sleepingIgnored boolean
---@field respawnLocation Location
---@field expCooldown integer
---@field exp number
---@field level integer
---@field totalExp infowhat
---@field allowFlight boolean
---@field flyingFallDamage boolean
---@field flying boolean
---@field spectatorTarget Entity | nil
---@field wardenWarningCooldown infowhat
---@field wardenTimeSinceLastWarning infowhat
---@field wardenWarningLevel integer
---@field flySpeed number
---@field walkSpeed number
---@field isTimeRelative boolean ***(read-only)***
---@field totalExpPoints integer ***(read-only)***
---@field expForNextLevel integer ***(read-only)***
---@field cooldownPeriod integer ***(read-only)***
---@field idleDuration integer ***(read-only)***
---@field inventory PlayerInventory ***(read-only)***
local Player = {}

---Sends a raw message to the player.
---@param ... string
function Player:sendMessageRaw(...) end

---Sends a chat message to the player.
---@param ... string
function Player:sendMessage(...) end

---Sends a message on the action bar.
---@param ... string
function Player:sendActionBar(...) end

---Sets the current time on the player's client. When relative is true the player's
---time will be kept synchronized to its world time with the specified offset.
---@param time integer
---@param relative? boolean defaults to `false`
function Player:setTime(time, relative) end

---Returns the player's current timestamp.
---@return number
function Player:getTime() end

---Returns the player's current time offset relative to server time, 
---or the current player's fixed time if the player's time is absolute.
---@return number
function Player:getTimeOffset() end

---Restores the normal condition where the player's time is synchronized with the server time.
function Player:resetTime() end

---Sets the type of weather the player will see. 
---When used, the weather status of the player is locked until resetPlayerWeather() is used.
---@param weather WeatherType
function Player:setWeather(weather) end

---Returns the type of weather the player is currently experiencing.
---@return WeatherType
function Player:getWeather() end

---Restores the normal condition where the player's weather is controlled by server conditions.
function Player:resetWeather() end

---Gives the player the amount of experience specified.
---@param exp? integer defaults to `1`
function Player:giveExp(exp) end

---Gives the player the amount of experience levels specified. 
---Levels can be taken by specifying a negative amount.
---@param levels? integer defaults to `1`
function Player:giveExpLevels(levels) end

---Checks to see if an entity has been visually hidden from this player.
---@param entity Entity
---@return boolean
function Player:canSee(entity) end

---Shows the player a title.
---@param title string
---@param subtitle? string
function Player:showTitle(title,subtitle) end

---Resets the title displayed to the player.
function Player:resetTitle() end

---Spawns the particle (the number of times specified by count) at the target location.
---comment
---@param particle string
---@param loc Location | Vector3
---@param count integer in range `[1,...)`
---@param offsetX number
---@param offsetY number
---@param offsetZ number
---@param speed number
---@overload fun(self:Player, particle:string, x:number, y:number, z:number, count:integer, offsetX:number, offsetY:number, offsetZ: number, speed:number)
function Player:spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, speed) end

---Returns the percentage of attack power available based on the cooldown (zero to one).
---@param adjustTicks integer
---@return number
function Player:getCooledAttackStrength(adjustTicks) end

---Reset the cooldown counter to 0, effectively starting the cooldown period.
function Player:resetCooldown() end

---Sets the player's rotation.
function Player:setRotation(yaw, pitch) end

---Causes the player to look towards the given position.
---@param x number
---@param y number
---@param z number
---@param playerAnchor LookAnchor
---@overload fun(self: Player, entity: Entity, playerAnchor: LookAnchor, entityAnchor:LookAnchor)
function Player:lookAt(x, y, z, playerAnchor) end

---Displays elder guardian effect and optionally plays a sound.
---@param silent boolean
function Player:showElderGuardian(silent) end

---Resets this player's idle duration.
function Player:resetIdleDuration() end

---Play a sound for a player at the location.
---@param entity Entity
---@param sound string
---@param volume number 
---@param pitch number
---@overload fun(self:Player, sound:string, volume:number, pitch:number)
function Player:playSound(entity, sound, volume, pitch) end

---Stop the specified sound from playing.
---@param sound string | nil defaults to `nil` *(will stop all sounds)*
function Player:stopSound(sound) end

---***Note:*** *This contains all available player events as of `7/15/2024`*
local lib = {}
_G.Player = lib

lib.onBedEnter = Callback("PlayerEvent")
lib.onBedLeave = Callback("PlayerEvent")
lib.onBucketEmpty = Callback("PlayerEvent")
lib.onBucketEntity = Callback("PlayerEvent")
lib.onBucketFill = Callback("PlayerEvent")
lib.onChangedWorld = Callback("PlayerEvent")
lib.onDropItem = Callback("PlayerEvent")
lib.onEditBook = Callback("PlayerEvent")
lib.onEggThrow = Callback("PlayerEvent")
lib.onExpChange = Callback("PlayerEvent")
lib.onExpCooldownChange = Callback("PlayerEvent")
lib.onFish = Callback("PlayerEvent")
lib.onGameModeChange = Callback("PlayerEvent")
lib.onInteractAtEntity = Callback("PlayerEvent")
lib.onInteractEntity = Callback("PlayerEvent")
lib.onInteract = Callback("PlayerEvent")
lib.onItemBreak = Callback("PlayerEvent")
lib.onItemConsume = Callback("PlayerEvent")
lib.onItemDamage = Callback("PlayerEvent")
lib.onItemHeld = Callback("PlayerEvent")
lib.onItemMend = Callback("PlayerEvent")
lib.onKick = Callback("PlayerEvent")
lib.onLevelChange = Callback("PlayerEvent")
lib.onMove = Callback("PlayerEvent")
lib.onPickupArrow = Callback("PlayerEvent")
lib.onPortal = Callback("PlayerEvent")
lib.onRespawn = Callback("PlayerEvent")
lib.onRiptide = Callback("PlayerEvent")
lib.onShearEntity = Callback("PlayerEvent")
lib.onSwapHandItems = Callback("PlayerEvent")
lib.onTakeLecternBook = Callback("PlayerEvent")
lib.onTeleport = Callback("PlayerEvent")
lib.onToggleFlight = Callback("PlayerEvent")
lib.onToggleSneak = Callback("PlayerEvent")
lib.onToggleSprint = Callback("PlayerEvent")
lib.onUnleashEntity = Callback("PlayerEvent")
lib.onVelocity = Callback("PlayerEvent")
lib.onDeath = Callback("PlayerEvent")
lib.onLeashEntity = Callback("PlayerEvent")
lib.onArmorChange = Callback("PlayerEvent")
lib.onAttackEntityCooldownReset = Callback("PlayerEvent")
lib.onElytraBoost = Callback("PlayerEvent")
lib.onJump = Callback("PlayerEvent")
lib.onLaunchProjectile = Callback("PlayerEvent")
lib.onPickupExperience = Callback("PlayerEvent")
lib.onPostRespawn = Callback("PlayerEvent")
lib.onReadyArrow = Callback("PlayerEvent")
lib.onSetSpawn = Callback("PlayerEvent")
lib.onStartSpectatingEntity = Callback("PlayerEvent")
lib.onStopSpectatingEntity = Callback("PlayerEvent")
lib.onTeleportEndGateway = Callback("PlayerEvent")
lib.onUseUnknownEntity = Callback("PlayerEvent")
lib.onArmSwing = Callback("PlayerEvent")
lib.onBedFailEnter = Callback("PlayerEvent")
lib.onChangeBeaconEffect = Callback("PlayerEvent")
lib.onDeepSleep = Callback("PlayerEvent")
lib.onFailMove = Callback("PlayerEvent")
lib.onFlowerPotManipulate = Callback("PlayerEvent")
lib.onInventorySlotChange = Callback("PlayerEvent")
lib.onItemCooldown = Callback("PlayerEvent")
lib.onItemFrameChange = Callback("PlayerEvent")
lib.onLecternPageChange = Callback("PlayerEvent")
lib.onLoomPatternSelect = Callback("PlayerEvent")
lib.onNameEntity = Callback("PlayerEvent")
lib.onOpenSign = Callback("PlayerEvent")
lib.onPickItem = Callback("PlayerEvent")
lib.onPurchase = Callback("PlayerEvent")
lib.onShieldDisable = Callback("PlayerEvent")
lib.onStonecutterRecipeSelect = Callback("PlayerEvent")
lib.onStopUsingItem = Callback("PlayerEvent")
lib.onTrade = Callback("PlayerEvent")
lib.onPreAttackEntity = Callback("PlayerEvent")
lib.onShearBlock = Callback("PlayerEvent")