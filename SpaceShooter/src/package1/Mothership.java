package package1;

public class Mothership extends Summoner{
	

	public Mothership(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,
			Corner goalDestination) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination);
		// TODO Auto-generated constructor stub
	}
	
	public static Mothership  makeNewMothership(double x, double y, GameObject[] enemys) {
		Mothership ai;
		
		Corner summoningPoint1 = new Corner(new double[] {x-65,y+65}, new double[] {x,y});
		Corner summoningPoint2 = new Corner(new double[] {x+65,y+65}, new double[] {x,y});
		Corner summoningPoint3 = new Corner(new double[] {x-65,y-65}, new double[] {x,y});
		Corner summoningPoint4 = new Corner(new double[] {x+65,y-65}, new double[] {x,y});

		Corner bo1 = new Corner(new double[] {x , y + -50}, new double[] {x,y});
		Corner bo2 = new Corner(new double[] {x + -50, y }, new double[] {x,y});
		Corner bo3 = new Corner(new double[] {x, y + 50}, new double[] {x,y});
		Corner bo4 = new Corner(new double[] {x + 50, y}, new double[] {x,y});
				
		Corner[] body = new Corner[]  {bo1,bo2,bo3,bo4};
		
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
	    
	    Corner[] attBody = GameObject.generatePeriodicObject(15, 4, new Corner(new double[] {x ,y-25})).getCorners();
	    Corner[] attBody1 = GameObject.generatePeriodicObject(15, 4, new Corner(new double[] {x ,y+25})).getCorners();
	    Corner[] attBody2 = GameObject.generatePeriodicObject(15, 4, new Corner(new double[] {x-25 ,y})).getCorners();
	    Corner[] attBody3 = GameObject.generatePeriodicObject(15, 4, new Corner(new double[] {x+25 ,y})).getCorners();
	    
	    for(Corner c : attBody) {
	    	c.setToNewRP(new double[] {x ,y});
	    }
	    for(Corner c : attBody1) {
	    	c.setToNewRP(new double[] {x ,y});
	    }
	    for(Corner c : attBody2) {
	    	c.setToNewRP(new double[] {x ,y});
	    }
	    for(Corner c : attBody3) {
	    	c.setToNewRP(new double[] {x ,y});
	    }
	    
	    ObjectAttachment att = new ObjectAttachment(attBody,new Corner(new double[] {x ,y}), new double[] {x ,y-25} ,0);
	    ObjectAttachment att1 = new ObjectAttachment(attBody1,new Corner(new double[] {x,y}),new double[] {x ,y+25} ,0);
	    ObjectAttachment att2 = new ObjectAttachment(attBody2,new Corner(new double[] {x,y}),new double[] {x-25 ,y} ,0);
	    ObjectAttachment att3 = new ObjectAttachment(attBody3,new Corner(new double[] {x,y}),new double[] {x+25 ,y} ,0);
	   
	   
		ai = new Mothership(body, new double[] {x,y}, 1, md, gd);
		ai.setSummoningPoint(new Corner[] {summoningPoint1,summoningPoint2,summoningPoint3,summoningPoint4});
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.setMaxSpeed(1.5);
	    ai.setReflectedSpeed(6);
	    ai.setStoppingDistance(800);
	    ai.addAttachment(att);
	    ai.addAttachment(att1);
	    ai.addAttachment(att2);
	    ai.addAttachment(att3);
	    ai.setStoppingDistance(600);
	    ai.setHP(12);
	    ai.getClosestEnemy(enemys);
	    ai.setSummoningSpeed(200);
	    ai.setPlayerFocus(true);
	    return ai;
	}
	
	public void rotateOb() {
		super.rotateWithoutObject();
		
	}
	
}
