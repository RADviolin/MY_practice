
package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.hardware.CANcoder;

public class SwerveModule {

    private final SparkMax driveMotor;
    private final SparkMax turnMotor;
    private final CANcoder absoluteEncoder;

    public SwerveModule(int driveMotorID, int turnMotorID, int encoderID) {

        driveMotor = new SparkMax(driveMotorID, MotorType.kBrushless);
        turnMotor = new SparkMax(turnMotorID, MotorType.kBrushless);
        absoluteEncoder = new CANcoder(encoderID);
    }

    // Controls how fast the wheel spins.
    public void setDrivePower(double power) {
        driveMotor.set(power);
    }

    // Controls the steering motor.
    public void setTurnPower(double power) {
        turnMotor.set(power);
    }

    // Stops both motors.
    public void stop() {
        driveMotor.stopMotor();
        turnMotor.stopMotor();
    }

    // Gets the current position of the encoder.
    public double getAbsoluteEncoderPosition() {
        return absoluteEncoder.getAbsolutePosition().getValueAsDouble();
    }

    // Changes the encoder reading from rotations to radians.
    public double getCurrentAngleRadians() {
        return getAbsoluteEncoderPosition() * 2.0 * Math.PI;
    }

    // Finds the best angle for the wheel to turn.
    public SwerveMath.WheelState getOptimizedState(
            SwerveMath.WheelState desiredState) {

        return SwerveMath.optimize(
            getCurrentAngleRadians(),
            desiredState
        );
    }
}
