package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.DeviceMap;
import org.firstinspires.ftc.teamcode.monitor.MonitorIMU;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;

import java.util.Locale;

public final class MecanumDriver implements IDriver {
    private static final float TURN_OFFSET = 7.5F;

    private boolean test;
    private DeviceMap map;
    private Telemetry telemetry;
    private DcMotor[] motors;

    private boolean conveyerCurrent;

    private static final double COUNTS_PER_MOTOR_REV = 560;
    private static final double WHEEL_DIAMETER_INCHES   = 4.0;
    private static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV/(WHEEL_DIAMETER_INCHES * Math.PI);

    public MecanumDriver() {
        this.map = DeviceMap.getInstance();
        this.motors = map.getDriveMotors();
        this.test = true;
    }

    /**
     * Move an unspecified amount of distance
     * @param direction
     * @param power
     */
    @Override
    public void move(Direction direction, double power) {

        map.getLeftTop().setPower(direction.getLeftTop() * power);
        map.getRightBottom().setPower(direction.getRightBottom() * power);
        map.getRightTop().setPower(direction.getRightTop() * power);
        map.getLeftBottom().setPower(direction.getLeftBottom() * power);
    }

    /**
     * encoder drive
     * @param direction
     * @param power
     * @param inches
     */
    @Override
    public void move(Direction direction, double power, double inches) {
        DcMotor leftTop = map.getLeftTop();
        DcMotor rightTop = map.getRightTop();
        DcMotor leftBottom = map.getLeftBottom();
        DcMotor rightBottom = map.getRightBottom();

        /*
        if((direction == Direction.LEFT) || (direction == Direction.RIGHT)){
            inches *= (1D / 0.7D);
        }else if(direction == Direction.FORWARD || direction == Direction.BACKWARD) {

         */
        inches -= 3.5D;
        //}

        double calc = COUNTS_PER_INCH * inches;


        for(DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        //other calculations needed
        int leftTopTarget = Math.abs(leftTop.getCurrentPosition() + (int) (calc * direction.getLeftTop()));
        int rightTopTarget = Math.abs(rightTop.getCurrentPosition() + (int) (calc * direction.getRightTop()));
        int leftBottomTarget = Math.abs(leftBottom.getCurrentPosition() + (int) (calc * direction.getLeftBottom()));
        int rightBottomTarget = Math.abs(rightBottom.getCurrentPosition() + (int) (calc * direction.getRightBottom()));

        move(direction, power);

        DeviceMap map = DeviceMap.getInstance();
        LinearOpMode linear = null;
        if(map.getCurrentOpMode() instanceof AutoOpMode) {
            linear = (LinearOpMode) map.getCurrentOpMode();
        }
        while((linear != null && linear.opModeIsActive()) && motorsBusy(leftTopTarget, rightTopTarget, leftBottomTarget, rightBottomTarget)) {
            //move(direction, power);
        }

        stop();

        for(DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public void move(AngleConverter angleConverter) {
        map.getLeftTop().setPower(angleConverter.getLeftTop());
        map.getRightTop().setPower(angleConverter.getRightTop());
        map.getRightBottom().setPower(angleConverter.getRightBottom());
        map.getLeftBottom().setPower(angleConverter.getLeftBottom());
    }

    public boolean motorsBusy(int leftTopTarget, int rightTopTarget, int leftBottomTarget, int rightBottomTarget) {
        DcMotor leftTop = map.getLeftTop();
        DcMotor rightTop = map.getRightTop();
        DcMotor leftBottom = map.getLeftBottom();
        DcMotor rightBottom = map.getRightBottom();

        int[] current = new int[] { leftTop.getCurrentPosition(), rightTop.getCurrentPosition(), leftBottom.getCurrentPosition(), rightBottom.getCurrentPosition()};
        int[] target = new int[] { leftTopTarget, rightTopTarget, leftBottomTarget, rightBottomTarget };
        for(int i = 0; i < current.length; i++) {
            addData("current: " + current[i], "target: " + target[i]);
        }
        updateTelemetry();
        for(int i = 0; i < current.length; i++) {
            if(Math.abs(current[i]) < target[i]) return true;
        }
        return false;
    }
    /**
     * Updated mecanum drive function this year (math is ? ?? ? )
     * @param left_stick_x gamepadleftX
     * @param left_stick_y gamepadleftY
     * @param right_stick_x gamepadrightX
     */
    public void move(double left_stick_x, double left_stick_y, double right_stick_x){
        double LF = Range.clip(left_stick_y + left_stick_x + right_stick_x, -1, 1);
        double RF = Range.clip(left_stick_y - left_stick_x - right_stick_x, -1, 1);
        double LB = Range.clip(left_stick_y - left_stick_x + right_stick_x, -1, 1);
        double RB = Range.clip(left_stick_y + left_stick_x - right_stick_x, -1, 1);

        map.getLeftTop().setPower(LF);
        map.getRightTop().setPower(RF);
        map.getLeftBottom().setPower(LB);
        map.getRightBottom().setPower(RB);
    }

    @Override
    public void turn(double power, double angle) {
        if(Math.abs(angle) > 180) {
            //if it's more than +180 or less than -180, add towards 0: 180
            if(angle > 180) turn(power, angle - 180);
            else if(angle < -180) turn(power, angle + 180);
            return;
        }
        Direction direction = angle > 0 ? Direction.CLOCKWISE : Direction.COUNTERCLOCKWISE;

        //angle = 50
        //turn_offset = 5

        //min = 45
        double min = angle - TURN_OFFSET;
        //min = 55
        double max = angle + TURN_OFFSET;

        move(direction, power);

        map.resetAngle();
        double currentAngle = map.getAngle();
        double firstAngle = currentAngle;

        min = min + firstAngle;
        max = max + firstAngle;

        DeviceMap map = DeviceMap.getInstance();
        LinearOpMode linear = null;
        if(map.getCurrentOpMode() instanceof AutoOpMode) {
            linear = (LinearOpMode) map.getCurrentOpMode();
        }

        boolean boost;
        final double oneThird = 1D/3D;
        while ((linear != null && linear.opModeIsActive()) && !(min <= currentAngle && currentAngle <= max)) {
            currentAngle = map.getAngle();
        }
        stop();
    }

    public void turnOrigin(double power) {
        double angle = map.getAngle();
        turn(power, angle);
    }

    public void intake(double leftPower, double rightPower) {
        map.getLeftIntake().setPower(leftPower);
        map.getRightIntake().setPower(rightPower);
    }

    public void conveyer(double power) {
        DcMotor motor = map.getConveyer();
        if(motor.getPower() == power) return;
        map.getConveyer().setPower(power);
    }

    public void autoArm(double posLeft, double posRight){
        if(posLeft != -100)
            map.getLeftAuto().setPosition(posLeft);
        if(posRight != -100)
            map.getRightAuto().setPosition(posRight);
    }


    @Override
    public void stop() {
        for(DcMotor motor : map.getDriveMotors())
            motor.setPower(0);
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public boolean motorsBusy() {
        DcMotor leftTop = map.getLeftTop();
        DcMotor rightTop = map.getRightTop();
        DcMotor leftBottom = map.getLeftBottom();
        DcMotor rightBottom = map.getRightBottom();

        return leftBottom.isBusy() && rightBottom.isBusy() && rightTop.isBusy() && leftTop.isBusy();
    }
    public DcMotor[] getMotors() {
        return motors;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
    
    public void addData(String header, Object value) {
        if(test) telemetry.addData(header, value);
    }
    public void updateTelemetry() {
        if(test) telemetry.update();
    }
}
