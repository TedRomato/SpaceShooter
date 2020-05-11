package package1;

public class Hunter extends AI{

	public Hunter(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, Corner goalDestination, int powerLvl) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination, powerLvl);
		// TODO Auto-generated constructor stub
		strenght = 3;

	}
	
	public static Hunter  makeNewHunter(double x, double y, GameObject[] gameObjects, int powerLvl) {
				//ai
				Corner leftTop = new Corner(new double[] {x-20,y - 45}, new double[] {x ,y});
				Corner	peak= new Corner(new double[] {x,y + 70}, new double[] {x ,y});
				Corner rightTop = new Corner(new double[] {x+20,y - 45}, new double[] {x ,y});
			    Corner rightBot = new Corner(new double[] {x+15,y + 50}, new double[] {x ,y});
			    Corner leftBot = new Corner(new double[] {x-15,y + 50}, new double[] {x ,y});
			    
			    //att1
			    Corner peakAIAt = new Corner(new double[] {x - 15,y + 50}, new double[] {x ,y});
				Corner botAIAt = new Corner(new double[] {x - 20,y - 35}, new double[] {x ,y});
			    Corner rightCornerAIAt = new Corner(new double[] {x - 35,y - 35}, new double[] {x ,y});
			    ObjectAttachment att = new ObjectAttachment(new Corner[] {peakAIAt,rightCornerAIAt,botAIAt}, new double[] {x ,y}, new double[] {x ,y}, 4);
			    att.setRotateWithParentOb(true);
			    //att2
			    Corner peakAIAt2 = new Corner(new double[] {x + 15,y + 50}, new double[] {x ,y});
				Corner botAIAt2 = new Corner(new double[] {x + 20,y - 35}, new double[] {x ,y});
			    Corner rightCornerAIAt2 = new Corner(new double[] {x + 35,y - 35}, new double[] {x ,y});
			    ObjectAttachment att2 = new ObjectAttachment(new Corner[] {peakAIAt2,rightCornerAIAt2,botAIAt2}, new double[] {x ,y}, new double[] {x ,y}, 4);
			    att2.setRotateWithParentOb(true);
			    //Hmatove vousky
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
			    //dl
			    DetectionLine mdl = new DetectionLine(base1, basePeak, new double[] {x ,y}, 4);
			    DetectionLine ldl = new DetectionLine(base2, leftP, new double[] {x ,y}, 4);
			    DetectionLine rdl = new DetectionLine(base3, rightP, new double[] {x ,y}, 4);
			    DetectionLine ldl2 = new DetectionLine(base4, leftP2, new double[] {x ,y}, 4);
			    DetectionLine rdl2 = new DetectionLine(base5, rightP2, new double[] {x ,y}, 4);
			    Corner goalCorner = new Corner(new double[] {1000,600} );
			    Hunter ai = new Hunter(new Corner[] {rightBot, rightTop, leftTop, leftBot,peak}, new double[] {x,y}, 1.7, new Corner(new double[] {x,y+25}, new double[] {x,y}), goalCorner, powerLvl);
			    
			    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
			    
			    //mainCanon
			    Corner mc1 =new Corner(new double[] {x-6,y + 20}, new double[] {x ,y});
			    Corner mc2 = new Corner(new double[] {x+6,y + 20}, new double[] {x ,y});
			    Corner mc3 = new Corner(new double[] {x+6,y - 10}, new double[] {x ,y});
			    Corner mc4 = new Corner(new double[] {x-6,y - 10}, new double[] {x ,y});
			    Corner wp = new Corner(new double[] {x,y + 20}, new double[] {x ,y});
			    InteractiveAttachment mc = new InteractiveAttachment(new Corner[] {mc1,mc2,mc3,mc4}, new Corner(new double[] {x,y}), new double[] {x ,y}, 0.0, wp, 800.0, 60.0);
			    mc.setDmg(2+powerLvl/3);
			    mc.setRotationSegment(new double[] {-20,20});
			    //sideCanon1
			    Corner sc11 =new Corner(new double[] {x+25,y + 30}, new double[] {x ,y});
			    Corner sc12 = new Corner(new double[] {x+18,y + 30}, new double[] {x ,y});
			    Corner sc13 = new Corner(new double[] {x+18,y + 50}, new double[] {x ,y});
			    Corner sc14 = new Corner(new double[] {x+25,y + 50}, new double[] {x ,y});
			    Corner wp1 = new Corner(new double[] {x+21,y + 50}, new double[] {x ,y});
			    InteractiveAttachment sc1 = new InteractiveAttachment(new Corner[] {sc11,sc12,sc13,sc14}, new Corner(new double[] {x,y}), new double[] {x ,y}, 0.0, wp1, 500.0, 200.0);
			    //sideCanon2
			    Corner sc21 =new Corner(new double[] {x-25,y + 30}, new double[] {x ,y});
			    Corner sc22 = new Corner(new double[] {x-18,y + 30}, new double[] {x ,y});
			    Corner sc23 = new Corner(new double[] {x-18,y + 50}, new double[] {x ,y});
			    Corner sc24 = new Corner(new double[] {x-25,y + 50}, new double[] {x ,y});
			    Corner wp2 = new Corner(new double[] {x-21,y + 50}, new double[] {x ,y});
			    InteractiveAttachment sc2 = new InteractiveAttachment(new Corner[] {sc21,sc22,sc23,sc24}, new Corner(new double[] {x,y}), new double[] {x ,y}, 0.0, wp2, 500.0, 200.0);
			    mc.setReloadLenght(50);
			      
			    sc1.setReloadLenght(30);
			    sc2.setReloadLenght(30);
			    
			    ai.addAttachment(mc);
			    ai.rotateOb(6);
			    ai.addAttachment(sc1);
			    ai.rotateOb(-12);
			    ai.addAttachment(sc2);
			    ai.rotateOb(6);
			    
			    ai.initialSetMaxSpeed(5.5);
			    ai.addAttachment(att);
			    ai.addAttachment(att2);
			    ai.setHP(4+powerLvl);
			    ai.initialSetReflectedSpeed(7);
			    ai.findAndSetToClosestEnemy(gameObjects);
			    ai.setPlayerFocus(true);
			    ai.setStoppingDistance(250);
			    return ai;
	}
	
	public void updateAI(GameObject[] enemys, GameObject[] gos, AI[] ais) {
		if(isInStoppingDistance()) {
		    setShootForInteractiveAtts(true);
		}
		super.updateAI(enemys, gos, ais);
		stopIfTooClose(getTargetedEnemy());
		handleHunterShooting(getTargetedEnemy());

	}
	
	private void handleHunterShooting(GameObject enemy) {
		if(checkIfInDistance(enemy, 900)) {
			((InteractiveAttachment) getAttachments()[0]).rotateToCorner(((InteractiveAttachment) getAttachments()[0]).getAimCorner());
			((InteractiveAttachment) getAttachments()[0]).setShoot(true);
			((InteractiveAttachment) getAttachments()[0]).setDmg(2);
		}else {
			((InteractiveAttachment) getAttachments()[0]).setShoot(false);
		}
		if(checkIfInDistance(enemy, 400)) {
			((InteractiveAttachment) getAttachments()[1]).setShoot(true);
			((InteractiveAttachment) getAttachments()[2]).setShoot(true);
			((InteractiveAttachment) getAttachments()[0]).setReloadLenght(30);
			((InteractiveAttachment) getAttachments()[0]).setDmg(1);

		}else {
			((InteractiveAttachment) getAttachments()[0]).setReloadLenght(50);
			((InteractiveAttachment) getAttachments()[1]).setShoot(false);
			((InteractiveAttachment) getAttachments()[2]).setShoot(false);
		}
		
	}
	
	protected void setGoalToGameObject(GameObject p) {
		if(checkIfInDistance(p, 900)) {
			setGoalToCorner(((InteractiveAttachment)getAttachments()[0]).getAimCorner());
		}else {
			super.setGoalToGameObject(p);
		}
	}

}
