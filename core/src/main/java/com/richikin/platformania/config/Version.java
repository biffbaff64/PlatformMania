package com.richikin.platformania.config;

import com.richikin.platformania.core.App;
import com.richikin.platformania.logging.Trace;

/**
 * Major Version - 0 == Development Version
 *               - 1 == Alpha release
 *               - 2 == Beta release
 *               - 3 == Master release
 *
 * Minor Version - 0 ==
 *               - 1 ==
 *               - 2 ==
 *               - etc...
 *
 * App Version details
 * ------------------------------------------------------------------
 * @version 0.0.1 Internal       initial issue
 */
@SuppressWarnings({"SameReturnValue", "WeakerAccess"})
public final class Version
{
    public static final int majorVersion    = 0;
    public static final int minorVersion    = 0;
    public static final int issueNumber     = 1;

    static final String appVersion  = "" + majorVersion + "." + minorVersion + "." + issueNumber;
    static final String projectID   = "platformania";
    static final String googleAppID = "000000000000";

    //
    // Release Version
    // TODO: 10/12/2020
    static final String clientID    = "000000000000-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.apps.googleusercontent.com";
    static final String sha1        = "00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00";

    //
    // Debug Version
    // TODO: 10/12/2020
    static final String clientID_debug = "000000000000-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.apps.googleusercontent.com";
    static final String sha1_debug     = "00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00";

    //
    // Google Base64-encoded RSA public key
    // TODO: 10/12/2020
    static final String googleRsaPublicKey = "ABCDefghIJKLmnopQRSTuvwxYZ";

    // ------------------------------------------------------

    public static void appDetails()
    {
        Trace.divider('*', 80);
        Trace.divider(80);

        Trace.dbg(getDisplayVersion());

        if (App.getAppConfig().isAndroidApp())
        {
            Trace.dbg("Signed in to Google?: " + App.getPlayServices().isSignedIn());
        }

        Trace.divider(80);
        Trace.divider('*', 80);
    }

    /**
     * Gets the app Version string for displaying on the settings screen
     */
    public static String getDisplayVersion()
    {
        return "Version  " + googleAppID + " : " + appVersion + " : " + projectID;
    }

    /**
     * Gets the app Version string
     */
    public static String getAppVersion()
    {
        return "V." + appVersion;
    }

    /**
     * Gets the Project ID, usually the project name.
     */
    public static String getProjectID()
    {
        return projectID;
    }
}
