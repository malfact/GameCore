-- PLAYER_BED_ENTER
local event = {
    name = "event_name"
}

local cancelable_event = {
    isCanceled = "function(self) → bool",
    setCanceled = "function(self,canceled)"
}

local player_event = {

    player = "userdata@player"
}

local entity_event = {

    entity = "userdata@entity"
}