---@meta

---@class AttributeInstance : userdata
---@field defaultValue number
---@field baseValue number
---@field value number
---@field attribute Attribute
local AttributeInstance = {}

---Get an attribute modifier for the instance.
---@param modifier string
---@return AttributeModifier | nil
function AttributeInstance:getModifier(modifier) end

---Add an attribute modifier to the instance.
---@param modifier AttributeModifier
function AttributeInstance:addModifier(modifier) end

---Remove an attribute modifier from the instance.
---@param modifier string | AttributeModifier
function AttributeInstance:removeModifier(modifier) end