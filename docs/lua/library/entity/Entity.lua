---@meta

---@class Entity: userdata
---@field name string ***(read-only)***
---@field uuid string ***(read-only)***
---@field isDead boolean ***(read-only)***
---@field isValid boolean ***(read-only)***
---@field isTracked boolean ***(read-only)***
---
---@field location Location
---@field velocity Vector3
---@field persistent boolean
---@field invisible boolean
---@field invulnerable boolean
---@field silent boolean
---@field noPhysics boolean
---@field gravity boolean
---@field glowing boolean
---@field sneaking boolean
---@field customName string | nil
---@field customNameVisible boolean
---@field type EntityType ***(read-only)***
---@field onGround boolean ***(read-only)***
---@field underWater boolean ***(read-only)***
---@field inWater boolean ***(read-only)***
---@field inLava boolean ***(read-only)***
local Entity = {}

---Spawns the entity.
---@param location? Location
---@return boolean | nil "was spawning was successful or `nil` if `Entity.Type == 'player' or there was an error`, otherwise "
function Entity:spawn(location) end

---Removes the entity
---@return boolean | nil "was removing was successful or `nil` if `Entity.Type == 'player' or there was an error`, otherwise "
function Entity:remove() end

---Checks if this equals other
---@param other any
---@return boolean
function Entity:equals(other) end

---Converts the entity to a string
---@return string
function Entity:toString() end

local lib = {}
_G.Entity = lib

---Creates a new Entity.
---<br>
---**Note:** *This does not spawn the entity.*
---<br>
---*If a location is not specified, it is set to `Location.new("world
---<br>
---@see EntityType.isSpawnable
---@param type EntityType<Entity>
---@param location? Location | Vector3
---@return Entity
---@overload fun(type: EntityType<LivingEntity>, location?: Location): LivingEntity
---@overload fun(type: EntityType<HumanEntity>, location?: Location): HumanEntity
---@overload fun(type: EntityType<Player>, location?: Location): Player
function lib.new(type, location) end

Entity.onAreaEffectCloudApply = Callback("Event")
Entity.onArrowBodyCountChange = Callback("Event")
Entity.onBatToggleSleep = Callback("Event")
Entity.onCreatureSpawn = Callback("Event")
Entity.onCreeperPower = Callback("Event")
Entity.onEnderDragonChangePhase = Callback("Event")
Entity.onAirChange = Callback("Event")
Entity.onBreakDoor = Callback("Event")
Entity.onBreed = Callback("Event")
Entity.onChangeBlock = Callback("Event")
Entity.onCombustByBlock = Callback("Event")
Entity.onCombustByEntity = Callback("Event")
Entity.onCombust = Callback("Event")
Entity.onDamageByBlock = Callback("Event")
Entity.onDamageByEntity = Callback("Event")
Entity.onDamage = Callback("Event")
Entity.onDeath = Callback("Event")
Entity.onDismount = Callback("Event")
Entity.onDropItem = Callback("Event")
Entity.onEnterBlock = Callback("Event")
Entity.onEnterLoveMode = Callback("Event")
Entity.onExhaustion = Callback("Event")
Entity.onExplode = Callback("Event")
Entity.onInteract = Callback("Event")
Entity.onMount = Callback("Event")
Entity.onPickupItem = Callback("Event")
Entity.onPlace = Callback("Event")
Entity.onPortalEnter = Callback("Event")
Entity.onPortal = Callback("Event")
Entity.onPortalExit = Callback("Event")
Entity.onPoseChange = Callback("Event")
Entity.onPotionEffect = Callback("Event")
Entity.onRegainHealth = Callback("Event")
Entity.onResurrect = Callback("Event")
Entity.onShootBow = Callback("Event")
Entity.onSpawn = Callback("Event")
Entity.onSpellCast = Callback("Event")
Entity.onTame = Callback("Event")
Entity.onTarget = Callback("Event")
Entity.onTeleport = Callback("Event")
Entity.onToggleGlide = Callback("Event")
Entity.onToggleSwim = Callback("Event")
Entity.onTransform = Callback("Event")
Entity.onUnleash = Callback("Event")
Entity.onExpBottle = Callback("Event")
Entity.onExplosionPrime = Callback("Event")
Entity.onFireworkExplode = Callback("Event")
Entity.onFoodLevelChange = Callback("Event")
Entity.onHorseJump = Callback("Event")
Entity.onItemDespawn = Callback("Event")
Entity.onItemMerge = Callback("Event")
Entity.onItemSpawn = Callback("Event")
Entity.onLingeringPotionSplash = Callback("Event")
Entity.onPiglinBarter = Callback("Event")
Entity.onPigZap = Callback("Event")
Entity.onPigZombieAnger = Callback("Event")
Entity.onPotionSplash = Callback("Event")
Entity.onProjectileHit = Callback("Event")
Entity.onSheepDyeWool = Callback("Event")
Entity.onSheepRegrowWool = Callback("Event")
Entity.onSlimeSplit = Callback("Event")
Entity.onSpawnerSpawn = Callback("Event")
Entity.onStriderTemperatureChange = Callback("Event")
Entity.onVillagerAcquireTrade = Callback("Event")
Entity.onVillagerCareerChange = Callback("Event")
Entity.onVillagerReplenishTrade = Callback("Event")
Entity.onCreeperIgnite = Callback("Event")
Entity.onEnderDragonFireballHit = Callback("Event")
Entity.onEnderDragonFlame = Callback("Event")
Entity.onEnderDragonShootFireball = Callback("Event")
Entity.onEndermanAttackPlayer = Callback("Event")
Entity.onEndermanEscape = Callback("Event")
Entity.onAddToWorld = Callback("Event")
Entity.onJump = Callback("Event")
Entity.onKnockbackByEntity = Callback("Event")
Entity.onPathfind = Callback("Event")
Entity.onRemoveFromWorld = Callback("Event")
Entity.onTeleportEndGateway = Callback("Event")
Entity.onZap = Callback("Event")
Entity.onExperienceOrbMerge = Callback("Event")
Entity.onPhantomPreSpawn = Callback("Event")
Entity.onPreCreatureSpawn = Callback("Event")
Entity.onSkeletonHorseTrap = Callback("Event")
Entity.onSlimeChangeDirection = Callback("Event")
Entity.onSlimePathfind = Callback("Event")
Entity.onSlimeSwim = Callback("Event")
Entity.onSlimeTargetLivingEntity = Callback("Event")
Entity.onSlimeWander = Callback("Event")
Entity.onThrownEggHatch = Callback("Event")
Entity.onTurtleGoHome = Callback("Event")
Entity.onWitchConsumePotion = Callback("Event")
Entity.onWitchReadyPotion = Callback("Event")
Entity.onWitchThrowPotion = Callback("Event")
Entity.onElderGuardianAppearance = Callback("Event")
Entity.onCompostItem = Callback("Event")
Entity.onDamageItem = Callback("Event")
Entity.onDye = Callback("Event")
Entity.onFertilizeEgg = Callback("Event")
Entity.onInsideBlock = Callback("Event")
Entity.onKnockback = Callback("Event")
Entity.onLoadCrossbow = Callback("Event")
Entity.onMove = Callback("Event")
Entity.onPortalReady = Callback("Event")
Entity.onPushedByEntityAttack = Callback("Event")
Entity.onToggleSit = Callback("Event")
Entity.onPufferFishStateChange = Callback("Event")
Entity.onShulkerDuplicate = Callback("Event")
Entity.onTameableDeathMessage = Callback("Event")
Entity.onWardenAngerChange = Callback("Event")
Entity.onWaterBottleSplash = Callback("Event")