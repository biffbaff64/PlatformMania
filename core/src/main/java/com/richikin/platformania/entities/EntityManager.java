package com.richikin.platformania.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.managers.DecorationsManager;
import com.richikin.platformania.entities.managers.EnemyManager;
import com.richikin.platformania.entities.managers.HazardsManager;
import com.richikin.platformania.entities.managers.InteractiveManager;
import com.richikin.platformania.entities.managers.PickupManager;
import com.richikin.platformania.entities.managers.PlayerManager;
import com.richikin.platformania.entities.managers.SpawnerManager;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.graphics.text.TextUtils;
import com.richikin.platformania.logging.Trace;

import org.jetbrains.annotations.Nullable;

public class EntityManager implements Disposable
{
    public int     playerIndex;
    public boolean playerReady;
    public boolean canDrawDebug;

    @Nullable
    private PlayerManager playerManager;

    public EntityManager()
    {
    }

    /**
     * Initialises objects used by EntityManager.
     * Adds managers to the manager list. Currently, the PlayerManager
     * is NOT added here as it is dealt with in {@link EntityManager#initialiseForLevel()}
     * Look in to changing PlayerManager so that it can be added here.
     */
    public void initialise()
    {
        App.getEntityData().addManager( new DecorationsManager() );
        App.getEntityData().addManager( new InteractiveManager() );
        App.getEntityData().addManager( new PickupManager() );
        App.getEntityData().addManager( new EnemyManager() );
        App.getEntityData().addManager( new SpawnerManager() );
        App.getEntityData().addManager( new HazardsManager() );
    }

    public void initialiseForLevel()
    {
        Trace.checkPoint();

        App.getAppConfig().entitiesExist = false;

        // For development purposes:-
        // The main player character can be disabled by moving the
        // preference setting '_DISABLE_PLAYER' from the 'disable' list
        // into the 'enable' list in AppConfig#setTempDeveloperSettings().
        if ( !App.getDev().isDevMode() || App.getSettings().isDisabled( Settings._DISABLE_PLAYER ) )
        {
            if ( App.getPlayer() == null )
            {
                // Main player needs creating
                playerManager = new PlayerManager();
                playerManager.init();
            }
            else
            {
                App.getPlayer().restartPlayer();
            }
        }

        // Initialise all other entities such as bricks, bonuses, balls etc.
        // The main player character isn't included here because of the
        // conditions described above.
        Array.ArrayIterator< IEntityManagerComponent > iter;
        iter = new Array.ArrayIterator<>( App.getEntityData().getManagerList() );

        while ( iter.hasNext() )
        {
            iter.next().init();
        }

        updateEntityMapIndexes();
        connectLinkedEntities();

        if ( !App.getGameProgress().isRestarting )
        {
            EntityStats.report();
        }

        canDrawDebug = false;

        App.getAppConfig().entitiesExist = true;
    }

    /**
     * Update all sprite entity actions. Does not update if the game is paused.
     * The main player sprite is updated after all others so that it can react
     * to any other entities actions.
     * Entity Managers are also updated here, after all entity sprites.
     */
    public void updateSprites()
    {
        if ( isEntityUpdateAllowed() && !App.getAppConfig().gamePaused )
        {
            //
            // Update all non-player entities.
            for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
            {
                GdxSprite entity = ( GdxSprite ) App.getEntityData().getEntity( i );

                if ( ( entity.getActionState() != ActionStates._DEAD )
                    && ( entity.gid != GraphicID.G_PLAYER ) )
                {
                    entity.preUpdate();
                    entity.update();
                }
            }

            //
            // Main Player, updated after all other entities.
            // Updated last to allow for possible reacting to
            // other entities actions.
            if ( playerReady && ( App.getPlayer().getActionState() != ActionStates._DEAD ) )
            {
                App.getPlayer().preUpdate();
                App.getPlayer().update();
            }

            //
            // Update the various entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if ( !App.getGameProgress().levelCompleted )
            {
                Array.ArrayIterator< IEntityManagerComponent > iter;
                iter = new Array.ArrayIterator<>( App.getEntityData().getManagerList() );

                while ( iter.hasNext() )
                {
                    iter.next().update();
                }
            }
        }
    }

    /**
     * Removes any entities from the entityMap that are _DEAD.
     * Box2D world bodies are dealt with elsewhere.
     */
    public void tidySprites()
    {
        if ( App.getEntityData().getEntityMap() != null )
        {
            for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
            {
                GdxSprite entity = ( GdxSprite ) App.getEntityData().getEntity( i );

                if ( entity != null )
                {
                    if ( entity.getActionState() != ActionStates._DEAD )
                    {
                        entity.postUpdate();
                    }

                    //
                    // NB: entity might have died in postUpdate, which is
                    // why this next if() is not an 'else'.
                    if ( ( entity.getActionState() == ActionStates._DEAD )
                        || App.getAppConfig().isShuttingMainScene )
                    {
                        switch ( entity.gid )
                        {
                            case G_PLAYER:
                            case G_NO_ID:
                            case _GROUND:
                            case _WALL:
                            {
                            }
                            break;

                            case G_EXPLOSION12:
                            case G_EXPLOSION64:
                            case G_EXPLOSION128:
                            case G_EXPLOSION256:
                            {
                                entity.tidy( i );
                            }
                            break;

                            default:
                            {
                                entity.getPhysicsBody().isAlive = false;
                                App.getWorldModel().bodiesList.add( entity.getPhysicsBody() );
                                App.getEntityData().removeEntityAt( i );
                                entity.tidy( i );
                            }
                            break;
                        }

                        updateEntityMapIndexes();
                    }
                }
            }
        }
    }

    /**
     * Draw all sprites to the scene.
     * Uses a Z-coord system: 0 at front, MAX at rear.
     * Any sprites with a Z value > MAX or < 0 will not be drawn.
     */
    public void drawSprites()
    {
        if ( isEntityUpdateAllowed() )
        {
            GdxSprite entity;

            for ( int z = Gfx._MAXIMUM_Z_DEPTH - 1; z >= 0; z-- )
            {
                for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
                {
                    entity = ( GdxSprite ) App.getEntityData().getEntity( i );

                    if ( ( entity != null ) && ( entity.zPosition == z ) )
                    {
                        entity.preDraw();

                        if ( isInViewWindow( entity ) && entity.isDrawable )
                        {
                            entity.draw( App.getSpriteBatch() );

                            if ( App.getDev().isDevMode() && canDrawDebug )
                            {
                                if ( ( App.getPlayer().getLink() > 0 )
                                    && ( entity.getSpriteNumber() == App.getPlayer().getLink() ) )
                                {
                                    TextUtils.inst().drawText
                                        (
                                            "Linked",
                                            entity.sprite.getX(),
                                            entity.sprite.getY() + entity.frameHeight,
                                            App.getSpriteBatch()
                                        );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isInViewWindow( GdxSprite entity )
    {
        boolean isInView = true;

        if ( App.getSettings().isEnabled( Settings._CULL_SPRITES ) )
        {
            isInView = App.getEntityUtils().isOnScreen( entity );
        }

        return isInView;
    }

    /**
     * Updates the 'spriteNumber' values for all GdxSprites,
     * to cater for any entities that have been removed from entityMap.
     */
    public void updateEntityMapIndexes()
    {
        playerIndex = 0;

        for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
        {
            GdxSprite entity = ( GdxSprite ) App.getEntityData().getEntity( i );

            if ( entity != null )
            {
                entity.spriteNumber = i;

                if ( entity.gid == GraphicID.G_PLAYER )
                {
                    playerIndex = i;
                }
            }
        }
    }

    /**
     * Establish links between certain entities.
     * i.e. Hitting a switch opens a specific door, so those
     * two entities (the door and the switch) must be linked.
     */
    public void connectLinkedEntities()
    {
        Array.ArrayIterator< IEntityComponent > iter = new Array.ArrayIterator<>( App.getEntityData().getEntityMap() );

        while ( iter.hasNext() )
        {
            IEntityComponent component = iter.next();

            if ( component.isLinked() )
            {
                for ( int j = 0; j < App.getEntityData().getEntityMap().size; j++ )
                {
                    IEntityComponent sprite = App.getEntityData().getEntityMap().get( j );

                    if ( sprite.getSpriteNumber() != component.getSpriteNumber() )
                    {
                        if ( sprite.getLink() == component.getLink() )
                        {
                            sprite.setLink( component.getSpriteNumber() );
                            component.setLink( sprite.getSpriteNumber() );
                        }
                    }
                }
            }
        }
    }

    public boolean isEntityUpdateAllowed()
    {
        return ( App.getAppConfig().entitiesExist && !App.getAppConfig().isShuttingMainScene );
    }

    @Override
    public void dispose()
    {
        playerManager = null;
    }
}
