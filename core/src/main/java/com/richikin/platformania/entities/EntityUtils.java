package com.richikin.platformania.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.graphics.Gfx;
import org.jetbrains.annotations.NotNull;

public class EntityUtils
{
    public EntityUtils()
    {
    }

    /**
     * Resets all members of entityMap to their initXY positions
     */
    public void resetAllPositions()
    {
        if ( App.getEntityData().getEntityMap() != null )
        {
            GdxSprite entity;

            for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
            {
                if ( App.getEntityData().getEntity( i ).getType() == GraphicID._MAIN )
                {
                    entity = ( GdxSprite ) App.getEntityData().getEntity( i );

                    entity.getPhysicsBody().body.setTransform
                        (
                            entity.initXYZ.getX(),
                            entity.initXYZ.getY(),
                            entity.getPhysicsBody().body.getAngle()
                        );
                }
            }
        }
    }

    /**
     * Fetch an initial Z position for the specified ID.
     *
     * @param graphicID The GraphicID.
     * @return Z position range is between 0 and Gfx._MAXIMUM_Z_DEPTH.
     */
    public int getInitialZPosition( @NotNull GraphicID graphicID )
    {
        int zed;

        switch ( graphicID )
        {
            case G_SELECTION_RING:
            {
                zed = Gfx._MAXIMUM_Z_DEPTH;
            }
            break;

            case G_EYES:
            case G_FLAMES:
            case G_BEES:
            case G_FISH:
            case G_LOCKED_DOOR:
            {
                zed = 9;
            }
            break;

            case G_SPIKES:
            case G_SMALL_SPIKES:
            case G_JAIL:
            {
                zed = 8;
            }
            break;

            case G_MOVING_PLATFORM:
            case G_BIG_BLOCK:
            case G_PUSHABLE_BOULDER:
            case G_PUSHABLE_CRATE:
            case G_FLOOR_LEVER:
            case G_FLAG:
            case G_TREASURE_CHEST:
            {
                zed = 7;
            }
            break;

            case G_GEM:
            case G_COIN:
            case G_HEART:
            case G_KEY:
            case G_APPLE:
            case _IC_CHEST:
            case _IC_LETTER:
            case _IC_MAP:
            case _IC_BOOK1:
            case _IC_SILVER_COIN:
            case _IC_SCROLL:
            case _IC_CANDLE:
            case _IC_RUBY:
            case _IC_RUNE:
            case _IC_PICKAXE:
            case _IC_BOOK2:
            case _IC_CRATE:
            case _IC_LANTERN:
            case _IC_GOLD_COIN:
            case _IC_AXE:
            case _IC_WIZARDS_HAT:
            case _IC_GREEN_POTION:
            case _IC_EGG:
            case _IC_METAL_HELMET:
            case _IC_HAMMER:
            case _IC_BOTTLE:
            case _IC_GEAR:
            case _IC_ARMOUR:
            case _IC_STRING:
            case _IC_BEER:
            case _IC_BOOK3:
            case _IC_BRONZE_COIN:
            case _IC_PARCHMENT:
            case _IC_FEATHER:
            case _IC_BOOT:
            case _IC_RUCKSACK:
            case _IC_BELT:
            case _IC_BREAD:
            case _IC_EYE:
            case _IC_ARROW:
            case _IC_RED_WAND:
            case _IC_GOLD_BAR:
            case _IC_LONGBOW:
            case _IC_BLUE_WAND:
            case _IC_LEATHER_CHESTPLATE:
            case _IC_GREEN_WAND:
            case _IC_MUSHROOM:
            case _IC_BLUE_POTION:
            case _IC_ROPE:
            case _IC_LEATHER_HELMET:
            case _IC_PEARL:
            case _IC_SWORD:
            case _IC_RED_POTION:
            {
                zed = 6;
            }
            break;

            case G_QUESTION_MARK:
            case G_EXCLAMATION_MARK:
            {
                zed = 5;
            }
            break;

            case G_ENEMY_BULLET:
            {
                zed = 4;
            }
            break;

            case G_BAT:
            case G_BEAST:
            case G_PLANT:
            case G_MUSHROOM:
            case G_CROW_MAN:
            case G_WORM_MAN:
            case G_DROP_BLOCK:
            case G_SPIKEY_TURTLE:
            case G_FROG:
            case G_RABBIT:
            case G_GOBLIN:
            case G_MUSHROOM_MONSTER:
            case G_SPAWNER:
            {
                zed = 3;
            }
            break;

            case G_PLAYER:
            case G_PRISONER:
            case G_PLAYER_KNIFE:
            case G_PLAYER_ROCK:
            {
                zed = 2;
            }
            break;

            case G_LASER_BEAM:
            case G_LASER_BEAM_VERTICAL:
            case G_LASER_BEAM_HORIZONTAL:
            {
                zed = 1;
            }
            break;

            case G_PRIZE_BALLOON:
            case G_MESSAGE_BUBBLE:
            case G_HELP_BUBBLE:
            case G_EXPLOSION12:
            case G_EXPLOSION32:
            case G_EXPLOSION64:
            case G_EXPLOSION128:
            case G_EXPLOSION256:
            {
                zed = 0;
            }
            break;

            default:
            {
                zed = Gfx._MAXIMUM_Z_DEPTH + 1;
            }
            break;
        }

        return zed;
    }

    public boolean isOnScreen( @NotNull GdxSprite spriteObject )
    {
        return App.getMapData().viewportBox.overlaps( spriteObject.sprite.getBoundingRectangle() );
    }

    public void tidy()
    {
        for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
        {
            if ( App.getEntityData().getEntity( i ).getActionState() == ActionStates._DEAD )
            {
                App.getEntityData().removeEntityAt( i );
            }
        }
    }

    public void killAllExcept( GraphicID gidToLeave )
    {
        for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
        {
            if ( App.getEntityData().getEntity( i ).getGID() != gidToLeave )
            {
                App.getEntityData().getEntity( i ).setActionState( ActionStates._DEAD );
                App.getEntityData().getEntity( i ).getPhysicsBody().isAlive = false;
                App.getWorldModel().bodiesList.add( App.getEntityData().getEntity( i ).getPhysicsBody() );
            }
        }

        tidy();
    }

    /**
     * Gets a random sprite from the entity map, making
     * sure to not return the specified sprite.
     */
    public GdxSprite getRandomSprite( @NotNull GdxSprite oneToAvoid )
    {
        GdxSprite randomSprite;

        do
        {
            randomSprite = ( GdxSprite ) App.getEntityData().getEntity( MathUtils.random( App.getEntityData().getEntityMap().size - 1 ) );
        }
        while ( ( randomSprite.gid == oneToAvoid.gid )
            || ( randomSprite.sprite == null )
            || ( randomSprite.getSpriteNumber() == oneToAvoid.getSpriteNumber() ) );

        return randomSprite;
    }

    /**
     * Finds the nearest sprite of type gid to the player.
     */
    public GdxSprite findNearest( GraphicID gid )
    {
        GdxSprite distantSprite = findFirstOf( gid );

        if ( distantSprite != null )
        {
            Vector2 playerPos  = new Vector2( App.getPlayer().sprite.getX(), App.getPlayer().sprite.getY() );
            Vector2 distantPos = new Vector2( distantSprite.sprite.getX(), distantSprite.sprite.getY() );
            Vector2 spritePos  = new Vector2();

            float distance = playerPos.dst( distantPos );

            for ( IEntityComponent entity : App.getEntityData().getEntityMap() )
            {
                if ( entity.getGID() == gid )
                {
                    GdxSprite gdxSprite = ( GdxSprite ) entity;

                    spritePos.set( gdxSprite.sprite.getX(), gdxSprite.sprite.getY() );

                    float tempDistance = playerPos.dst( spritePos );

                    if ( Math.abs( tempDistance ) < Math.abs( distance ) )
                    {
                        distance      = tempDistance;
                        distantSprite = gdxSprite;
                    }
                }
            }
        }

        return distantSprite;
    }

    /**
     * Finds the furthest sprite of type gid to the player.
     */
    public GdxSprite getDistantSprite( GraphicID targetGID )
    {
        GdxSprite distantSprite = App.getPlayer();

        Vector2 playerPos  = new Vector2( App.getPlayer().sprite.getX(), App.getPlayer().sprite.getY() );
        Vector2 distantPos = new Vector2();
        Vector2 spritePos  = new Vector2();

        float distance = 0;

        for ( IEntityComponent entity : App.getEntityData().getEntityMap() )
        {
            GdxSprite gdxSprite = ( GdxSprite ) entity;

            spritePos.set( gdxSprite.sprite.getX(), gdxSprite.sprite.getY() );

            float tempDistance = playerPos.dst( spritePos );

            if ( Math.abs( tempDistance ) > Math.abs( distance ) )
            {
                distance      = tempDistance;
                distantSprite = gdxSprite;
            }
        }

        return distantSprite;
    }

    public GdxSprite findFirstOf( final GraphicID gid )
    {
        GdxSprite gdxSprite = null;

        for ( IEntityComponent entity : App.getEntityData().getEntityMap() )
        {
            if ( entity.getGID() == gid )
            {
                gdxSprite = ( GdxSprite ) entity;
                break;
            }
        }

        return gdxSprite;
    }

    public GdxSprite findLastOf( final GraphicID gid )
    {
        GdxSprite gdxSprite = null;

        for ( IEntityComponent entity : App.getEntityData().getEntityMap() )
        {
            if ( entity.getGID() == gid )
            {
                gdxSprite = ( GdxSprite ) entity;
            }
        }

        return gdxSprite;
    }

    public int findNumberOfGid( final GraphicID gid )
    {
        int count = 0;

        for ( IEntityComponent entity : App.getEntityData().getEntityMap() )
        {
            if ( entity.getGID() == gid )
            {
                count++;
            }
        }

        return count;
    }

    public int findNumberOfType( final GraphicID type )
    {
        int count = 0;

        for ( IEntityComponent entity : App.getEntityData().getEntityMap() )
        {
            if ( entity.getType() == type )
            {
                count++;
            }
        }

        return count;
    }

    public boolean canRandomlyTurn( IEntityComponent entity )
    {
        return ( ( MathUtils.random( 100 ) == 5 )
            && ( entity.getPhysicsBody().contactCount > 1 ) );
    }

    public int getHittingSameCount( GraphicID gid )
    {
        int count = 0;

        for ( IEntityComponent entity : App.getEntityData().getEntityMap() )
        {
            if ( ( entity.getGID() == gid ) && entity.isHittingSame() )
            {
                count++;
            }
        }

        return count;
    }
}
