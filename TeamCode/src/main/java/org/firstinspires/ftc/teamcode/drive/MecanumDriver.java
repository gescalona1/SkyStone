package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import net.jafama.FastMath;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DeviceMap;
import org.firstinspires.ftc.teamcode.Ultro;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;
import org.openftc.revextensions2.RevBulkData;
//import org.openftc.revextensions2.RevBulkData;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public final class MecanumDriver implements IDriver {
    private static final double TURN_OFFSET = 2.5F;

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

    public void append(Direction direction, double power) {
        DcMotor leftTop = map.getLeftTop();
        DcMotor rightTop = map.getRightTop();
        DcMotor leftBottom = map.getLeftBottom();
        DcMotor rightBottom = map.getRightBottom();

        leftTop.setPower(leftTop.getPower() + direction.getLeftTop() * power);
        rightTop.setPower(rightTop.getPower() + direction.getRightTop() * power);
        leftBottom.setPower(leftBottom.getPower() + direction.getLeftBottom() * power);
        rightBottom.setPower(rightBottom.getPower() + direction.getRightBottom() * power);
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

    @Override
    public void move(Direction direction, double power, double inches) {
        move(direction, power, inches, false);
    }
    /**
     * encoder drive
     * @param direction
     * @param power
     * @param inches
     */
    public void move(Direction direction, double power, double inches, boolean gyroAssist) {
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

        int[] current = getMotorCounts();
        //other calculations needed
        int leftTopTarget = FastMath.abs(current[0] + (int) (calc * direction.getLeftTop()));
        int rightTopTarget = FastMath.abs(current[1] + (int) (calc * direction.getRightTop()));
        int leftBottomTarget = FastMath.abs(current[2] + (int) (calc * direction.getLeftBottom()));
        int rightBottomTarget = FastMath.abs(current[3] + (int) (calc * direction.getRightBottom()));

        double angle = 0, initialAngle = 0;
        if(gyroAssist) {
            DeviceMap map = DeviceMap.getInstance();
            Ultro.imuNotif.resetAngle();
            angle = Ultro.imuNotif.getAngle();
            initialAngle = angle;
        }
        LinearOpMode linear = null;
        if(map.getCurrentOpMode() instanceof AutoOpMode) {
            linear = (LinearOpMode) map.getCurrentOpMode();
            if(linear == null) return;
        }
        move(direction, power);

        while(linear.opModeIsActive() && motorsBusy(leftTopTarget, rightTopTarget, leftBottomTarget, rightBottomTarget)) {
            if (gyroAssist){
                double correctedPower = power * calculatePowerMultiplierLinear(0, angle, power);
                addData("initial angle", initialAngle);
                addData("angle", angle);
                addData("power", power);
                if (angle > initialAngle + 2) {
                    addData("Increasing right side", correctedPower);
                    gyroAssist(direction.getRightSide(), power + 0.1);
                    gyroAssist(direction.getLeftSide(), correctedPower);
                } else if(angle < initialAngle - 2) {
                    addData("Increasing left side", correctedPower);
                    gyroAssist(direction.getLeftSide(), power + 0.1);
                    gyroAssist(direction.getRightSide(), correctedPower);
                }else {
                    addData("normal", "side");
                    move(direction, power);
                }
                updateTelemetry();

            }
            angle = Ultro.imuNotif.getAngle();
        }

        stop();

        for(DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    private void gyroAssist(int[] side, double power) {
        for(int index : side) {
            DcMotor motor = map.getDriveMotors()[index];
            motor.setPower(power);
        }
    }

    /**
     * See: https://www.desmos.com/calculator/tugir8g8tf
     * @param expectedAngle
     * @param currentAngle
     * @param defaultPower
     * @return
     */
    private double calculatePowerMultiplier(double expectedAngle, double currentAngle, double defaultPower) {
        return defaultPower * (1D - 0.1D * FastMath.ceilToInt(FastMath.abs(expectedAngle - currentAngle)));
    }
    private double calculatePowerMultiplierLinear(double expectedAngle, double currentAngle, double defaultPower) {
        return defaultPower * (1D - 0.1D * FastMath.abs(expectedAngle - currentAngle));
    }

    public void move(AngleConverter angleConverter) {
        map.getLeftTop().setPower(angleConverter.getLeftTop());
        map.getRightTop().setPower(angleConverter.getRightTop());
        map.getRightBottom().setPower(angleConverter.getRightBottom());
        map.getLeftBottom().setPower(angleConverter.getLeftBottom());
    }

    public boolean motorsBusy(int leftTopTarget, int rightTopTarget, int leftBottomTarget, int rightBottomTarget) {
        int[] current = getMotorCounts();
        int[] target = new int[] { leftTopTarget, rightTopTarget, leftBottomTarget, rightBottomTarget };
        for(int i = 0; i < current.length; i++) {
            if(FastMath.abs(current[i]) < target[i]) return true;
        }
        return false;
    }

    public int[] getMotorCounts() {
        RevBulkData data = map.getExpansionHub().getBulkInputData();

        int leftTop = data.getMotorCurrentPosition(map.getLeftTop());
        int rightTop = data.getMotorCurrentPosition(map.getRightTop());
        int leftBottom = data.getMotorCurrentPosition(map.getLeftBottom());
        int rightBottom = data.getMotorCurrentPosition(map.getRightBottom());

        return new int[] { leftTop, rightTop, leftBottom, rightBottom};
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
        if(FastMath.abs(angle) > 180) {
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


        double currentAngle = Ultro.imuNotif.getAngle();
        double firstAngle = currentAngle;

        min = min + firstAngle;
        max = max + firstAngle;

        DeviceMap map = DeviceMap.getInstance();
        LinearOpMode linear = null;
        if(map.getCurrentOpMode() instanceof AutoOpMode) {
            linear = (LinearOpMode) map.getCurrentOpMode();
        }


        move(direction, power);
        boolean boost;
        final double oneThird = 1D/3D;
        RobotLog.dd("UltroAngle", "START ANGLE: " + firstAngle);

        Set<Double> yes = new LinkedHashSet<>();
        while ((linear != null && linear.opModeIsActive()) && !(min <= currentAngle && currentAngle <= max)) {
            yes.add(currentAngle);
            currentAngle = Ultro.imuNotif.getAngle();
        }
        stop();

        for(double dd : yes) {
            String line = String.format(Locale.ENGLISH, "%f <= %f <= %f", min, dd, max);
            RobotLog.dd("UltroAngle", line);
        }
        RobotLog.dd("UltroAngle", "END ANGLE: " + currentAngle);
        RobotLog.dd("UltroAngle", "DIFF END ANGLE: " + Ultro.imuNotif.getAngle());
        updateTelemetry();
    }

    public void turnOrigin(double power) {
        double angle = Ultro.imuNotif.getAngle();
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