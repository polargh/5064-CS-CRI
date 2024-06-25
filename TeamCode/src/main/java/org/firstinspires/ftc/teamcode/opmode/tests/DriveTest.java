package org.firstinspires.ftc.teamcode.opmode.tests;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class DriveTest extends OpMode {

    ElapsedTime timer;

    private Motor fL, fR, bL, bR;
    private MecanumDrive drive;
    private GamepadEx driverOp;

    @Override
    public void init() {
        fL = new Motor(hardwareMap, "frontLeft");
        fR = new Motor(hardwareMap, "frontRight");
        bL = new Motor(hardwareMap, "backLeft");
        bR = new Motor(hardwareMap, "backRight");

        drive = new MecanumDrive(fL, fR, bL, bR);
        driverOp = new GamepadEx(gamepad1);
    }

    @Override
    public void loop() {
        if (timer == null) {
            timer = new ElapsedTime();
            timer.reset();
        }

        drive.driveRobotCentric(
                driverOp.getLeftX(),
                driverOp.getLeftY(),
                driverOp.getRightY()
        );

        telemetry.addData("loop", timer.milliseconds() + "ms");
        timer.reset();
        telemetry.update();
    }

}