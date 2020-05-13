package package1;

public class PlayerBonus extends GameObject{
	
	boolean dashUpgrade = false;
	boolean pulseUpgrade = false;
	boolean hpBoost = false;

	public PlayerBonus(Corner[] corners, Corner rp, double rotationAngle) {
		super(corners, rp, rotationAngle);
		// TODO Auto-generated constructor stub
	}
	
	public void chooseRandomType() {
		double rand = Math.random();
		if(rand < 0.33) {
			dashUpgrade = true;
		}
		else if(rand < 0.66) {
			hpBoost = true;
			
		}else {
			pulseUpgrade = true;
		}
	}
	
	public void handlePlayerCollision(Player p) {
		if(p.checkCollision(this)) {
			if(dashUpgrade) {
				handleDashBonus(p);
			}else if(pulseUpgrade){
				handlePulseUpgrade(p);
			}else if(hpBoost) {
				p.setHP(p.getHP()+5);
			}
		}
	}
	
	public void handlePulseUpgrade(Player p) {
		if(p.isPulseUnlocked()) {
			p.upgradePulse();
		}else {
			p.setPulseUnlocked(true);
		}
	}
	
	
	
	
	public void handleDashBonus(Player p) {
		if(p.isDashUnlocked() == true) {
			p.upgradeDash(20);
		}else {
			p.setDashUnlocked(true);
		}
	}
				
	
	public static PlayerBonus makeNewPlayerBonus(double d, double e) {
		Corner[] corners = GameObject.generatePeriodicObject(20, 4, new Corner(new double[] {d,e})).getCorners();
		PlayerBonus pb = new PlayerBonus(corners, new Corner(new double[] {d,e}), 0);
		pb.chooseRandomType();
		pb.rotateOb(90);
		return pb;
	}
	
	
	public boolean checkCollision(GameObject go) {
		if(go instanceof Player) {
			handlePlayerCollision((Player) go);
		}
		if(go instanceof Shield) {
			return false;
		}else {
			return super.checkCollision(go);
		}
	}
}
