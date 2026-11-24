
package frc.robot.subsystems;

public class SwerveMath {

    public static class WheelState {
        public double speed;
        public double angle;

        public WheelState(double speed, double angle) {
            this.speed = speed;
            this.angle = angle;
        }
    }

    public static WheelState[] calculate(
            double forward,
            double strafe,
            double rotation,
            double wheelbase,
            double trackwidth) {

        double radius = Math.sqrt(
            wheelbase * wheelbase +
            trackwidth * trackwidth
        );

        double a = strafe - rotation * (wheelbase / radius);
        double b = strafe + rotation * (wheelbase / radius);
        double c = forward - rotation * (trackwidth / radius);
        double d = forward + rotation * (trackwidth / radius);

        WheelState frontLeft = new WheelState(
            Math.sqrt(b * b + c * c),
            Math.atan2(b, c)
        );

        WheelState frontRight = new WheelState(
            Math.sqrt(b * b + d * d),
            Math.atan2(b, d)
        );

        WheelState backLeft = new WheelState(
            Math.sqrt(a * a + c * c),
            Math.atan2(a, c)
        );

        WheelState backRight = new WheelState(
            Math.sqrt(a * a + d * d),
            Math.atan2(a, d)
        );

        return new WheelState[] {
            frontLeft,
            frontRight,
            backLeft,
            backRight
        };
    }

    // Finds the shortest direction for a wheel to turn.
    public static WheelState optimize(
            double currentAngle,
            WheelState desiredState) {

        double angleDifference = desiredState.angle - currentAngle;

        // Keep the angle difference between -180 and 180 degrees.
        angleDifference = Math.atan2(
            Math.sin(angleDifference),
            Math.cos(angleDifference)
        );

        double speed = desiredState.speed;
        double targetAngle = desiredState.angle;

        // If turning more than 90 degrees, reverse the wheel direction.
        if (Math.abs(angleDifference) > Math.PI / 2) {
            speed = -speed;

            targetAngle += Math.PI;
        }

        // Keep the target angle between -180 and 180 degrees.
        targetAngle = Math.atan2(
            Math.sin(targetAngle),
            Math.cos(targetAngle)
        );

        return new WheelState(speed, targetAngle);
    }
}
