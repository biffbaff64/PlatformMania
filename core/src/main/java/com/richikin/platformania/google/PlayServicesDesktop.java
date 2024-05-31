package com.richikin.platformania.google;

import com.richikin.platformania.logging.Trace;

public class PlayServicesDesktop implements PlayServices
{
    @Override
    public void setup()
    {
    }

    @Override
    public void createApiClient()
    {
    }

    @Override
    public void signIn()
    {
    }

    @Override
    public void signInSilently()
    {
    }

    @Override
    public void signOut()
    {
    }

    @Override
    public boolean isSignedIn()
    {
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        return false;
    }

    @Override
    public void submitScore( int score, int level )
    {
        Trace.dbg( "Desktop: Services not enabled : " + score + ", " + level );
    }

    @Override
    public void unlockAchievement( String achievementId )
    {
        Trace.dbg( "Desktop: Services not enabled : " + achievementId );
    }

    @Override
    public void showAchievementScreen()
    {
        Trace.dbg( "Desktop: Services not enabled" );
    }

    @Override
    public void showLeaderboard()
    {
        Trace.dbg( "Desktop: Services not enabled" );
    }
}
