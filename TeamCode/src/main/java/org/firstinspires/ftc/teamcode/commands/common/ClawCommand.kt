package org.firstinspires.ftc.teamcode.commands.common

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.subsystem.state.ClawState

class ClawCommand(state: ClawState) : InstantCommand(
    Runnable { Bluey.instance.depositSubsystem.update(state) }
)