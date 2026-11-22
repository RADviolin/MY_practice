//this and that//

public enum ArmState {

    STOW(0),
    INTAKE(25),
    LOW(45),
    HIGH(80);

    private final double angle;

    ArmState(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
