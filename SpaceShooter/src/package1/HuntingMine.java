package package1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class HuntingMine extends AI{
	
	BufferedImage Att, Body;
	
	public HuntingMine(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,Corner goalDestination, int powerLvl) {
		super(corners,rotationPoint,rotationAngle,md,goalDestination, powerLvl);
		strenght = 1;

		
		try {
			Att = ImageIO.read(new File("src/Icons/HuntingMineAtt.png"));
			Att = resize(Att,210, 210);
			Body = ImageIO.read(new File("src/Icons/HuntingMineBody.png"));
			Body = resize(Body,125, 125);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static HuntingMine makeNewHuntingMine(double x, double y, GameObject[] gameObjects, int powerLvl) {
		//ai
		Corner peakAI = new Corner(new double[] {x,y - 22}, new double[] {x ,y});
		Corner botAI = new Corner(new double[] {x,y + 22}, new double[] {x ,y});
	    Corner rightCornerAI = new Corner(new double[] {x-22,y}, new double[] {x ,y});
	    Corner leftCornerAI = new Corner(new double[] {x+22,y}, new double[] {x ,y});
	    
	    //att
	    Corner peakAIAt = new Corner(new double[] {x,y - 12}, new double[] {x ,y});
		Corner botAIAt = new Corner(new double[] {x,y + 12}, new double[] {x ,y});
	    Corner rightCornerAIAt = new Corner(new double[] {x-12,y}, new double[] {x ,y});
	    Corner leftCornerAIAt = new Corner(new double[] {x+12,y}, new double[] {x ,y});
	    ObjectAttachment att = new ObjectAttachment(new Corner[] {peakAIAt,rightCornerAIAt,botAIAt,leftCornerAIAt}, new double[] {x ,y}, new double[] {x ,y}, 4);
	    att.setRotateWithParentOb(false);
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
		HuntingMine ai = new HuntingMine(new Corner[] {peakAI, rightCornerAI,botAI, leftCornerAI}, new double[] {x,y}, 4, new Corner(new double[] {x,y+25}, new double[] {x,y}), goalCorner, powerLvl);
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.setMaxSpeed(5+powerLvl);
	    ai.addAttachment(att);
	    ai.setHP(1);
	    ai.findAndSetToClosestEnemy(gameObjects);
	    ai.setPlayerFocus(true);
	    return ai;
	}
	
	public void rotateOb() {
		super.rotateWithoutObject();
		
	}
	
	public void moveOb() {
		super.moveOb();
		getAttachments()[0].rotateAttachmentAroundItsCorner(4);		
	}
	
	public void render(Graphics g) {
	/*	Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		rotateImage(g2, Body, 0,this.getRotationPoint(),58,55);		
		rotateImage(g2, Att,  getAttachments()[0].getAttachmentAngleRotated(), getAttachments()[0].getRotationPoint(),90,80);		*/
		super.render(g);
	}
	
	
}
