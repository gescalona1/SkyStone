package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.monitor.MonitorManager;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;

@Autonomous(name = "AutoRed")
public class AutonomousRed extends AutoOpMode {
    private SkystonePipeline pipeline;
    @Override
    public void setup(DeviceMap map) {
        map.setUpImu(hardwareMap);
        map.setUpMotors(hardwareMap);
        map.setupSensors(hardwareMap);
        map.setupServos(hardwareMap);
        map.setupOpenCV(hardwareMap);
        MonitorManager.startAll(map);

        map.getCamera().setPipeline(pipeline = new SkystonePipeline());
    }

    @Override
    public void beforeLoop() {

    }

    @Override
    public void run() {
//        driver.move(Direction.FORWARD, 1.0, 10);
        driver.move(Direction.LEFT, 1.0, 10);
    }


}
