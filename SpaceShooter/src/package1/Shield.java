package package1;


public class Shield extends GameObject{

	boolean AIBlock = false;
	GameObject[] whoToBlock = new GameObject[0];
	
		
	public Shield(Corner[] corners, Corner rp, double rotationAngle) {
		super(corners, rp, rotationAngle);
		// TODO Auto-generated constructor stub
	}
	
	

	public static Shield makeShield(Corner rp, int radius) {
		Corner[] outerCorners;
		Corner[] innerCorners;
		int cornerAmount = 14;
		outerCorners = GameObject.generatePeriodicObject(radius, cornerAmount, rp).getCorners();
		innerCorners = GameObject.generatePeriodicObject(radius-15, cornerAmount, rp).getCorners();
		Corner[] corners = new Corner[cornerAmount*2+2];
		for(int i = 0; i< cornerAmount; i++) {
			corners[i] = outerCorners[i];
			corners[cornerAmount*2-1 - i] = innerCorners[i];
		}
		corners[cornerAmount*2] = innerCorners[innerCorners.length-1];
		corners[cornerAmount*2 + 1] = outerCorners[outerCorners.length-1];
		
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
		if(go instanceof LivingObject) {
			return false;
		}
		if(go instanceof Missile) {
			if(!friendlyMissile((Missile) go)) {
				return super.checkCollision(go);
			}
			
		}
		return false;
	}
	
	public void setUpShield(boolean aiBlock, GameObject[] toBlock) {
		AIBlock = aiBlock;
		whoToBlock = toBlock;
	}

}
