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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.teamcode.DeviceMap;
import org.firstinspires.ftc.teamcode.Ultro;
import org.firstinspires.ftc.teamcode.drive.Direction;
import org.firstinspires.ftc.teamcode.drive.IActive;
import org.firstinspires.ftc.teamcode.drive.MecanumDriver;
import org.firstinspires.ftc.teamcode.imu.UltroImu;
import org.firstinspires.ftc.teamcode.monitor.MonitorIMU;
import org.firstinspires.ftc.teamcode.monitor.MonitorManager;

import java.util.Locale;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public abstract class AutoOpMode extends LinearOpMode implements IActive {
    protected final String TAG = "UltroTag";
    protected MecanumDriver driver;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public boolean opModeisActive() {
        return this.opModeIsActive();
    }

    public void preInit() {
        DeviceMap mapper = DeviceMap.getInstance(hardwareMap);
        mapper.setCurrentOpMode(this);
        mapper.setTelemetry(telemetry);
        setup(mapper);
        Ultro.imuNotif.setUp();

        driver = new MecanumDriver();
        driver.setTelemetry(telemetry);

        addData("Status", "Initialized");

        updateTelemetry();
    }

    public void afterStop() {
        MonitorManager.stopAll();
        DeviceMap map = DeviceMap.getInstance();
        map.deactivateTfod();
        map.deactivateOpenCV();
        map.deactivateVuforia();

    }

    /**
     * Override this just to setup certain things.
     * @param map
     */
    public void setup(DeviceMap map) {
        map.setupAll(hardwareMap);
        MonitorManager.startAll(map);
    }

    @Override
    public void runOpMode() {
        preInit();
        // Wait for the game to start (driver presses PLAY)
        while(!isStarted())
            beforeLoop();

        waitForStart();
        runtime.reset();

        Ultro.imuNotif.start();
        Ultro.imuNotif.resetAngle();
        run();


        // run until the end of the match (driver presses STOP)
        afterStop();
        Ultro.imuNotif.stop();
    }

    public abstract void beforeLoop();

    public abstract void run();

    protected void addData(String header, Object value) {
        driver.addData(header, value);
    }
    protected void updateTelemetry() {
        driver.updateTelemetry();
    }
}
