package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class Player extends LivingObject{
	
	//Zone variables
	boolean wasDamagedByZone = false;
	double zoneDamagedTimerLenght = 60;
	double zoneDamagedTimer = 0;
	
	boolean shieldIsUnlocked = false;
	//pulse variables
	double pulseCooldown = 800,pulseCooldownTimer = pulseCooldown;
	boolean pulse = false, pulseIsUnlocked = false;
	double stunLenght = 300;
	double pulseRange = 900;

	//berserkMode variables
	double  berserkModeCooldown = 1800, berserkModeTimer = berserkModeCooldown, exploTimer = 0,  exploLenght = 20;
	int  costInLives = 5,exploWave = 10, exploWaveCounter = 0;
	double berserkSpeed = 12*Game.tickMultiply;
	int chunks = 20;
	boolean berserkMode = false;
	boolean berserkModeUnlocked = false;
	 
	//dash variables
	double dashCooldown = 300, dashCooldownTimer = dashCooldown;
	double baseSpeed, dashSpeed = 20*Game.tickMultiply;
	boolean dashUnlocked = false;
	
	//
	int moveChar = 87, turnLeftChar = 65, turnRightChar = 68, dashChar = 16, reloadChar = 82, abilityChar = 32, berserkChar = 67, pushChar = 81, shieldChar = 70;
	
	//Canon indexes
	int faceCanon = -1;
	int machinegun = -1;
	int baseCanon = 2;
	
	//canon booleans 
	boolean usingBC = true;
	boolean usingFC = false;
	boolean usingMG = false;
	
	boolean fireMG = false;
	
	boolean cameraAttached = true;
	
	BufferedImage PlayerCannon, PlayerSkin, FaceCanon, MachineGun1, MachineGun2;

	
	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		initialSetMaxSpeed(7);
		setReflectedLenght(35);
		initialRAngleSet(3.9);
		initialSetAcceleration(getMaxSpeed() / 45);
		baseSpeed = getMaxSpeed();
		try {
			MachineGun1 = ImageIO.read(new File("src/Icons/mg.png"));
			MachineGun1 = Game.resize(MachineGun1,200, 200);
			MachineGun2 = ImageIO.read(new File("src/Icons/mg.png"));
			MachineGun2 = Game.resize(MachineGun2,200, 200);
			
			FaceCanon = ImageIO.read(new File("src/Icons/FrontCanon.png"));
			FaceCanon = Game.resize(FaceCanon,100,100);
			PlayerCannon = ImageIO.read(new File("src/Icons/PlayerCannon.png"));
			PlayerCannon = Game.resize(PlayerCannon,14,40);
			PlayerSkin = ImageIO.read(new File("src/Icons/PlayerSkin.png"));
	//		PlayerSkin = resize(PlayerSkin,360,380);
			PlayerSkin = Game.resize(PlayerSkin,360,380);
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
		Shield s = Shield.makeShield(this.getRotationPoint(), shieldRadius);
		s.setHP(shieldHP);
		s.setDuration(shieldDuration);
		s.setUpShield(true, new GameObject[] {}, this);
		setShieldIsUp(true);
		shield = s;
		return s;
	}

	public void usePulse(GameObject[] obs) {

		if(pulseCooldownTimer >= pulseCooldown) {
			for(GameObject go : obs) {
				if(go instanceof Grenade) {
					if(go.getRotationPoint().getPointDistance(getRotationPoint()) < pulseRange && !isShotImune(go)) {
						go.setHP(0);
					}	
					
				}
				if(go instanceof Meteor) {
					((MovingObject) go).pushFromObject(this, ((Meteor) go).getCurrentSpeed());
				}
				if(go instanceof LivingObject) {
					if(go.getRotationPoint().getPointDistance(getRotationPoint()) < pulseRange && !isShotImune(go)) {
						((MovingObject) go).pushFromObject(this, ((LivingObject) go).getVelToGoDistance(pulseRange/2));
						((LivingObject) go).startStun(stunLenght);
					}	
				}else if(go instanceof Missile) {
					if(go.getRotationPoint().getPointDistance(getRotationPoint()) < pulseRange) {
						((MovingObject) go).pushFromObject(this, ((MovingObject) go).getCurrentSpeed());
						((Missile) go).setImune(new GameObject[] {this});
					}
				}
				
			}
			
			pulseCooldownTimer = 0;
			pulse =false;
		}
	}
	
	public void handlePulseCooldown() {
		if(pulseCooldownTimer < pulseCooldown) {
			pulseCooldownTimer+= Game.tickOne;
		}
	}
	
	public void upgradePulse() {
		stunLenght+= Game.tickOne;
		pulseRange += 50;
	}
		
	private void handleZoneTimer() {
		if(wasDamagedByZone) {
			zoneDamagedTimer+= Game.tickOne;
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
			dashCooldownTimer+= Game.tickOne;
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
			exploTimer+=Game.tickOne;
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
			berserkModeTimer+=Game.tickOne;
		}
		return null;
	}
	
	public void upgradeBerserkMode() {
		exploWave ++;
		chunks ++;
		
	}
	
	public void updatePlayer() {
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
			
			Corner c11 = new Corner(new double[] {rp[0]+34,rp[1]+18},getRotationPoint());
			Corner c12 = new Corner(new double[] {rp[0]+34,rp[1]+50},getRotationPoint());		
			Corner c13 = new Corner(new double[] {rp[0]+41,rp[1]+50},getRotationPoint());
			Corner c14 = new Corner(new double[] {rp[0]+41,rp[1]-12},getRotationPoint());
			
			Corner[] corners1 = new Corner[] {c11,c12,c13,c14};
			
			Corner wp1 = new Corner(new double[] {rp[0]+38,rp[1]+35},getRotationPoint());
			
			mg1 = new MagazineAttachment(corners1, new Corner(getRotationPoint(),getRotationPoint()), new double[] {rp[0]+38,rp[1]-11}, 0, wp1, 0, 0);
		
			mg1.setRotateWithParentOb(true);
			
			MagazineAttachment mg2;
			Corner c21 = new Corner(new double[] {rp[0]-34,rp[1]+18},getRotationPoint());
			Corner c22 = new Corner(new double[] {rp[0]-34,rp[1]+50},getRotationPoint());		
			Corner c23 = new Corner(new double[] {rp[0]-41,rp[1]+50},getRotationPoint());
			Corner c24 = new Corner(new double[] {rp[0]-41,rp[1]-12},getRotationPoint());
			
			Corner[] corners2 = new Corner[] {c21,c22,c23,c24};
			
			Corner wp2 = new Corner(new double[] {rp[0]-38,rp[1]+35},getRotationPoint());
			
			mg2 = new MagazineAttachment(corners2, new Corner(getRotationPoint(), getRotationPoint()), new double[] {rp[0] - 38,rp[1] - 11}, 0, wp2, 0, 0);
		
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
			Corner c11 = new Corner(new double[] {rp[0]+10,rp[1]+70},getRotationPoint());
			Corner c12 = new Corner(new double[] {rp[0]+9,rp[1]+80},getRotationPoint());		
			Corner c13 = new Corner(new double[] {rp[0]-9,rp[1]+80},getRotationPoint());
			Corner c14 = new Corner(new double[] {rp[0]-10,rp[1]+70},getRotationPoint());
			
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

		
		Corner peakA1 = new Corner(new double[] {rp[0]-50,rp[1]-45}, rp);
	    Corner rightCornerA1 = new Corner(new double[] {rp[0] - 20, rp[1]+50}, rp);
	    Corner leftCornerA1 = new Corner(new double[] {rp[0] - 20,  rp[1]-40}, rp);
	    
	    
	    attachment1 = new ObjectAttachment(new Corner[] {peakA1, rightCornerA1, leftCornerA1}, rp,new double[] {rp[0],rp[1]},-5);
	    
	    
	    Corner peakA2 = new Corner(new double[] {rp[0]+50,rp[1]-45}, rp);
	    Corner rightCornerA2 = new Corner(new double[] {rp[0] + 20, rp[1]-40}, rp);
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
	    canon.initialSetAttRangle(60);
	    canon.setRotateWithParentOb(false);
	//    canon.setRotationSegment(new double[] {-220,220});
	    

	    Corner rightTCorner = new Corner(new double[] {rp[0] - 20, rp[1] - 40}, rp);
	    Corner leftTCorner = new Corner(new double[] {rp[0] + 20, rp[1] - 40}, rp);
	    Corner rightBCorner = new Corner(new double[] {rp[0] - 20, rp[1] + 50}, rp);
	    Corner leftBCorner = new Corner(new double[] {rp[0] + 20, rp[1] + 50}, rp);

	    
	    p = new Player(new Corner[] {leftTCorner,leftBCorner,r2,l2,rightBCorner,rightTCorner},rp, 1, new Corner(new double[] {rp[0],rp[1]+25}, rp));
	    p.addAttachment(attachment1);	    
	    p.addAttachment(attachment2);	  
	    p.setHP(50);
	    p.initialSetReflectedSpeed(6);
	    p.addAttachment(canon);
	    p.setReflectedLenght(20);

//		p.addFrontCanon();
//	    p.addFrontMachineGun();
//	    p.setDashUnlocked(true);
//	    p.setPulseUnlocked(true);
//	    p.setBerserkModeUnlocked(true);
//	    p.shieldIsUnlocked = true;
//	    p.addAttachment(straightLine);
//	    Shield s = Shield.makeShield(new Corner(rp), 150);
//	    p.addAttachment(s);

	  
	    return p;

	}
	

	public void render(Graphics g) {
		g.setColor(Color.CYAN);
		super.render(g);
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



	public double getDashCooldownTimer() {
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



	public double getDashCooldown() {
		return dashCooldown;
	}


	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}


	
	//SHIELD
	

	public boolean isShieldIsUnlocked() {
		return shieldIsUnlocked;
	}



	public void setShieldIsUnlocked(boolean shieldIsUnlocked) {
		this.shieldIsUnlocked = shieldIsUnlocked;
	}
	
	public double getShieldTimer() {
		return shieldTimer;
	}



	public void setShieldTimer(int shieldTimer) {
		this.shieldTimer = shieldTimer;
	}



	public double getPulseCooldown() {
		return pulseCooldown;
	}



	public void setPulseCooldown(int pulseCooldown) {
		this.pulseCooldown = pulseCooldown;
	}



	public double getPulseCooldownTimer() {
		return pulseCooldownTimer;
	}



	public void setPulseCooldownTimer(int pulseCooldownTimer) {
		this.pulseCooldownTimer = pulseCooldownTimer;
	}



	public double getBerserkModeCooldown() {
		return berserkModeCooldown;
	}



	public void setBerserkModeCooldown(int berserkModeCooldown) {
		this.berserkModeCooldown = berserkModeCooldown;
	}



	public double getBerserkModeTimer() {
		return berserkModeTimer;
	}



	public void setBerserkModeTimer(int berserkModeTimer) {
		this.berserkModeTimer = berserkModeTimer;
	}
	
	public double getExploTimer() {
		return exploTimer;
	}
	


	public void setExploTimer(int exploTimer) {
		this.exploTimer = exploTimer;
	}



	public double getExploLenght() {
		return exploLenght;
	}



	public void setExploLenght(int exploLenght) {
		this.exploLenght = exploLenght;
	}
	



	public int getExploWave() {
		return exploWave;
	}



	public void setExploWave(int exploWave) {
		this.exploWave = exploWave;
	}



	public int getExploWaveCounter() {
		return exploWaveCounter;
	}



	public void setExploWaveCounter(int exploWaveCounter) {
		this.exploWaveCounter = exploWaveCounter;
	}




	public Shield getShield() {
		return shield;
	}
	
	public int getCurrentShieldHP() {
		return shield.getHP();
		
	}


}	

