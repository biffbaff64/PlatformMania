package com.richikin.platformania.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.platformania.assets.AssetLoader;
import com.richikin.platformania.enums.GraphicID;

public class GameAssets extends AssetLoader
{
    // ------------------------------------------------------------------------
    // MainPlayer assets
    public static final String _PLAYER_IDLE_ASSET   = "idle";
    public static final String _PLAYER_CROUCH_ASSET = "crouch";
    public static final String _PLAYER_WALK_ASSET   = "run";
    public static final String _PLAYER_FIGHT_ASSET  = "attack";
    public static final String _PLAYER_JUMP_ASSET   = "jump";
    public static final String _PLAYER_HURT_ASSET   = "hit";
    public static final String _PLAYER_DEATH_ASSET  = "death";
    public static final String _PLAYER_SPAWN_ASSET  = "spawn";
    public static final String _PLAYER_CLIMB_ASSET  = "climb";

    // ------------------------------------------------------------------------
    // Weapons
    public static final String _PLAYER_KNIFE_ASSET = "throw_dagger";

    // ------------------------------------------------------------------------
    // Prisoner assets
    public static final String _PRISONER_IDLE_ASSET = "villager1_idle";

    // ------------------------------------------------------------------------
    // Pickups
    public static final String _BLUE_GEMS_ASSET  = "gems_blue";
    public static final String _GREEN_GEMS_ASSET = "gems_green";
    public static final String _GOLD_GEMS_ASSET  = "gems_gold";
    public static final String _RED_GEMS_ASSET   = "gems_red";
    public static final String _COIN_ASSET       = "coin";
    public static final String _HEART_ASSET      = "heart";
    public static final String _KEY_ASSET        = "gold_key";
    public static final String _APPLE_ASSET      = "apple";

    public static final String _ICONS_ASSET                 = "icons";
    public static final String _IC_CHEST_ASSET              = "ic_icon0";
    public static final String _IC_LETTER_ASSET             = "ic_icon1";
    public static final String _IC_MAP_ASSET                = "ic_icon2";
    public static final String _IC_BOOK1_ASSET              = "ic_icon3";
    public static final String _IC_SILVER_COIN_ASSET        = "ic_icon4";
    public static final String _IC_SCROLL_ASSET             = "ic_icon5";
    public static final String _IC_CANDLE_ASSET             = "ic_icon6";
    public static final String _IC_RUBY_ASSET               = "ic_icon7";
    public static final String _IC_RUNE_ASSET               = "ic_icon8";
    public static final String _IC_PICKAXE_ASSET            = "ic_icon9";
    public static final String _IC_BOOK2_ASSET              = "ic_icon10";
    public static final String _IC_GOLD_KEY_ASSET           = "ic_icon11";
    public static final String _IC_LANTERN_ASSET            = "ic_icon12";
    public static final String _IC_GOLD_COIN_ASSET          = "ic_icon13";
    public static final String _IC_AXE_ASSET                = "ic_icon14";
    public static final String _IC_HAT_ASSET                = "ic_icon15";
    public static final String _IC_GREEN_POTION_ASSET       = "ic_icon16";
    public static final String _IC_EGG_ASSET                = "ic_icon17";
    public static final String _IC_METAL_HELMET_ASSET       = "ic_icon18";
    public static final String _IC_HAMMER_ASSET             = "ic_icon19";
    public static final String _IC_BOTTLE_ASSET             = "ic_icon20";
    public static final String _IC_GEAR_ASSET               = "ic_icon21";
    public static final String _IC_ARMOUR_ASSET             = "ic_icon22";
    public static final String _IC_KEY_ASSET                = "ic_icon23";
    public static final String _IC_BEER_ASSET               = "ic_icon24";
    public static final String _IC_BOOK3_ASSET              = "ic_icon25";
    public static final String _IC_BRONZE_COIN_ASSET        = "ic_icon26";
    public static final String _IC_PARCHMENT_ASSET          = "ic_icon27";
    public static final String _IC_FEATHER_ASSET            = "ic_icon28";
    public static final String _IC_SILVER_KEY_ASSET         = "ic_icon29";
    public static final String _IC_RUCKSACK_ASSET           = "ic_icon30";
    public static final String _IC_BELT_ASSET               = "ic_icon31";
    public static final String _IC_BREAD_ASSET              = "ic_icon32";
    public static final String _IC_EYE_ASSET                = "ic_icon33";
    public static final String _IC_LEATHER_HELMET_ASSET     = "ic_icon34";
    public static final String _IC_RED_WAND_ASSET           = "ic_icon35";
    public static final String _IC_GOLD_BAR_ASSET           = "ic_icon36";
    public static final String _IC_LONGBOW_ASSET            = "ic_icon37";
    public static final String _IC_BLUE_WAND_ASSET          = "ic_icon38";
    public static final String _IC_LEATHER_CHESTPLATE_ASSET = "ic_icon39";
    public static final String _IC_GREEN_WAND_ASSET         = "ic_icon40";
    public static final String _IC_MUSHROOM_ASSET           = "ic_icon41";
    public static final String _IC_ARROW_ASSET              = "ic_icon42";
    public static final String _IC_RED_POTION_ASSET         = "ic_icon43";
    public static final String _IC_PEARL_ASSET              = "ic_icon44";
    public static final String _IC_SWORD_ASSET              = "ic_icon45";
    public static final String _IC_BLUE_POTION_ASSET        = "ic_icon46";
    public static final String _IC_ROPE_ASSET               = "ic_icon47";

    // ------------------------------------------------------------------------
    // Interactive Items
    public static final String _TREASURE_CHEST_ASSET         = "treasure_chest";
    public static final String _TREASURE_CHEST_OPENING_ASSET = "treasure_chest_opening";
    public static final String _MOVING_PLATFORM_ASSET        = "moving_platform";
    public static final String _SMALL_MOVING_PLATFORM_ASSET  = "moving_platform_small";
    public static final String _SMALL_BOULDER_ASSET          = "boulder32x32";
    public static final String _CRATE_ASSET                  = "crate";
    public static final String _LOCKED_DOOR_ASSET            = "locked_door";
    public static final String _FLOOR_LEVER_ASSET            = "floor_lever";
    public static final String _GREEN_FLAG_ASSET             = "flag_green";
    public static final String _RED_FLAG_ASSET               = "flag_red";
    public static final String _BLOCK_6X6_ASSET              = "block6x6";

    // ------------------------------------------------------------------------
    // Decorations
    public static final String _EYES_ASSET  = "eyes";
    public static final String _FLAME_ASSET = "flames1";
    public static final String _BEES_ASSET  = "bees";
    public static final String _FISH1_ASSET = "fish1";

    // ------------------------------------------------------------------------
    // Hazards
    public static final String _SPIKES_ASSET       = "spike";
    public static final String _DOWN_SPIKES_ASSET  = "down_spike";
    public static final String _LEFT_SPIKES_ASSET  = "left_spike";
    public static final String _RIGHT_SPIKES_ASSET = "right_spike";
    public static final String _SMALL_SPIKES_ASSET = "small_spike";
    public static final String _DROP_BLOCK_ASSET   = "square_block48x48_eyes";
    public static final String _BAT_STONE_ASSET    = "small_boulder";

    // ------------------------------------------------------------------------
    // Static Enemies
    public static final String _PLANT_IDLE_ASSET   = "plant_idle";
    public static final String _PLANT_ATTACK_ASSET = "plant_attack";
    public static final String _PLANT_HURT_ASSET   = "plant_hurt";

    // ------------------------------------------------------------------------
    // Mobile Enemies
    public static final String _BAT_FLY_ASSET              = "bat_fly";
    public static final String _BAT_ATTACK_ASSET           = "bat_attack";
    public static final String _BEAST_IDLE_ASSET           = "beast_idle";
    public static final String _BEAST_WALK_ASSET           = "beast_walk";
    public static final String _BEAST_ATTACK_ASSET         = "beast_attack";
    public static final String _MUSHROOM_WALK_ASSET        = "mushroom_walk";
    public static final String _CROW_MAN_IDLE_ASSET        = "enemy04_idle";
    public static final String _CROW_MAN_WALK_ASSET        = "enemy04_walk";
    public static final String _CROW_MAN_ATTACK_ASSET      = "enemy04_attack";
    public static final String _WORM_MAN_IDLE_ASSET        = "enemy05_idle";
    public static final String _WORM_MAN_WALK_ASSET        = "enemy05_walk";
    public static final String _WORM_MAN_ATTACK_ASSET      = "enemy05_attack";
    public static final String _SPIKEY_TURTLE_ASSET        = "spikey_turtle_green";
    public static final String _RABBIT_ASSET               = "sunny-bunny-run";
    public static final String _FROG_ASSET                 = "sunny-froggy-walk";
    public static final String _FROG_JUMP_ASSET            = "sunny-froggy-jump";
    public static final String _FROG_TAUNT_ASSET           = "sunny-froggy-taunting";
    public static final String _GOBLIN_ASSET               = "goblin_idle";
    public static final String _MUSHROOM_MONSTER_ASSET     = "mushroom_monster_idle";
    public static final String _MUSHROOM_MONSTER_RUN_ASSET = "mushroom_monster_run";
    public static final String _SPAWNER_SPAWN_ASSET        = "spawner_spawn";
    public static final String _SPAWNER_RUN_ASSET          = "spawner_run";

    // ------------------------------------------------------------------------
    // In-Game Messaging
    public static final String _HELP_ME_ASSET           = "help_me_speech";
    public static final String _GAMEOVER_MSG_ASSET      = "gameover";
    public static final String _GETREADY_MSG_ASSET      = "getready";
    public static final String _DEFAULT_TEXTPANEL_ASSET = "text_box";

    // ------------------------------------------------------------------------
    // Weaponry and Explosions
    public static final String _EXPLOSION64_ASSET = "explosion64";

    // ------------------------------------------------------------------------
    // Fonts, Backgrounds and HUD assets
    public static final String _ACME_FONT             = "fonts/Acme-Regular.ttf";
    public static final String _PRO_WINDOWS_FONT      = "fonts/ProFontWindows.ttf";
    public static final String _HUD_PANEL_ASSET       = "hud_panel_candidate.png";
    public static final String _SPLASH_SCREEN_ASSET   = "splash_screen.png";
    public static final String _CREDITS_PANEL_ASSET   = "credits_panel.png";
    public static final String _OPTIONS_PANEL_ASSET   = "options_panel.png";
    public static final String _CONTROLLER_TEST_ASSET = "controller_test_panel.png";
    public static final String _PAUSE_PANEL_ASSET     = "pause_panel.png";
    public static final String _INTRO_PANEL_ASSET     = "intro_panel.png";
    public static final String _BACKGROUND_ASSET      = "water_background.png";
    public static final String _SMALL_MAN             = "life";

    // ------------------------------------------------------------------------
    // Frame counts for animations
    public static final int _SINGLE_FRAME = 1;

    public static final int _HELP_ME_FRAMES     = 3;
    public static final int _LOCKED_DOOR_FRAMES = 1;

    public static final int _PLAYER_IDLE_FRAMES   = 9;
    public static final int _PLAYER_CROUCH_FRAMES = 9;
    public static final int _PLAYER_RUN_FRAMES    = 8;
    public static final int _PLAYER_FIGHT_FRAMES  = 7;
    public static final int _PLAYER_JUMP_FRAMES   = 7;
    public static final int _PLAYER_HURT_FRAMES   = 4;
    public static final int _PLAYER_DEATH_FRAMES  = 5;
    public static final int _PLAYER_SPAWN_FRAMES  = 12;
    public static final int _PLAYER_CLIMB_FRAMES  = 2;

    public static final int _PLAYER_KNIFE_FRAMES = 4;

    public static final int _PRISONER_IDLE_FRAMES = 9;

    public static final int _BAT_FLY_FRAMES              = 7;
    public static final int _BAT_ATTACK_FRAMES           = 10;
    public static final int _BEAST_IDLE_FRAMES           = 11;
    public static final int _BEAST_WALK_FRAMES           = 8;
    public static final int _BEAST_ATTACK_FRAMES         = 8;
    public static final int _PLANT_IDLE_FRAMES           = 8;
    public static final int _PLANT_ATTACK_FRAMES         = 16;
    public static final int _PLANT_HURT_FRAMES           = 3;
    public static final int _MUSHROOM_WALK_FRAMES        = 10;
    public static final int _CROW_MAN_IDLE_FRAMES        = 7;
    public static final int _CROW_MAN_WALK_FRAMES        = 6;
    public static final int _CROW_MAN_ATTACK_FRAMES      = 7;
    public static final int _WORM_MAN_IDLE_FRAMES        = 9;
    public static final int _WORM_MAN_WALK_FRAMES        = 6;
    public static final int _WORM_MAN_ATTACK_FRAMES      = 6;
    public static final int _DROP_BLOCK_FRAMES           = 2;
    public static final int _SPIKEY_TURTLE_FRAMES        = 8;
    public static final int _FROG_FRAMES                 = 10;
    public static final int _FROG_JUMP_FRAMES            = 7;
    public static final int _FROG_TAUNT_FRAMES           = 4;
    public static final int _RABBIT_FRAMES               = 6;
    public static final int _GOBLIN_FRAMES               = 4;
    public static final int _MUSHROOM_MONSTER_RUN_FRAMES = 8;
    public static final int _SPAWNER_SPAWN_FRAMES        = 12;
    public static final int _SPAWNER_RUN_FRAMES          = 8;

    public static final int _FLAME_FRAMES = 8;
    public static final int _BEES_FRAMES  = 10;
    public static final int _EYES_FRAMES  = 3;
    public static final int _FISH_FRAMES  = 6;

    public static final int _SPIKE_FRAMES           = 1;
    public static final int _MOVING_PLATFORM_FRAMES = 1;
    public static final int _SMALL_BOULDER_FRAMES   = 1;
    public static final int _CRATE_FRAMES           = 4;
    public static final int _BAT_STONE_FRAMES       = 9;
    public static final int _FLOOR_LEVER_FRAMES     = 7;
    public static final int _FLAG_FRAMES            = 5;

    public static final int _COIN_FRAMES           = 4;
    public static final int _GEMS_FRAMES           = 6;
    public static final int _HEART_FRAMES          = 6;
    public static final int _KEY_FRAMES            = 6;
    public static final int _APPLE_FRAMES          = 6;
    public static final int _TREASURE_CHEST_FRAMES = 5;
    public static final int _IC_FRAMES             = 1;

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public String getRedObjectName()
    {
        return "solid_red32x32";
    }

    @Override
    public String getBlueObjectName()
    {
        return "solid_blu32x32";
    }

    @Override
    public String getGreenObjectName()
    {
        return "solid_green32x32";
    }

    @Override
    public String getYellowObjectName()
    {
        return "solid_yellow32x32";
    }

    @Override
    public String getWhiteObjectName()
    {
        return "solid_white32x32";
    }

    @Override
    public String getSkinFilename()
    {
        return "ui/uiskin.json";
    }

    @Override
    public String getDevPanelFont()
    {
        return GameAssets._PRO_WINDOWS_FONT;
    }

    @Override
    public String getDevPanelBackground()
    {
        return GameAssets._BACKGROUND_ASSET;
    }

    @Override
    public String getPausePanelBackground()
    {
        return GameAssets._PAUSE_PANEL_ASSET;
    }

    @Override
    public String getOptionsPanelAsset()
    {
        return GameAssets._OPTIONS_PANEL_ASSET;
    }

    @Override
    public String getControllerTestAsset()
    {
        return GameAssets._CONTROLLER_TEST_ASSET;
    }

    /**
     * Interface method for [com.richikin.platformania].
     *
     * @return The name of the image used for [StarField][com.richikin.platformania.graphics.effects.StarField],
     * minus the file extension.
     */
    public TextureRegion getStarfieldObject()
    {
        return getObjectRegion( "solid_white32x32" );
    }

    /**
     * Interface method for [com.richikin.platformania].
     *
     * @return The [GraphicID] for the Player.
     */
    @Override
    public GraphicID getPlayerGID()
    {
        return GraphicID.G_PLAYER;
    }
}
