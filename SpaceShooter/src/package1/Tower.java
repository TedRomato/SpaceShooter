package package1;

public class Tower extends LivingObject{
	private boolean SniperOn = false, MachineGunOn = false, GranadeLauncherOn = false, TurretOn = false; 
	private MagazineAttachment turret;
	
	public Tower(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	public static Tower makeNewTower(double x, double y){
		Tower tower = new Tower(GameObject.generatePeriodicObject(120, 8, new Corner(new double[] {x,y})).getCorners(), new Corner(new double[] {x,y}),0.0, new Corner(new double[] {x,y}));
		
		return tower;
	}
	
	public void updateAllTurrets(GameObject[] enemys) {
		if(getAttachments()!=null) {
			for(ObjectAttachment att : getAttachments()) {
				if(att instanceof AutomaticTurret) {
					((AutomaticTurret) att).updateAutoTurert(enemys);
				}
				if(att instanceof AutomaticExplosiveTurret) {
					((AutomaticExplosiveTurret) att).updateAutoTurret(enemys);
				}
			}
		}
	}
	public void addTurret() {
		this.addAttachment(AutomaticTurret.MakeTurret(this.getRotationPoint().getX(), this.getRotationPoint().getY()));
		turret = (MagazineAttachment) this.getAttachments()[0];
		turret.setMagazineParameters(1, 150);
		TurretOn = true;
	}
	public void upgradeToGranadeLauncher() {
		this.getAttachments()[0] = (AutomaticExplosiveTurret.MakeTurret(this.getRotationPoint().getX(), this.getRotationPoint().getY()));
		turret = (MagazineAttachment) this.getAttachments()[0];
		turret.setMagazineParameters(1, 350);
		GranadeLauncherOn = true;
	}
	public void upgradeToSniper() {
		turret.setDmg(3);
		turret.setReloadLenght(250);
		SniperOn = true;
	}
	public void upgradeToMachineGun() {
		turret.setMagazineParameters(3, 200);
		MachineGunOn = true;
	}
	public void UpgradeReloadTime() {		
		turret.setReloadLenght(turret.getReloadLenght()-10);
	}
	public void UpgradeDMG() {
		turret.setDmg(turret.getDmg()+1);
	}
	public void UpgradeMagazineSize() {
		turret.setMagazineSize(turret.getMagazineSize()+1);
	}
	public void UpgradeAccuracy() {
		turret.setInaccuracy(turret.getInaccuracy()-25);
	}

	public boolean isSniperOn() {
		return SniperOn;
	}

	public void setSniperOn(boolean sniperOn) {
		SniperOn = sniperOn;
	}

	public boolean isMachineGunOn() {
		return MachineGunOn;
	}

	public void setMachineGunOn(boolean machineGunOn) {
		MachineGunOn = machineGunOn;
	}

	public boolean isGranadeLauncherOn() {
		return GranadeLauncherOn;
	}

	public void setGranadeLauncherOn(boolean granadeLauncherOn) {
		GranadeLauncherOn = granadeLauncherOn;
	}

	public boolean isTurretOn() {
		return TurretOn;
	}

	public void setTurretOn(boolean turretOn) {
		TurretOn = turretOn;
	}
	}
	

