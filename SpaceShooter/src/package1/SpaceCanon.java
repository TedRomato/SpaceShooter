package package1;

public class SpaceCanon extends AI{
	public SpaceCanon(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,Corner goalDestination) {
		super(corners,rotationPoint,rotationAngle,md,goalDestination);
		
	}
	
	public static SpaceCanon makeNewSpaceCanon(double x, double y) {
		//ai
		Corner[] corners = GameObject.generatePeriodicObject(30, 8, new Corner(new double[] {x,y})).getCorners();
	    
	    //att
	    Corner peakAIAt = new Corner(new double[] {x-14,y -14}, new double[] {x ,y});
		Corner botAIAt = new Corner(new double[] {x-14,y +14}, new double[] {x ,y});
		Corner canonAt1 = new Corner(new double[] {x-8,y + 14}, new double[] {x ,y});
		Corner canonAt2 = new Corner(new double[] {x-8,y + 50}, new double[] {x ,y});
		Corner canonAt3 = new Corner(new double[] {x+8,y + 50}, new double[] {x ,y});
		Corner canonAt4 = new Corner(new double[] {x+8,y + 14}, new double[] {x ,y});
	    Corner rightCornerAIAt = new Corner(new double[] {x+14,y+14}, new double[] {x ,y});
	    Corner leftCornerAIAt = new Corner(new double[] {x+14,y-14}, new double[] {x ,y});
	    Corner wayPoint = new Corner(new double[] {x,y + 60}, new double[] {x ,y});
	    InteractiveAttachment att = new InteractiveAttachment(new Corner[] {peakAIAt,botAIAt,canonAt1,canonAt2,canonAt3,canonAt4,rightCornerAIAt,leftCornerAIAt},new Corner(new double[] {x ,y}), new double[] {x ,y}, 1, wayPoint, 600, 45);
	    att.setRotateWithParentOb(false);
	    att.setAttRangle(2);
	    att.setReloadTimer(120);
	    att.setDmg(2);
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
	    SpaceCanon ai = new SpaceCanon(corners, new double[] {x,y}, 5, new Corner(new double[] {x,y+25}, new double[] {x,y}), goalCorner);
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.setMaxSpeed(3.5);
	    ai.addAttachment(att);
	    ai.setHP(4);
	    ai.setAcceleration(0.1);
	    ai.setReflectedSpeed(6);
	    ai.setStoppingDistance(600);
	    ai.setShootForInteractiveAtts(true);
	    
	    return ai;
	}
	
	public void updateAI(Player p, GameObject[] gos, AI[] ais) {
		((InteractiveAttachment)this.getAttachments()[0]).rotateToCorner(p.getRotationPoint());
		if(isInStoppingDistance()) {
		    setShootForInteractiveAtts(true);
		}
		super.updateAI(p, gos, ais);
		stopIfTooClose(p);

	}
	
	public void rotateOb() {
		super.rotateWithoutObject();
		
	}
	
	public void moveOb() {
		super.moveOb();
				


	}
}
