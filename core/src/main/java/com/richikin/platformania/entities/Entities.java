package com.richikin.platformania.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.*;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.enums.TileID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2;
import com.richikin.platformania.entities.actors.hero.MainPlayer;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.GameAssets;

public class Entities
{
    /**
     * Table of SpriteDescriptors describing the basic properties
     * of entities. Used to create placement tiles.
     */
    public final SpriteDescriptor[] entityList =
        {
            // -----------------------------------------------------
            // Player
            new SpriteDescriptor
                (
                    "Player",
                    GraphicID.G_PLAYER, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_IDLE_ASSET, GameAssets._PLAYER_IDLE_FRAMES,
                    new SimpleVec2( 64, 36 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Player Spawn",
                    GraphicID.G_PLAYER_SPAWNING, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_SPAWN_ASSET, GameAssets._PLAYER_SPAWN_FRAMES,
                    new SimpleVec2( 64, 36 )
                ),
            new SpriteDescriptor
                (
                    "Player Crouch",
                    GraphicID.G_PLAYER_CROUCH, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_CROUCH_ASSET, GameAssets._PLAYER_CROUCH_FRAMES,
                    new SimpleVec2( 64, 36 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Player Run",
                    GraphicID.G_PLAYER_RUN, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_WALK_ASSET, GameAssets._PLAYER_RUN_FRAMES,
                    new SimpleVec2( 64, 36 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Player Fight",
                    GraphicID.G_PLAYER_FIGHT, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_FIGHT_ASSET, GameAssets._PLAYER_FIGHT_FRAMES,
                    new SimpleVec2( 64, 36 )
                ),
            new SpriteDescriptor
                (
                    "Player Jump",
                    GraphicID.G_PLAYER_JUMP, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_JUMP_ASSET, GameAssets._PLAYER_JUMP_FRAMES,
                    new SimpleVec2( 64, 38 )
                ),
            new SpriteDescriptor
                (
                    "Player Climb",
                    GraphicID.G_PLAYER_CLIMB, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_CLIMB_ASSET, GameAssets._PLAYER_CLIMB_FRAMES,
                    new SimpleVec2( 64, 36 )
                ),
            new SpriteDescriptor
                (
                    "Player Hurt",
                    GraphicID.G_PLAYER_HURT, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_HURT_ASSET, GameAssets._PLAYER_HURT_FRAMES,
                    new SimpleVec2( 64, 36 )
                ),
            new SpriteDescriptor
                (
                    "Player Death",
                    GraphicID.G_PLAYER_DYING, GraphicID._MAIN, TileID._PLAYER_TILE,
                    GameAssets._PLAYER_DEATH_ASSET, GameAssets._PLAYER_DEATH_FRAMES,
                    new SimpleVec2( 64, 36 )
                ),
            new SpriteDescriptor
                (
                    "Player Knife",
                    GraphicID.G_PLAYER_KNIFE, GraphicID._WEAPON, TileID._PLAYER_WEAPON_TILE,
                    GameAssets._PLAYER_KNIFE_ASSET, GameAssets._PLAYER_KNIFE_FRAMES,
                    new SimpleVec2( 15, 16 )
                ),

            // -----------------------------------------------------
            // Other Main Characters
            new SpriteDescriptor
                (
                    "Prisoner",
                    GraphicID.G_PRISONER, GraphicID._MAIN, TileID._PRISONER_TILE,
                    GameAssets._PRISONER_IDLE_ASSET, GameAssets._PRISONER_IDLE_FRAMES,
                    new SimpleVec2( 80, 80 ),
                    Animation.PlayMode.LOOP
                ),

            // -----------------------------------------------------
            // Mobile Enemies
            new SpriteDescriptor
                (
                    "Bat",
                    GraphicID.G_BAT, GraphicID._ENEMY, TileID._BAT_TILE,
                    GameAssets._BAT_FLY_ASSET, GameAssets._BAT_FLY_FRAMES,
                    new SimpleVec2( 32, 32 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Bat Attack",
                    GraphicID.G_BAT_ATTACK, GraphicID._ENEMY, TileID._BAT_TILE,
                    GameAssets._BAT_ATTACK_ASSET, GameAssets._BAT_ATTACK_FRAMES,
                    new SimpleVec2( 32, 32 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Beast",
                    GraphicID.G_BEAST, GraphicID._ENEMY, TileID._BEAST_TILE,
                    GameAssets._BEAST_IDLE_ASSET, GameAssets._BEAST_IDLE_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Beast Walk",
                    GraphicID.G_BEAST_WALK, GraphicID._ENEMY, TileID._BEAST_TILE,
                    GameAssets._BEAST_WALK_ASSET, GameAssets._BEAST_WALK_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Beast Attack",
                    GraphicID.G_BEAST_ATTACK, GraphicID._ENEMY, TileID._BEAST_TILE,
                    GameAssets._BEAST_ATTACK_ASSET, GameAssets._BEAST_ATTACK_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Mushroom",
                    GraphicID.G_MUSHROOM, GraphicID._ENEMY, TileID._MUSHROOM_TILE,
                    GameAssets._MUSHROOM_WALK_ASSET, GameAssets._MUSHROOM_WALK_FRAMES,
                    new SimpleVec2( 41, 30 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Crow Man",
                    GraphicID.G_CROW_MAN, GraphicID._ENEMY, TileID._CROW_MAN_TILE,
                    GameAssets._CROW_MAN_IDLE_ASSET, GameAssets._CROW_MAN_IDLE_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Crow Man Run",
                    GraphicID.G_CROW_MAN_RUN, GraphicID._ENEMY, TileID._CROW_MAN_TILE,
                    GameAssets._CROW_MAN_WALK_ASSET, GameAssets._CROW_MAN_WALK_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Crow Man Attack",
                    GraphicID.G_CROW_MAN_ATTACK, GraphicID._ENEMY, TileID._CROW_MAN_TILE,
                    GameAssets._CROW_MAN_ATTACK_ASSET, GameAssets._CROW_MAN_ATTACK_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Worm Man",
                    GraphicID.G_WORM_MAN, GraphicID._ENEMY, TileID._WORM_MAN_TILE,
                    GameAssets._WORM_MAN_IDLE_ASSET, GameAssets._WORM_MAN_IDLE_FRAMES,
                    new SimpleVec2( 74, 64 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Worm Man Run",
                    GraphicID.G_WORM_MAN_RUN, GraphicID._ENEMY, TileID._WORM_MAN_TILE,
                    GameAssets._WORM_MAN_WALK_ASSET, GameAssets._WORM_MAN_WALK_FRAMES,
                    new SimpleVec2( 74, 64 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Worm Man Attack",
                    GraphicID.G_WORM_MAN_ATTACK, GraphicID._ENEMY, TileID._WORM_MAN_TILE,
                    GameAssets._WORM_MAN_ATTACK_ASSET, GameAssets._WORM_MAN_ATTACK_FRAMES,
                    new SimpleVec2( 74, 64 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Turtle",
                    GraphicID.G_SPIKEY_TURTLE, GraphicID._ENEMY, TileID._SPIKEY_TURTLE_TILE,
                    GameAssets._SPIKEY_TURTLE_ASSET, GameAssets._SPIKEY_TURTLE_FRAMES,
                    new SimpleVec2( 48, 34 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Frog",
                    GraphicID.G_FROG, GraphicID._ENEMY, TileID._FROG_TILE,
                    GameAssets._FROG_ASSET, GameAssets._FROG_FRAMES,
                    new SimpleVec2( 42, 38 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Frog Jump",
                    GraphicID.G_FROG_JUMP, GraphicID._ENEMY, TileID._FROG_TILE,
                    GameAssets._FROG_JUMP_ASSET, GameAssets._FROG_JUMP_FRAMES,
                    new SimpleVec2( 42, 38 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Frog Taunt",
                    GraphicID.G_FROG_TAUNTING, GraphicID._ENEMY, TileID._FROG_TILE,
                    GameAssets._FROG_TAUNT_ASSET, GameAssets._FROG_TAUNT_FRAMES,
                    new SimpleVec2( 42, 38 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Rabbit",
                    GraphicID.G_RABBIT, GraphicID._ENEMY, TileID._RABBIT_TILE,
                    GameAssets._RABBIT_ASSET, GameAssets._RABBIT_FRAMES,
                    new SimpleVec2( 34, 44 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Goblin",
                    GraphicID.G_GOBLIN, GraphicID._ENEMY, TileID._GOBLIN_TILE,
                    GameAssets._GOBLIN_ASSET, GameAssets._GOBLIN_FRAMES,
                    new SimpleVec2( 42, 48 ),
                    Animation.PlayMode.LOOP,
                    0.8f
                ),
            new SpriteDescriptor
                (
                    "Mushroom Monster",
                    GraphicID.G_MUSHROOM_MONSTER, GraphicID._ENEMY, TileID._MUSHROOM_MONSTER_TILE,
                    GameAssets._MUSHROOM_MONSTER_RUN_ASSET, GameAssets._MUSHROOM_MONSTER_RUN_FRAMES,
                    new SimpleVec2( 36, 52 ),
                    Animation.PlayMode.LOOP,
                    0.5f
                ),
            new SpriteDescriptor
                (
                    "Spawner",
                    GraphicID.G_SPAWNER, GraphicID._ENEMY, TileID._SPAWNER_TILE,
                    GameAssets._SPAWNER_SPAWN_ASSET, GameAssets._SPAWNER_SPAWN_FRAMES,
                    new SimpleVec2( 48, 32 ),
                    Animation.PlayMode.LOOP,
                    0.5f
                ),
            new SpriteDescriptor
                (
                    "Spawner Running",
                    GraphicID.G_SPAWNER_RUN, GraphicID._ENEMY, TileID._SPAWNER_TILE,
                    GameAssets._SPAWNER_RUN_ASSET, GameAssets._SPAWNER_RUN_FRAMES,
                    new SimpleVec2( 48, 32 ),
                    Animation.PlayMode.LOOP,
                    0.5f
                ),
            new SpriteDescriptor
                (
                    "Spawner Dying",
                    GraphicID.G_SPAWNER_DYING, GraphicID._ENEMY, TileID._SPAWNER_TILE,
                    GameAssets._SPAWNER_SPAWN_ASSET, GameAssets._SPAWNER_SPAWN_FRAMES,
                    new SimpleVec2( 48, 32 ),
                    Animation.PlayMode.REVERSED,
                    0.5f
                ),

            // -----------------------------------------------------
            // Stationary Enemies / Hazards
            new SpriteDescriptor
                (
                    "Plant",
                    GraphicID.G_PLANT, GraphicID._ENEMY, TileID._PLANT_TILE,
                    GameAssets._PLANT_IDLE_ASSET, GameAssets._PLANT_IDLE_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.LOOP,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Plant Fight",
                    GraphicID.G_PLANT_FIGHTING, GraphicID._ENEMY, TileID._PLANT_TILE,
                    GameAssets._PLANT_ATTACK_ASSET, GameAssets._PLANT_ATTACK_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Plant Hurt",
                    GraphicID.G_PLANT_HURT, GraphicID._ENEMY, TileID._PLANT_TILE,
                    GameAssets._PLANT_HURT_ASSET, GameAssets._PLANT_HURT_FRAMES,
                    new SimpleVec2( 64, 64 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Drop Block",
                    GraphicID.G_DROP_BLOCK, GraphicID._ENEMY, TileID._DROP_BLOCK_TILE,
                    GameAssets._DROP_BLOCK_ASSET, GameAssets._DROP_BLOCK_FRAMES,
                    new SimpleVec2( 48, 48 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Spikes",
                    GraphicID.G_SPIKES, GraphicID._ENEMY, TileID._SPIKES_TILE,
                    GameAssets._SPIKES_ASSET, GameAssets._SPIKE_FRAMES,
                    new SimpleVec2( 16, 48 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Down Spikes",
                    GraphicID.G_DOWN_SPIKES, GraphicID._ENEMY, TileID._DOWN_SPIKES_TILE,
                    GameAssets._DOWN_SPIKES_ASSET, GameAssets._SPIKE_FRAMES,
                    new SimpleVec2( 16, 48 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Left Spikes",
                    GraphicID.G_LEFT_SPIKES, GraphicID._ENEMY, TileID._LEFT_SPIKES_TILE,
                    GameAssets._LEFT_SPIKES_ASSET, GameAssets._SPIKE_FRAMES,
                    new SimpleVec2( 48, 16 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Right Spikes",
                    GraphicID.G_RIGHT_SPIKES, GraphicID._ENEMY, TileID._RIGHT_SPIKES_TILE,
                    GameAssets._RIGHT_SPIKES_ASSET, GameAssets._SPIKE_FRAMES,
                    new SimpleVec2( 48, 16 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Small Spikes",
                    GraphicID.G_SMALL_SPIKES, GraphicID._ENEMY, TileID._SPIKES_TILE,
                    GameAssets._SMALL_SPIKES_ASSET, GameAssets._SPIKE_FRAMES,
                    new SimpleVec2( 16, 40 ),
                    Animation.PlayMode.NORMAL,
                    0.4f
                ),
            new SpriteDescriptor
                (
                    "Bat Stone",
                    GraphicID.G_BAT_BOMB, GraphicID._ENEMY, TileID._SMALL_BOULDER_TILE,
                    GameAssets._BAT_STONE_ASSET, GameAssets._BAT_STONE_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),

            // -----------------------------------------------------
            // Pickups
            new SpriteDescriptor
                (
                    "Coin",
                    GraphicID.G_COIN, GraphicID._PICKUP, TileID._COIN_TILE,
                    GameAssets._COIN_ASSET, GameAssets._COIN_FRAMES,
                    new SimpleVec2( 24, 24 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Blue Gem",
                    GraphicID.G_GEM, GraphicID._PICKUP, TileID._GEM_TILE,
                    GameAssets._BLUE_GEMS_ASSET, GameAssets._GEMS_FRAMES,
                    new SimpleVec2( 28, 24 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Red Gem",
                    GraphicID.G_GEM, GraphicID._PICKUP, TileID._RED_GEM_TILE,
                    GameAssets._RED_GEMS_ASSET, GameAssets._GEMS_FRAMES,
                    new SimpleVec2( 28, 24 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Green Gem",
                    GraphicID.G_GEM, GraphicID._PICKUP, TileID._GREEN_GEM_TILE,
                    GameAssets._GREEN_GEMS_ASSET, GameAssets._GEMS_FRAMES,
                    new SimpleVec2( 28, 24 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Yellow Gem",
                    GraphicID.G_GEM, GraphicID._PICKUP, TileID._YELLOW_GEM_TILE,
                    GameAssets._GOLD_GEMS_ASSET, GameAssets._GEMS_FRAMES,
                    new SimpleVec2( 28, 24 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Key",
                    GraphicID.G_KEY, GraphicID._PICKUP, TileID._KEY_TILE,
                    GameAssets._KEY_ASSET, GameAssets._KEY_FRAMES,
                    new SimpleVec2( 32, 32 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Apple",
                    GraphicID.G_APPLE, GraphicID._PICKUP, TileID._APPLE_TILE,
                    GameAssets._APPLE_ASSET, GameAssets._APPLE_FRAMES,
                    new SimpleVec2( 32, 32 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Heart",
                    GraphicID.G_HEART, GraphicID._PICKUP, TileID._HEART_TILE,
                    GameAssets._HEART_ASSET, GameAssets._HEART_FRAMES,
                    new SimpleVec2( 24, 24 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Treasure Chest",
                    GraphicID.G_TREASURE_CHEST, GraphicID._PICKUP, TileID._CHEST_TILE,
                    GameAssets._TREASURE_CHEST_ASSET, GameAssets._TREASURE_CHEST_FRAMES,
                    new SimpleVec2( 48, 32 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Treasure Chest Opening",
                    GraphicID.G_TREASURE_CHEST_OPENING, GraphicID._PICKUP, TileID._CHEST_TILE,
                    GameAssets._TREASURE_CHEST_OPENING_ASSET, GameAssets._TREASURE_CHEST_FRAMES,
                    new SimpleVec2( 48, 32 )
                ),

            // -----------------------------------------------------
            // Item-bar Pickups
            new SpriteDescriptor
                (
                    "IC Chest",
                    GraphicID._IC_CHEST, GraphicID._PICKUP, TileID._IC_CHEST_TILE,
                    GameAssets._IC_CHEST_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Letter",
                    GraphicID._IC_LETTER, GraphicID._PICKUP, TileID._IC_LETTER_TILE,
                    GameAssets._IC_LETTER_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Map",
                    GraphicID._IC_MAP, GraphicID._PICKUP, TileID._IC_MAP_TILE,
                    GameAssets._IC_MAP_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Book1",
                    GraphicID._IC_BOOK1, GraphicID._PICKUP, TileID._IC_BOOK1_TILE,
                    GameAssets._IC_BOOK1_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Book2",
                    GraphicID._IC_BOOK2, GraphicID._PICKUP, TileID._IC_BOOK2_TILE,
                    GameAssets._IC_BOOK2_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Book3",
                    GraphicID._IC_BOOK3, GraphicID._PICKUP, TileID._IC_BOOK3_TILE,
                    GameAssets._IC_BOOK3_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Gold Coin",
                    GraphicID._IC_GOLD_COIN, GraphicID._PICKUP, TileID._IC_GOLD_COIN_TILE,
                    GameAssets._IC_GOLD_COIN_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Silver Coin",
                    GraphicID._IC_SILVER_COIN, GraphicID._PICKUP, TileID._IC_SILVER_COIN_TILE,
                    GameAssets._IC_SILVER_COIN_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Bronze Coin",
                    GraphicID._IC_BRONZE_COIN, GraphicID._PICKUP, TileID._IC_BRONZE_COIN_TILE,
                    GameAssets._IC_BRONZE_COIN_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Scroll",
                    GraphicID._IC_SCROLL, GraphicID._PICKUP, TileID._IC_SCROLL_TILE,
                    GameAssets._IC_SCROLL_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Candle",
                    GraphicID._IC_CANDLE, GraphicID._PICKUP, TileID._IC_CANDLE_TILE,
                    GameAssets._IC_CANDLE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Ruby",
                    GraphicID._IC_RUBY, GraphicID._PICKUP, TileID._IC_RUBY_TILE,
                    GameAssets._IC_RUBY_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Rune",
                    GraphicID._IC_RUNE, GraphicID._PICKUP, TileID._IC_RUNE_TILE,
                    GameAssets._IC_RUNE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Pickaxe",
                    GraphicID._IC_PICKAXE, GraphicID._PICKUP, TileID._IC_PICKAXE_TILE,
                    GameAssets._IC_PICKAXE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Gold Key",
                    GraphicID._IC_CRATE, GraphicID._PICKUP, TileID._IC_GOLD_KEY_TILE,
                    GameAssets._IC_GOLD_KEY_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Silver Key",
                    GraphicID._IC_BOOT, GraphicID._PICKUP, TileID._IC_SILVER_KEY_TILE,
                    GameAssets._IC_SILVER_KEY_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Key",
                    GraphicID._IC_STRING, GraphicID._PICKUP, TileID._IC_KEY_TILE,
                    GameAssets._IC_KEY_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Lantern",
                    GraphicID._IC_LANTERN, GraphicID._PICKUP, TileID._IC_LANTERN_TILE,
                    GameAssets._IC_LANTERN_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Axe",
                    GraphicID._IC_AXE, GraphicID._PICKUP, TileID._IC_AXE_TILE,
                    GameAssets._IC_AXE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Hat",
                    GraphicID._IC_WIZARDS_HAT, GraphicID._PICKUP, TileID._IC_HAT_TILE,
                    GameAssets._IC_HAT_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Potion",
                    GraphicID._IC_GREEN_POTION, GraphicID._PICKUP, TileID._IC_POTION_TILE,
                    GameAssets._IC_GREEN_POTION_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Egg",
                    GraphicID._IC_EGG, GraphicID._PICKUP, TileID._IC_EGG_TILE,
                    GameAssets._IC_EGG_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Helmet",
                    GraphicID._IC_METAL_HELMET, GraphicID._PICKUP, TileID._IC_HELMET_TILE,
                    GameAssets._IC_METAL_HELMET_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Hammer",
                    GraphicID._IC_HAMMER, GraphicID._PICKUP, TileID._IC_HAMMER_TILE,
                    GameAssets._IC_HAMMER_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Bottle",
                    GraphicID._IC_BOTTLE, GraphicID._PICKUP, TileID._IC_BOTTLE_TILE,
                    GameAssets._IC_BOTTLE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Gear",
                    GraphicID._IC_GEAR, GraphicID._PICKUP, TileID._IC_GEAR_TILE,
                    GameAssets._IC_GEAR_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Armour",
                    GraphicID._IC_ARMOUR, GraphicID._PICKUP, TileID._IC_ARMOUR_TILE,
                    GameAssets._IC_ARMOUR_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Beer",
                    GraphicID._IC_BEER, GraphicID._PICKUP, TileID._IC_BEER_TILE,
                    GameAssets._IC_BEER_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Parchment",
                    GraphicID._IC_PARCHMENT, GraphicID._PICKUP, TileID._IC_PARCHMENT_TILE,
                    GameAssets._IC_PARCHMENT_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Feather",
                    GraphicID._IC_FEATHER, GraphicID._PICKUP, TileID._IC_FEATHER_TILE,
                    GameAssets._IC_FEATHER_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Rucksack",
                    GraphicID._IC_RUCKSACK, GraphicID._PICKUP, TileID._IC_EMERALD_TILE,
                    GameAssets._IC_RUCKSACK_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Belt",
                    GraphicID._IC_BELT, GraphicID._PICKUP, TileID._IC_BELT_TILE,
                    GameAssets._IC_BELT_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Bread",
                    GraphicID._IC_BREAD, GraphicID._PICKUP, TileID._IC_BREAD_TILE,
                    GameAssets._IC_BREAD_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Eye",
                    GraphicID._IC_EYE, GraphicID._PICKUP, TileID._IC_EYE_TILE,
                    GameAssets._IC_EYE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Leather Helmet",
                    GraphicID._IC_LEATHER_HELMET, GraphicID._PICKUP, TileID._IC_LEATHER_HELMET_TILE,
                    GameAssets._IC_LEATHER_HELMET_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Red Wand",
                    GraphicID._IC_RED_WAND, GraphicID._PICKUP, TileID._IC_RED_WAND_TILE,
                    GameAssets._IC_RED_WAND_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Gold Bar",
                    GraphicID._IC_GOLD_BAR, GraphicID._PICKUP, TileID._IC_GOLD_BAR_TILE,
                    GameAssets._IC_GOLD_BAR_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Longbow",
                    GraphicID._IC_LONGBOW, GraphicID._PICKUP, TileID._IC_LONGBOW_TILE,
                    GameAssets._IC_LONGBOW_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Blue Wand",
                    GraphicID._IC_BLUE_WAND, GraphicID._PICKUP, TileID._IC_BLUE_WAND_TILE,
                    GameAssets._IC_BLUE_WAND_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Leather Chestplate",
                    GraphicID._IC_LEATHER_CHESTPLATE, GraphicID._PICKUP, TileID._IC_LEATHER_CHESTPLATE_TILE,
                    GameAssets._IC_LEATHER_CHESTPLATE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Green Wand",
                    GraphicID._IC_GREEN_WAND, GraphicID._PICKUP, TileID._IC_GREEN_WAND_TILE,
                    GameAssets._IC_GREEN_WAND_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Mushroom",
                    GraphicID._IC_MUSHROOM, GraphicID._PICKUP, TileID._IC_MUSHROOM_TILE,
                    GameAssets._IC_MUSHROOM_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Arrow",
                    GraphicID._IC_ARROW, GraphicID._PICKUP, TileID._IC_ARROW_TILE,
                    GameAssets._IC_ARROW_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Red Potion",
                    GraphicID._IC_RED_POTION, GraphicID._PICKUP, TileID._IC_RED_POTION_TILE,
                    GameAssets._IC_RED_POTION_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Pearl",
                    GraphicID._IC_PEARL, GraphicID._PICKUP, TileID._IC_PEARL_TILE,
                    GameAssets._IC_PEARL_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Sword",
                    GraphicID._IC_SWORD, GraphicID._PICKUP, TileID._IC_SWORD_TILE,
                    GameAssets._IC_SWORD_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Blue Potion",
                    GraphicID._IC_BLUE_POTION, GraphicID._PICKUP, TileID._IC_BLUE_POTION_TILE,
                    GameAssets._IC_BLUE_POTION_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "IC Rope",
                    GraphicID._IC_ROPE, GraphicID._PICKUP, TileID._IC_ROPE_TILE,
                    GameAssets._IC_ROPE_ASSET, GameAssets._IC_FRAMES,
                    new SimpleVec2( 32, 32 )
                ),

            // -----------------------------------------------------
            // Interactive
            new SpriteDescriptor
                (
                    "Moving Platform",
                    GraphicID.G_MOVING_PLATFORM, GraphicID._INTERACTIVE, TileID._MOVING_PLATFORM_TILE,
                    GameAssets._MOVING_PLATFORM_ASSET, GameAssets._SINGLE_FRAME,
                    new SimpleVec2( 80, 16 )
                ),
            new SpriteDescriptor
                (
                    "Small Platform",
                    GraphicID.G_MOVING_PLATFORM, GraphicID._INTERACTIVE, TileID._SMALL_MOVING_PLATFORM_TILE,
                    GameAssets._SMALL_MOVING_PLATFORM_ASSET, GameAssets._SINGLE_FRAME,
                    new SimpleVec2( 40, 16 )
                ),
            new SpriteDescriptor
                (
                    "Small Boulder",
                    GraphicID.G_PUSHABLE_BOULDER, GraphicID._INTERACTIVE, TileID._SMALL_BOULDER_TILE,
                    GameAssets._SMALL_BOULDER_ASSET, GameAssets._SINGLE_FRAME,
                    new SimpleVec2( 32, 32 )
                ),
            new SpriteDescriptor
                (
                    "Crate",
                    GraphicID.G_PUSHABLE_CRATE, GraphicID._INTERACTIVE, TileID._CRATE_TILE,
                    GameAssets._CRATE_ASSET, GameAssets._CRATE_FRAMES,
                    new SimpleVec2( 48, 48 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Locked Door",
                    GraphicID.G_LOCKED_DOOR, GraphicID._INTERACTIVE, TileID._LOCKED_DOOR_TILE,
                    GameAssets._LOCKED_DOOR_ASSET, GameAssets._LOCKED_DOOR_FRAMES,
                    new SimpleVec2( 64, 96 )
                ),
            new SpriteDescriptor
                (
                    "Floor Lever",
                    GraphicID.G_FLOOR_LEVER, GraphicID._INTERACTIVE, TileID._FLOOR_LEVER_TILE,
                    GameAssets._FLOOR_LEVER_ASSET, GameAssets._FLOOR_LEVER_FRAMES,
                    new SimpleVec2( 92, 58 )
                ),
            new SpriteDescriptor
                (
                    "Checkpoint",
                    GraphicID.G_RED_FLAG, GraphicID._INTERACTIVE, TileID._FLAG_TILE,
                    GameAssets._RED_FLAG_ASSET, GameAssets._FLAG_FRAMES,
                    new SimpleVec2( 32, 50 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Checkpoint On",
                    GraphicID.G_GREEN_FLAG, GraphicID._INTERACTIVE, TileID._FLAG_TILE,
                    GameAssets._GREEN_FLAG_ASSET, GameAssets._FLAG_FRAMES,
                    new SimpleVec2( 32, 50 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "block6x6",
                    GraphicID.G_BIG_BLOCK, GraphicID._INTERACTIVE, TileID._BLOCK_6X6_TILE,
                    GameAssets._BLOCK_6X6_ASSET, GameAssets._SINGLE_FRAME,
                    new SimpleVec2( 96, 96 )
                ),

            // -----------------------------------------------------
            // Decorations
            new SpriteDescriptor
                (
                    "Eyes",
                    GraphicID.G_EYES, GraphicID._DECORATION, TileID._EYES_TILE,
                    GameAssets._EYES_ASSET, GameAssets._EYES_FRAMES,
                    new SimpleVec2( 30, 20 ),
                    Animation.PlayMode.LOOP_PINGPONG
                ),
            new SpriteDescriptor
                (
                    "Flames",
                    GraphicID.G_FLAMES, GraphicID._DECORATION, TileID._FLAME_TILE,
                    GameAssets._FLAME_ASSET, GameAssets._FLAME_FRAMES,
                    new SimpleVec2( 32, 32 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Bees",
                    GraphicID.G_BEES, GraphicID._DECORATION, TileID._BEES_TILE,
                    GameAssets._BEES_ASSET, GameAssets._BEES_FRAMES,
                    new SimpleVec2( 32, 32 ),
                    Animation.PlayMode.LOOP
                ),
            new SpriteDescriptor
                (
                    "Fish",
                    GraphicID.G_FISH, GraphicID._DECORATION, TileID._FISH_TILE,
                    GameAssets._FISH1_ASSET, GameAssets._FISH_FRAMES,
                    new SimpleVec2( 16, 16 ),
                    Animation.PlayMode.LOOP
                ),

            // -----------------------------------------------------
            // Obstacles
            new SpriteDescriptor
                (
                    "",
                    GraphicID._WALL, GraphicID._OBSTACLE, TileID._WALL_TILE,
                    null, 0,
                    new SimpleVec2( 202, 124 )
                ),

            // -----------------------------------------------------
            // Miscellaneous
            new SpriteDescriptor
                (
                    "Speech Bubble",
                    GraphicID.G_HELP_BUBBLE, GraphicID._SPEECH, TileID._SPEECH_BUBBLE_TILE,
                    GameAssets._HELP_ME_ASSET, GameAssets._HELP_ME_FRAMES,
                    new SimpleVec2( 72, 52 )
                ),
        };

    public MainPlayer mainPlayer;

    // --------------------------------------------------------
    // CODE
    // --------------------------------------------------------

    public Entities()
    {
        toJson();
//        fromJson();
    }

    private void toJson()
    {
        Json json = new Json( JsonWriter.OutputType.json );
        json.addClassTag( "Entities", Entities.class );

        FileHandle entitiesFile = Gdx.files.local( "entities.json" );

        entitiesFile.writeString( "[", false );

        for ( SpriteDescriptor descriptor : entityList )
        {
            entitiesFile.writeString( json.prettyPrint( descriptor ), true );
            entitiesFile.writeString( ",\n", true );
        }

        entitiesFile.writeString( "]\n", true );
    }

    private void fromJson()
    {
        JsonReader reader = new JsonReader();
        JsonValue  value  = reader.parse( Gdx.files.local( "entities.json" ) );

        SpriteDescriptor descriptor;

        for ( JsonValue v : value )
        {
            descriptor = new SpriteDescriptor();

            descriptor._NAME      = v.getString( "_NAME" );
            descriptor._GID       = GraphicID.valueOf( v.getString( "_GID" ) );
            descriptor._TILE      = TileID.valueOf( v.getString( "_TILE" ) );
            descriptor._ASSET     = v.getString( "_ASSET" );
            descriptor._FRAMES    = v.getInt( "_FRAMES" );
            descriptor._TYPE      = GraphicID.valueOf( v.getString( "_TYPE" ) );
            descriptor._POSITION  = new SimpleVec2();
            descriptor._SIZE      = new SimpleVec2();
            descriptor._INDEX     = 0;
            descriptor._PLAYMODE  = Animation.PlayMode.valueOf( v.getString( "_PLAYMODE" ) );
            descriptor._ANIM_RATE = 0;
            descriptor._PARENT    = null;
            descriptor._LINK      = 0;
            descriptor._DIR       = null;
            descriptor._DIST      = null;
            descriptor._SPEED     = null;
            descriptor._BOX       = null;
        }
    }

    public SimpleVec2 getAssetSize( GraphicID _gid )
    {
        SimpleVec2 size = new SimpleVec2();

        for ( final SpriteDescriptor descriptor : entityList )
        {
            if ( descriptor._GID == _gid )
            {
                size = descriptor._SIZE;
            }
        }

        if ( size.isEmpty() )
        {
            Trace.dbg( "***** SIZE FOR " + _gid + " NOT FOUND! *****" );
        }

        return size;
    }

    public int getDescriptorIndex( GraphicID gid )
    {
        int     index      = 0;
        int     defsIndex  = 0;
        boolean foundIndex = false;

        for ( SpriteDescriptor descriptor : entityList )
        {
            if ( descriptor._GID == gid )
            {
                defsIndex  = index;
                foundIndex = true;
            }

            index++;
        }

        if ( !foundIndex )
        {
            Trace.dbg( "INDEX FOR " + gid + " NOT FOUND!!!" );
        }

        return defsIndex;
    }

    public int getDescriptorIndex( TileID tileID )
    {
        int     index      = 0;
        int     defsIndex  = 0;
        boolean foundIndex = false;

        for ( SpriteDescriptor descriptor : entityList )
        {
            if ( descriptor._TILE == tileID )
            {
                defsIndex  = index;
                foundIndex = true;
            }

            index++;
        }

        if ( !foundIndex )
        {
            Trace.dbg( "INDEX FOR " + tileID + " NOT FOUND!!!" );
        }

        return defsIndex;
    }

    public SpriteDescriptor getDescriptor( GraphicID gid )
    {
        SpriteDescriptor descriptor = new SpriteDescriptor();

        descriptor.set( entityList[ getDescriptorIndex( gid ) ] );

        return descriptor;
    }

    public SpriteDescriptor getDescriptor( TileID tileID )
    {
        SpriteDescriptor descriptor = new SpriteDescriptor();

        descriptor.set( entityList[ getDescriptorIndex( tileID ) ] );

        return descriptor;
    }

    public void dispose()
    {
    }
}
