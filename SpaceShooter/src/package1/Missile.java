package package1;

public class Missile extends MovingObject{
	int dmg = 1;
	GameObject whoShot;

	public Missile(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner movingDirection, double speed, GameObject whoShot) {
		super(corners, rotationPoint, rotationAngle, movingDirection);
		setHP(1);
		setCurrentSpeed(speed);
		this.whoShot = whoShot;

		// TODO Auto-generated constructor stub
	}

	public Missile(Corner[] corners, Corner rp, int rotationAngle, Corner md, int speed, GameObject whoShot) {
		super(corners, rp, rotationAngle, md);
		setHP(1);
		setCurrentSpeed(speed);
		this.whoShot = whoShot;

	}
	
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}
	
	
	public void handleMissileCollision(Missile ms) {
		if(whoShot != ms.getWhoShot()) {
			if(this.dmg - 1 <= ms.getDmg()) {
				this.setHP(0);
			}
		}
		
	}

	
	public int getDmg() {
		return dmg;
	}
	
	public GameObject getWhoShot() {
		return whoShot;
	}
	
	
}
