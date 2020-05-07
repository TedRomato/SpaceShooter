package package1;

public class GameModeRun extends Game{

	public GameModeRun(int sw, int sh, boolean softBorder) {
		super(sw, sh, softBorder);
		// TODO Auto-generated constructor stub
		safeZoneHeight = 4000;
		bordersTLCorner = new Corner(new double[] {0, - 2000});
	}
	
	public void moveSoftBorders(double velX, double velY) {
		bordersTLCorner.moveCorner(velX, velY);
	}
	
	public void adjustSafeZone() {
		if(getDifference(p.getRotationPoint().getY(), bordersTLCorner.getY()) < 2500) {
			bordersTLCorner.moveCorner(0, -100);;
		}
	}
	
	public void tick() {
		super.tick();
		moveSoftBorders(0,-4);
		adjustSafeZone();
	}
}
