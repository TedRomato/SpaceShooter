package package1;

public class MagazineAttachment extends InteractiveAttachment{
	
	int magazineSize = 1;
	int magazineMaxSize = 1;
	int magazineReloadTimer = 60;
	int magazineReloadLenght = 60;
	boolean reloadingMag = false;

	public MagazineAttachment(Corner[] corners, Corner rp, double[] attachmentRP, double rotationAngle, Corner wayPoint,
			double lenght, double width) {
		super(corners, rp, attachmentRP, rotationAngle, wayPoint, lenght, width);
		// TODO Auto-generated constructor stub
	}
	

	
	public boolean shouldShoot(Corner goalCorner) {
		if(getShoot() && goalCorner != null) {
			if(decideIfFire(goalCorner) && getReloadingMag() == false) {
				magazineSize--;
				return true;
			}
		}
		return false;
	}
	

	
	public boolean shouldShoot() {
		if(getShoot() && getReloadingMag() == false) {
			magazineSize--;
			return true;
		}
		else return false;
	}
	
	public void handleMagazine() {
		if(magazineSize <= 0) {
			magazineReloadTimer--;
			reloadingMag = true;
		}
		if(magazineReloadTimer == 0) {
			reloadingMag = false;
			magazineReloadTimer = magazineReloadLenght;
			magazineSize = magazineMaxSize;
		}
	}
	
	public void shootFromMag() {
		magazineSize--;
	}
	
	
	public void setMagazineParameters(int magazineMaxSize, int magazineReloadLenght) {
		this.magazineMaxSize = magazineMaxSize;
		magazineSize = magazineMaxSize;
		this.magazineReloadLenght = magazineReloadLenght;
		magazineReloadTimer = magazineReloadLenght;
	}
	
	
	public boolean getReloadingMag(){
		return reloadingMag;
	}
	
	public int getMagazineSize() {
		return magazineSize;
	}



	public void setMagazineSize(int magazineSize) {
		this.magazineSize = magazineSize;
	}



	public int getMagazineMaxSize() {
		return magazineMaxSize;
	}



	public void setMagazineMaxSize(int magazineMaxSize) {
		this.magazineMaxSize = magazineMaxSize;
	}



	public int getMagazineReloadTimer() {
		return magazineReloadTimer;
	}



	public void setMagazineReloadTimer(int magazineReloadTimer) {
		this.magazineReloadTimer = magazineReloadTimer;
	}



	public int getMagazineReloadLenght() {
		return magazineReloadLenght;
	}



	public void setMagazineReloadLenght(int magazineReloadLenght) {
		this.magazineReloadLenght = magazineReloadLenght;
	}
}
