package package1;

import java.awt.Color;
import java.awt.Graphics;

public class Missile extends MovingObject{
	int dmg = 1;
	GameObject[] immune;
	Color colorUsed = Color.red;

	public Missile(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner movingDirection, double speed, GameObject[] immune) {
		super(corners, rotationPoint, rotationAngle, movingDirection);
		setHP(1);
		setCurrentSpeed(speed*Game.tickMultiply);
		setImune(immune);

		// TODO Auto-generated constructor stub
	}

	public Missile(Corner[] corners, Corner rp, int rotationAngle, Corner md, int speed, GameObject[] immune) {
		super(corners, rp, rotationAngle, md);
		setHP(1);
		setCurrentSpeed(speed*Game.tickMultiply);
		setImune(immune);

	}
	
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}
	
	public boolean checkCollision(GameObject go) {
		if(go instanceof Shield) {
			if(((Shield) go).friendlyMissile(this)) {
				return false;
			}
		}
		if(go instanceof Tower) {
			for(GameObject ob : this.getImunne()) {
				if(ob instanceof Tower) {
					return false;
				}
			}
		}
		return super.checkCollision(go);
	}
	
	
	public void handleMissileCollision(Missile ms) {
		if(!isFriendlyBullet(ms)) {
			if(this.dmg - 1 <= ms.getDmg()) {
				this.setHP(0);
			}
		}
	}
	
	public boolean isFriendlyBullet(Missile ms) {
		for(GameObject go1 : getImunne()) {
			for(GameObject go2 : ms.getImunne()) {
				if(go1 == go2) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void handleCollision(GameObject ob) {
		if(ob instanceof Missile) {
			handleMissileCollision((Missile) ob);
		}else if(isImune(ob)) {
			return;
		}
		else {
			setHP(getHP() - 1);
			startInvulnurability();
		}
	}

	
	public int getDmg() {
		return dmg;
	}
	
	public GameObject[] getImunne() {
		return immune;
	}
	
	static Missile makeNewMissile(Corner rp, int dmg, Corner md, GameObject[] whoShot) {
		Corner TopLeft = new Corner(new double[] {rp.getX()-4*dmg,rp.getY()-4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner BotLeft = new Corner(new double[] {rp.getX()-4*dmg,rp.getY()+4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner BotRight = new Corner(new double[] {rp.getX()+4*dmg,rp.getY()+4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner TopRight = new Corner(new double[] {rp.getX()+4*dmg,rp.getY()-4*dmg}, new Corner(new double[] {rp.getX(), rp.getY()}));
		Corner mdN = new Corner(md, rp);
		Missile m = new Missile(new Corner[] {TopLeft, BotLeft, BotRight, TopRight}, new Corner(new double[] {rp.getX(), rp.getY()}), 0,mdN,12,whoShot);
		m.getNewRatios();
		m.setNewVels();
		m.setDmg(dmg);
		return m;
	}
	
	public boolean isImune(GameObject ob) {
		for(GameObject i : immune) {
			if(i == ob) {
				return true;
			}
		}
		return false;
	}

	public void setImune(GameObject[] gameObjects) {
		immune = gameObjects;
		for(GameObject go : immune) {
			if(go instanceof Player) {
				colorUsed = Color.green;
			}
		}
	}
	
	
	
	public void render(Graphics g) {
		g.setColor(colorUsed);
		super.render(g);
	}
	
	
}
