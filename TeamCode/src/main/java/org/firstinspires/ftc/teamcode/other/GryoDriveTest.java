package org.firstinspires.ftc.teamcode.other;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.DeviceMap;
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

    }

    @Override
    public void run() {
        driver.move(Direction.FORWARD, 0.5, 48, true);
    }
}
