package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.monitor.MonitorManager;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;

@Autonomous(name = "Test1")
public class AutoPart1 extends AutoOpMode {
    private enum Status {
        RIGHT_CORNER, LEFT_CORNER, MIDDLE, NOT_SEEN, ALL_YELLOW
    }

    private SkystonePipeline pipeline;
    @Override
    public void setup(DeviceMap map) {
        map.setUpImu(hardwareMap);
        map.setUpMotors(hardwareMap);
        map.setupSensors(hardwareMap);
        map.setupServos(hardwareMap);
        map.setupOpenCV(hardwareMap);
        MonitorManager.startAll(map);

        int orient = hardwareMap.appContext.getResources().getConfiguration().orientation;
        map.getCamera().setPipeline(pipeline = new SkystonePipeline(orient, 640, 480));
    }

    @Override
    public void beforeLoop() {

    }

    @Override
    public void run() {
        driver.move(Direction.FORWARD, 1, 6);

        pickSkyStone();
    }

    public void pickSkyStone() {
        findSkystone();
        pickUpSkystone();
    }

    public Status skystone() {
        int left = SkystonePipeline.getValLeft();
        int center = SkystonePipeline.getValMid();
        int right = SkystonePipeline.getValRight();

        if(left == center && center == right) {
            if(right == 0) return Status.NOT_SEEN;
            else if(right == 255) return Status.ALL_YELLOW;
        }

        if(left == 255 && center == 0 && right == 255)
            return Status.MIDDLE;

        if(center == 0) {
            if(left == 0 && right == 255)
                return Status.LEFT_CORNER;
            if(left == 255 && right == 0)
                return Status.RIGHT_CORNER;
        }

        return Status.NOT_SEEN;
    }

    public void findSkystone() {
        Status status = skystone();
        while(status != Status.MIDDLE) {
            driver.move(Direction.LEFT, 0.5);
            status = skystone();
        }
        driver.stop();
    }

    public void pickUpSkystone() {
        driver.move(Direction.FORWARD, 1, 6);
        driver.autoArm(-100, 0);
        driver.move(Direction.BACKWARD, 1, 6);

    }

}
