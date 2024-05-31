package com.richikin.platformania.google;

public interface PlayServices
{
    @SuppressWarnings( "EmptyMethod" )
    void setup();

    void createApiClient();

    void signIn();

    void signInSilently();

    void signOut();

    boolean isSignedIn();

    boolean isEnabled();

    void submitScore( int score, int level );

    void unlockAchievement( String achievementId );

    void showAchievementScreen();

    void showLeaderboard();
}
