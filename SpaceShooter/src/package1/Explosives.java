package package1;

public class Explosives extends LivingObject{
	GameObject whoShot;
	double distanceTraveled = 0;
	double BOOMDistance = 1500;
	boolean shouldExplode = false;
	boolean explodesOnImpact = true;
	
	public Explosives(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		setHP(5);
		setMaxSpeed(5);
		
		// TODO Auto-generated constructor stub
	}
	
	public void updateOb() {
		setCurrentSpeed(getMaxSpeed());
		super.updateOb();
	}
	
	public Missile[] explode() {
		setHP(0);
		return null;
	}
	
	public void updateExplosive() {
		if(getHP() <= 0) {
			shouldExplode = true;
		}	
		if(distanceTraveled >= BOOMDistance ) {
			shouldExplode = true;
			setHP(0);
		}
	}
	
	
	public boolean checkCollision(GameObject go) {
		if(go == this.getWhoShot()) {
			return false;
		}
		boolean b = super.checkCollision(go);
		if(go instanceof LivingObject && explodesOnImpact && b) {
			setHP(0);
			return true;
		}else if(b){
			return true;
		}else {
			return false;
		}
	}

	
	
	public void checkAndHandleReflect(GameObject otherOb) {
		if(otherOb == getWhoShot()) {
			return;
		}
		super.checkAndHandleReflect(otherOb);
	}
	
	public double getDistanceTraveled() {
		return distanceTraveled;
	}



	public void setDistanceTraveled(double distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}



	public void setWhoShot(GameObject whoShot) {
		this.whoShot = whoShot;
	}

	public void setExplodesOnImpact(boolean b) {
		explodesOnImpact = b;
	}
	public boolean getExplodesOnImpact() {
		return explodesOnImpact;
	}

	public void moveOb() {
		super.moveOb();
		distanceTraveled += getCurrentSpeed();
	}
	
	public GameObject getWhoShot() {
		return whoShot;
	}
	
	public boolean getShouldExplode() {
		return shouldExplode;
	}
	
	public void setSpeed(double d) {
		setMaxSpeed(d);
		setReflectedSpeed(getMaxSpeed());
	}
}
