
package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.hardware.CANcoder;

public class SwerveModule {

    // motors and encoder for this wheel
    private SparkMax driveMotor;
    private SparkMax turnMotor;
    private CANcoder encoder;

    public SwerveModule(int driveID, int turnID, int encoderID) {

        // set up the motors
        driveMotor = new SparkMax(driveID, MotorType.kBrushless);
        turnMotor = new SparkMax(turnID, MotorType.kBrushless);

        // set up the encoder
        encoder = new CANcoder(encoderID);
    }

    // wheel speed
    public void setDrivePower(double power) {
        driveMotor.set(power);
    }

    // controls which direction the wheel turns
    public void setTurnPower(double power) {
        turnMotor.set(power);
    }

    // stops both motors
    public void stop() {
        driveMotor.stopMotor();
        turnMotor.stopMotor();
    }

    // gets the encoder position
    public double getAbsoluteEncoderPosition() {
        return encoder.getAbsolutePosition().getValueAsDouble();
    }

    // changes the encoder position into radians
    public double getCurrentAngleRadians() {
        return getAbsoluteEncoderPosition() * 2 * Math.PI;
    }

    // wheel direction
    public SwerveMath.WheelState getOptimizedState(
            SwerveMath.WheelState desiredState) {

        double currentAngle = getCurrentAngleRadians();

        return SwerveMath.optimize(currentAngle, desiredState);
    }
}
