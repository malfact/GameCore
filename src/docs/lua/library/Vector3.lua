---@meta

--[[ Library ]] --

---The Vector3 library.
Vector3 = {}

---@type Vector3
Vector3.Zero = Vector3.new(0,0,0)
---@type Vector3
Vector3.Up = Vector3.new(0,1,0)
---@type Vector3
Vector3.Down = Vector3.new(0,-1,0)

---@param x number
---@param y number
---@param z number
---@return Vector3
function Vector3.new(x,y,z) end

---@return Vector3
function Vector3.random() end

--[[ Class ]] --

---@class Vector3: userdata
---@field x number
---@field y number
---@field z number
---@field blockX integer
---@field blockY integer
---@field blockZ integer
---@operator add(Vector3): Vector3
---@operator sub(Vector3): Vector3
---@operator mul(Vector3 | number): Vector3
---@operator div(Vector3 | number): Vector3
---@operator unm: Vector3
---@operator len: number
local instance = {}

---Clone the Vector3.
---@return Vector3
function instance.clone(self) end

---Get the length of the Vector3.
---@return number
function instance.length(self) end

---Get the square length of the Vector3.
---@return number
function instance.lengthSquared(self) end

---Get the Vector3 multiplied by `-1`.
---@return Vector3
function instance.neg(self) end

---Normalize the Vector3.
---@return Vector3
function instance.normalize(self) end

---Convert the Vector3 to a string.
---@return string
function instance.toString(self) end

---Get the Vector3 with the coordinates floored.
---@return Vector3
function instance.floor(self) end

---Get the Vector3 with the coordinates ceiled.
---@return Vector3
function instance.ceil(self) end

---Get the distance between this and another Vector3.
---@param other Vector3
---@return number
function instance.distance(self, other) end

---Get the square distance between this and another Vector3.
---@param other Vector3
---@return number
function instance.distanceSquared(self, other) end

---Get the midpoint between this and another Vector3.
---@param other Vector3
---@return Vector3
function instance.mid(self, other) end

---Get the cross product of this and another Vector3.
---@param other Vector3
---@return Vector3
function instance.cross(self, other) end

---Get the angle between this and another Vector3.
---@param other Vector3
---@return number
function instance.angle(self, other) end

---Get the dot product of this and another Vector3.
---@param other Vector3
---@return number
function instance.dot(self, other) end

---Add this and another Vector3.
---@param other Vector3
---@return Vector3
function instance.add(self, other) end

---Subtract this and another Vector3.
---@param other Vector3
---@return Vector3
function instance.sub(self, other) end

---Get the *(element-wise)* miximum between this and another Vector3.
---@param other Vector3
---@return Vector3
function instance.max(self, other) end

---Get the *(element-wise)* minimum between this and another Vector3.
---@param other Vector3
---@return Vector3
function instance.min(self, other) end

---Multiply this *(element-wise)* and another Vector3. 
---<br> **or** <br>
---Multiply this by a number.
---@param other Vector3 | number
---@return Vector3
function instance.multiply(self, other) end

---Divide this *(element-wise)* and another Vector3. 
---<br> **or** <br>
---Divide this by a number.
---@param other Vector3 | number
---@return Vector3
function instance.divide(self, other) end

---Check if this is equal to another value.
---@param other any
---@return boolean
function instance.equals(self, other) end

Vector3.clone = instance.clone
Vector3.length = instance.length
Vector3.lengthSquared = instance.lengthSquared
Vector3.neg = instance.neg
Vector3.normalize = instance.normalize
Vector3.toString = instance.toString
Vector3.floor = instance.floor
Vector3.ceil = instance.ceil
Vector3.distance = instance.distance
Vector3.distanceSquared = instance.distanceSquared
Vector3.mid = instance.mid
Vector3.cross = instance.cross
Vector3.angle = instance.angle
Vector3.dot = instance.dot
Vector3.add = instance.add
Vector3.sub = instance.sub
Vector3.max = instance.max
Vector3.min = instance.min
Vector3.multiply = instance.multiply
Vector3.divide = instance.divide
Vector3.equals = instance.equals