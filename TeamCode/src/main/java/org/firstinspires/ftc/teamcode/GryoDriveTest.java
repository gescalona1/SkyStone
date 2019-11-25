package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;

import java.util.Locale;

@Autonomous(name="GyroDrive Test", group="Linear Opmode")
public class GryoDriveTest extends AutoOpMode {
    @Override
    public void preInit() {
        super.preInit();
        //if you're pro, do this
        //driver.setTest(false);
        driver.setTest(true);
        //MonitorManager.startAll(DeviceMap.getInstance());
    }

    @Override
    public void setup(DeviceMap map) {
        map.setUpMotors(hardwareMap);
        map.setUpImu(hardwareMap);
    }

    @Override
    public void beforeLoop() {
        for(DcMotor motor : driver.getMotors())
            telemetry.addLine("Motor: " + motor.getCurrentPosition());
        BNO055IMU imu = DeviceMap.getInstance().getImu();
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        float[] floats = new float[] {
                orientation.firstAngle,
                orientation.secondAngle,
                orientation.thirdAngle
        };
        telemetry.addLine(String.format(Locale.ENGLISH, "Angles: %f %f %f", floats[0], floats[1], floats[2]));
        telemetry.update();

    }

    @Override
    public void run() {
        driver.move(Direction.FORWARD, 0.25, 20, true);
    }
}
