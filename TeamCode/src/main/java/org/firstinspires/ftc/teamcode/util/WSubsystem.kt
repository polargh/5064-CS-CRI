package org.firstinspires.ftc.teamcode.util


abstract class WSubsystem {
    abstract fun periodic()
    abstract fun read()
    abstract fun write()
    abstract fun reset()
}