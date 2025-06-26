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
