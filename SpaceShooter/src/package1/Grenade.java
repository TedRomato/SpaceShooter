package package1;

public class Grenade extends Explosives{
	
	int dmg = 4;
	int chunks = 16;

	public Grenade(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	public void handleGrenade() {
		if(getDistanceTraveled() > 500) {
			setHP(0);
		}
	}

	public Missile[] explode() {
		if(getHP() <= 0) {
			return makePeriodicExplosion(25, getRotationPoint(), chunks, getShotImunes(), dmg);
		}else {
			return null;
		}
	}
	
	public static Grenade makeNewGrenade(double x, double y, Corner md) {
		Corner rp = new Corner(new double[] {x,y});
		Corner[] c = GameObject.generatePeriodicObject(20, 8, rp).getCorners();
		Grenade nade = new Grenade(c,rp,0,md);
		nade.setSpeed(10);
		nade.setHP(5);
		return nade;
	}
	
	public void setDMG(int dmg) {
		this.dmg = dmg;
	}
	
	public void setChunks(int i) {
		chunks = i;
	}
}
