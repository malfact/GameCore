-- A game builder is passed into the script when initiated
-- It doesn't have to be returned
local gameBuilder = (...)

--[[ Game Settings ]]--

-- You can set the display name of the game here
gameBuilder:setDisplayName('{NAME}')

--[[ Game Events ]]--

gameBuilder:registerCallback('ON_PLAYER_QUEUE_JOIN',
        function(game, player) end)

gameBuilder:registerCallback('ON_PLAYER_QUEUE_LEFT',
        function(game, player) end)

-- Register a callback for when the game starts
gameBuilder:registerCallback('ON_GAME_START',
        function(game) end)

-- Register a callback for when the game ends
gameBuilder:registerCallback('ON_GAME_END',
        function(game) end)

-- Register a callback for when the game ticks
gameBuilder:registerCallback('ON_GAME_TICK',
        function(game) end)

-- Register a callback for when a game event is fired
gameBuilder:registerCallback('ON_GAME_EVENT',
        function(game, event) end)

gameBuilder:registerCallback('ON_PLAYER_DEATH',
        function(game, player) end)