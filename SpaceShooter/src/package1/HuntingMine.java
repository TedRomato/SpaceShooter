package package1;

public class HuntingMine extends AI{
	
	public HuntingMine() {
		super()
	}
	
	public static HuntingMine makeNewHuntingMine(double x, double y) {
		//ai
		Corner peakAI = new Corner(new double[] {x,y - 30}, new double[] {x ,y});
		Corner botAI = new Corner(new double[] {x,y + 30}, new double[] {x ,y});
	    Corner rightCornerAI = new Corner(new double[] {x-30,y}, new double[] {x ,y});
	    Corner leftCornerAI = new Corner(new double[] {x+30,y}, new double[] {x ,y});
	    
	    //att
	    Corner peakAIAt = new Corner(new double[] {x,y - 20}, new double[] {x ,y});
		Corner botAIAt = new Corner(new double[] {x,y + 20}, new double[] {x ,y});
	    Corner rightCornerAIAt = new Corner(new double[] {x-20,y}, new double[] {x ,y});
	    Corner leftCornerAIAt = new Corner(new double[] {x+20,y}, new double[] {x ,y});
	    //Hmatove vousky
	    Corner base1 = new Corner(new double[] {x,y + 25}, new double[] {x ,y});
	    Corner base2 = new Corner(new double[] {x-55,y-25}, new double[] {x ,y});
	    Corner base3 = new Corner(new double[] {x+55,y-25}, new double[] {x ,y});
	    Corner base4 = new Corner(new double[] {x-100,y-15}, new double[] {x ,y});
	    Corner base5 = new Corner(new double[] {x+100,y-15}, new double[] {x ,y});
	    Corner basePeak = new Corner(new double[] {x,y+205}, new double[] {x ,y}); 
	    Corner rightP = new Corner(new double[] {x+45,y+205}, new double[] {x ,y});
	    Corner leftP = new Corner(new double[] {x-45,y+205}, new double[] {x ,y});
	    Corner rightP2 = new Corner(new double[] {x+60,y+175}, new double[] {x ,y});
	    Corner leftP2 = new Corner(new double[] {x-60,y+175}, new double[] {x ,y});
	    //dl
	    DetectionLine mdl = new DetectionLine(base1, basePeak, new double[] {x ,y}, 0.5);
	    DetectionLine ldl = new DetectionLine(base2, leftP, new double[] {x ,y}, 0.5);
	    DetectionLine rdl = new DetectionLine(base3, rightP, new double[] {x ,y}, 0.5);
	    DetectionLine ldl2 = new DetectionLine(base4, leftP2, new double[] {x ,y}, 0.5);
	    DetectionLine rdl2 = new DetectionLine(base5, rightP2, new double[] {x ,y}, 0.5);
	    AI ai = new AI(new Corner[] {peakAI, rightCornerAI, leftCornerAI}, new double[] {x,y}, 0.5, new Corner(new double[] {x,y+25}, new double[] {x,y}), goalCorner);
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.setMaxSpeed(1.2);
	    return ai;
	}
}
