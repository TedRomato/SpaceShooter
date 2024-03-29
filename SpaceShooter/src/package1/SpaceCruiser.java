package package1;

public class SpaceCruiser extends LongRangeAI{

	public SpaceCruiser(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,
			Corner goalDestination, Corner wayPoint, int powerLvl) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination, wayPoint, powerLvl);
		// TODO Auto-generated constructor stub
		strenght = 5;
		setMoneyDropped(100);


	}
	
	public void updateInSD(GameObject[] enemys, GameObject[] gos, AI[] ais) {
		handleTurretFire(getTargetedEnemy().getRotationPoint().getPointDistance(this.getRotationPoint()));
		updateAllAimCorners(getTargetedEnemy());
		checkAndHandleTrack(gos);
		if(collisionDanger == false) {
			setGoalDestination(((InteractiveAttachment) getAttachments()[0]).getAimCorner());
		}
		updateIsInStoppingDistance(getTargetedEnemy());
		updateForward();
		rotateToCorner(getTargetedEnemy().getRotationPoint());
		stopIfCollisionDanger();
		handleAllFriendlyFire(ais);
		
	}
	public void updateAI(GameObject[] enemys, GameObject[] gos, AI[] ais) {
		super.updateAI(enemys, gos, ais);
		handleTurrets();

	}
	
	
	
	public void handleTurretFire(double d){
		if(d > 550) {
			((InteractiveAttachment) getAttachments()[0]).setShoot(true);
			((InteractiveAttachment) getAttachments()[1]).setShoot(false);
			((InteractiveAttachment) getAttachments()[2]).setShoot(false);
		}else {
			((InteractiveAttachment) getAttachments()[0]).setShoot(false);
			((InteractiveAttachment) getAttachments()[1]).setShoot(true);
			((InteractiveAttachment) getAttachments()[2]).setShoot(true);
		}
	}
	
	
	
	public void handleTurrets() {
		for(int i = 0; i < 3; i++) {
			((InteractiveAttachment) getAttachments()[i]).rotateToCorner(((InteractiveAttachment) getAttachments()[i]).getAimCorner());
		}
	}
	
	public static SpaceCruiser makeNewSpaceCruiser(double x, double y, GameObject[] gameObjects, int powerLvl) {
		SpaceCruiser ai;
		//H1
		Corner h11 = new Corner(new double[] {x + -50, y + -10}, new double[] {x,y});
		Corner h12 = new Corner(new double[] {x + -70, y + -30}, new double[] {x,y});
		Corner h13 = new Corner(new double[] {x + -90, y + -30}, new double[] {x,y});
		Corner h14 = new Corner(new double[] {x + -110, y + -10}, new double[] {x,y});
		Corner h15 = new Corner(new double[] {x + -110, y + 10}, new double[] {x,y});
		Corner h16 = new Corner(new double[] {x + -90, y + 30}, new double[] {x,y});
		Corner h17 = new Corner(new double[] {x + -70, y + 30}, new double[] {x,y});
		Corner h18 = new Corner(new double[] {x + -50, y + 10}, new double[] {x,y});
		//H2
		Corner h21 = new Corner(new double[] {x + 50, y + -10}, new double[] {x,y});
		Corner h22 = new Corner(new double[] {x + 70, y + -30}, new double[] {x,y});
		Corner h23 = new Corner(new double[] {x + 90, y + -30}, new double[] {x,y});
		Corner h24 = new Corner(new double[] {x + 110, y + -10}, new double[] {x,y});
		Corner h25 = new Corner(new double[] {x + 110, y + 10}, new double[] {x,y});
		Corner h26 = new Corner(new double[] {x + 90, y + 30}, new double[] {x,y});
		Corner h27 = new Corner(new double[] {x + 70, y + 30}, new double[] {x,y});
		Corner h28 = new Corner(new double[] {x + 50, y + 10}, new double[] {x,y});
		//Bo
		Corner bo1 = new Corner(new double[] {x + -40, y + -10}, new double[] {x,y});
		Corner bo2 = new Corner(new double[] {x + -20, y + -35}, new double[] {x,y});
		Corner bo3 = new Corner(new double[] {x + 20, y + -35}, new double[] {x,y});
		Corner bo4 = new Corner(new double[] {x + 40, y + -10}, new double[] {x,y});
		
		Corner bo5 = new Corner(new double[] {x + -40, y + 10}, new double[] {x,y});
		Corner bo6 = new Corner(new double[] {x + -20, y + 35}, new double[] {x,y});
		Corner bo9 = new Corner(new double[] {x + 20, y + 35}, new double[] {x,y});
		Corner bo10 = new Corner(new double[] {x + 40, y + 10}, new double[] {x,y});
		
		Corner[] body = new Corner[]  {h11,h12,h13,h14,h15,h16,h17,h18,bo5,bo6,bo9,bo10,h28,h27,h26,h25,h24,h23,h22,h21,bo4,bo3,bo2,bo1};
		
		Corner md = new Corner(new double[] {x, y + 50}, new double[] {x,y});
		Corner wp = new Corner(new double[] {x, y + 50}, new double[] {x,y});
		Corner gd = new Corner(new double[] {1000,600});
		
		//hmatove vousky
		Corner base1 = new Corner(new double[] {x,y + 40}, new double[] {x ,y});
	    Corner base2 = new Corner(new double[] {x-25,y+30}, new double[] {x ,y});
	    Corner base3 = new Corner(new double[] {x+25,y+30}, new double[] {x ,y});
	    Corner base4 = new Corner(new double[] {x-70,y+10}, new double[] {x ,y});
	    Corner base5 = new Corner(new double[] {x+70,y+10}, new double[] {x ,y});
	    Corner basePeak = new Corner(new double[] {x,y+205}, new double[] {x ,y}); 
	    Corner rightP = new Corner(new double[] {x+45,y+205}, new double[] {x ,y});
	    Corner leftP = new Corner(new double[] {x-45,y+205}, new double[] {x ,y});
	    Corner rightP2 = new Corner(new double[] {x+100,y+175}, new double[] {x ,y});
	    Corner leftP2 = new Corner(new double[] {x-100,y+175}, new double[] {x ,y});
	    
	    DetectionLine mdl = new DetectionLine(base1, basePeak, new double[] {x ,y}, 1);
	    DetectionLine ldl = new DetectionLine(base2, leftP, new double[] {x ,y}, 1);
	    DetectionLine rdl = new DetectionLine(base3, rightP, new double[] {x ,y}, 1);
	    DetectionLine ldl2 = new DetectionLine(base4, leftP2, new double[] {x ,y}, 1);
	    DetectionLine rdl2 = new DetectionLine(base5, rightP2, new double[] {x ,y}, 1);
	    
	    //Main Canon
	    InteractiveAttachment mc;
	    Corner c1 = new Corner(new double[] {x-15, y+10},new double[] {x ,y});
	    Corner c2 = new Corner(new double[] {x-11, y+65},new double[] {x ,y});
	    Corner c3 = new Corner(new double[] {x+11, y+65},new double[] {x ,y});
	    Corner c4 = new Corner(new double[] {x+15, y+10},new double[] {x ,y});
	    Corner c5 = new Corner(new double[] {x+5, y+2},new double[] {x ,y});
	    Corner c6 = new Corner(new double[] {x-5, y+2},new double[] {x ,y});
	    Corner[] corners = new Corner[] {c1,c2,c3,c4,c5,c6}; 
	    mc = new InteractiveAttachment(corners, new Corner(new double[] {x ,y}),new double[] {x ,y}, 5, new Corner(new double[] {x, y + 50},new double[] {x, y}),1000,70);
	    mc.setDmg(4+powerLvl*2);
	    mc.setReloadLenght(90);
	    mc.setRotationSegment(new double[] {-60,60});
	    //Side canons 
	    MagazineAttachment canon1;
	    c1 = new Corner(new double[] {x - 90, y -10}, new double[] {x,y});
	    c2 = new Corner(new double[] {x - 70, y-10},new double[] {x ,y});
	    c3 = new Corner(new double[] {x - 70, y+10},new double[] {x ,y});
	    c5 = new Corner(new double[] {x-75, y+10},new double[] {x ,y});
	    c6 = new Corner(new double[] {x-75, y+25},new double[] {x ,y});
	    Corner c7 = new Corner(new double[] {x-85, y+25},new double[] {x ,y});
	    Corner c8 = new Corner(new double[] {x-85, y+10},new double[] {x ,y});
	    c4 = new Corner(new double[] {x - 90, y+10},new double[] {x ,y});
	    
	    corners = new Corner[] {c1,c2,c3,c5,c6,c7,c8,c4};
	    
	    canon1 = new MagazineAttachment(corners, new Corner(new double[] {x,y}), new double[] {x-80,y}, 5, new Corner(new double[] {x - 80, y+50}, new double[] {x,y}), 650, 150);
	    
	    MagazineAttachment canon2;
	    c1 = new Corner(new double[] {x + 90, y -10}, new double[] {x,y});
	    c2 = new Corner(new double[] {x + 70, y-10},new double[] {x ,y});
	    c3 = new Corner(new double[] {x + 70, y+10},new double[] {x ,y});
	    c5 = new Corner(new double[] {x+75, y+10},new double[] {x ,y});
	    c6 = new Corner(new double[] {x+75, y+25},new double[] {x ,y});
	    c7 = new Corner(new double[] {x+85, y+25},new double[] {x ,y});
	    c8 = new Corner(new double[] {x+85, y+10},new double[] {x ,y});
	    c4 = new Corner(new double[] {x + 90, y+10},new double[] {x ,y});
	    
	    corners = new Corner[] {c1,c2,c3,c5,c6,c7,c8,c4};
	    
	    canon2 = new MagazineAttachment(corners, new Corner(new double[] {x,y}), new double[] {x+80,y}, 5, new Corner(new double[] {x + 80, y+50}, new double[] {x,y}), 650,150);
	    canon1.setInaccuracy(100);
	    canon2.setInaccuracy(100);
//	    double[] segment1 = new double[] {-40,200};
//	    double[] segment2 = new double[] {-200,40};
//	    canon2.setRotationSegment(segment2);
	    canon1.setMagazineParameters(10+powerLvl*2,90);
	    canon2.setMagazineParameters(10+powerLvl*2,90);

	    
		ai = new SpaceCruiser(body, new double[] {x,y}, 2.5, md, gd, wp, powerLvl);
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.initialSetMaxSpeed(3.3+powerLvl/3);
	    ai.initialSetReflectedSpeed(6);
	    ai.setStoppingDistance(650);
	    ai.addAttachment(mc);
	    ai.addAttachment(canon1);
	    ai.addAttachment(canon2);
	    ai.setHP(20+powerLvl*2);
	    ai.findAndSetToClosestEnemy(gameObjects);
	    ai.setGoingDistance(400);

	   
	    return ai;
	}
	
	
}
