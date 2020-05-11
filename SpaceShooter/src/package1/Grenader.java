package package1;

public class Grenader extends LongRangeAI{
	public Grenader(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, Corner goalDestination, Corner wayPoint, int powerLvl) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination, wayPoint,powerLvl);
		// TODO Auto-generated constructor stub
		strenght = 6;

	}
	
	public void updateInSD(GameObject[] enemys, GameObject[] gos, AI[] ais) {
		super.updateInSD(enemys, gos, ais);
		handleTurrets();
	}

	public void handleTurrets() {
		for(int i = 0; i < 1; i++) {
			((InteractiveAttachment) getAttachments()[i]).rotateToCorner(((InteractiveAttachment) getAttachments()[i]).getAimCorner());
		}
	}
	
	public static Grenader makeNewGrenader(double x, double y, GameObject[] enemys, int powerLvl) {
		Grenader ai;
		ExplosiveShootingAtt canon;
		//H1
		Corner h11 = new Corner(new double[] {x + -60, y + -30}, new double[] {x,y});
		Corner h12 = new Corner(new double[] {x + -90, y + -30}, new double[] {x,y});
		Corner h13 = new Corner(new double[] {x + -60, y + 10}, new double[] {x,y});

		//H2
		Corner h21 = new Corner(new double[] {x + 60, y + -30}, new double[] {x,y});
		Corner h22 = new Corner(new double[] {x + 90, y + -30}, new double[] {x,y});
		Corner h23 = new Corner(new double[] {x + 60, y + 10}, new double[] {x,y});

		//Bo
		Corner bo1 = new Corner(new double[] {x - 60, y - 30}, new double[] {x,y});
		Corner bo2 = new Corner(new double[] {x - 45, y + 30}, new double[] {x,y});
		Corner bo3 = new Corner(new double[] {x + 45, y + 30}, new double[] {x,y});
		Corner bo4 = new Corner(new double[] {x + 60, y - 30}, new double[] {x,y});
		Corner bo5 = new Corner(new double[] {x -60, y + 10}, new double[] {x,y});
		Corner bo6 = new Corner(new double[] {x +60, y + 10}, new double[] {x,y});

		
		Corner[] body = new Corner[]  {bo1,bo5,bo2,bo3,bo6,bo4};
		
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
	    
	    ObjectAttachment k1 =  new ObjectAttachment(new Corner[] {h11,h12,h13}, new double[] {x,y},  new double[] {x,y}, 0);
	    ObjectAttachment k2 =  new ObjectAttachment(new Corner[] {h21,h22,h23}, new double[] {x,y},  new double[] {x,y}, 0);
	    
	    //gl
	    Corner c1 = new Corner(new double[] {x-15, y+10},new double[] {x ,y});
	    Corner c2 = new Corner(new double[] {x-11, y+65},new double[] {x ,y});
	    Corner c3 = new Corner(new double[] {x+11, y+65},new double[] {x ,y});
	    Corner c4 = new Corner(new double[] {x+15, y+10},new double[] {x ,y});
	    Corner c5 = new Corner(new double[] {x+5, y+2},new double[] {x ,y});
	    Corner c6 = new Corner(new double[] {x-5, y+2},new double[] {x ,y});
	    Corner[] corners = new Corner[] {c1,c2,c3,c4,c5,c6}; 
	    canon = new ExplosiveShootingAtt(corners, new Corner(new double[] {x ,y}),new double[] {x ,y}, 5, new Corner(new double[] {x, y + 50},new double[] {x, y}),1200,200);
	    canon.setRotationSegment(new double[] {-60,60});
	    canon.setFireGrenade(true);
	    canon.setInaccuracy(200-powerLvl*15);
	    canon.setMagazineParameters(4, 240);
	    canon.setReloadLenght(100);
	    canon.setDmg(2+powerLvl/3);

	    
		ai = new Grenader(body, new double[] {x,y}, 2, md, gd, wp,powerLvl);
	    ai.addAttachment(canon);
		ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.initialSetMaxSpeed(4.2+powerLvl/3);
	    ai.initialSetReflectedSpeed(6);
	    ai.setStoppingDistance(800);
	    ai.addAttachment(k1);
	    ai.addAttachment(k2);
	    ai.setHP(8+powerLvl);
	    ai.findAndSetToClosestEnemy(enemys);
	    ai.setGoingDistance(600);	   
	    return ai;
	}
}
