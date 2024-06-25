package org.firstinspires.ftc.teamcode.commands.teleop

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.Bluey

class SlideDownPixelCommand: InstantCommand(
    Runnable { Bluey.instance.depositSubsystem.remove() }
)