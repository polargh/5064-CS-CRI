package org.firstinspires.ftc.teamcode.opmode.tests

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.pedro.localization.Pose
import org.firstinspires.ftc.teamcode.util.Alliance
import org.firstinspires.ftc.teamcode.util.OpModeType

@TeleOp(name = "Drive Test", group = "tests")
class MecDriveTest: CommandOpMode() {
    lateinit var timer: ElapsedTime
    lateinit var robot: Bluey

    lateinit var mecDrive: MecanumDrive

    lateinit var controller1: GamepadEx

    override fun initialize() {
        robot = Bluey(
            hardwareMap = hardwareMap,
            t = telemetry,
            alliance = Alliance.RED,
            startPose = Pose(0.0, 0.0, 0.0),
            opModeType = OpModeType.TELE_OP
        )

        robot.init()

        /*
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
         */

        mecDrive = MecanumDrive(
            motor("leftFront"),
            motor("rightFront"),
            motor("leftRear"),
            motor("rightRear")
        )

        controller1 = GamepadEx(gamepad1)

        timer = ElapsedTime()
    }

    override fun run() {
        super.run()

        robot.read()

        mecDrive.driveRobotCentric(
            controller1.leftX,
            controller1.leftY,
            controller1.rightX
        )

        robot.loop()
        robot.write()
        robot.clearBulkCache()

        telemetry.addData("loop", timer.milliseconds())
        timer.reset()
        telemetry.update()
    }

    private fun motor(name: String): Motor = Motor(robot.hardwareMap, name)
}