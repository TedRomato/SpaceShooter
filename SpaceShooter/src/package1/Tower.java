package package1;

public class Tower extends LivingObject{
	private boolean SniperOn = false, MachineGunOn = false, GrenadeLauncherOn = false, TurretOn = false; 
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
	
	public void applyRandomUpgrade(int chanceInPercents) {
		double rand = Math.random()*100;
		if(rand < chanceInPercents) {
			changeToRandomTurretType();
		}	
	}
	
	public void changeToRandomTurretType() {
		double rand = Math.random();
		if(rand < 0.33) {
			this.upgradeToGrenadeLauncher();
		}else if(rand < 0.66){
			this.upgradeToMachineGun();
		}else{
			this.upgradeToSniper();
		}
	}
	
	public void addTurret() {
		this.addAttachment(AutomaticTurret.MakeTurret(this.getRotationPoint().getX(), this.getRotationPoint().getY()));
		turret = (MagazineAttachment) this.getAttachments()[0];
		turret.setMagazineParameters(1, 150);
		TurretOn = true;
	}
	public void upgradeToGrenadeLauncher() {
		this.getAttachments()[0] = (AutomaticExplosiveTurret.MakeTurret(this.getRotationPoint().getX(), this.getRotationPoint().getY()));
		turret = (MagazineAttachment) this.getAttachments()[0];
		turret.setMagazineParameters(1, 350);
		turret.setInaccuracy(200);
		GrenadeLauncherOn = true;
	}
	public void upgradeToSniper() {
		turret.setDmg(3);
		turret.setReloadLenght(250);
		turret.setInaccuracy(25);
		SniperOn = true;
	}
	public void upgradeToMachineGun() {
		turret.setMagazineParameters(3, 200);
		turret.setInaccuracy(50);
		MachineGunOn = true;
	}
	public void UpgradeReloadTime() {		
		turret.setReloadLenght(turret.getReloadLenght()-10);
	}
	public void UpgradeDMG() {
		turret.setDmg((turret.getDmg())+1);
	}
	public void UpgradeMagazineSize() {
		turret.setMagazineSize(turret.getMagazineSize()+1);
	}
	public void UpgradeAccuracy() {
		turret.setInaccuracy(turret.getInaccuracy()-5);
	}
	public void UpgradeGrenadeChunks() {
		((ExplosiveShootingAtt)turret).setGrenadeChunks(((ExplosiveShootingAtt)turret).getGrenadeChunks()+2);
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

	public boolean isGrenadeLauncherOn() {
		return GrenadeLauncherOn;
	}

	public void setGrenadeLauncherOn(boolean grenadeLauncherOn) {
		GrenadeLauncherOn = grenadeLauncherOn;
	}

	public boolean isTurretOn() {
		return TurretOn;
	}

	public void setTurretOn(boolean turretOn) {
		TurretOn = turretOn;
	}
	
	public boolean checkCollision(GameObject go) {
		if(go instanceof Missile) {
			for(GameObject ob :((Missile) go).getImunne()) {
				if(ob instanceof Tower) {
					return false;
				}
			}
		}
		return super.checkCollision(go);
	}
}
	

