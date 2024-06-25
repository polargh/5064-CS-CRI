package org.firstinspires.ftc.teamcode.commands.common

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.subsystem.state.V4BState

class V4BPositionCommand(state: V4BState): InstantCommand(
    Runnable { Bluey.instance.depositSubsystem.updatePosition(state) }
)