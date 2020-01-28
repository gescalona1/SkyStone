package org.firstinspires.ftc.teamcode.other;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.DeviceMap;
import org.firstinspires.ftc.teamcode.opmode.AutoOpMode;

import java.util.Locale;

@Autonomous(name = "DATA VALUES", group = "")
public class DataOpMode extends AutoOpMode  {
    @Override
    public void setup(DeviceMap mapper) {
        mapper.setUpMotors(hardwareMap);
        mapper.setupServos(hardwareMap);
        mapper.setupSensors(hardwareMap);


    }

    @Override
    public void beforeLoop() {
        DeviceMap map = DeviceMap.getInstance();

        for(DcMotor motor : map.getAllMotors()) {
            telemetry.addData("motor: ", motor.getCurrentPosition());
        }
        for(Servo servo : map.getServos()) {
            telemetry.addData("servo:", servo.getPosition());
        }
        for(ColorSensor colorSensor : map.getColorSensors()) {
            telemetry.addData("colorsensor (r, g, b): ", String.format(Locale.ENGLISH, "%d, %d, %d", colorSensor.red(), colorSensor.green(), colorSensor.blue()));
        }
        for(DistanceSensor distanceSensor : map.getDistanceSensors()) {
            telemetry.addData("Distance sensor ", distanceSensor.getDistance(DistanceUnit.CM));
        }
        telemetry.update();
    }

    @Override
    public void run() {

    }
}
