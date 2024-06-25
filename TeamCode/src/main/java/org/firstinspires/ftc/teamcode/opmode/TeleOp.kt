package org.firstinspires.ftc.teamcode.opmode

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.pedro.localization.Pose
import org.firstinspires.ftc.teamcode.util.Alliance
import org.firstinspires.ftc.teamcode.util.OpModeType


class TeleOp: CommandOpMode() {
    lateinit var timer: ElapsedTime
    lateinit var robot: Bluey

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

        controller1 = GamepadEx(gamepad1)

        timer = ElapsedTime()
    }

    override fun run() {
        super.run()

        robot.read()

        // code

        robot.loop()
        robot.write()
        robot.clearBulkCache()

        telemetry.addData("loop", timer.milliseconds())
        timer.reset()
        telemetry.update()
    }

    fun getIntakeSpeed(): Double {
        return controller1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - controller1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)
    }
}