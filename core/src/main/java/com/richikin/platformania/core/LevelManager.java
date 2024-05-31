package com.richikin.platformania.core;

import com.richikin.platformania.enums.CamID;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.logging.Trace;

// TODO: 25/09/2022 - Could this class be combined with GameProgress to create GameManager??

public class LevelManager
{
    // TRUE if the current level is being processed for the first time.
    private boolean isFirstTime;

    public LevelManager()
    {
        isFirstTime = true;
    }

    /**
     * Set up everything necessary for a new game,
     * called in MainScene#initialise.
     */
    public void prepareNewGame()
    {
        if ( isFirstTime )
        {
            Trace.checkPoint();

            //
            // Make sure all progress counters are initialised.
            App.getGameProgress().initialise();

            //
            // Initialise the room that the game will start in.
            App.getRoomManager().initialise();

            //
            // Only the HUD camera is enabled initially.
            App.getBaseRenderer().enableCamera( CamID._HUD );

            App.getEntityManager().initialise();
            App.getMapData().update();

            // Score, Lives display etc.
            App.getHud().createHud();
        }

        isFirstTime = false;
    }

    /**
     * Prepare the current level by setting up maps, entities
     * and any relevant flags/variables.
     */
    public void prepareCurrentLevel( boolean firstTime )
    {
        Trace.checkPoint();

        if ( App.getGameProgress().isRestarting )
        {
            restartCurrentLevel();
        }
        else if ( firstTime || App.getGameProgress().levelCompleted )
        {
            setupForNewLevel();
        }

        App.getAppConfig().gamePaused          = false;
        App.getAppConfig().isShuttingMainScene = false;
        App.getAppConfig().forceQuitToMenu     = false;

        App.getHud().refillItems();

        App.getGameProgress().isRestarting   = false;
        App.getGameProgress().levelCompleted = false;
        App.getGameProgress().gameOver       = false;
    }

    /**
     * Reset all entity positions, and re-init
     * the main player, ready to replay the current
     * level.
     */
    private void restartCurrentLevel()
    {
        Trace.checkPoint();

        // TODO: 25/09/2022 - This is incorrect. It should take into account items that have been
        //                  - collected, as these should not reappear if a level restarts.

        App.getEntityUtils().killAllExcept( GraphicID.G_PLAYER );
        App.getEntityData().getEntityMap().setSize( 1 );
        App.getEntityManager().initialiseForLevel();
    }

    /**
     * Sets up the map and entities for a new level.
     */
    private void setupForNewLevel()
    {
        Trace.checkPoint();

        App.getRoomManager().setRoom( App.getLevel(), 0 );

        App.getMapParser().initialiseLevelMap();        // Load tiled map and create renderer
        App.getMapParser().createPositioningData();     // Process the tiled map data
        App.getEntityManager().initialiseForLevel();

        // Reset the key count as the keys collected only apply
        // to the jails in the current level, they do not carry
        // over from level to level.
        App.getGameProgress().keyCount.setToMinimum();

        //
        // Create entity paths if any relevant data
        // exists in the tilemap data.
        App.getPathUtils().setup();
    }

    /**
     * Actions to perform when a level has been completed.
     * Remove all entities/pickups/etc from the level, but
     * make sure that the main player is untouched, except
     * for its PhysicsBody, which will be rebuilt.
     */
    public void closeCurrentLevel()
    {
        Trace.checkPoint();

        App.getEntityUtils().killAllExcept( GraphicID.G_PLAYER );
        App.getEntityData().getEntityMap().setSize( 1 );
        App.getPlayer().killBody();
        App.getMapUtils().destroyBodies();
        App.getMapData().placementTiles.clear();
        App.getMapParser().getCurrentMap().dispose();

        Trace.dbg( "EntityMap Size: ", App.getEntityData().getEntityMap().size );
    }

    public void gameLevelUp()
    {
        App.getGameProgress().gameLevel = Math.min( App.getLevel() + 1, GameConstants._MAX_LEVEL);
    }

    public void gameLevelDown()
    {
        App.getGameProgress().gameLevel = Math.max( App.getLevel() - 1, GameConstants._MIN_LEVEL);
    }
}
