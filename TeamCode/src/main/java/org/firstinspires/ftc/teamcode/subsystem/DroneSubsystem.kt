package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.Bluey
import org.firstinspires.ftc.teamcode.constants.ServoConstants
import org.firstinspires.ftc.teamcode.util.WSubsystem

class DroneSubsystem(val robot: Bluey): WSubsystem() {
    var drone: Servo = servo("drone")

    companion object {
        var launched = false
    }

    override fun periodic() {
        drone.position = if (launched) ServoConstants.droneLaunched else ServoConstants.droneIdle
    }

    override fun read() {}

    override fun write() {}

    override fun reset() {}

    private fun servo(name: String): Servo = robot.hardwareMap.get(Servo::class.java, name)
}