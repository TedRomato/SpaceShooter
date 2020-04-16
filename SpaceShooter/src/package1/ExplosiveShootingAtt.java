package package1;

public class ExplosiveShootingAtt extends MagazineAttachment {
	
	boolean fireGrenade=false;;
	boolean fireSideShooter=false;
	boolean fireRotatingCharge=false;

	public ExplosiveShootingAtt(Corner[] corners, Corner rp, double[] attachmentRP, double rotationAngle,
			Corner wayPoint, double lenght, double width) {
		super(corners, rp, attachmentRP, rotationAngle, wayPoint, lenght, width);
		// TODO Auto-generated constructor stub
	}
	
	public Explosives Fire(GameObject whoShot) {
		if(fireGrenade) { 
			Grenade nade = Grenade.makeNewGrenade(getSP().getX(), getSP().getY(), getSD());
			nade.setWhoShot(whoShot);
			return nade;
		}
		if(fireSideShooter) {
			SideShootingCharge nade = SideShootingCharge.makeNewSideShootingCharge(getSP().getX(), getSP().getY(), getSD());
			nade.setWhoShot(whoShot);
			return nade;
		}
		if(fireRotatingCharge) {
			RotatingCharge nade = RotatingCharge.makeNewRotatingCharge(getSP().getX(), getSP().getY(), getSD());
			nade.setWhoShot(whoShot);
			return nade;
		}
		return null;
	}
	


	public Explosives Fire() {
		if(fireGrenade) { 
			return Grenade.makeNewGrenade(getSP().getX(), getSP().getY(), getSD());
		}
		if(fireSideShooter) {
			return SideShootingCharge.makeNewSideShootingCharge(getSP().getX(), getSP().getY(), getSD());
		}
		if(fireRotatingCharge) {
			return RotatingCharge.makeNewRotatingCharge(getSP().getX(), getSP().getY(), getSD());
		}
		return null;
	}
	
	public boolean isFireGrenade() {
		return fireGrenade;
	}

	public void setFireGrenade(boolean fireGrenade) {
		this.fireGrenade = fireGrenade;
	}

	public boolean isFireSideShooter() {
		return fireSideShooter;
	}

	public void setFireSideShooter(boolean fireSideShooter) {
		this.fireSideShooter = fireSideShooter;
	}

	public boolean isFireRotatingCharge() {
		return fireRotatingCharge;
	}

	public void setFireRotatingCharge(boolean fireRotatingCharge) {
		this.fireRotatingCharge = fireRotatingCharge;
	}
	
}
