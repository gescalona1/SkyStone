/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DeviceMap;
import org.firstinspires.ftc.teamcode.drive.MecanumDriver;
import org.firstinspires.ftc.teamcode.monitor.MonitorManager;


/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public abstract class DriveOpMode extends OpMode {
    private MecanumDriver driver;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        DeviceMap mapper = DeviceMap.getInstance(hardwareMap);
        mapper.setCurrentOpMode(this);
        mapper.setTelemetry(telemetry);

        mapper.setUpMotors(hardwareMap);
        mapper.setupServos(hardwareMap);


        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        // Tell the driver that initialization is complete.
        driver = new MecanumDriver();


        driver.setTelemetry(telemetry);
        addData("Status", "Initialized");
        updateTelemetry();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        DeviceMap map = DeviceMap.getInstance();
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double right_stick_x = -gamepad1.right_stick_x;
        double multiplier = gamepad1.left_trigger + 1;
        driver.move(x / multiplier, y / multiplier, right_stick_x / multiplier);

        driver.intake(gamepad2.left_stick_y, gamepad2.right_stick_y);
        driver.conveyer(-gamepad2.right_trigger);

        Servo left = map.getLeftAuto();
        if (gamepad1.b) {
            left.setPosition(0.4);
        }
        if (gamepad1.y) {
            left.setPosition(1);
        }
        telemetry.addData("leftPos: ", left.getPosition());

        Servo right = map.getRightAuto();
        if (gamepad1.a) {
            right.setPosition(1);
        }

        if (gamepad1.x) {
            right.setPosition(0);
        }
        telemetry.addData("rightPos: ", right.getPosition());


        if (gamepad2.left_trigger > 0) {
            map.getLeftBat().setPosition(1);
            map.getRightBat().setPosition(0);
        } else {
            map.getLeftBat().setPosition(0);
            map.getRightBat().setPosition(1);

        }

        if (gamepad2.right_bumper) {
            map.getClaw().setPosition(1);
        } else if (gamepad2.left_bumper) {
            map.getClaw().setPosition(0);
        }

        if (gamepad2.a) {
            map.getArm1().setPosition(1);
            map.getArm2().setPosition(1);
        } else if (gamepad2.y) {
            map.getArm1().setPosition(0);
            map.getArm2().setPosition(0);
        }


        //lift control

        boolean liftOverride = false;
        if (gamepad2.back) {
            liftOverride = true;
        } else if (gamepad2.start) {
            liftOverride = false;
        }


        double liftPos = map.getLift().getCurrentPosition();

        if (gamepad2.dpad_up && liftPos < 7050 && liftOverride == false) {
            map.getLift().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.getLift().setPower(1);
        } else if (gamepad2.dpad_down && liftPos > 100 && liftOverride == false) {
            map.getLift().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.getLift().setPower(-1);
        } else if (gamepad2.dpad_up && liftOverride == true) {
            map.getLift().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.getLift().setPower(1);
        } else if (gamepad2.dpad_down && liftOverride == true) {
            map.getLift().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.getLift().setPower(-1);
        }else if (gamepad2.b){
            map.getLift().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.getLift().setTargetPosition(50);
            map.getLift().setPower(-1);
        }else if (map.getLift().isBusy() != true){
            map.getLift().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.getLift().setPower(0);
        }

        telemetry.addData("lift: ", map.getLift().getCurrentPosition());
        telemetry.addData("Overrride: ", liftOverride);
        telemetry.addData("Motor Status: ", map.getLift().getMode());
        telemetry.addData("trigger: ", gamepad2.left_trigger);

        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        MonitorManager.stopAll();
    }

    protected void addData(String header, Object value) {
        driver.addData(header, value);
    }
    protected void updateTelemetry() {
        driver.updateTelemetry();
    }

}
