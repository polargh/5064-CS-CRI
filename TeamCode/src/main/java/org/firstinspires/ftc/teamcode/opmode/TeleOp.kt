package org.firstinspires.ftc.teamcode.opmode

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.commands.teleop.RetractPostClawOpenCommand
import org.firstinspires.ftc.teamcode.commands.teleop.TriggerIntakeCommand
import org.firstinspires.ftc.teamcode.pedro.follower.Follower
import org.firstinspires.ftc.teamcode.pedro.localization.Pose
import org.firstinspires.ftc.teamcode.pedro.pathGeneration.MathFunctions
import org.firstinspires.ftc.teamcode.pedro.pathGeneration.Vector
import org.firstinspires.ftc.teamcode.subsystem.state.ClawState
import org.firstinspires.ftc.teamcode.util.Alliance
import org.firstinspires.ftc.teamcode.util.OpModeType

class TeleOp: CommandOpMode() {
    lateinit var timer: ElapsedTime
    lateinit var robot: Bluey

    lateinit var controller1: GamepadEx

    private lateinit var driveVector: Vector
    private lateinit var  headingVector: Vector

    lateinit var follower: Follower

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

        follower = robot.follower

        if (
            robot.depositSubsystem.lastClawState == ClawState.BOTH_OPEN &&
            !robot.depositSubsystem.isLiftAtBottom() &&
            !robot.depositSubsystem.isCurrentlyRetracting
        ) {
            schedule(RetractPostClawOpenCommand())
        }

        schedule(TriggerIntakeCommand(this::getIntakeSpeed))

        timer = ElapsedTime()

        driveVector = Vector()
        headingVector = Vector()
    }

    override fun run() {
        super.run()

        robot.read()

        driveVector.setOrthogonalComponents(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble()
        )

        driveVector.magnitude = MathFunctions.clamp(driveVector.magnitude, 0.0, 1.0)
        driveVector.rotateVector(follower.pose.heading)

        headingVector.setComponents(
            -gamepad1.left_stick_x.toDouble(),
            follower.pose.heading
        )

        follower.setMovementVectors(
            follower.getCentripetalForceCorrection(),
            headingVector,
            driveVector
        )

        follower.update()

        robot.loop()
        robot.write()
        robot.clearBulkCache()

        telemetry.addData("loop", timer.milliseconds())
        timer.reset()
        telemetry.update()
    }

    private fun getIntakeSpeed(): Double {
        return controller1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - controller1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)
    }
}