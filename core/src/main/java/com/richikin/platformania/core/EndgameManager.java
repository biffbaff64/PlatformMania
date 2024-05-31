package com.richikin.platformania.core;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.ui.GameCompletedPanel;

public class EndgameManager
{
    public EndgameManager()
    {
    }

    /**
     * Performs all necessary checks for either level completion
     * or game completion.
     */
    public boolean update()
    {
        boolean returnFlag = false;

        //
        // Player is dead, no lives left
        //
        if ( ( ( App.getPlayer() != null ) && ( App.getPlayer().getActionState() == ActionStates._DEAD ) )
            || App.getAppConfig().forceQuitToMenu )
        {
            Trace.boxedDbg( "PLAYER IS DEAD, NO LIVES LEFT" );

            App.getAppState().set( StateID._STATE_PREPARE_GAME_OVER_MESSAGE );

            App.getAppConfig().isShuttingMainScene = true;

            returnFlag = true;
        }
        else
        {
            //
            // Waheyy!! All levels completed!
            //
            if ( App.getGameProgress().gameCompleted )
            {
                Trace.boxedDbg( "GAME COMPLETED" );

                App.getMainScene().gameCompletedPanel = new GameCompletedPanel();
                App.getMainScene().gameCompletedPanel.setup();

                App.getHud().setStateID( StateID._STATE_GAME_FINISHED );
                App.getAppState().set( StateID._STATE_GAME_FINISHED );

                returnFlag = true;
            }
            else
            {
                //
                // Current level completed
                //
                if ( App.getGameProgress().levelCompleted )
                {
                    Trace.boxedDbg( "LEVEL COMPLETED" );

                    App.getHud().setStateID( StateID._STATE_PANEL_UPDATE );
                    App.getAppState().set( StateID._STATE_PREPARE_LEVEL_FINISHED );

                    returnFlag = true;
                }
                else
                {
                    //
                    // Restarting due to life lost and player is resetting...
                    //
                    if ( App.getGameProgress().isRestarting )
                    {
                        assert App.getPlayer() != null;

                        if ( App.getPlayer().getActionState() == ActionStates._RESETTING )
                        {
                            Trace.boxedDbg( "LIFE LOST - TRY AGAIN" );

                            App.getMainScene().retryDelay = new StopWatch();
                            App.getAppState().set( StateID._STATE_PREPARE_LEVEL_RETRY );
                        }

                        returnFlag = true;
                    }
                }
            }
        }

        return returnFlag;
    }
}
