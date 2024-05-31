package com.richikin.platformania.core;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.ui.ItemBar;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.Item;
import com.richikin.platformania.maths.NumberUtils;

import java.util.Arrays;

public class GameProgress implements Disposable
{
    public boolean     isRestarting;            // TRUE If the game is restarting
    public boolean     playerLifeLost;          // ...
    public boolean     levelCompleted;          // ...
    public boolean     gameCompleted;           // ...
    public boolean     gameOver;                // ...
    public boolean     gameSetupDone;           // ...
    public int         gameLevel;               // ...
    public float       gameDifficulty;          // Value used to modify game difficulty(enemy speeds, fire rates etc).
    public boolean[][] collectItems;

    //
    // Stacks are used to allow counting up/down
    // (visually) of scores etc.
    public enum Stack
    {
        _GEM,
        _COIN,
        _KEY,
        _PRISONER,
        _LIVES,
    }

    public  Item[] gameItems;
    private int[]  stacks;

    public Item lives;          // Number of main player lives
    public Item gemCount;       // Number of gems collected so far
    public Item coinCount;      // number of coins collected so far
    public Item keyCount;       // number of keys collected so far
    public Item rescueCount;    // Number of prisoners rescued

    private int livesStack;
    private int gemsStack;
    private int coinsStack;
    private int keysStack;
    private int prisonersStack;

    // -----------------------------------------------------------
    // Code
    // -----------------------------------------------------------

    public GameProgress()
    {
        setup();
    }

    private void setup()
    {
        // These essential game counters are created on App Start not game start.
        // Therefore, they should be re-initialised to default values when a game
        // is started, which could mean calling setToMinimum() or setToMaximum().
        lives       = new Item( 0, GameConstants._MAX_LIVES, GameConstants._MAX_LIVES );
        gemCount    = new Item( 0, GameConstants._MAX_GEMS, 0 );
        coinCount   = new Item( 0, GameConstants._MAX_COINS, 0 );
        keyCount    = new Item( 0, GameConstants._MAX_KEYS, 0 );
        rescueCount = new Item( 0, GameConstants._MAX_PRISONERS, 0 );

        collectItems = new boolean[ ItemBar._NUM_ITEM_PANELS ][ ItemBar._ITEMS_PER_PANEL ];

        for ( int i = 0; i < ItemBar._NUM_ITEM_PANELS; i++ )
        {
            Arrays.fill( collectItems[ i ], false );
        }
    }

    /**
     * Resets all game-related counters to their
     * default values.
     */
    public void initialise()
    {
        Trace.checkPoint();

        isRestarting   = false;
        playerLifeLost = false;
        levelCompleted = false;
        gameCompleted  = false;
        gameOver       = false;
        gameSetupDone  = false;

        gameLevel      = 1;
        gameDifficulty = 1.0f;

        gemsStack      = 0;
        coinsStack     = 0;
        keysStack      = 0;
        prisonersStack = 0;
        livesStack     = 0;

        resetGameItems();
    }

    public void resetGameItems()
    {
        lives.setToMaximum();
        gemCount.setToMinimum();
        coinCount.setToMinimum();
        keyCount.setToMinimum();
        rescueCount.setToMinimum();
    }

    public void update()
    {
        switch ( App.getAppState().peek() )
        {
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_PREPARE_LEVEL_FINISHED:
            case _STATE_MESSAGE_PANEL:
            {
                if ( isRestarting )
                {
                    App.getPlayer().setActionState( ActionStates._RESETTING );
                }

                updateStacks();
                updateDifficulty();
            }
            break;

            default:
            {
            }
            break;
        }
    }

    /**
     * Pushes the supplied amount onto the update stack for
     * the specified Stack ID. This value will then be added
     * onto the relevant counter over the next few frames in
     * the private method {@link #updateStacks()}.
     */
    public void stackPush( Stack stack, int amount )
    {
        switch ( stack )
        {
            case _COIN:
                coinsStack += amount;
                break;

            case _GEM:
                gemsStack += amount;
                break;

            case _KEY:
                keysStack += amount;
                break;

            case _LIVES:
                livesStack += amount;
                break;

            case _PRISONER:
                prisonersStack += amount;
                break;

            default:
                break;
        }
    }

    private void updateStacks()
    {
        if ( coinsStack > 0 )
        {
            int amount = NumberUtils.getCount( coinsStack );

            coinCount.add( amount );
            coinsStack -= amount;
        }

        if ( gemsStack > 0 )
        {
            int amount = NumberUtils.getCount( gemsStack );

            gemCount.add( amount );
            gemsStack -= amount;
        }

        if ( prisonersStack > 0 )
        {
            int amount = NumberUtils.getCount( prisonersStack );

            rescueCount.add( amount );
            prisonersStack -= amount;
        }

        if ( keysStack > 0 )
        {
            int amount = NumberUtils.getCount( keysStack );

            keyCount.add( amount );
            keysStack -= amount;
        }

        if ( livesStack > 0 )
        {
            int amount = NumberUtils.getCount( livesStack );

            lives.add( amount );
            livesStack -= amount;
        }
    }

    public boolean stacksAreEmpty()
    {
        return ( coinsStack == 0 )
            && ( gemsStack == 0 )
            && ( prisonersStack == 0 )
            && ( keysStack == 0 )
            && ( livesStack == 0 );
    }

    // TODO: 06/12/2021
    private void updateDifficulty()
    {
        gameDifficulty += 0.001f;
    }

    @Override
    public void dispose()
    {
        lives        = null;
        gemCount     = null;
        coinCount    = null;
        keyCount     = null;
        rescueCount  = null;
        collectItems = null;
    }
}
