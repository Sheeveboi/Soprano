package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.RenderMethods.Waypoint;

public class Values {
    public static float[] greenColor        = {.145f, .917f, .364f};
    public static float[] whiteColor        = {.900f, .900f, .900f};
    public static float[] yellowColor       = {.917f, .815f, .145f};
    public static float[] redColor          = {.917f, .145f, .364f};
    public static float[] blueColor         = {.145f, .427f, .917f};
    public static float[] purpleColor       = {.749f, .145f, .917f};

    public static float waypointScale = 14f;
    public static float goodGuyScale = 1f;
    public static float normalScale = 1f;
    public static float shitterScale = 1f;
    public static float hitlerScale = 1f;
    public static float snitchScale = 1f;
    public static float snitchAlertScale = 1f;
    public static float pingScale = 1f;
    public static float alertScale = 1f;
    public static float permanentScale = 1f;

    public static float waypointImportance = .8f;
    public static float goodGuyImportance = 1f;
    public static float normalImportance = .3f;
    public static float shitterImportance = .7f;
    public static float hitlerImportance = 1f;
    public static float snitchImportance = .2f;
    public static float snitchAlertImportance = .8f;
    public static float pingImportance = 1f;
    public static float alertImportance = 1f;
    public static float permanentImportance = .7f;

    public static float waypointDecayRate = .001f;
    public static float goodGuyDecayRate = .0001f;
    public static float normalDecayRate = .0001f;
    public static float shitterDecayRate = .0001f;
    public static float hitlerDecayRate = .0001f;
    public static float snitchDecayRate = .01f;
    public static float snitchAlertDecayRate = .0001f;
    public static float pingDecayRate = .001f;
    public static float alertDecayRate = .0001f;
    public static float permanentDecayRate = 0f;

    public static float scaleRegistry(Waypoint.Type type) {
        switch (type) {
            case GOOD_GUY -> { return goodGuyScale; }
            case NORMAL   -> { return normalScale; }
            case SHITTER  -> { return shitterScale; }
            case HITLER   -> { return hitlerScale; }

            case SNITCH         -> { return snitchScale; }
            case SNITCH_ALERT   -> { return snitchAlertScale; }

            case PING  -> { return pingScale; }
            case ALERT -> { return alertScale; }

            case PERMANENT -> { return permanentScale; }

            default -> { return waypointScale; }
        }
    }

    public static float importanceRegistry(Waypoint.Type type) {
        switch (type) {
            case GOOD_GUY -> { return goodGuyImportance; }
            case NORMAL   -> { return normalImportance; }
            case SHITTER  -> { return shitterImportance; }
            case HITLER   -> { return hitlerImportance; }

            case SNITCH         -> { return snitchImportance; }
            case SNITCH_ALERT   -> { return snitchAlertImportance; }

            case PING  -> { return pingImportance; }
            case ALERT -> { return alertImportance; }

            case PERMANENT -> { return permanentImportance; }

            default -> { return waypointImportance; }
        }
    }

    public static float decayRateRegistry(Waypoint.Type type) {
        switch (type) {
            case GOOD_GUY -> { return goodGuyDecayRate; }
            case NORMAL   -> { return normalDecayRate; }
            case SHITTER  -> { return shitterDecayRate; }
            case HITLER   -> { return hitlerDecayRate; }

            case SNITCH         -> { return snitchDecayRate; }
            case SNITCH_ALERT   -> { return snitchAlertDecayRate; }

            case PING  -> { return pingDecayRate; }
            case ALERT -> { return alertDecayRate; }

            case PERMANENT -> { return permanentDecayRate; }

            default -> { return waypointDecayRate; }
        }
    }

    public static float[] waypointRegistry(Waypoint.Type type) {
        switch (type) {
            case GOOD_GUY -> { return greenColor; }
            case NORMAL   -> { return whiteColor; }
            case SHITTER  -> { return yellowColor; }
            case HITLER   -> { return redColor; }

            case SNITCH         -> { return whiteColor; }
            case SNITCH_ALERT   -> { return redColor; }

            case PING  -> { return blueColor; }
            case ALERT -> { return redColor; }

            case PERMANENT -> { return purpleColor; }

            default -> { return redColor; }
        }
    }
}
