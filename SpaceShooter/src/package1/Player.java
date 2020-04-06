package package1;




public class Player extends LivingObject{
	
	char moveChar = 'w', turnLeftChar = 'a', turnRightChar = 'd', shootChar = ' ';
	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		setReflectedLenght(35);
		setRotationAngle(3.9);
		setAcceleration(getMaxSpeed() / 45);
	}

	


	
	public Player(Corner[] corners, Corner rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		setReflectedLenght(35);
		setRotationAngle(3.9);
		setAcceleration(getMaxSpeed() / 45);
		
	}

	
	public void handlePlayerKeys() {
		
		if(Game.keyChecker.checkIfCharIsPressed(moveChar)) {
			if(getReflected() == false) {

				setForward(true);
			}
			
			
		}
		if(Game.keyChecker.checkIfCharIsPressed(turnLeftChar)) {
			setLeft(true);
			makeNegativeRotation();
		}
		if(Game.keyChecker.checkIfCharIsPressed(turnRightChar)) {
			setRight(true);
			makePositiveRotation();
			
		}
		if(Game.keyChecker.isLeftMousePressed()) {
			setShootForInteractiveAtts(true);
		}
		
		if(Game.keyChecker.checkIfCharIsPressed(moveChar)==false) {
			setForward(false);
			
		}
		if(Game.keyChecker.checkIfCharIsPressed(turnLeftChar)==false) {
			setLeft(false);
		}
		if(Game.keyChecker.checkIfCharIsPressed(turnRightChar)==false) {
			setRight(false);
			
		}
		if(Game.keyChecker.isLeftMousePressed()==false) {
			setShootForInteractiveAtts(false);
		}

		
	}


	
	
	public void updatePlayer() {
		((MagazineAttachment)getAttachments()[3]).rotateToCorner(((MagazineAttachment)getAttachments()[3]).getAimCorner());
	}
	
	public void setPlayerAimCorner(double x, double y) {
		((MagazineAttachment)getAttachments()[3]).setAimCorner(x,y);
	}
	
	
	public static Player makeNewPlayer(double[] rp) {
		Player p;
		ObjectAttachment attachment1;
		ObjectAttachment attachment2;
		ObjectAttachment attachment3;
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
	    
	    Corner l1 = new Corner(new double[] {rp[0] - 20,rp[1]+50}, rp);
	    Corner l2 = new Corner(new double[] {rp[0] - 10, rp[1]+65}, rp);
	    Corner r2 = new Corner(new double[] {rp[0] + 10,  rp[1]+65}, rp);
	    Corner r1 = new Corner(new double[] {rp[0] + 20,  rp[1]+50}, rp);
	    
	    attachment3 = new ObjectAttachment(new Corner[] {l1,l2,r2,r1}, rp,new double[] {rp[0],rp[1]},-5);
	    
	    

//	    straightLine = new ObjectAttachment(new Corner[] {new Corner(new double[] {rp[0] ,rp[1] + 25}, rp), new Corner(new double[] {rp[0] ,rp[1] + 350}, rp),}, rp,new double[] {rp[0],rp[1]-25},-5);
	    Corner b1 = new Corner(new double[] {rp[0] - 6,rp[1] + 5}, rp);
	    Corner b2 =	new Corner(new double[] {rp[0] + 6,rp[1] + 5}, rp);
	    Corner b3 = new Corner(new double[] {rp[0] + 6,rp[1] + 40}, rp);
	    Corner b4 = new Corner(new double[] {rp[0] - 6,rp[1] + 40}, rp);

		
	    canon = new MagazineAttachment(new Corner[] {b1,b2,b3,b4}, new Corner(rp) , new double[] {rp[0], rp[1] + 5}, 0, wp, 0,0);
	    canon.setMagazineParameters(5, 60);
	    canon.setAttRangle(5);
	    canon.setRotateWithParentOb(false);
	//    canon.setRotationSegment(new double[] {-220,220});
	    
	    
	    Corner rightTCorner = new Corner(new double[] {rp[0] - 20, rp[1] - 50}, rp);
	    Corner leftTCorner = new Corner(new double[] {rp[0] + 20, rp[1] - 50}, rp);
	    Corner rightBCorner = new Corner(new double[] {rp[0] - 20, rp[1] + 50}, rp);
	    Corner leftBCorner = new Corner(new double[] {rp[0] + 20, rp[1] + 50}, rp);
	   
	    p = new Player(new Corner[] {leftTCorner,leftBCorner,rightBCorner,rightTCorner},rp, 1, new Corner(new double[] {rp[0],rp[1]+25}, rp));
	    p.addAttachment(attachment1);	    
	    p.addAttachment(attachment2);	  
	    p.addAttachment(attachment3);
	    p.setHP(50);
	    p.setReflectedSpeed(6);
	    p.addAttachment(canon);
	    p.setReflectedLenght(20);
//	    p.addAttachment(straightLine);

	    
	    return p;
	}
		
	
}
