package package1;

import java.awt.Graphics;

public class Shield extends GameObject{
	public Shield(Corner[] corners, Corner rp, double rotationAngle) {
		super(corners, rp, rotationAngle);
		// TODO Auto-generated constructor stub
	}

	LivingObject parent;
	boolean AIBlock = false;
	GameObject[] whoToBlock = new GameObject[0];
	
	int duration = 600;
	int durationTimer = 0;
	boolean hasDuration = false;
	
	public void updateDuration() {
		if(hasDuration) {
			durationTimer++;
			if(durationTimer >= duration) {
				this.setHP(0);
				if(parent instanceof Player) {
					((Player) parent).setShieldIsUp(false);
					((Player) parent).setShield(null);
				}
			}
		}
	}
	

	public static Shield makeShield(Corner rp, int radius) {
		Corner[] outerCorners;
		Corner[] innerCorners;
		int cornerAmount = 16;
		outerCorners = GameObject.generatePeriodicObject(radius, cornerAmount, rp).getCorners();
		innerCorners = GameObject.generatePeriodicObject(radius-15, cornerAmount, rp).getCorners();
		Corner[] corners = new Corner[cornerAmount*2+2];
		for(int i = 0; i< cornerAmount; i++) {
			corners[i] = outerCorners[i];
			corners[cornerAmount*2-1 - i] = innerCorners[i];
		}
		corners[cornerAmount*2] = new Corner(innerCorners[innerCorners.length-1],rp);
		corners[cornerAmount*2 + 1] = new Corner(outerCorners[outerCorners.length-1], rp);
		
		return new Shield(corners, rp, 0);
		
	}
	
	
	public boolean friendlyMissile(Missile ms) {
		for(GameObject go : ms.getImunne()) {
			if(AIBlock) {
				if(go instanceof AI) {
					return false;
				}
			}
		}
		for(GameObject toBlock : whoToBlock) {
			if(ms.isImune(toBlock)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkCollision(GameObject go) {
		if(!AIBlock) {
			if(go instanceof AI) {
				return false;
			}
		}
		else if(go instanceof Missile) {
			if(!friendlyMissile((Missile) go)) {
				return super.checkCollision(go);
			}
		}
		
		else if(isFriendly(go)) {
			return false;
		}
		
		else if(go instanceof Explosives || go instanceof HuntingMine) {
			if(super.checkCollision(go)) {
				go.setHP(0);
				return true;
			}
		}
		else {
			if(super.checkCollision(go)) {
				parent.startStun(30);
				parent.setCurrentSpeed(0);
				return true;
			}
		}
		return false;
	}
	
	public boolean isFriendly(GameObject go) {
		if(AIBlock) {
			if(go instanceof AI) {
				return false;
			}
		}
		boolean amFriendly = true;
		for(GameObject toBlock : whoToBlock) {
			if(toBlock == go) {
				amFriendly = false;
			}
		}
		return amFriendly;
	}
	
	public void updateOb() {
		if(parent != null) {
			setRotationPoint(parent.getRotationPoint());
			rotateOb();
			getCollisionSquare().setToRP(getRotationPoint());
		}
		updateDuration();
		if(parent instanceof Player && getHP() == 0) {
			((Player) parent).setShieldIsUp(false);
		}
	}
	
	public void setUpShield(boolean aiBlock, GameObject[] toBlock, LivingObject go) {
		AIBlock = aiBlock;
		whoToBlock = toBlock;
		parent = go;
	}
	public void setDuration(int duration) {
		this.duration = duration;
		hasDuration = true;
	}
	
	public void render(Graphics g) {
		super.render(g);
	//	getCollisionSquare().render(g);
	}

}
