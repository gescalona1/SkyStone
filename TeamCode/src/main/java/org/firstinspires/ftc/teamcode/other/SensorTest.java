package org.firstinspires.ftc.teamcode.other;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DeviceMap;
import org.firstinspires.ftc.teamcode.Ultro;
import org.firstinspires.ftc.teamcode.imu.UltroImu;
import org.firstinspires.ftc.teamcode.monitor.MonitorIMU;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

import java.util.Locale;

@Autonomous(name="SensorTest", group="Linear Opmode")
public class SensorTest extends AutoOpMode {
//    @Override
    public void preInit() {
        super.preInit();
        //if you're pro, do this
        //driver.setTest(false);

    }

    @Override
    public void beforeLoop() {

    }

    @Override
    public void run() {
        while(opModeisActive()) {
            ExpansionHubEx hub = DeviceMap.getInstance().getExpansionHub();
            RevBulkData bulkData = hub.getBulkInputData();
            for(DcMotor motor : driver.getMotors()) {
                telemetry.addLine("Motor Position: " + bulkData.getMotorCurrentPosition(motor));
            }

            double[] floats = new double[] {
                    Ultro.imuNotif.getYaw(),
                    Ultro.imuNotif.getRoll(),
                    Ultro.imuNotif.getPitch(),
            };
            telemetry.addLine(String.format(Locale.ENGLISH, "Angles: %f %f %f", floats[0], floats[1], floats[2]));


            telemetry.addData("Current I2C Bus Power Draw:", hub.getI2cBusCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
            telemetry.update();
        }
    }
}
