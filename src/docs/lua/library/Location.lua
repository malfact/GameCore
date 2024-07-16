---@meta

---@alias World string

local lib = {}
_G.Location = lib

---Creates a new Location.
---@param world World
---@param x number
---@param y number
---@param z number
---@param yaw number
---@param pitch number
---@return Location
---@overload fun(world: World, x: number, y:number, z:number): Location
---@overload fun(world: World, vec: Vector3, yaw: number, pitch: number): Location
---@overload fun(world: World, vec: Vector3): Location
function lib.new(world, x, y, z, yaw, pitch) end

---@class Location: userdata
---@field world string   ***(read-only)***
---@field x number       ***(read-only)***
---@field y number       ***(read-only)***
---@field z number       ***(read-only)***
---@field blockX integer ***(read-only)***
---@field blockY integer ***(read-only)***
---@field blockZ integer ***(read-only)***
---@field position Vector3 the location converted to a Vector3
---@field direction Vector3
---@field yaw number
---@field pitch number
---@operator add(Location | Vector3 | number): Location
---@operator sub(Location | Vector3 | number): Location
---@operator mul(number): Location
---@operator div(number): Location
---@operator unm: Location
---@operator len: number
local Location = {}

---Clones the Location.
---@return Location
function Location:clone() end

---Multiplies the Location by `-1`.
---@return Location
function Location:negate() end

---@param other Location | Vector3 | number 
---@return Location
function Location:add(other) end

---@param other Location | Vector3 | number 
---@return Location
function Location:sub(other) end

---@param value number
---@return Location
function Location:multiply(value) end

---@param value number
---@return Location
function Location:divide(value) end

---@return Location
function Location:toHighest() end

---Get the length of the location.
---@return number
function Location:length() end

---Get the square length of the location.
---@return number
function Location:squareLength() end

---Convert the location to a string.
---@return string
function Location:toString() end

lib.clone = Location.clone
lib.negate = Location.negate
lib.add = Location.add
lib.sub = Location.sub
lib.multiply = Location.multiply
lib.divide = Location.divide
lib.toHighest = Location.toHighest
lib.length = Location.length
lib.toString = Location.toString