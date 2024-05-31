package com.richikin.platformania.enums;

public enum GraphicID
{
    // ----------------------------
    // The Player
    G_PLAYER,
    G_PLAYER_RUN,
    G_PLAYER_FIGHT,
    G_PLAYER_JUMP,
    G_PLAYER_CAST,
    G_PLAYER_DYING,
    G_PLAYER_SPAWNING,
    G_PLAYER_CROUCH,
    G_PLAYER_HURT,
    G_PLAYER_CLIMB,

    // ----------------------------
    // Weapons
    G_PLAYER_KNIFE,
    G_PLAYER_ROCK,
    G_PLAYER_SPELL,

    // ----------------------------
    // Other main, non-enemy, characters
    G_PRISONER,

    // ----------------------------
    // Pickup Items
    G_COIN,
    G_GEM,
    G_BLUE_GEM,
    G_RED_GEM,
    G_YELLOW_GEM,
    G_GREEN_GEM,
    G_HEART,
    G_KEY,
    G_APPLE,
    G_TREASURE_CHEST,
    G_TREASURE_CHEST_OPENING,

    // ----------------------------
    // Itembar Collectibles
    // Row 1
    _IC_CHEST,
    _IC_LETTER,
    _IC_MAP,
    _IC_BOOK1,
    _IC_SILVER_COIN,
    _IC_SCROLL,
    _IC_CANDLE,
    _IC_RUBY,

    // Row 2
    _IC_RUNE,
    _IC_PICKAXE,
    _IC_BOOK2,
    _IC_CRATE,
    _IC_LANTERN,
    _IC_GOLD_COIN,
    _IC_AXE,
    _IC_WIZARDS_HAT,

    // Row 3
    _IC_GREEN_POTION,
    _IC_EGG,
    _IC_METAL_HELMET,
    _IC_HAMMER,
    _IC_BOTTLE,
    _IC_GEAR,
    _IC_ARMOUR,
    _IC_STRING,

    // Row 4
    _IC_BEER,
    _IC_BOOK3,
    _IC_BRONZE_COIN,
    _IC_PARCHMENT,
    _IC_FEATHER,
    _IC_BOOT,
    _IC_RUCKSACK,
    _IC_BELT,

    // Row 5
    _IC_BREAD,
    _IC_EYE,
    _IC_LEATHER_HELMET,
    _IC_RED_WAND,
    _IC_GOLD_BAR,
    _IC_LONGBOW,
    _IC_BLUE_WAND,
    _IC_LEATHER_CHESTPLATE,

    // Row 6
    _IC_GREEN_WAND,
    _IC_MUSHROOM,
    _IC_ARROW,
    _IC_RED_POTION,
    _IC_PEARL,
    _IC_SWORD,
    _IC_BLUE_POTION,
    _IC_ROPE,

    // ----------------------------
    // Pushable Items
    G_PUSHABLE_CRATE,
    G_PUSHABLE_BOULDER,

    // ----------------------------
    // Decorations
    G_JAIL,
    G_FISH,

    // ----------------------------
    // Interactive items
    G_DOOR,
    G_OPEN_DOOR,
    G_LOCKED_DOOR,
    G_FLOOR_LEVER,
    G_PRIZE_BALLOON,
    G_MESSAGE_BUBBLE,
    G_MESSAGE_PANEL,
    G_HELP_BUBBLE,
    G_QUESTION_MARK,
    G_EXCLAMATION_MARK,
    G_MOVING_PLATFORM,
    G_SMALL_MOVING_PLATFORM,
    G_SELECTION_RING,
    G_FLAG,
    G_RED_FLAG,
    G_GREEN_FLAG,

    // ----------------------------
    G_FLAMES,
    G_BEES,
    G_EYES,

    // ----------------------------
    G_EXPLOSION12,
    G_EXPLOSION32,
    G_EXPLOSION64,
    G_EXPLOSION128,
    G_EXPLOSION256,

    // ----------------------------
    // Enemies
    G_SPIKES,
    G_DOWN_SPIKES,
    G_LEFT_SPIKES,
    G_RIGHT_SPIKES,
    G_SMALL_SPIKES,
    G_ENEMY_BULLET,
    G_BIG_BLOCK,
    G_BIG_BLOCK_HORIZONTAL,
    G_BIG_BLOCK_VERTICAL,
    G_BAT,
    G_BAT_ATTACK,
    G_BAT_BOMB,
    G_ROAMING_BAT,
    G_BEAST,
    G_BEAST_WALK,
    G_BEAST_ATTACK,
    G_PLANT,
    G_PLANT_FIGHTING,
    G_PLANT_HURT,
    G_MUSHROOM,
    G_CROW_MAN,
    G_CROW_MAN_RUN,
    G_CROW_MAN_ATTACK,
    G_WORM_MAN,
    G_WORM_MAN_RUN,
    G_WORM_MAN_ATTACK,
    G_DROP_BLOCK,
    G_SPIKEY_TURTLE,
    G_BOULDER,
    G_SMALL_BOULDER,
    G_RABBIT,
    G_FROG,
    G_FROG_JUMP,
    G_FROG_TAUNTING,
    G_GOBLIN,
    G_GOBLIN_ATTACK,
    G_MUSHROOM_MONSTER,
    G_MUSHROOM_MONSTER_ATTACK,
    G_SPAWNER,
    G_SPAWNER_RUN,
    G_SPAWNER_DYING,

    // ----------------------------
    G_LASER_BEAM,
    G_LASER_BEAM_VERTICAL,
    G_LASER_BEAM_HORIZONTAL,

    // #########################################################
    // Generic IDs
    // ----------------------------
    _MONSTER,
    _BLOCKS,
    _GROUND,
    _CEILING,
    _WALL,
    _LETHAL_OBJECT,
    _HAZARD,
    _SIGN,
    _SPEECH,
    _HUD_PANEL,
    _EXIT_BOX,
    _BACKGROUND_ENTITY,
    _BRIDGE,
    _CRATER,
    _FOOD,

    // ----------------------------
    // Managers
    _BLOCKS_MANAGER,
    _DECORATIONS_MANAGER,
    _ENEMY_MANAGER,
    _HAZARDS_MANAGER,
    _INTERACTIVE_MANAGER,
    _PICKUPS_MANAGER,
    _PLAYER_MANAGER,
    _SPAWNER_MANAGER,

    // ----------------------------
    // Main Character type, i.e. Player
    _MAIN,

    // ----------------------------
    // Enemy Character type, but not stationary entities
    // like rocket launchers etc.
    _ENEMY,

    // ----------------------------
    // Encapsulating type, covering any collision IDs that can be stood on.
    // This will be checked against the collision object TYPE, not the NAME.
    _OBSTACLE,

    // ----------------------------
    // As above but for objects that can't be stood on and are not entities
    _DECORATION,

    // As above, but for entities
    _ENTITY,

    // ----------------------------
    // Interactive objects
    _PICKUP,
    _WEAPON,
    _INTERACTIVE,
    _PRISONER,
    _PLATFORM,
    _AUTO_FLOOR,
    _ENTITY_BARRIER,

    // ----------------------------
    // Messages
    _PRESS_FOR_TREASURE,
    _PRESS_FOR_PRISONER,
    _PRESS_FOR_GUIDE,
    _KEY_NEEDED,

    // ----------------------------

    G_DUMMY,
    G_UNKNOWN,
    G_NO_ID
}
