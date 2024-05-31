package com.richikin.platformania.enums;

public enum ItemID
{
    //@formatter:off
    _IC_CHEST               ( 0 ),
    _IC_LETTER              ( 1 ),
    _IC_MAP                 ( 2 ),
    _IC_BOOK1               ( 3 ),
    _IC_SILVER_COIN         ( 4 ),
    _IC_SCROLL              ( 5 ),
    _IC_CANDLE              ( 6 ),
    _IC_RUBY                ( 7 ),

    _IC_RUNE                ( 8 ),
    _IC_PICKAXE             ( 9 ),
    _IC_BOOK2               ( 10 ),
    _IC_CRATE               ( 11 ),
    _IC_LANTERN             ( 12 ),
    _IC_GOLD_COIN           ( 13 ),
    _IC_AXE                 ( 14 ),
    _IC_WIZARDS_HAT         ( 15 ),

    _IC_GREEN_POTION        ( 16 ),
    _IC_EGG                 ( 17 ),
    _IC_METAL_HELMET        ( 18 ),
    _IC_HAMMER              ( 19 ),
    _IC_BOTTLE              ( 20 ),
    _IC_GEAR                ( 21 ),
    _IC_ARMOUR              ( 22 ),
    _IC_STRING              ( 23 ),

    _IC_BEER                ( 24 ),
    _IC_BOOK3               ( 25 ),
    _IC_BRONZE_COIN         ( 26 ),
    _IC_PARCHMENT           ( 27 ),
    _IC_FEATHER             ( 28 ),
    _IC_BOOT                ( 29 ),
    _IC_RUCKSACK            ( 30 ),
    _IC_BELT                ( 31 ),

    _IC_BREAD               ( 32 ),
    _IC_EYE                 ( 33 ),
    _IC_LEATHER_HELMET      ( 34 ),
    _IC_RED_WAND            ( 35 ),
    _IC_GOLD_BAR            ( 36 ),
    _IC_LONGBOW             ( 37 ),
    _IC_BLUE_WAND           ( 38 ),
    _IC_LEATHER_CHESTPLATE  ( 39 ),

    _IC_GREEN_WAND          ( 40 ),
    _IC_MUSHROOM            ( 41 ),
    _IC_ARROW               ( 42 ),
    _IC_RED_POTION          ( 43 ),
    _IC_PEARL               ( 44 ),
    _IC_SWORD               ( 45 ),
    _IC_BLUE_POTION         ( 46 ),
    _IC_ROPE                ( 47 );
    //@formatter:on

    private final int itemNumber;

    ItemID( int value)
    {
        this.itemNumber = value;
    }

    public int get()
    {
        return itemNumber;
    }
}
