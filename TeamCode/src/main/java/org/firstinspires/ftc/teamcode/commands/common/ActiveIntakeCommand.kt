package org.firstinspires.ftc.teamcode.commands.common

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.Bluey


class ActiveIntakeCommand(power: Double) : InstantCommand(
    Runnable { Bluey.instance.intakeSubsystem.setPower(power) }
)