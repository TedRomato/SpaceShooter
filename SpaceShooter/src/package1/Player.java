package package1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;



public class Player extends LivingObject{
	

	boolean wasDamagedByZone = false;
	int zoneDamagedTimerLenght = 60;
	int zoneDamagedTimer = 0;
	
	int shieldHP = 5, shieldDuration = 300, shieldCooldown = 600, shieldTimer = shieldCooldown;
	boolean activateShield = false, shieldIsUnlocked = false;
	
	
	int pulseCooldown = 800,pulseCooldownTimer = pulseCooldown;
	boolean pulse = false, pulseIsUnlocked = false;
	int stunLenght = 300;
	double pulseRange = 900;

	int  berserkModeCooldown = 1800, berserkModeTimer = berserkModeCooldown, costInLives = 5;
	int exploWave = 10, exploWaveCounter = 0,  exploTimer = 0,  exploLenght = 20;
	double berserkSpeed = 12;
	int chunks = 20;
	boolean berserkMode = false;
	boolean berserkModeUnlocked = false;
	 

	int dashCooldown = 300, dashCooldownTimer = dashCooldown;

	double baseSpeed, dashSpeed = 20;
	
	int moveChar = 87, turnLeftChar = 65, turnRightChar = 68, dashChar = 16, reloadChar = 82, abilityChar = 32, berserkChar = 67, pushChar = 81, shieldChar = 70;
	int faceCanon = -1;
	int machinegun = -1;
	int baseCanon = 2;
	
	boolean dashUnlocked = false;
	
	BufferedImage PlayerCannon, PlayerSkin;
	
	boolean usingBC = true;
	boolean usingFC = false;
	boolean usingMG = false;
	
	boolean fireMG = false;
	
	boolean cameraAttached = true;
	
	
	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		setMaxSpeed(7);
		setReflectedLenght(35);
		setRotationAngle(3.9);
		setAcceleration(getMaxSpeed() / 45);
		baseSpeed = getMaxSpeed();
		try {
			PlayerCannon = ImageIO.read(new File("src/Icons/PlayerCannon.png"));
			PlayerSkin = ImageIO.read(new File("src/Icons/PlayerSkin.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	public Player(Corner[] corners, Corner rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		setMaxSpeed(7);
		setReflectedLenght(35);
		setRotationAngle(3.9);
		setAcceleration(getMaxSpeed() / 45);
		baseSpeed = getMaxSpeed();

		
	}

	
	public void handlePlayerKeys() {
		if(Game.keyChecker.checkIfkeyIsPressed(shieldChar)) {
			
			if(shieldTimer >= shieldCooldown && shieldIsUnlocked) {
				activateShield = true;
				shieldTimer=0;
			}
		}
		if(Game.keyChecker.checkIfkeyIsPressed(pushChar)) {
			
			if(pulseCooldownTimer >= pulseCooldown && pulseIsUnlocked) {
				pulse = true;
			}
		}
		
		if(Game.keyChecker.checkIfkeyIsPressed(berserkChar)) {

			if(berserkModeTimer >= berserkModeCooldown && berserkModeUnlocked && berserkMode == false) {
				startBerserkMode();
			}		
		}
		
		if(Game.keyChecker.checkIfkeyIsPressed(abilityChar)) {
			
			if(machinegun != -1) {
				usingMG = true;
				usingBC = false;
				usingFC = false;
			}
		
		}
		if(Game.keyChecker.isRightMousePressed()) {
			if(faceCanon != -1) {
				usingFC = true;
				usingMG = false;
				usingBC = false;

			}
		}
		
		if(Game.keyChecker.checkIfkeyIsPressed(reloadChar)) {
			if(((MagazineAttachment) getAttachments()[baseCanon]).getMagazineSize() != ((MagazineAttachment) getAttachments()[baseCanon]).getMagazineMaxSize()) {
				((MagazineAttachment) getAttachments()[baseCanon]).reloadMag();
			}
		}
		if(Game.keyChecker.checkIfkeyIsPressed(dashChar) && dashUnlocked == true) {
			startDash();
		}
		
		if(Game.keyChecker.checkIfkeyIsPressed(moveChar)) {
			if(getReflected() == false) {

				setForward(true);
			}
		}
		if(Game.keyChecker.checkIfkeyIsPressed(turnLeftChar)) {
			setLeft(true);
			makeNegativeRotation();
		}
		if(Game.keyChecker.checkIfkeyIsPressed(turnRightChar)) {
			setRight(true);
			makePositiveRotation();
			
		}
		if(Game.keyChecker.isLeftMousePressed()) {
			if(usingBC) {
				((InteractiveAttachment) getAttachments()[baseCanon]).setShoot(true);
			}else if(usingMG) {
				fireMG = true;
			}else if(usingFC) {
				((InteractiveAttachment) getAttachments()[faceCanon]).setShoot(true);
			}
		}
		
		if(Game.keyChecker.checkIfkeyIsPressed(moveChar)==false) {
			setForward(false);
			
		}
		if(Game.keyChecker.checkIfkeyIsPressed(turnLeftChar)==false) {
			setLeft(false);
		}
		if(Game.keyChecker.checkIfkeyIsPressed(turnRightChar)==false) {
			setRight(false);
			
		}
		if(Game.keyChecker.isLeftMousePressed()==false) {
			((InteractiveAttachment) getAttachments()[baseCanon]).setShoot(false);
		}

		//Abilities that set bc to false;
		if(Game.keyChecker.isRightMousePressed()==false) {
			if(faceCanon != -1) {
				((InteractiveAttachment) getAttachments()[faceCanon]).setShoot(false);
				if(usingMG == false) {
					usingBC = true;
				}
				usingFC = false;
				}
			}
		if(Game.keyChecker.checkIfkeyIsPressed(abilityChar) == false) {
			if(usingFC == false) {
				usingBC = true;
			}
			usingMG = false;
			}
		}
	
	public Shield useShield() {
		Shield s = Shield.makeShield(this.getRotationPoint(), 150);
		s.setHP(shieldHP);
		s.setDuration(shieldDuration);
		s.setUpShield(true, new GameObject[] {}, this);
		return s;
	}
	
	public void handleShieldCooldown() {
		if(shieldTimer < shieldCooldown) {
			shieldTimer++;
		}
	}
	
	public void upgradeShield() {
		shieldHP ++;
		shieldDuration += 60;
		shieldCooldown -= 15;  
	}
	

	public void usePulse(GameObject[] obs) {

		if(pulseCooldownTimer >= pulseCooldown) {
			for(GameObject go : obs) {
				if(go instanceof LivingObject) {
					if(go.getRotationPoint().getPointDistance(getRotationPoint()) < pulseRange && !isShotImune(go)) {
						((MovingObject) go).pushFromObject(this, ((LivingObject) go).getVelToGoDistance(pulseRange/2));
						((LivingObject) go).startStun(stunLenght);
					}	
				}else if(go instanceof Missile) {
					if(go.getRotationPoint().getPointDistance(getRotationPoint()) < pulseRange) {
						((MovingObject) go).pushFromObject(this, ((MovingObject) go).getCurrentSpeed());
					}
				}
				
			}
			
			pulseCooldownTimer = 0;
			pulse =false;
		}
	}
	
	public void handlePulseCooldown() {
		if(pulseCooldownTimer < pulseCooldown) {
			pulseCooldownTimer++;
		}
	}
	
	public void upgradePulse() {
		stunLenght++;
		pulseRange += 50;
	}
		
	private void handleZoneTimer() {
		if(wasDamagedByZone) {
			zoneDamagedTimer++;
			if(zoneDamagedTimer >= zoneDamagedTimerLenght) {
				zoneDamagedTimer = 0;
				wasDamagedByZone = false;
			}
		}
	}
	
	public void startDash() {
		if(dashCooldownTimer >= dashCooldown) {
			updateMDtoMP();
			getNewRatios();
			setNewVels();
			setCurrentSpeed(dashSpeed);
			dashCooldownTimer = 0;
		}
	}
	public void handleDashCooldown() {
		if(dashCooldownTimer < dashCooldown) {
			dashCooldownTimer++;
		}
	}
	
	public void upgradeDash(int toSubtract) {
		if(dashCooldown - toSubtract > 60) {
			dashCooldown -= toSubtract;

		}
	}
	
	private void fireMG() {
		if(machinegun != -1) {
			if(((MagazineAttachment) getAttachments()[machinegun]).getMagazineReloadTimer() > 0) {
				fireMG = false;
			}
			if(fireMG == false) {
				((InteractiveAttachment) getAttachments()[machinegun]).setShoot(false);
				((InteractiveAttachment) getAttachments()[machinegun+1]).setShoot(false);
			}	
			else if(fireMG) {
				((InteractiveAttachment) getAttachments()[machinegun]).setShoot(true);
				((InteractiveAttachment) getAttachments()[machinegun+1]).setShoot(true);
			}
		}
	}
	
	public void startBerserkMode() {
		berserkMode = true;
		this.setHP(getHP()-costInLives);
	}
	
	public Missile[] handleBereserkMode() {
		if(berserkMode) {
			setCurrentSpeed(berserkSpeed);
			exploTimer++;
			if(exploTimer >= exploLenght) {
				exploWaveCounter++;
				if(exploWaveCounter <= exploWave) {
					exploTimer = 0;
					return makePeriodicExplosion(50, getRotationPoint(), chunks, getShotImunes(),1);
				}else {
					exploWaveCounter = 0;
					exploTimer = 0;
					berserkModeTimer = 0;
					berserkMode = false;
					return null;
				}
			}
		}else if(berserkModeTimer < berserkModeCooldown){
			berserkMode = false;
			berserkModeTimer++;
		}
		return null;
	}
	
	public void upgradeBerserkMode() {
		exploWave ++;
		chunks ++;
		
	}
	
	public void updatePlayer() {
		System.out.println(this.getHP());
		handleZoneTimer();
		fireMG();
		handleDashCooldown();
		handlePulseCooldown();
		handleShieldCooldown();
		rotateGuns();
	}
	
	public void rotateGuns() {
		if(usingBC) {
			((MagazineAttachment)getAttachments()[baseCanon]).rotateToCorner(((MagazineAttachment)getAttachments()[baseCanon]).getAimCorner());
		}
		if(machinegun != -1) {
			if(usingMG) {
				((MagazineAttachment)getAttachments()[machinegun]).rotateToCorner(((MagazineAttachment)getAttachments()[machinegun]).getAimCorner());
				((MagazineAttachment)getAttachments()[machinegun+1]).rotateToCorner(((MagazineAttachment)getAttachments()[machinegun+1]).getAimCorner());
			}else if(usingMG == false && fireMG == false && getAttachments()[machinegun].getAttachmentAngle()!=0) {
				((InteractiveAttachment)getAttachments()[machinegun]).rotateAttachmentAroundItsCorner(0-getAttachments()[machinegun].getAttachmentAngle());
				((InteractiveAttachment)getAttachments()[machinegun+1]).rotateAttachmentAroundItsCorner(0-getAttachments()[machinegun+1].getAttachmentAngle());			
				}
		}
		if(faceCanon != -1) {
			if(usingFC) {
				((MagazineAttachment)getAttachments()[faceCanon]).rotateToCorner(((MagazineAttachment)getAttachments()[faceCanon]).getAimCorner());
			}else if(usingFC == false&& getAttachments()[faceCanon].getAttachmentAngle()!=0) {
				((InteractiveAttachment)getAttachments()[faceCanon]).rotateAttachmentAroundItsCorner(0-getAttachments()[faceCanon].getAttachmentAngle());
				}
		}
	}
	
	public void setPlayerAimCorner(double x, double y) {
		((MagazineAttachment)getAttachments()[baseCanon]).setAimCorner(x,y);
		if(machinegun != -1) {
			((MagazineAttachment)getAttachments()[machinegun]).setAimCorner(x,y);
			((MagazineAttachment)getAttachments()[machinegun+1]).setAimCorner(x,y);
		}
		if(faceCanon != -1) {
			((MagazineAttachment)getAttachments()[faceCanon]).setAimCorner(x,y);

		}
		

	}
	
	public void moveOb() {
		super.moveOb();
		if(cameraAttached) {
			Game.camera.moveCam(getVelX(), getVelY());
		}
	}
	
	public void addFrontMachineGun() {
		if(machinegun == -1) {
			double ang = getMP().getAngle(getRotationPoint());
			this.rotateOb(180 - ang);
			double[] rp = new double[]{getRotationPoint().getX(), getRotationPoint().getY()};
			
			MagazineAttachment mg1;
			
			Corner c11 = new Corner(new double[] {rp[0]+27,rp[1]+18},getRotationPoint());
			Corner c12 = new Corner(new double[] {rp[0]+27,rp[1]+50},getRotationPoint());		
			Corner c13 = new Corner(new double[] {rp[0]+34,rp[1]+50},getRotationPoint());
			Corner c14 = new Corner(new double[] {rp[0]+34,rp[1]-12},getRotationPoint());
			
			Corner[] corners1 = new Corner[] {c11,c12,c13,c14};
			
			Corner wp1 = new Corner(new double[] {rp[0]+30,rp[1]+35},getRotationPoint());
			
			mg1 = new MagazineAttachment(corners1, new Corner(getRotationPoint(),getRotationPoint()), new double[] {rp[0]+30,rp[1]-11}, 0, wp1, 0, 0);
		
			mg1.setRotateWithParentOb(true);
			
			MagazineAttachment mg2;
			Corner c21 = new Corner(new double[] {rp[0]-27,rp[1]+18},getRotationPoint());
			Corner c22 = new Corner(new double[] {rp[0]-27,rp[1]+50},getRotationPoint());		
			Corner c23 = new Corner(new double[] {rp[0]-34,rp[1]+50},getRotationPoint());
			Corner c24 = new Corner(new double[] {rp[0]-34,rp[1]-12},getRotationPoint());
			
			Corner[] corners2 = new Corner[] {c21,c22,c23,c24};
			
			Corner wp2 = new Corner(new double[] {rp[0]-30,rp[1]+35},getRotationPoint());
			
			mg2 = new MagazineAttachment(corners2, new Corner(getRotationPoint(), getRotationPoint()), new double[] {rp[0] - 30,rp[1] - 11}, 0, wp2, 0, 0);
		
			mg2.setRotateWithParentOb(true);
			
			machinegun = getAttachments().length;
			
			mg1.setMagazineParameters(5, 600);
			mg2.setMagazineParameters(5, 600);

			mg1.setAttRangle(2);
			mg2.setAttRangle(2);
			
			double[] s = new double[] {-15,15};
			
			mg1.setRotationSegment(s);
			mg2.setRotationSegment(s);
			
			this.addAttachment(mg1);
			this.addAttachment(mg2);
			
			this.rotateOb(ang - 180);
			this.rotateOb(0.5);
		}
		
	}
	
	public void upgradeMG() {
		((MagazineAttachment) getAttachments()[machinegun]).upgradeMag(1);
		((MagazineAttachment) getAttachments()[machinegun]).upgradeMagReload(40);
		((MagazineAttachment) getAttachments()[machinegun+1]).upgradeMag(1);
		((MagazineAttachment) getAttachments()[machinegun+1]).upgradeMagReload(40);
	}
	
	public void addFrontCanon() {
		if(faceCanon == -1) {
			double ang = getMP().getAngle(getRotationPoint());
			this.rotateOb(180 - ang);
			double[] rp = new double[]{getRotationPoint().getX(), getRotationPoint().getY()};
			MagazineAttachment canon;
			Corner c11 = new Corner(new double[] {rp[0]+10,rp[1]+50},getRotationPoint());
			Corner c12 = new Corner(new double[] {rp[0]+9,rp[1]+80},getRotationPoint());		
			Corner c13 = new Corner(new double[] {rp[0]-9,rp[1]+80},getRotationPoint());
			Corner c14 = new Corner(new double[] {rp[0]-10,rp[1]+50},getRotationPoint());
			
			Corner[] corners1 = new Corner[] {c11,c12,c13,c14};
			
			Corner wp1 = new Corner(new double[] {rp[0], rp[1] + 80},getRotationPoint());
			
			canon = new MagazineAttachment(corners1, new Corner(getRotationPoint(), getRotationPoint()), new double[] {rp[0],rp[1]+50}, 0, wp1, 0, 0);
		
			canon.setRotateWithParentOb(true);
			
			canon.setReloadTimer(30);
			canon.setDmg(3);
			canon.setMagazineParameters(1, 200);
			
			canon.setRotationSegment(new double[] {-60,60});
			
			faceCanon = getAttachments().length;
			
			canon.setAttRangle(10);
			
			addAttachment(canon);
			this.rotateOb(ang - 180);
			this.rotateOb(0.5);

		}
	}
	
	public void upgradeFaceCanon() {
		((MagazineAttachment) getAttachments()[faceCanon]).setDmg(((MagazineAttachment) getAttachments()[faceCanon]).getDmg()+1);
		((MagazineAttachment) getAttachments()[faceCanon]).upgradeMagReload(30);
	}
	
	public static Player makeNewPlayer(double[] rp) {
		Player p;
		ObjectAttachment attachment1;
		ObjectAttachment attachment2;
		MagazineAttachment canon;
//		ObjectAttachment straightLine;
		Corner wp = new Corner(new double[] {rp[0] ,rp[1] + 10}, rp);

		
		Corner peakA1 = new Corner(new double[] {rp[0]-40,rp[1]-40}, rp);
	    Corner rightCornerA1 = new Corner(new double[] {rp[0] - 20, rp[1]+50}, rp);
	    Corner leftCornerA1 = new Corner(new double[] {rp[0] - 20,  rp[1]-50}, rp);
	    
	    attachment1 = new ObjectAttachment(new Corner[] {peakA1, rightCornerA1, leftCornerA1}, rp,new double[] {rp[0],rp[1]},-5);
	    
	    Corner peakA2 = new Corner(new double[] {rp[0]+40,rp[1]-40}, rp);
	    Corner rightCornerA2 = new Corner(new double[] {rp[0] + 20, rp[1]-50}, rp);
	    Corner leftCornerA2 = new Corner(new double[] {rp[0] + 20,  rp[1]+50}, rp);
	    
	    attachment2 = new ObjectAttachment(new Corner[] {peakA2, rightCornerA2, leftCornerA2}, rp,new double[] {rp[0],rp[1]},-5);
	    
	    Corner l2 = new Corner(new double[] {rp[0] - 10, rp[1]+65}, rp);
	    Corner r2 = new Corner(new double[] {rp[0] + 10,  rp[1]+65}, rp);
	    
	    
	    

//	    straightLine = new ObjectAttachment(new Corner[] {new Corner(new double[] {rp[0] ,rp[1] + 25}, rp), new Corner(new double[] {rp[0] ,rp[1] + 350}, rp),}, rp,new double[] {rp[0],rp[1]-25},-5);
	    Corner b1 = new Corner(new double[] {rp[0] - 6,rp[1] + 5}, rp);
	    Corner b2 =	new Corner(new double[] {rp[0] + 6,rp[1] + 5}, rp);
	    Corner b3 = new Corner(new double[] {rp[0] + 6,rp[1] + 40}, rp);
	    Corner b4 = new Corner(new double[] {rp[0] - 6,rp[1] + 40}, rp);

		
	    canon = new MagazineAttachment(new Corner[] {b1,b2,b3,b4}, new Corner(rp) , new double[] {rp[0], rp[1] + 5}, 0, wp, 0,0);
	    canon.setMagazineParameters(5, 60);
	    canon.setAttRangle(25);
	    canon.setRotateWithParentOb(false);
	//    canon.setRotationSegment(new double[] {-220,220});
	    
	    
	    Corner rightTCorner = new Corner(new double[] {rp[0] - 20, rp[1] - 50}, rp);
	    Corner leftTCorner = new Corner(new double[] {rp[0] + 20, rp[1] - 50}, rp);
	    Corner rightBCorner = new Corner(new double[] {rp[0] - 20, rp[1] + 50}, rp);
	    Corner leftBCorner = new Corner(new double[] {rp[0] + 20, rp[1] + 50}, rp);
	   
	    p = new Player(new Corner[] {leftTCorner,leftBCorner,r2,l2,rightBCorner,rightTCorner},rp, 1, new Corner(new double[] {rp[0],rp[1]+25}, rp));
	    p.addAttachment(attachment1);	    
	    p.addAttachment(attachment2);	  
	 //   p.addAttachment(attachment3);
	    p.setHP(50);
	    p.setReflectedSpeed(6);
	    p.addAttachment(canon);
	    p.setReflectedLenght(20);
//		p.addFrontCanon();
//	    p.addFrontMachineGun();
//	    p.setDashUnlocked(true);
//      p.setPulseUnlocked(true);
//	    p.setBerserkModeUnlocked(true);
//	    p.shieldIsUnlocked = true;
//	    p.addAttachment(straightLine);
//	    Shield s = Shield.makeShield(new Corner(rp), 150);
//	    p.addAttachment(s);

	    
	    return p;

	}
	

	public void render(Graphics g) {

		super.render(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		rotateImage(g2, PlayerSkin, this.getRotatedAngle(),this.getRotationPoint(),90,115,41,50);		
		rotateImage(g2, PlayerCannon, this.getAttachments()[2].getAttachmentAngleRotated() ,this.getAttachments()[2].getAttachmentRP(),14,40,5,2);
	}


	public boolean isBerserkModeUnlocked() {
		return berserkModeUnlocked;

	}
	
	public void setBerserkModeUnlocked(boolean b) {
		berserkModeUnlocked = b;
	}
	
	public boolean isPulseUnlocked() {
		return pulseIsUnlocked;
	}
	
	public void setPulseUnlocked(boolean b) {
		pulseIsUnlocked = b;
	}
	
	public boolean isDashUnlocked() {
		return dashUnlocked;
	}



	public void setDashUnlocked(boolean dashUnlocked) {
		this.dashUnlocked = dashUnlocked;
	}



	public int getDashCooldownTimer() {
		return dashCooldownTimer;
	}



	public void setDashCooldownTimer(int dashCooldownTimer) {
		this.dashCooldownTimer = dashCooldownTimer;
	}



	public boolean isPulse() {
		return pulse;
	}



	public void setPulse(boolean pulse) {
		this.pulse = pulse;
	}



	public int getDashCooldown() {
		return dashCooldown;
	}



	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}



	public boolean isShieldIsUnlocked() {
		return shieldIsUnlocked;
	}



	public void setShieldIsUnlocked(boolean shieldIsUnlocked) {
		this.shieldIsUnlocked = shieldIsUnlocked;
	}



	public int getShieldHP() {
		return shieldHP;
	}



	public void setShieldHP(int shieldHP) {
		this.shieldHP = shieldHP;
	}



	public int getShieldDuration() {
		return shieldDuration;
	}



	public void setShieldDuration(int shieldDuration) {
		this.shieldDuration = shieldDuration;
	}



	public int getShieldCooldown() {
		return shieldCooldown;
	}



	public void setShieldCooldown(int shieldCooldown) {
		this.shieldCooldown = shieldCooldown;
	}


	
	public int getShieldTimer() {
		return shieldTimer;
	}



	public void setShieldTimer(int shieldTimer) {
		this.shieldTimer = shieldTimer;
	}



	public int getPulseCooldown() {
		return pulseCooldown;
	}



	public void setPulseCooldown(int pulseCooldown) {
		this.pulseCooldown = pulseCooldown;
	}



	public int getPulseCooldownTimer() {
		return pulseCooldownTimer;
	}



	public void setPulseCooldownTimer(int pulseCooldownTimer) {
		this.pulseCooldownTimer = pulseCooldownTimer;
	}



	public int getBerserkModeCooldown() {
		return berserkModeCooldown;
	}



	public void setBerserkModeCooldown(int berserkModeCooldown) {
		this.berserkModeCooldown = berserkModeCooldown;
	}



	public int getBerserkModeTimer() {
		return berserkModeTimer;
	}



	public void setBerserkModeTimer(int berserkModeTimer) {
		this.berserkModeTimer = berserkModeTimer;
	}

	



		
	
}
