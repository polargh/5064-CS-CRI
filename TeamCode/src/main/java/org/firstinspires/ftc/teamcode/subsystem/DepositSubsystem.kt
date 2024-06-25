package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDFController
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.constants.ServoConstants
import org.firstinspires.ftc.teamcode.subsystem.state.ClawState
import org.firstinspires.ftc.teamcode.subsystem.state.V4BState
import org.firstinspires.ftc.teamcode.util.WSubsystem
import kotlin.math.abs

@Config
class DepositSubsystem(val robot: Bluey): WSubsystem() {
    private var leftLiftMotor: DcMotorEx
    private var rightLiftMotor: DcMotorEx

    private var v4bLeft: Servo
    private var v4bRight: Servo
    private var v4bPitch: Servo

    private var clawLeft: Servo
    private var clawRight: Servo
    private var wrist: Servo

    private var liftEncoder: Motor.Encoder

    var lastPosition = 0.0

    private var liftPid: PIDFController

    var P = 0.0
    var I = 0.0
    var D = 0.0
    var kStatic = 0.0

    private var lastPower = 0.0

    private var clawRotationIdx: Int = 0

    var heightOfPixel = 20
    var lastHeight = 0
    var lastClawState: ClawState
    var lastV4BState: V4BState

    var liftAtBottom: Double = 0.0

    init {
        leftLiftMotor = motor("leftLiftMotor")
        rightLiftMotor = motor("rightLiftMotor")

        leftLiftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightLiftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        leftLiftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        rightLiftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

        liftEncoder = MotorEx(robot.hardwareMap, "frontLeftMotor").encoder

        v4bLeft = servo("v4bLeftServo")
        v4bRight = servo("v4bRightServo")
        v4bPitch = servo("v4bPitchServo")

        clawLeft = servo("clawLeft")
        clawRight = servo("clawRight")
        wrist = servo("wrist")

        liftPid = PIDFController(P, I, D, 0.0)

        lastClawState = ClawState.BOTH_OPEN
        lastV4BState = V4BState.INTERMEDIATE

        targetPosition = liftAtBottom
    }

    override fun periodic() {
        var power: Double

        if (isUsingPID) {
            val controlSignal = liftPid.calculate(abs(lastPosition), targetPosition)
            power = Range.clip(controlSignal, -1.0, 1.0)

            if (power != lastPower) {
                leftLiftMotor.power = power
                rightLiftMotor.power = power
            }

            lastPower = power
        }
    }

    override fun read() {
        lastPosition = abs(liftEncoder.position).toDouble()
    }

    override fun write() {}

    fun setPowerManually(power: Double) {
        if (power != lastPower) {
            leftLiftMotor.power = power
            rightLiftMotor.power = power
        }

        lastPower = power
    }

    override fun reset() {
        targetPosition = 0.0
        lastPower = 0.0
        lastPosition = 0.0
    }

    fun updateClawRotation(increment: Boolean, turns: Int) {
        if (increment)
            clawRotationIdx += turns
        else
            clawRotationIdx -= turns

        wrist.position = ((clawRotationIdx * 30) / (355.0 / 2.0) + 0.5)
    }

    fun update(state: ClawState) {
        var actualState = state

        if (
            state == ClawState.LEFT_CLOSED && lastClawState == ClawState.RIGHT_CLOSED ||
            state == ClawState.RIGHT_CLOSED && lastClawState == ClawState.LEFT_CLOSED
        ) {
            actualState = ClawState.BOTH_CLOSED
        }

        if (
            state == ClawState.LEFT_OPEN && lastClawState == ClawState.RIGHT_OPEN ||
            state == ClawState.RIGHT_OPEN && lastClawState == ClawState.LEFT_OPEN
        ) {
            actualState = ClawState.BOTH_OPEN
        }

        when (actualState) {
            ClawState.LEFT_OPEN -> {
                clawLeft.position = ServoConstants.clawOpen
            }
            ClawState.RIGHT_OPEN -> {
                clawRight.position = ServoConstants.clawOpen
            }
            ClawState.BOTH_OPEN -> {
                clawRight.position = ServoConstants.clawOpen
                clawLeft.position = ServoConstants.clawOpen
            }
            ClawState.LEFT_CLOSED -> {
                clawLeft.position = ServoConstants.clawClosed
            }
            ClawState.RIGHT_CLOSED -> {
                clawRight.position = ServoConstants.clawClosed
            }
            ClawState.BOTH_CLOSED -> {
                clawRight.position = ServoConstants.clawClosed
                clawLeft.position = ServoConstants.clawClosed
            }
        }

        lastClawState = actualState
    }

    fun retractLift() {
        targetPosition = liftAtBottom
    }

    fun update(state: V4BState) {
        when (state) {
            V4BState.AWAITING_TRANSFER -> {
                v4bLeft.position = ServoConstants.v4bAwaitingTransfer
                v4bRight.position = ServoConstants.v4bAwaitingTransfer
                v4bPitch.position = ServoConstants.v4bPitchAwaitingTransfer
            }
            V4BState.TRANSFERRING -> {
                v4bLeft.position = ServoConstants.v4bTransferring
                v4bRight.position = ServoConstants.v4bTransferring
                v4bPitch.position = ServoConstants.v4bPitchTransferring
            }
            V4BState.INTERMEDIATE -> {
                v4bLeft.position = ServoConstants.v4bIntermediate
                v4bRight.position = ServoConstants.v4bIntermediate
                v4bPitch.position = ServoConstants.v4bPitchIntermediate
            }
            V4BState.DEPOSITING -> {
                v4bLeft.position = ServoConstants.v4bDepositing
                v4bRight.position = ServoConstants.v4bDepositing
                v4bPitch.position = ServoConstants.v4bPitchDepositing
            }
            V4BState.PLACING_PURPLE -> {
                v4bLeft.position = ServoConstants.v4bPlacingPurple
                v4bRight.position = ServoConstants.v4bPlacingPurple
                v4bPitch.position = ServoConstants.v4bPitchPlacingPurple
            }
        }
    }

    fun add() {
        targetPosition += heightOfPixel
    }

    fun remove() {
        targetPosition -= heightOfPixel
    }

    fun toggleClimbMode() {
        isUsingPID = !isUsingPID
    }

    private fun motor(name: String): DcMotorEx = robot.hardwareMap.get(DcMotorEx::class.java, name)
    private fun servo(name: String): Servo = robot.hardwareMap.get(Servo::class.java, name)

    companion object {
        var isUsingPID = true
        var power = 0
        var targetPosition = 0.0
    }
}