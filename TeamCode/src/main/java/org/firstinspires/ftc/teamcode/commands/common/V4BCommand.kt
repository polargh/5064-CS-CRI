package org.firstinspires.ftc.teamcode.commands.common

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.subsystem.state.V4BState

class V4BCommand(state: V4BState): SequentialCommandGroup(
    V4BPositionCommand(state),
    WaitCommand(150),
    V4BPitchCommand(state)
)