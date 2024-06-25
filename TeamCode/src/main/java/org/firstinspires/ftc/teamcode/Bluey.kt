package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.robot.Robot
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.pedro.localization.Pose
import org.firstinspires.ftc.teamcode.subsystem.DepositSubsystem
import org.firstinspires.ftc.teamcode.subsystem.IntakeSubsystem
import org.firstinspires.ftc.teamcode.util.Alliance
import org.firstinspires.ftc.teamcode.util.OpModeType

/**
 * FTC #5064 CRI Rebuild "Bluey"
 *
 * @param hardwareMap
 * @param t
 * @param opModeType
 * @param alliance
 * @param startPose
 */
class Bluey(
    val hardwareMap: HardwareMap,
    t: Telemetry,
    val opModeType: OpModeType,
    val alliance: Alliance,
    val startPose: Pose
) {
    companion object {
        lateinit var instance: Bluey
            private set

        var lastKnownPose = Pose(0.0, 0.0, 0.0)
    }

    var awayFromDriverStationHeading = when (alliance) {
        Alliance.RED -> Math.PI / 2.0
        Alliance.BLUE -> -Math.PI / 2.0
    }

    val telemetry: MultipleTelemetry = MultipleTelemetry(t, FtcDashboard.getInstance().telemetry)

    lateinit var depositSubsystem: DepositSubsystem
    lateinit var intakeSubsystem: IntakeSubsystem

    fun init() {
        instance = this

        depositSubsystem = DepositSubsystem(this)
        intakeSubsystem = IntakeSubsystem(this)
    }

    fun loop() {
        depositSubsystem.periodic()
        intakeSubsystem.periodic()
    }

    fun write() {}

    fun read() {
        depositSubsystem.read()
        intakeSubsystem.read()
    }

    fun reset() {
        depositSubsystem.reset()
        intakeSubsystem.reset()
    }
}