package org.firstinspires.ftc.teamcode.other;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.DeviceMap;
import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;

@Autonomous(name="GyroDrive2 Test", group="Linear Opmode")
public class GryoDriveTest2 extends AutoOpMode {
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
        driver.move(Direction.FORWARD, 0.5, 48);
    }
}
