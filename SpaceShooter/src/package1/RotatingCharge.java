package package1;

public class RotatingCharge extends SpecialCharge{

	double distanceToStartShooting = 1200;
	
	public RotatingCharge(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	public Missile[] explode() {
		return makePeriodicExplosion(25, this.getRotationPoint(), 8);
	}

	public void updateExplosive() {
		if(getDistanceTraveled() >= distanceToStartShooting) {
			setMaxSpeed(0);
			setCurrentSpeed(getMaxSpeed());
			getNewRatios();
			setNewVels();
			setRotationAngle(5);
			setRight(true);
			setShootForInteractiveAtts(true);
		}
		if(getHP() <= 0) {
			shouldExplode = true;
		}	
		if(((MagazineAttachment)getAttachments()[0]).getReloadingMag() ) {
			shouldExplode = true;
			setHP(0);
		}
	}
	
	public static RotatingCharge makeNewRotatingCharge(double x, double y, Corner md) {
		Corner rp = new Corner(new double[] {x,y});
		
		Corner c1 = new Corner(new double[] {x-5,y+10}, new double[] {x,y});
		Corner c2 = new Corner(new double[] {x-5,y+30}, new double[] {x,y});
		Corner c3 = new Corner(new double[] {x+5,y+30}, new double[] {x,y});
		Corner c4 = new Corner(new double[] {x+5,y+10}, new double[] {x,y});
		
		MagazineAttachment m1= new MagazineAttachment(new Corner[] {c1,c2,c3,c4}, new Corner (new double[] {x,y}), new double[] {x,y}, 0, new Corner(new double[] {x,y+70}), 0.0, 0.0);
		
		Corner c21 = new Corner(new double[] {x-5,y+10}, new double[] {x,y});
		Corner c22 = new Corner(new double[] {x-5,y+30}, new double[] {x,y});
		Corner c23 = new Corner(new double[] {x+5,y+30}, new double[] {x,y});
		Corner c24 = new Corner(new double[] {x+5,y+10}, new double[] {x,y});
		
		MagazineAttachment m2= new MagazineAttachment(new Corner[] {c21,c22,c23,c24}, new Corner (new double[] {x,y}), new double[] {x,y}, 0, new Corner(new double[] {x,y+70}), 0.0, 0.0);
		
		Corner c31 = new Corner(new double[] {x-5,y+10}, new double[] {x,y});
		Corner c32 = new Corner(new double[] {x-5,y+30}, new double[] {x,y});
		Corner c33 = new Corner(new double[] {x+5,y+30}, new double[] {x,y});
		Corner c34 = new Corner(new double[] {x+5,y+10}, new double[] {x,y});
		
		MagazineAttachment m3= new MagazineAttachment(new Corner[] {c31,c32,c33,c34}, new Corner (new double[] {x,y}), new double[] {x,y}, 0, new Corner(new double[] {x,y+70}), 0.0, 0.0);
		

		Corner c41 = new Corner(new double[] {x-5,y+10}, new double[] {x,y});
		Corner c42 = new Corner(new double[] {x-5,y+30}, new double[] {x,y});
		Corner c43 = new Corner(new double[] {x+5,y+30}, new double[] {x,y});
		Corner c44 = new Corner(new double[] {x+5,y+10}, new double[] {x,y});
		
		MagazineAttachment m4 = new MagazineAttachment(new Corner[] {c41,c42,c43,c44}, new Corner (new double[] {x,y}), new double[] {x,y}, 0, new Corner(new double[] {x,y+70}), 0.0, 0.0);
		
		Corner[] c = GameObject.generatePeriodicObject(35, 8, rp).getCorners();
		RotatingCharge rc = new RotatingCharge(c,rp,0,md);
		
		m1.setReloadTimer(20);
		m2.setReloadTimer(20);
		m3.setReloadTimer(20);
		m4.setReloadTimer(20);
		
		m1.setMagazineMaxSize(30);
		m2.setMagazineMaxSize(30);
		m3.setMagazineMaxSize(30);
		m4.setMagazineMaxSize(30);
		
		rc.addAttachment(m1);
		rc.rotateOb(90);
		rc.addAttachment(m2);
		rc.rotateOb(90);
		rc.addAttachment(m3);
		rc.rotateOb(90);
		rc.addAttachment(m4);
		rc.rotateOb(90);
		rc.setShootForInteractiveAtts(false);
		

		
		rc.setMaxSpeed(10);
		rc.setCurrentSpeed(rc.getMaxSpeed());
		rc.getNewRatios();
		rc.setNewVels();
		rc.setHP(5);
		return rc;
	}
	
	
	
	
}
