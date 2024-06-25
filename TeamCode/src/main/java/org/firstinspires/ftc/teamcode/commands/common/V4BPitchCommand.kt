package org.firstinspires.ftc.teamcode.commands.common

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.subsystem.state.V4BState

class V4BPitchCommand(state: V4BState): InstantCommand(
    Runnable { Bluey.instance.depositSubsystem.updatePitch(state) }
)