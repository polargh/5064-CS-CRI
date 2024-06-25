package org.firstinspires.ftc.teamcode.commands.teleop

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.Bluey

class SlideUpPixelCommand: InstantCommand(
    Runnable { Bluey.instance.depositSubsystem.add() }
)