package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.configuration.LynxConstants
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.pedro.follower.Follower
import org.firstinspires.ftc.teamcode.pedro.localization.Pose
import org.firstinspires.ftc.teamcode.subsystem.DepositSubsystem
import org.firstinspires.ftc.teamcode.subsystem.DroneSubsystem
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

    var allHubs: List<LynxModule> = listOf()
    lateinit var controlHub: LynxModule

    val telemetry: MultipleTelemetry = MultipleTelemetry(t, FtcDashboard.getInstance().telemetry)

    lateinit var depositSubsystem: DepositSubsystem
    lateinit var intakeSubsystem: IntakeSubsystem
    lateinit var droneSubsystem: DroneSubsystem

    lateinit var follower: Follower

    fun init() {
        instance = this

        allHubs = hardwareMap.getAll(LynxModule::class.java)

        allHubs.forEach { hub ->
            hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

            if (hub.isParent && LynxConstants.isEmbeddedSerialNumber(hub.serialNumber)) {
                controlHub = hub
            }
        }

        val auto = opModeType == OpModeType.AUTO

        follower = Follower(hardwareMap, auto)

        depositSubsystem = DepositSubsystem(this)
        droneSubsystem = DroneSubsystem(this)
        intakeSubsystem = IntakeSubsystem(this)
    }

    fun loop() {
        depositSubsystem.periodic()
        intakeSubsystem.periodic()
        droneSubsystem.periodic()
    }

    fun write() {}

    fun read() {
        depositSubsystem.read()
        intakeSubsystem.read()
    }

    fun clearBulkCache() {
        controlHub.clearBulkCache()
    }

    fun reset() {
        depositSubsystem.reset()
        intakeSubsystem.reset()
    }

    private fun motor(name: String): DcMotorEx = hardwareMap.get(DcMotorEx::class.java, name)
}