package com.richikin.platformania.google;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.core.App;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.Vec2String;

public class PlayServicesData implements Disposable
{
    // -----------------------------------------------
    // The codes for achievements and leaderboards will be given via Google play
    // developers console when they are submitted.

    //@formatter:off
    private final String[][] achievements =
        {
            {   "achievement_1_tbc",    "000000000000000000" },
            {   "achievement_2_tbc",    "000000000000000000" },
            {   "achievement_3_tbc",    "000000000000000000" },
            {   "achievement_4_tbc",    "000000000000000000" },
            {   "achievement_5_tbc",    "000000000000000000" },
            {   "achievement_6_tbc",    "000000000000000000" },
            };

    private final String[][] leaderboards =
        {
            {   "leaderboard_leaderboard",         "000000000000000000" },
            {   "leaderboard_leaderboard_tester",  "000000000000000000" },
            };
    //@formatter:on

    private static final boolean achievementsEnabled = true;
    private static final boolean leaderboardEnabled  = false;

    private Array< Vec2String > achievementsIDs;
    private Array< Vec2String > leaderBoardIDs;

    // ------------------------------------------------------------------------

    public PlayServicesData()
    {
        this.achievementsIDs = new Array<>();
        this.leaderBoardIDs  = new Array<>();
    }

    public void setup()
    {
        if ( achievementsEnabled )
        {
            for ( String[] achievement : achievements )
            {
                App.getPlayServicesData().addAchievementID( achievement[ 0 ], achievement[ 1 ] );
            }

            Trace.dbg( "Achievements ENABLED." );
        }

        if ( leaderboardEnabled )
        {
            for ( String[] leaderboard : leaderboards )
            {
                App.getPlayServicesData().addLeaderBoardID( leaderboard[ 0 ], leaderboard[ 1 ] );
            }

            Trace.dbg( "Leaderboard ENABLED." );
        }
    }

    public void addAchievementID( String idName, String idValue )
    {
        if ( achievementsIDs == null )
        {
            achievementsIDs = new Array<>();
        }

        achievementsIDs.add( new Vec2String( idName, idValue ) );
    }

    public void addLeaderBoardID( String idName, String idValue )
    {
        if ( leaderBoardIDs == null )
        {
            leaderBoardIDs = new Array<>();
        }

        leaderBoardIDs.add( new Vec2String( idName, idValue ) );
    }

    @Override
    public void dispose()
    {
        this.achievementsIDs.clear();
        this.leaderBoardIDs.clear();

        this.achievementsIDs = null;
        this.leaderBoardIDs  = null;
    }
}
