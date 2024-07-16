local function duplicate(table)
    local t = {}
    for k,v in pairs(table) do
        t[k] = v
    end
    return t
end

local function make_read_only(t)
    return setmetatable(t, {
        __index = t,
        __newindex = function() error('table is read only') end,
        __metatable = false
    })
end

local g = {
    print = print,
    assert = assert,
    error = error,
    getmetatable = getmetatable,
    pcall = pcall,
    select = select,
    setmetatable = setmetatable,
    tonumber = tonumber,
    tostring = tostring,
    type = type,
    xpcall = xpcall,
    next = next,
    pairs = pairs,
    ipairs = ipairs,
    table = duplicate(table),
    string = duplicate(string),
    os = {
        clock = os.clock,
        date = os.date,
        difftime = os.difftime,
        time = os.time
    },
    math = duplicate(math),
    bit32 = duplicate(bit32)
}
g._G = g
make_read_only(g.table)
make_read_only(g.string)
make_read_only(g.os)
make_read_only(g.math)
make_read_only(g.bit32)
make_read_only(g)

local const_time = {
    DAY =          0,
    NOON =      6000,
    SUNSET =   12000,
    NIGHT =    13000,
    MIDNIGHT = 18000,
    SUNRISE =  23000
}

local e = {}
e._G = e
e.Time = const_time
setmetatable(e, {
    __index = _G,
})

return e