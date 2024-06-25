package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDFController
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.util.WSubsystem
import kotlin.math.abs

@Config
class IntakeSubsystem(val robot: Bluey): WSubsystem() {
    private var leftExtendoMotor: DcMotorEx
    private var rightExtendoMotor: DcMotorEx

    private var linkage: Servo
    private var door: Servo

    private var intakeMagnum: CRServo

    private var extendoEncoder: Motor.Encoder

    var lastPosition = 0.0
    var lastPower = 0.0

    private var extendoPid: PIDFController

    var P = 0.0
    var I = 0.0
    var D = 0.0
    var kStatic = 0.0

    var extendoFullyOut = 0.0
    var extendoRetracted = 0.0

    init {
        leftExtendoMotor = motor("leftExtendoMotor")
        rightExtendoMotor = motor("rightExtendoMotor")

        leftExtendoMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        rightExtendoMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

        leftExtendoMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightExtendoMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        linkage = servo("intakeLinkage")
        door = servo("intakeDoor")

        intakeMagnum = crServo("intakeMagnum")

        extendoEncoder = MotorEx(robot.hardwareMap, "frontRightMotor").encoder

        extendoPid = PIDFController(P, I, D, 0.0)
        targetPosition = extendoRetracted
    }

    fun setPower(power: Double) {
        val fixedPower = Range.clip(power, -1.0, 1.0)

        if (fixedPower != lastPower) {
            intakeMagnum.power = fixedPower
        }

        lastPower = fixedPower
    }

    override fun periodic() {
        var power: Double

        val controlSignal = extendoPid.calculate(abs(lastPosition), targetPosition)
        power = Range.clip(controlSignal, -1.0, 1.0)

        if (power != lastPower) {
            leftExtendoMotor.power = power
            leftExtendoMotor.power = power
        }

        lastPower = power
    }

    override fun read() {
        lastPosition = abs(extendoEncoder.position).toDouble()
    }

    override fun write() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        targetPosition = 0.0
        lastPower = 0.0
        lastPosition = 0.0
    }

    fun extend() {
        targetPosition = extendoFullyOut
    }

    fun retract() {
        targetPosition = extendoRetracted
    }

    private fun motor(name: String): DcMotorEx = robot.hardwareMap.get(DcMotorEx::class.java, name)
    private fun servo(name: String): Servo = robot.hardwareMap.get(Servo::class.java, name)
    private fun crServo(name: String): CRServo = robot.hardwareMap.get(CRServo::class.java, name)

    companion object {
        var power = 0
        var targetPosition = 0.0
    }
}