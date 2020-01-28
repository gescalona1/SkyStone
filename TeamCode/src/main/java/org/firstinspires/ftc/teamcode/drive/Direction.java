package org.firstinspires.ftc.teamcode.drive;

/**
 * Using enums, the motions in the picture will be much easier to use
 * https://seeeddoc.github.io/4WD_Mecanum_Wheel_Robot_Kit_Series/img/Working_Principle-Simplified-.PNG
 */
public enum Direction {
    //LeftTop, RightTop, Leftbottom, RightBottom
    //or
    //LeftTop(0), RightTop (1)
    //LeftBottom(2), RightBottom (3)
    FORWARD(new int[]{0, 2}, new int[]{1, 3},1, 1, 1, 1),
    LEFT(new int[] {1, 2}, new int[] {0, 3},-1, 1, 1, -1),
    RIGHT(new int[] {3, 4}, new int[] {1, 2}, 1, -1, -1, 1),
    BACKWARD(new int[]{1, 3}, new int[]{0, 2}, -1, -1, -1, -1),

    UPPERLEFT(0, 1, 1, 0),
    UPPERRIGHT(1, 0, 0, 1),
    BOTTOMLEFT(-1, 0, 0, -1),
    BOTTOMRIGHT(0, -1, -1, 0),

    COUNTERCLOCKWISE(-1, 1, -1, 1),
    CLOCKWISE(1,-1, 1, -1);

    private int[] movement;
    private int[] left;
    private int[] right;

    Direction(int[] left, int[] right, int... movement) {
        this(movement);
        this.left = left;
        this.right = right;
    }
    Direction(int... movement) {
        this.movement = movement;
    }

    public int[] getMovement() {
        return movement;
    }

    public int getLeftTop() {
        return movement[0];
    }
    public int getRightTop() {
        return movement[1];
    }
    public int getLeftBottom() {
        return movement[2];
    }
    public int getRightBottom() {
        return movement[3];
    }

    @Override
    public String toString() {
        return this.name();
    }


    /**
     * Returns the indexes that are responsibly for pushing the robot from the left side
     * Left Side Power > 0 --> Robot Turns Right
     * Left Side Power < 0 --> Robot Turns Left
     */
    public int[] getLeftSide() {
        return left;
    }

    /**
     * Returns the indexes that are responsibly for pushing the robot from the right side
     * Right Side Power > 0 --> Robot Turns Left
     * Right Side Power < 0 --> Robot Turns Right
     */
    public int[] getRightSide() {
        return right;
    }
}
