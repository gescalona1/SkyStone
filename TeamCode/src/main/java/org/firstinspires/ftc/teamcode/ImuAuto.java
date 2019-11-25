package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImpl;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;

import java.util.Locale;

@Autonomous(name="IMU: help me", group="Linear Opmode")
public class ImuAuto extends AutoOpMode {
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
        map.setUpImu(hardwareMap);
        map.setUpMotors(hardwareMap);
        telemetry.addData("Setted up imu!", "ready to go!");
    }

    @Override
    public void beforeLoop() {
        DeviceMap map = DeviceMap.getInstance();

        telemetry.addLine(String.format(Locale.ENGLISH, "Angle: %f", map.getAngle()));
        telemetry.update();

    }

    @Override
    public void run() {
        driver.turn(0.4, -90);
        sleep(3000);
        driver.turn(0.4, -90);
        driver.turn(0.4, -90);

        telemetry.addLine("Passed turning negative thrice");


    }
}
