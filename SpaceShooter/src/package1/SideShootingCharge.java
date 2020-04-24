package package1;

import java.awt.Color;
import java.awt.Graphics;

public class SideShootingCharge extends SpecialCharge{
	
	boolean periodicShooter = false;
	Corner goal1;
	Corner goal0;
	
	
	public SideShootingCharge(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	public Missile[] explode() {
		return makePeriodicExplosion(25, this.getRotationPoint(), 8, getShotImunes(),1);
	}

	public void updateExplosive() {
		handleAttRotation();
		if(getHP() <= 0) {
			shouldExplode = true;
		}	
	}
	
	//TODO rotovat atts po odrazu 
	
	private void handleAttRotation() {
		((MagazineAttachment)getAttachments()[0]).rotateToCorner(goal0);
		((MagazineAttachment)getAttachments()[1]).rotateToCorner(goal1);
	}
	
	public void reflect(Corner c1, Corner c2) {
		super.reflect(c1, c2);
		updateAttGoals();
	}
	
	private void updateAttGoals() {
		goal0 = Corner.makeCornerUsinAngle(90, getMD().getAngle(getRotationPoint())-65, getRotationPoint());
		goal1 = Corner.makeCornerUsinAngle(90, getMD().getAngle(getRotationPoint())+65, getRotationPoint());

	}
	
	public void moveOb() {
		super.moveOb();
		goal0.moveCorner(getVelX(), getVelY());		
		goal1.moveCorner(getVelX(), getVelY());

	}
	
	public void setPeriodicShooter(boolean b) {
		periodicShooter = b;
		if(periodicShooter) {
			((MagazineAttachment)getAttachments()[1]).setMagazineMaxSize(1);
			((MagazineAttachment)getAttachments()[1]).setMagazineReloadLenght(1);
			((MagazineAttachment)getAttachments()[1]).setMagazineReloadTimer(1);
			((MagazineAttachment)getAttachments()[1]).setReloadLenght(18);


			((MagazineAttachment)getAttachments()[0]).setMagazineMaxSize(1);
			((MagazineAttachment)getAttachments()[0]).setMagazineReloadLenght(1);
			((MagazineAttachment)getAttachments()[0]).setMagazineReloadTimer(1);
			((MagazineAttachment)getAttachments()[0]).setReloadLenght(18);

		}else {
			((MagazineAttachment)getAttachments()[1]).setMagazineMaxSize(8);
			((MagazineAttachment)getAttachments()[1]).setMagazineReloadLenght(30);
			((MagazineAttachment)getAttachments()[1]).setReloadLenght(10);
			
			((MagazineAttachment)getAttachments()[0]).setMagazineMaxSize(8);
			((MagazineAttachment)getAttachments()[0]).setMagazineReloadLenght(30);
			((MagazineAttachment)getAttachments()[0]).setReloadLenght(10);

		}
	}
	
	public static SideShootingCharge makeNewSideShootingCharge(double x, double y, Corner md) {
		Corner rp = new Corner(new double[] {x,y});
		
		Corner c1 = new Corner(new double[] {x-5,y+10}, new double[] {x,y});
		Corner c2 = new Corner(new double[] {x-5,y+30}, new double[] {x,y});
		Corner c3 = new Corner(new double[] {x+5,y+30}, new double[] {x,y});
		Corner c4 = new Corner(new double[] {x+5,y+10}, new double[] {x,y});
		
		MagazineAttachment m1= new MagazineAttachment(new Corner[] {c1,c2,c3,c4}, new Corner (new double[] {x,y}), new double[] {x,y}, 0, new Corner(new double[] {x,y+70}, new double[] {x,y}), 0.0, 0.0);
		
		Corner c21 = new Corner(new double[] {x-5,y+10}, new double[] {x,y});
		Corner c22 = new Corner(new double[] {x-5,y+30}, new double[] {x,y});
		Corner c23 = new Corner(new double[] {x+5,y+30}, new double[] {x,y});
		Corner c24 = new Corner(new double[] {x+5,y+10}, new double[] {x,y});
		
		MagazineAttachment m2= new MagazineAttachment(new Corner[] {c21,c22,c23,c24}, new Corner (new double[] {x,y}), new double[] {x,y}, 0, new Corner(new double[] {x,y+70}, new double[] {x,y}), 0.0, 0.0);
		
		Corner[] c = GameObject.generatePeriodicObject(35, 8, rp).getCorners();
		SideShootingCharge rc = new SideShootingCharge(c,rp,0,md);

		m1.setAttRangle(25);
		m2.setAttRangle(25);
		
		rc.rotateOb(180);
		rc.addAttachment(m2);
		rc.rotateOb(180);
		rc.addAttachment(m1);
		rc.setShootForInteractiveAtts(true);
		
		double ang = md.getAngle(rc.getRotationPoint())-90;
		
		
		rc.rotateOb(ang);
		rc.getMP().rotateCorner(rc.getRotationPoint(), -ang);
		rc.setMaxSpeed(4);
		rc.setCurrentSpeed(rc.getMaxSpeed());
		rc.getNewRatios();
		rc.setNewVels();
		rc.setHP(5);
		rc.setExplodesOnImpact(false);
		rc.updateAttGoals();
		rc.setPeriodicShooter(true);

		return rc;
	}
	
	public void render(Graphics g) {
		super.render(g);
		goal0.renderCorner(g, 10);
		goal1.renderCorner(g, 10);

	}
}
