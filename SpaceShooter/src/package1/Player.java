package package1;




public class Player extends LivingObject{
	int dashCooldownTimer = 0, dashCooldown = 300;
	double baseSpeed, dashSpeed = 20;
	
	int moveChar = 87, turnLeftChar = 65, turnRightChar = 68, dashChar = 16, reloadChar = 82, abilityChar = 32;
	int faceCanon = -1;
	int machinegun = -1;
	int baseCanon = 2;
	
	boolean dashUnlocked = false;

	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		setMaxSpeed(7);
		setReflectedLenght(35);
		setRotationAngle(3.9);
		setAcceleration(getMaxSpeed() / 45);
		baseSpeed = getMaxSpeed();
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
		if(Game.keyChecker.checkIfkeyIsPressed(abilityChar)) {
			
			if(machinegun != -1) {
				((InteractiveAttachment) getAttachments()[machinegun]).setShoot(true);
				((InteractiveAttachment) getAttachments()[machinegun+1]).setShoot(true);

			}
		
		}
		if(Game.keyChecker.isRightMousePressed()) {
			if(faceCanon != -1) {
				((InteractiveAttachment) getAttachments()[faceCanon]).setShoot(true);

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
			((InteractiveAttachment) getAttachments()[baseCanon]).setShoot(true);
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
		if(Game.keyChecker.isRightMousePressed()==false) {
			if(faceCanon != -1) {
				((InteractiveAttachment) getAttachments()[faceCanon]).setShoot(false);

			}
		}
		if(Game.keyChecker.checkIfkeyIsPressed(abilityChar) == false) {
			if(machinegun != -1) {
				((InteractiveAttachment) getAttachments()[machinegun]).setShoot(false);
				((InteractiveAttachment) getAttachments()[machinegun+1]).setShoot(false);

			}
			
		}

		
	}
	
	private void startDash() {
		if(dashCooldownTimer <= 0) {
			updateMDtoMP();
			getNewRatios();
			setNewVels();
			setCurrentSpeed(dashSpeed);
			dashCooldownTimer = dashCooldown;
		}
	}

	private void handleDashCooldown() {
		if(dashCooldownTimer > 0) {
			dashCooldownTimer--;
		}
	}
	
	public void upgradeDash(int toSubtract) {
		if(dashCooldown - toSubtract > 60) {
			dashCooldown -= toSubtract;

		}
	}
	
	
	public void updatePlayer() {
		handleDashCooldown();
		((MagazineAttachment)getAttachments()[2]).rotateToCorner(((MagazineAttachment)getAttachments()[2]).getAimCorner());
	}
	
	public void setPlayerAimCorner(double x, double y) {
		((MagazineAttachment)getAttachments()[2]).setAimCorner(x,y);
	}
	
	public void addFrontMachineGun() {
		if(machinegun == -1) {
			double ang = getMP().getAngle(getRotationPoint());
			this.rotateOb(180 - ang);
			double[] rp = new double[]{getRotationPoint().getX(), getRotationPoint().getY()};
			
			MagazineAttachment mg1;
			
			Corner c11 = new Corner(new double[] {rp[0]+33,rp[1]},getRotationPoint());
			Corner c12 = new Corner(new double[] {rp[0]+33,rp[1]+50},getRotationPoint());		
			Corner c13 = new Corner(new double[] {rp[0]+40,rp[1]+50},getRotationPoint());
			Corner c14 = new Corner(new double[] {rp[0]+40,rp[1]-35},getRotationPoint());
			
			Corner[] corners1 = new Corner[] {c11,c12,c13,c14};
			
			Corner wp1 = new Corner(new double[] {rp[0]+36,rp[1]+50},getRotationPoint());
			
			mg1 = new MagazineAttachment(corners1, new Corner(getRotationPoint(),getRotationPoint()), new double[] {rp[0],rp[1]}, 0, wp1, 0, 0);
		
			mg1.setRotateWithParentOb(true);
			
			MagazineAttachment mg2;
			Corner c21 = new Corner(new double[] {rp[0]-33,rp[1]},getRotationPoint());
			Corner c22 = new Corner(new double[] {rp[0]-33,rp[1]+50},getRotationPoint());		
			Corner c23 = new Corner(new double[] {rp[0]-40,rp[1]+50},getRotationPoint());
			Corner c24 = new Corner(new double[] {rp[0]-40,rp[1]-35},getRotationPoint());
			
			Corner[] corners2 = new Corner[] {c21,c22,c23,c24};
			
			Corner wp2 = new Corner(new double[] {rp[0]-36,rp[1]+50},getRotationPoint());
			
			mg2 = new MagazineAttachment(corners2, new Corner(getRotationPoint(), getRotationPoint()), new double[] {rp[0],rp[1]}, 0, wp2, 0, 0);
		
			mg2.setRotateWithParentOb(true);
			
			machinegun = getAttachments().length;
			
			mg1.setMagazineParameters(5, 600);
			mg2.setMagazineParameters(5, 600);

			
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
			Corner c11 = new Corner(new double[] {rp[0]+10,rp[1]+65},getRotationPoint());
			Corner c12 = new Corner(new double[] {rp[0]+9,rp[1]+80},getRotationPoint());		
			Corner c13 = new Corner(new double[] {rp[0]-9,rp[1]+80},getRotationPoint());
			Corner c14 = new Corner(new double[] {rp[0]-10,rp[1]+65},getRotationPoint());
			
			Corner[] corners1 = new Corner[] {c11,c12,c13,c14};
			
			Corner wp1 = new Corner(new double[] {rp[0], rp[1] + 80},getRotationPoint());
			
			canon = new MagazineAttachment(corners1, new Corner(getRotationPoint(), getRotationPoint()), new double[] {rp[0],rp[1]}, 0, wp1, 0, 0);
		
			canon.setRotateWithParentOb(true);
			
			canon.setReloadTimer(30);
			canon.setDmg(3);
			canon.setMagazineParameters(1, 200);
			
			faceCanon = getAttachments().length;
			
			addAttachment(canon);
			this.rotateOb(ang - 180);
			this.rotateOb(0.5);

		}
	}
	
	public void upgradeFaceCanon() {
		((MagazineAttachment) getAttachments()[faceCanon]).upgradeMag(1);
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
//	    p.addAttachment(straightLine);

	    
	    return p;
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



	public int getDashCooldown() {
		return dashCooldown;
	}



	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}
		
	
}
