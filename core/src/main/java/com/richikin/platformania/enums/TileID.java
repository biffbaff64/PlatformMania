package com.richikin.platformania.enums;

public enum TileID
{
    //@formatter:off
    _DEFAULT_TILE                   (0),

    // Row 1
    _PLAYER_TILE                    (1),
    _COIN_TILE                      (2),
    _GEM_TILE                       (3),
    _CHEST_TILE                     (4),
    _HEART_TILE                     (5),
    _KEY_TILE                       (6),
    _APPLE_TILE                     (7),
    _GREEN_GEM_TILE                 (8),
    _RED_GEM_TILE                   (9),
    _YELLOW_GEM_TILE                (10),

    // Row 2
    _IC_CHEST_TILE                  (11),
    _IC_LETTER_TILE                 (12),
    _IC_MAP_TILE                    (13),
    _IC_BOOK1_TILE                  (14),
    _IC_SILVER_COIN_TILE            (15),
    _IC_SCROLL_TILE                 (16),
    _IC_CANDLE_TILE                 (17),
    _IC_RUBY_TILE                   (18),
    _MUSHROOM_MONSTER_TILE          (19),
    _GOBLIN_TILE                    (20),

    // Row 3
    _IC_RUNE_TILE                   (21),
    _IC_PICKAXE_TILE                (22),
    _IC_BOOK2_TILE                  (23),
    _IC_GOLD_KEY_TILE               (24),
    _IC_LANTERN_TILE                (25),
    _IC_GOLD_COIN_TILE              (26),
    _IC_AXE_TILE                    (27),
    _IC_HAT_TILE                    (28),
    _U29_TILE                       (29),
    _U30_TILE                       (30),

    // Row 4
    _IC_POTION_TILE                 (31),
    _IC_EGG_TILE                    (32),
    _IC_HELMET_TILE                 (33),
    _IC_HAMMER_TILE                 (34),
    _IC_BOTTLE_TILE                 (35),
    _IC_GEAR_TILE                   (36),
    _IC_ARMOUR_TILE                 (37),
    _IC_KEY_TILE                    (38),
    _U39_TILE                       (39),
    _U40_TILE                       (40),

    // Row 5
    _IC_BEER_TILE                   (41),
    _IC_BOOK3_TILE                  (42),
    _IC_BRONZE_COIN_TILE            (43),
    _IC_PARCHMENT_TILE              (44),
    _IC_FEATHER_TILE                (45),
    _IC_SILVER_KEY_TILE             (46),
    _IC_EMERALD_TILE                (47),
    _IC_BELT_TILE                   (48),
    _U49_TILE                       (49),
    _U50_TILE                       (50),

    // Row 6
    _MOVING_PLATFORM_TILE           (51),
    _SPIKES_TILE                    (52),
    _SMALL_MOVING_PLATFORM_TILE     (53),
    _SMALL_BOULDER_TILE             (54),
    _CRATE_TILE                     (55),
    _FLAME_TILE                     (56),
    _BEES_TILE                      (57),
    _FISH_TILE                      (58),
    _BLOCK_6X6_TILE                 (59),
    _DOWN_SPIKES_TILE               (60),

    // Row 7
    _BAT_TILE                       (61),
    _BEAST_TILE                     (62),
    _PLANT_TILE                     (63),
    _MUSHROOM_TILE                  (64),
    _CROW_MAN_TILE                  (65),
    _WORM_MAN_TILE                  (66),
    _DROP_BLOCK_TILE                (67),
    _SPIKEY_TURTLE_TILE             (68),
    _FROG_TILE                      (69),
    _RABBIT_TILE                    (70),

    // Row 8
    _EYES_TILE                      (71),
    _JAIL_TILE                      (72),
    _PRISONER_TILE                  (73),
    _LOCKED_DOOR_TILE               (74),
    _FLOOR_LEVER_TILE               (75),
    _FLAG_TILE                      (76),
    _SPAWNER_TILE                   (77),
    _LEFT_SPIKES_TILE               (78),
    _RIGHT_SPIKES_TILE              (79),
    _U80_TILE                       (80),

    // Row 9
    _IC_BREAD_TILE                  (81),
    _IC_EYE_TILE                    (82),
    _IC_LEATHER_HELMET_TILE         (83),
    _IC_RED_WAND_TILE               (84),
    _IC_GOLD_BAR_TILE               (85),
    _IC_LONGBOW_TILE                (86),
    _IC_BLUE_WAND_TILE              (87),
    _IC_LEATHER_CHESTPLATE_TILE     (88),
    _U89_TILE                       (89),
    _U90_TILE                       (90),

    // Row 10
    _IC_GREEN_WAND_TILE             (91),
    _IC_MUSHROOM_TILE               (92),
    _IC_ARROW_TILE                  (93),
    _IC_RED_POTION_TILE             (94),
    _IC_PEARL_TILE                  (95),
    _IC_SWORD_TILE                  (96),
    _IC_BLUE_POTION_TILE            (97),
    _IC_ROPE_TILE                   (98),
    _U99_TILE                       (99),
    _U100_TILE                      (100),

    // Row 11
    _U101_TILE                      (101),
    _U102_TILE                      (102),
    _U103_TILE                      (103),
    _U104_TILE                      (104),
    _U105_TILE                      (105),
    _U106_TILE                      (106),
    _U107_TILE                      (107),
    _U108_TILE                      (108),
    _U109_TILE                      (109),
    _U110_TILE                      (110),

    // Row 12
    _U111_TILE                      (111),
    _U112_TILE                      (112),
    _U113_TILE                      (113),
    _U114_TILE                      (114),
    _U115_TILE                      (115),
    _U116_TILE                      (116),
    _U117_TILE                      (117),
    _U118_TILE                      (118),
    _U119_TILE                      (119),
    _U120_TILE                      (120),

    // Row 13
    _U121_TILE                      (121),
    _U122_TILE                      (122),
    _U123_TILE                      (123),
    _U124_TILE                      (124),
    _U125_TILE                      (125),
    _U126_TILE                      (126),
    _U127_TILE                      (127),
    _U128_TILE                      (128),
    _U129_TILE                      (129),
    _U130_TILE                      (130),

    // Row 14
    _U131_TILE                      (131),
    _U132_TILE                      (132),
    _U133_TILE                      (133),
    _U134_TILE                      (134),
    _U135_TILE                      (135),
    _U136_TILE                      (136),
    _U137_TILE                      (137),
    _U138_TILE                      (138),
    _U139_TILE                      (139),
    _U140_TILE                      (140),

    // Row 15
    _U141_TILE                      (141),
    _U142_TILE                      (142),
    _U143_TILE                      (143),
    _U144_TILE                      (144),
    _U145_TILE                      (145),
    _U146_TILE                      (146),
    _U147_TILE                      (147),
    _U148_TILE                      (148),
    _U149_TILE                      (149),
    _NO_ACTION_TILE                 (150),

    // Tile IDs that are used in path finding
    _GROUND                         (200),
    _HOLE                           (201),
    _WATER                          (202),
    _GRASS                          (203),
    _WALL_EDGE                      (204),
    _WALL_TILE                      (205),

    // Tile IDs that aren't indexes into the tileset
    _EXPLOSION_TILE                 (250),
    _BAT_STONE_TILE                 (251),
    _SPEECH_BUBBLE_TILE             (252),
    _PLAYER_WEAPON_TILE             (253 ),

    _UNKNOWN                        (255);
    //@formatter:on

    private final int tileNumber;

    TileID(int value)
    {
        this.tileNumber = value;
    }

    public static TileID fromValue(int value)
    {
        TileID returnValue = _UNKNOWN;

        for (TileID tileID : values())
        {
            if (tileID.get() == value)
            {
                returnValue = tileID;
            }
        }

        return returnValue;
    }

    public int get()
    {
        return tileNumber;
    }
}
