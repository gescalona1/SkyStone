package org.firstinspires.ftc.teamcode.drive;

public final class MathUtil {
    public static double convert180to360(double angle) {
        if(angle > 0) return angle;
        else return 360 + angle;
    }
    public static float convert180to360(float angle) {
        if(angle > 0) return angle;
        else return 360 + angle;
    }

    public static float limit360(float angle) {
        if(angle < 360) return angle;
        if(angle > 360) {
            angle -= 360;
        }

        return limit360(angle);
    }
    public static double limit360(double angle) {
        if(angle < 360) return angle;
        if(angle > 360) {
            angle -= 360;
        }

        return limit360(angle);
    }
 }
