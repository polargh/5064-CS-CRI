package org.firstinspires.ftc.teamcode.commands.teleop

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.Bluey
import java.util.function.DoubleSupplier

class TriggerIntakeCommand(private val doubleSupplier: DoubleSupplier): CommandBase() {
    override fun execute() {
        Bluey.instance.intakeSubsystem.setPower(doubleSupplier.asDouble)
    }

    override fun isFinished(): Boolean {
        return false
    }
}