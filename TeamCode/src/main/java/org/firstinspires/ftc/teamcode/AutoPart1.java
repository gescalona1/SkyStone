package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.monitor.MonitorManager;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;
import org.firstinspires.ftc.teamcode.skystone.SkystonePipeline;
import org.firstinspires.ftc.teamcode.skystone.Status;

public abstract class AutoPart1 extends AutoOpMode {
    @Override
    public void preInit() {
        super.preInit();
    }

    protected SkystonePipeline pipeline;
    @Override
    public void setup(DeviceMap map) {
        map.setUpImu(hardwareMap);
        map.setUpMotors(hardwareMap);
        map.setupSensors(hardwareMap);
        map.setupServos(hardwareMap);
        map.setupOpenCV(hardwareMap);

        int orient = hardwareMap.appContext.getResources().getConfiguration().orientation;
        map.getCamera().setPipeline(pipeline = new SkystonePipeline(orient, 640, 480));

        DeviceMap.getInstance().getCamera().startStreaming(pipeline.getRows(), pipeline.getCols());
    }


    @Override
    public void beforeLoop() {

    }

    protected Status skystone() {
        return pipeline.skystone();
    }



}
