package org.firstinspires.ftc.teamcode.commands.teleop

import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.Bluey

class RetractPostClawOpenCommand: SequentialCommandGroup(
    WaitCommand(300),
    SlideRetractCommand().alongWith(
        InstantCommand(
            Runnable { Bluey.instance.depositSubsystem.isCurrentlyRetracting = true }
        )
    )
)