package package1;

public class DetectionLine extends GameObject{
	public DetectionLine(Corner c1, Corner c2, Corner rp, double rotationAngle) {
		super(new Corner[] {c1,c2}, rp, rotationAngle);
	}
}
