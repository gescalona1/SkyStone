package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Autonomous(name="Camera: help me", group="Linear Opmode")
public class CameraTestAuto extends AutoOpMode {
    private SkystonePipeline pipeline;
    @Override
    public void setup(DeviceMap map) {
        //map.setUpVuforia(hardwareMap);
        map.setupOpenCV(hardwareMap);
        map.initTfod(hardwareMap);

        map.getCamera().setPipeline(pipeline = new SkystonePipeline());
        telemetry.update();
        DeviceMap.getInstance().getCamera().startStreaming(pipeline.getRows(), pipeline.getCols());
    }

    @Override
    public void beforeLoop() {
        addData("Values", SkystonePipeline.getValLeft() + "   "+
                SkystonePipeline.getValMid()+"   "+
                SkystonePipeline.getValRight());
        updateTelemetry();
    }

    @Override
    public void run() {
    }

    private class Pipeline extends OpenCvPipeline {

        public Mat processFrame(Mat mat) {
            telemetry.addLine("updating image");
            telemetry.update();
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2GRAY);
            return mat;
        }
    }


}
