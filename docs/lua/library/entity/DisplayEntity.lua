---@meta

---@alias Billboard "fixed" | "vertical" | "horizontal" | "center"
---@alias TextAlignment "center" | "left" | "right"

---@class Display : Entity
---@field interpolationDuration integer
---@field teleportDuration integer
---@field viewRange number
---@field shadowRadius number
---@field shadowStrength number
---@field displayWidth number
---@field displayHeight number
---@field interpolationDelay number
---@field billboard Billboard
---@field glowColorOverride string
local Display = {}

---Gets the transformation applied to this display.
---@return Vector3 translation
---@return number leftRotation
---@return Vector3 scale
---@return number rightRotation
function Display:getTransformation() end

---Sets the transformation applied to this display
---@param translation Vector3
---@param leftRotation number
---@param scale Vector3
---@param rightRotation number
function Display:setTransformation(translation, leftRotation, scale, rightRotation) end

---Sets the brightness override of the entity.
---@param blockLight integer
---@param skyLight integer
function Display:getBrightness(blockLight, skyLight) end

---Gets the brightness override of the entity.
---@return integer blockLight
---@return integer skyLight
function Display:setBrightness() end

---@class TextDisplay : Display
---@field text string
---@field lineWidth integer
---@field backgroundColor string
---@field textOpacity number
---@field shadowed boolean
---@field seeThrough boolean
---@field defaultBackground boolean
---@field alignment TextAlignment