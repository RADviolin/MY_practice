

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;

public class ArmSubsystem {

    private final SparkMax armMotor =
        new SparkMax(Constants.Arm.MOTOR_ID, MotorType.kBrushless);

    private final CANcoder armEncoder =
        new CANcoder(Constants.Arm.CANCODER_ID);

    private final PIDController pid =
        new PIDController(
            Constants.Arm.kP,
            Constants.Arm.kI,
            Constants.Arm.kD
        );

    private double targetPosition = 0;

    private boolean manualMode = false;
    private double manualOutput = 0;

    public ArmSubsystem() {

        armMotor.setInverted(Constants.Arm.MOTOR_INVERTED);

        // Start by holding the current position.
        targetPosition = getArmAngle();

        pid.reset();
    }

    // Read the CANcoder and convert rotations to degrees.
    public double getArmAngle() {

        double rotations =
            armEncoder.getAbsolutePosition().getValueAsDouble();

        double degrees = rotations * 360.0;

        degrees -= Constants.Arm.CANCODER_ZERO_OFFSET;

        // Keep the angle between -180 and 180.
        while (degrees > 180) {
            degrees -= 360;
        }

        while (degrees < -180) {
            degrees += 360;
        }

        return degrees;
    }

    // Move to a preset arm position.
    public void setTarget(ArmState state) {

        targetPosition = state.getAngle();

        manualMode = false;

        pid.reset();
    }

    // Move the arm manually.
    public void setManualOutput(double output) {

        manualMode = true;

        manualOutput = Math.max(-0.15,
            Math.min(0.15, output));
    }

    // Hold the position when manual movement ends.
    public void holdCurrentPosition() {

        targetPosition = getArmAngle();

        manualMode = false;

        manualOutput = 0;

        pid.reset();
    }

    // Stop the motor.
    public void stop() {

        armMotor.set(0);
    }

    // Runs repeatedly while the robot is enabled.
    public void periodic() {

        if (!DriverStation.isEnabled()) {
            armMotor.set(0);
            return;
        }

        double currentAngle = getArmAngle();

        double motorOutput;

        if (manualMode) {

            motorOutput = manualOutput;

        } else {

            motorOutput = pid.calculate(
                currentAngle,
                targetPosition
            );
        }

        // Keep motor output limited.
        motorOutput = Math.max(
            -Constants.Arm.MAX_OUTPUT,
            Math.min(Constants.Arm.MAX_OUTPUT, motorOutput)
        );

        // Basic software movement limits.
        if (currentAngle >= Constants.Arm.MAX_ANGLE
                && motorOutput > 0) {
            motorOutput = 0;
        }

        if (currentAngle <= Constants.Arm.MIN_ANGLE
                && motorOutput < 0) {
            motorOutput = 0;
        }

        armMotor.set(motorOutput);
    }

    public double getTargetPosition() {
        return targetPosition;
    }

    public boolean isManualMode() {
        return manualMode;
    }
}
