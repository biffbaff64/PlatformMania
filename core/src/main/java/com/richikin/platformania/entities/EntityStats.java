package com.richikin.platformania.entities;

import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;

import java.util.Arrays;

public abstract class EntityStats
{
    public static final int MIN = 0;
    public static final int MAX = 1;
    public static final int NUM = 2;

    private static class StatsMeter
    {
        public final GraphicID gid;
        public final String    name;
        public final int[]     meter;

        public StatsMeter( String _name )
        {
            gid   = GraphicID.G_NO_ID;
            name  = _name;
            meter = new int[]{ 0, 0, 0 };
        }

        public StatsMeter( GraphicID _gid, String _name )
        {
            gid   = _gid;
            name  = _name;
            meter = new int[]{ 0, 0, 0 };
        }

        public void reset()
        {
            Arrays.fill( meter, 0 );
        }

        public void debug()
        {
            Trace.info( name, meter[ NUM ] );
        }
    }

    //@formatter:off
    public static final StatsMeter[] metersList =
        {
            new StatsMeter( GraphicID.G_BAT,                "Bats           : " ),
            new StatsMeter( GraphicID.G_BEAST,              "Beasts         : " ),
            new StatsMeter( GraphicID.G_MUSHROOM,           "Mushrooms      : " ),
            new StatsMeter( GraphicID.G_PLANT,              "Plants         : " ),
            new StatsMeter( GraphicID.G_DROP_BLOCK,         "DropBlocks     : " ),
            new StatsMeter( GraphicID.G_SPIKEY_TURTLE,      "Turtles        : " ),
            new StatsMeter( GraphicID.G_SPIKES,             "Spikes         : " ),
            new StatsMeter( GraphicID.G_CROW_MAN,           "Crow Men       : " ),
            new StatsMeter( GraphicID.G_WORM_MAN,           "Worm Men       : " ),
            new StatsMeter( GraphicID.G_SPAWNER,            "Spawner        : " ),
            new StatsMeter( GraphicID.G_PUSHABLE_BOULDER,   "Boulders       : " ),
            new StatsMeter( GraphicID.G_HEART,              "Hearts         : " ),
            new StatsMeter( GraphicID.G_COIN,               "Coins          : " ),
            new StatsMeter( GraphicID.G_GEM,                "Gems           : " ),
            new StatsMeter( GraphicID.G_KEY,                "Keys           : " ),
            new StatsMeter( GraphicID.G_TREASURE_CHEST,     "Treasure Chests: " ),
            new StatsMeter( GraphicID.G_PRISONER,           "Villagers      : " ),
            new StatsMeter( GraphicID.G_MOVING_PLATFORM,    "Platforms      : " ),
            new StatsMeter( GraphicID.G_EYES,               "Eyes           : " ),
            new StatsMeter( GraphicID.G_PUSHABLE_CRATE,     "Crates         : " ),
            new StatsMeter( GraphicID.G_FLAMES,             "Flames         : " ),
            new StatsMeter( GraphicID.G_SPAWNER,            "Spawners       : " ),
            new StatsMeter( GraphicID.G_UNKNOWN,            "Unknown        : " ),
            // ------------------------------------
            new StatsMeter( GraphicID._IC_ARMOUR,           "IC Armour      : " ),
            new StatsMeter( GraphicID._IC_AXE,              "IC Axe         : " ),
            new StatsMeter( GraphicID._IC_BEER,             "IC Beer        : " ),
            new StatsMeter( GraphicID._IC_BELT,             "IC Belt        : " ),
            new StatsMeter( GraphicID._IC_BOOK1,            "IC Book1       : " ),
            new StatsMeter( GraphicID._IC_BOOK2,            "IC Book2       : " ),
            new StatsMeter( GraphicID._IC_BOOK3,            "IC Book3       : " ),
            new StatsMeter( GraphicID._IC_BOOT,             "IC Boot        : " ),
            // ------------------------------------
            new StatsMeter( GraphicID._IC_BOTTLE,           "IC Bottle      : " ),
            new StatsMeter( GraphicID._IC_BRONZE_COIN,      "IC Bronze Coin : " ),
            new StatsMeter( GraphicID._IC_CANDLE,           "IC Candle      : " ),
            new StatsMeter( GraphicID._IC_CHEST,            "IC Chest       : " ),
            new StatsMeter( GraphicID._IC_CRATE,            "IC Egg         : " ),
            new StatsMeter( GraphicID._IC_RUCKSACK,         "IC Emerald     : " ),
            new StatsMeter( GraphicID._IC_FEATHER,          "IC Feather     : " ),
            // ------------------------------------
            new StatsMeter( GraphicID._IC_GEAR,             "IC Gear        : " ),
            new StatsMeter( GraphicID._IC_GOLD_COIN,        "IC Gold Coin   : " ),
            new StatsMeter( GraphicID._IC_HAMMER,           "IC Hammer      : " ),
            new StatsMeter( GraphicID._IC_WIZARDS_HAT,      "IC Hat         : " ),
            new StatsMeter( GraphicID._IC_METAL_HELMET,     "IC Helmet      : " ),
            new StatsMeter( GraphicID._IC_LANTERN,          "IC Lantern     : " ),
            new StatsMeter( GraphicID._IC_LETTER,           "IC Letter      : " ),
            new StatsMeter( GraphicID._IC_MAP,              "IC Map         : " ),
            // ------------------------------------
            new StatsMeter( GraphicID._IC_PARCHMENT,        "IC Parchment   : " ),
            new StatsMeter( GraphicID._IC_PICKAXE,          "IC Pickaxe     : " ),
            new StatsMeter( GraphicID._IC_GREEN_POTION,     "IC Potion      : " ),
            new StatsMeter( GraphicID._IC_SCROLL,           "IC Scroll      : " ),
            new StatsMeter( GraphicID._IC_SILVER_COIN,      "IC Silver Coin : " ),
            new StatsMeter( GraphicID._IC_STRING,           "IC String      : " ),
            new StatsMeter( GraphicID._IC_RUBY,             "IC Ruby        : " ),
            new StatsMeter( GraphicID._IC_RUNE,             "IC Rune        : " ),
        };
    //@formatter:on

    public static void log( GraphicID graphicID )
    {
        for ( StatsMeter statsMeter : metersList )
        {
            if ( statsMeter.gid == graphicID )
            {
                statsMeter.meter[ NUM ]++;
            }
        }
    }

    public static void clearAllMeters()
    {
        for ( StatsMeter statsMeter : metersList )
        {
            Arrays.fill( statsMeter.meter, 0 );
        }
    }

    public static void report()
    {
        Trace.checkPoint();

        for ( StatsMeter statsMeter : metersList )
        {
            statsMeter.debug();
        }
    }
}
