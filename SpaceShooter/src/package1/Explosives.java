package package1;

public class Explosives extends LivingObject{
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
		super.updateOb();
	}
	
	public Missile[] explode() {
		setHP(0);
		return null;
	}
	public void startStun(int stunLenght) {
        setHP(0);
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
		if(isShotImune(go)) {
			return false;
		}
		boolean b = super.checkCollision(go);
		if(go instanceof Meteor && explodesOnImpact && b) {
			setHP(0);
			return true;
		}else if(go instanceof LivingObject && explodesOnImpact && b) {
			setHP(0);
			return true;
		}else if(go instanceof Shield && explodesOnImpact && b) {
			setHP(0);
			return true;
		}else if(b){
			return true;
		}else {
			return false;
		}
	}

	
	
	public void checkAndHandleReflect(GameObject otherOb) {
		if(isShotImune(otherOb)) {
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
	
	
	
	public boolean getShouldExplode() {
		return shouldExplode;
	}
	
	public void setSpeed(double d) {
		setMaxSpeed(d);
		setReflectedSpeed(getMaxSpeed());
		setAcceleration(0);
		setCurrentSpeed(getMaxSpeed());
		setNewVels();
	}
	

}
