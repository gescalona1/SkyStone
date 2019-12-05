package org.firstinspires.ftc.teamcode.imu;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.teamcode.DeviceMap;

public class UltroImu implements OpModeManagerNotifier.Notifications, Runnable {
    private boolean running;
    private Thread t;
    private BNO055IMU imu;

    private double roll = 0, pitch = 0, yaw = 0;

    public UltroImu() {
        setUp();
    }

    public void setUp() {
        this.imu = DeviceMap.getInstance().getImu();
    }
    public void start() {
        t = new Thread(this);
        this.running = true;
        t.start();
    }
    public void stop() {
        this.running = false;
    }
    public void updateAngles(){
        Quaternion quatAngles = imu.getQuaternionOrientation();

        double w = quatAngles.w;
        double x = quatAngles.x;
        double y = quatAngles.y;
        double z = quatAngles.z;

        roll = Math.atan2( 2*(w*x + y*z) , 1 - (2*(x*x + y*y)) ) * 180.0 / Math.PI;
        pitch = Math.asin( 2*(w*y - x*z) ) * 180.0 / Math.PI;
        yaw = Math.atan2( 2*(w*z + x*y), 1 - (2*(y*y + z*z)) ) * 180.0 / Math.PI;
    }

    @Override
    public void run() {
        while(isRunning()) {
            updateAngles();
        }
        t.interrupt();
    }

    @Override
    public void onOpModePreInit(OpMode opMode) {
        if(opMode instanceof OpModeManagerImpl.DefaultOpMode) return;
            opMode.telemetry.addData("Test", "ya1");
        opMode.telemetry.update();
        if(opMode instanceof LinearOpMode) t = new Thread(this);
    }

    @Override
    public void onOpModePreStart(OpMode opMode) {
        if(opMode instanceof OpModeManagerImpl.DefaultOpMode) return;
        opMode.telemetry.addData("Test", "ya2");
        opMode.telemetry.update();
        if(opMode instanceof LinearOpMode) {
            this.setUp();
            if(t == null) t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void onOpModePostStop(OpMode opMode) {
        if(opMode instanceof OpModeManagerImpl.DefaultOpMode) return;
        opMode.telemetry.addData("Test", "ya3");
        opMode.telemetry.update();
        running = false;
    }

    public double getRoll() {
        return roll;
    }

    public double getPitch() {
        return pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public boolean isRunning() {
        OpMode opMode = DeviceMap.getInstance().getCurrentOpMode();
        if(opMode instanceof LinearOpMode) {
            return ((LinearOpMode) opMode).opModeIsActive();
        }
        return false;
    }
}
