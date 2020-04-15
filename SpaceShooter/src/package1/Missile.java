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
	
	static Missile makeNewMissile(Corner rp, int dmg, Corner md, GameObject whoShot) {
		Corner TopLeft = new Corner(new double[] {rp.getX()-4*dmg,rp.getY()-4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner BotLeft = new Corner(new double[] {rp.getX()-4*dmg,rp.getY()+4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner BotRight = new Corner(new double[] {rp.getX()+4*dmg,rp.getY()+4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner TopRight = new Corner(new double[] {rp.getX()+4*dmg,rp.getY()-4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner mdN = new Corner(md, rp);
		Missile m = new Missile(new Corner[] {TopLeft, BotLeft, BotRight, TopRight}, new Corner(new double[] {rp.getX(), rp.getY()}), 0,mdN,12,whoShot);
		m.getNewRatios();
		m.setNewVels();
		return m;
	}
	
	
}
