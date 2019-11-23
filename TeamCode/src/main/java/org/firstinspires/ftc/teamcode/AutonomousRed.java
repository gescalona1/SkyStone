package org.firstinspires.ftc.teamcode;

import android.os.AsyncTask;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.TrackableResult;

import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.monitor.MonitorManager;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Autonomous(name = "AutoRed")
public class AutonomousRed extends AutoPart1 {
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
        DeviceMap map = DeviceMap.getInstance();
        telemetry.addData("Saved Status", pos);
        //moves forward then aligns with the wall
        driver.move(Direction.FORWARD, 0.5, 20);
        driver.move(Direction.RIGHT, 0.5, 30);

        //grabs the skystone
        if (pos == Status.LEFT_CORNER){
            driver.move(Direction.LEFT, 0.5, 8);
        }
        driver.move(Direction.FORWARD, 0.5, 10);
        if (pos == Status.LEFT_CORNER || pos == Status.MIDDLE){
            map.getLeftAuto().setPosition(0);
        } else {
            map.getRightAuto().setPosition(1);
        }
        sleep(1200);
        driver.move(Direction.BACKWARD, 0.5, 10);

        //turns and drives past bridge
        driver.turn(0.3, 90);
        driver.move(Direction.FORWARD, 0.5, 100);
        map.getLeftAuto().setPosition(0.4);
        map.getRightAuto().setPosition(0.4);

        //goes back to pick up the next skystone
        if (pos == Status.LEFT_CORNER || pos == Status.MIDDLE){
            driver.move(Direction.BACKWARD, 0.5, 20); //Inches value will need to be fixed
        } else {
            driver.move(Direction.BACKWARD, 0.5, 28); //Inches value will need to be fixed
        }
        driver.turn(0.3, -90);
        driver.move(Direction.FORWARD, 0.5, 10);
        map.getLeftAuto().setPosition(0);
        driver.move(Direction.BACKWARD, 0.5, 10);
        driver.turn(0.3, 90);

        //drives past bridge, drops, then parks
        driver.move(Direction.FORWARD, 0.5, 30);
        if (pos == Status.LEFT_CORNER){
            driver.move(Direction.BACKWARD, 0.5, 10); //Inches value will need to be fixed
        } else if (pos == Status.MIDDLE){
            driver.move(Direction.BACKWARD, 0.5, 18); //Inches value will need to be fixed
        } else {
            driver.move(Direction.BACKWARD, 0.5, 26); //Inches value will need to be fixed
        }
        //should be parked
    }

}
