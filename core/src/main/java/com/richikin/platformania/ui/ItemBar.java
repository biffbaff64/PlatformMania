package com.richikin.platformania.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.enums.ItemID;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ItemBar implements Disposable
{
    public static final int _NUM_ITEM_PANELS = 6;
    public static final int _ITEMS_PER_PANEL = 8;

    private static final int _X               = 0;
    private static final int _Y               = 1;
    private static final int _WIDTH           = 2;
    private static final int _HEIGHT          = 3;
    private static final int _COLLECT_PANEL   = 0;
    private static final int _ITEMS_INDEX     = 1;
    private static final int _HIGHLIGHT_INDEX = 9;

    private static final int _ICON_WIDTH  = 48;
    private static final int _ICON_HEIGHT = 48;

    public static class ItemDescriptor
    {
        public GraphicID id;
        public String    name;

        ItemDescriptor( GraphicID _id, String _name )
        {
            id   = _id;
            name = _name;
        }
    }

    //@formatter:off
    public final ItemDescriptor[][] itemsTable =
        {
            {
                new ItemDescriptor( GraphicID._IC_CHEST,                "chest_text_box"                ),
                new ItemDescriptor( GraphicID._IC_LETTER,               "letter_text_box"               ),
                new ItemDescriptor( GraphicID._IC_MAP,                  "map_text_box"                  ),
                new ItemDescriptor( GraphicID._IC_BOOK1,                "book_text_box"                 ),
                new ItemDescriptor( GraphicID._IC_SILVER_COIN,          "silver_coin_text_box"          ),
                new ItemDescriptor( GraphicID._IC_SCROLL,               "scroll_text_box"               ),
                new ItemDescriptor( GraphicID._IC_CANDLE,               "candle_text_box"               ),
                new ItemDescriptor( GraphicID._IC_RUBY,                 "ruby_text_box"                 ),
            },
            {
                new ItemDescriptor( GraphicID._IC_RUNE,                 "rune_text_box"                 ),
                new ItemDescriptor( GraphicID._IC_PICKAXE,              "pickaxe_text_box"              ),
                new ItemDescriptor( GraphicID._IC_BOOK2,                "book_text_box"                 ),
                new ItemDescriptor( GraphicID._IC_CRATE,                "crate_text_box"                ),
                new ItemDescriptor( GraphicID._IC_LANTERN,              "lantern_text_box"              ),
                new ItemDescriptor( GraphicID._IC_GOLD_COIN,            "gold_coin_text_box"            ),
                new ItemDescriptor( GraphicID._IC_AXE,                  "axe_text_box"                  ),
                new ItemDescriptor( GraphicID._IC_WIZARDS_HAT,          "wizard_hat_text_box"           ),
            },
            {
                new ItemDescriptor( GraphicID._IC_GREEN_POTION,         "green_potion_text_box"         ),
                new ItemDescriptor( GraphicID._IC_EGG,                  "egg_text_box"                  ),
                new ItemDescriptor( GraphicID._IC_METAL_HELMET,         "metal_helmet_text_box"         ),
                new ItemDescriptor( GraphicID._IC_HAMMER,               "hammer_text_box"               ),
                new ItemDescriptor( GraphicID._IC_BOTTLE,               "bottle_text_box"               ),
                new ItemDescriptor( GraphicID._IC_GEAR,                 "gear_text_box"                 ),
                new ItemDescriptor( GraphicID._IC_ARMOUR,               "armour_text_box"               ),
                new ItemDescriptor( GraphicID._IC_STRING,               "string_text_box"               ),
            },
            {
                new ItemDescriptor( GraphicID._IC_BEER,                 "beer_text_box"                 ),
                new ItemDescriptor( GraphicID._IC_BOOK3,                "book_text_box"                 ),
                new ItemDescriptor( GraphicID._IC_BRONZE_COIN,          "bronze_coin_text_box"          ),
                new ItemDescriptor( GraphicID._IC_PARCHMENT,            "parchment_text_box"            ),
                new ItemDescriptor( GraphicID._IC_FEATHER,              "feather_text_box"              ),
                new ItemDescriptor( GraphicID._IC_BOOT,                 "boot_text_box"                 ),
                new ItemDescriptor( GraphicID._IC_RUCKSACK,             "rucksack_text_box"             ),
                new ItemDescriptor( GraphicID._IC_BELT,                 "belt_text_box"                 ),
            },
            {
                new ItemDescriptor( GraphicID._IC_BREAD,                "bread_text_box"                ),
                new ItemDescriptor( GraphicID._IC_EYE,                  "eye_text_box"                  ),
                new ItemDescriptor( GraphicID._IC_LEATHER_HELMET,       "leather_helmet_text_box"       ),
                new ItemDescriptor( GraphicID._IC_RED_WAND,             "red_wand_text_box"             ),
                new ItemDescriptor( GraphicID._IC_GOLD_BAR,             "gold_bar_text_box"             ),
                new ItemDescriptor( GraphicID._IC_LONGBOW,              "longbow_text_box"              ),
                new ItemDescriptor( GraphicID._IC_BLUE_WAND,            "blue_wand_text_box"            ),
                new ItemDescriptor( GraphicID._IC_LEATHER_CHESTPLATE,   "leather_chestplate_text_box"   ),
            },
            {
                new ItemDescriptor( GraphicID._IC_GREEN_WAND,           "green_wand_text_box"           ),
                new ItemDescriptor( GraphicID._IC_MUSHROOM,             "mushroom_text_box"             ),
                new ItemDescriptor( GraphicID._IC_ARROW,                "arrow_text_box"                ),
                new ItemDescriptor( GraphicID._IC_RED_POTION,           "red_potion_text_box"           ),
                new ItemDescriptor( GraphicID._IC_PEARL,                "pearl_text_box"                ),
                new ItemDescriptor( GraphicID._IC_SWORD,                "sword_text_box"                ),
                new ItemDescriptor( GraphicID._IC_BLUE_POTION,          "blue_potion_text_box"          ),
                new ItemDescriptor( GraphicID._IC_ROPE,                 "rope_text_box"                 ),
            },
        };

    private static final int[][] displayPos =
        {
            //
            { 414, 12, 452, 82 },  // Collection Panel

            //
            { 441, 29, 48, 48, 441, 29, 48, 48 },
            { 491, 29, 48, 48, 491, 29, 48, 48 },
            { 541, 29, 48, 48, 541, 29, 48, 48 },
            { 591, 29, 48, 48, 591, 29, 48, 48 },
            { 641, 29, 48, 48, 641, 29, 48, 48 },
            { 691, 29, 48, 48, 691, 29, 48, 48 },
            { 741, 29, 48, 48, 741, 29, 48, 48 },
            { 791, 29, 48, 48, 791, 29, 48, 48 },
        };
    //@formatter:on

    private TextureRegion[][] itemTextures;
    private TextureRegion     highlightBox;
    private int               highlightIndex;
    private StopWatch         stopWatch;
    private ItemID            itemIndex;

    public ItemBar()
    {
        createIndividualItems();
    }

    public void draw( float originX, float originY )
    {
        TextureRegion textureRegion;

        for ( int i = 0; i < 8; i++ )
        {
            if ( App.getGameProgress().collectItems[ App.getHud().getItemPanelIndex() ][ i ] )
            {
                textureRegion = itemTextures[ App.getHud().getItemPanelIndex() ][ i ];
            }
            else
            {
                textureRegion = itemTextures[ App.getHud().getItemPanelIndex() + _NUM_ITEM_PANELS ][ i ];
            }

            if ( textureRegion != null )
            {
                App.getSpriteBatch().draw
                    (
                        textureRegion,
                        originX + displayPos[ _ITEMS_INDEX + i ][ _X ],
                        originY + displayPos[ _ITEMS_INDEX + i ][ _Y ],
                        displayPos[ _ITEMS_INDEX + i ][ _WIDTH ],
                        displayPos[ _ITEMS_INDEX + i ][ _HEIGHT ]
                    );
            }
        }

        if ( highlightBox != null )
        {
            if ( stopWatch.time( TimeUnit.MILLISECONDS ) > 3000 )
            {
                stopWatch    = null;
                highlightBox = null;
            }
            else
            {
                App.getSpriteBatch().draw( highlightBox, displayPos[ highlightIndex ][ 4 ], displayPos[ highlightIndex ][ 5 ] );
            }
        }
    }

    public ItemDescriptor getItem( int panel, int item )
    {
        return itemsTable[ panel ][ item ];
    }

    public TextureRegion getIconTexture( GraphicID graphicID )
    {
        int panel = 0;
        int item  = 0;

        for ( int i = 0; i < _NUM_ITEM_PANELS; i++ )
        {
            for ( int j = 0; j < _ITEMS_PER_PANEL; j++ )
            {
                if ( itemsTable[ i ][ j ].id == graphicID )
                {
                    panel = i;
                    item  = j;
                }
            }
        }

        return itemTextures[ panel ][ item ];
    }

    public void markItemCollected( GraphicID graphicID )
    {
        for ( int i = 0; i < _NUM_ITEM_PANELS; i++ )
        {
            for ( int j = 0; j < _ITEMS_PER_PANEL; j++ )
            {
                if ( itemsTable[ i ][ j ].id == graphicID )
                {
                    App.getGameProgress().collectItems[ i ][ j ] = true;

                    App.getHud().setItemPanelIndex( i );

                    announceCollection( i, j );
                }
            }
        }
    }

    public boolean isItemBarItem( @NotNull GraphicID gid )
    {
        boolean isItem;

        switch ( gid )
        {
            case _IC_CANDLE:            // Level - 1
            case _IC_RUCKSACK:          // Level - 1
            case _IC_RED_POTION:        // Level - 1
            case _IC_AXE:               // Level - 1
                // --------------------------------
                // Level - 2
                // Level - 2
                // Level - 2
                // Level - 2
                // --------------------------------
                // Level - 3
                // Level - 3
                // Level - 3
                // Level - 3
                // --------------------------------
                // Level - 4
                // Level - 4
                // Level - 4
                // Level - 4
                // --------------------------------
                // Level - 5
                // Level - 5
                // Level - 5
                // Level - 5
                // --------------------------------
                // Level - 6
                // Level - 6
                // Level - 6
                // Level - 6
                // --------------------------------
                // Level - 7
                // Level - 7
                // Level - 7
                // Level - 7
                // --------------------------------
                // Level - 8
                // Level - 8
                // Level - 8
                // Level - 8
                // --------------------------------
            case _IC_CHEST:
            case _IC_LETTER:
            case _IC_MAP:
            case _IC_BOOK1:
            case _IC_SILVER_COIN:
            case _IC_SCROLL:
            case _IC_RUBY:
                // --------------------------------
            case _IC_RUNE:
            case _IC_PICKAXE:
            case _IC_BOOK2:
            case _IC_CRATE:
            case _IC_LANTERN:
            case _IC_GOLD_COIN:
            case _IC_WIZARDS_HAT:
                // --------------------------------
            case _IC_GREEN_POTION:
            case _IC_EGG:
            case _IC_METAL_HELMET:
            case _IC_HAMMER:
            case _IC_BOTTLE:
            case _IC_GEAR:
            case _IC_ARMOUR:
            case _IC_STRING:
                // --------------------------------
            case _IC_BEER:
            case _IC_BOOK3:
            case _IC_BRONZE_COIN:
            case _IC_PARCHMENT:
            case _IC_FEATHER:
            case _IC_BOOT:
            case _IC_BELT:
                // --------------------------------
            case _IC_BREAD:
            case _IC_EYE:
            case _IC_LEATHER_HELMET:
            case _IC_RED_WAND:
            case _IC_GOLD_BAR:
            case _IC_LONGBOW:
            case _IC_BLUE_WAND:
            case _IC_LEATHER_CHESTPLATE:
                // --------------------------------
            case _IC_GREEN_WAND:
            case _IC_MUSHROOM:
            case _IC_ARROW:
            case _IC_PEARL:
            case _IC_SWORD:
            case _IC_BLUE_POTION:
            case _IC_ROPE:
            {
                isItem = true;
            }
            break;

            default:
            {
                isItem = false;
            }
            break;
        }

        return isItem;
    }

    private void createIndividualItems()
    {
        TextureRegion[][] tmpFrames;
        TextureRegion     srcTexture = App.getAssets().getAnimationRegion( GameAssets._ICONS_ASSET );

        tmpFrames    = new TextureRegion( srcTexture ).split( _ICON_WIDTH, _ICON_HEIGHT );
        itemTextures = new TextureRegion[ _NUM_ITEM_PANELS * 2 ][ _ITEMS_PER_PANEL ];

        for ( int row = 0; row < tmpFrames.length; row++ )
        {
            System.arraycopy( tmpFrames[ row ], 0, itemTextures[ row ], 0, tmpFrames[ row ].length );
        }
    }

    private void announceCollection( int row, int column )
    {
        App.getHud().getTextPanel().setTexture( itemsTable[ row ][ column ].name );
        App.getHud().getTextPanel().activate();
        App.getHud().getTextPanel().setDelay( 1500 );

        stopWatch      = new StopWatch();
        highlightIndex = column + 1;
        highlightBox   = App.getAssets().getObjectRegion( "itembar_highlight" );
    }

    @Override
    public void dispose()
    {
        for ( int i = 0; i < _NUM_ITEM_PANELS; i++ )
        {
            for ( int j = 0; j < _ITEMS_PER_PANEL; j++ )
            {
                itemTextures[ i ][ j ] = null;
            }
        }

        itemTextures = null;
        highlightBox = null;
        stopWatch    = null;
    }
}
