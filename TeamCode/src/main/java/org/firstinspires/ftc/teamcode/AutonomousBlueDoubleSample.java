package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.skystone.Status;

@Autonomous(name = "AutonomousBlueDoubleSample")
public class AutonomousBlueDoubleSample extends AutonomousBlueSample {
    private Status pos;
    @Override
    public void beforeLoop() {
        Status status = skystone();
        telemetry.addData("Status: ", status.name());
        updateTelemetry();
        pos = status;
    }

    @Override
    public void run() {
        super.singleSample();
        secondSample();
        super.park();
    }

    public void secondSample() {
        DeviceMap map = DeviceMap.getInstance();
        //goes back to pick up the next skystone
        if (pos == Status.LEFT_CORNER || pos == Status.MIDDLE){
            driver.move(Direction.BACKWARD, 0.6, 30); //Inches value will need to be fixed
            driver.move(Direction.LEFT, 0.5, 8);
            driver.move(Direction.BACKWARD, 0.6, 9); //Inches value will need to be fixed
        } else {
            driver.move(Direction.BACKWARD, 0.6, 30); //Inches value will need to be fixed
            driver.move(Direction.LEFT, 0.5, 8);
            driver.move(Direction.BACKWARD, 0.6, 16); //Inches value will need to be fixed
        }
        driver.turn(0.3, -90);
        driver.move(Direction.FORWARD, 0.3, 15);
        map.getRightAuto().setPosition(1);
        sleep(200);
        driver.move(Direction.BACKWARD, 0.3, 15);
        driver.turn(0.3, 90);

        //drives past bridge, drops, then parks
        driver.move(Direction.FORWARD, 0.6, 50);
        map.getLeftAuto().setPosition(1);
        map.getRightAuto().setPosition(0);
        driver.move(Direction.RIGHT, 0.6, 3);
    }
}