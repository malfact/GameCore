---@meta

---Gets the `__userdata_type__` of the passed value.
---<br>
---If the userdata type is not defined then `type(value)` is returned.
---@param value any value
---@return string
function typeOf(value) end

---@enum Time
Time = {
    DAY =          0,
    NOON =      6000,
    SUNSET =   12000,
    NIGHT =    13000,
    MIDNIGHT = 18000,
    SUNRISE =  23000
}

---@alias WeatherType "downfall" | "clear"

---@alias LookAnchor "feet" | "eyes"