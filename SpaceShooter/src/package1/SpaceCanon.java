package package1;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpaceCanon extends AI{
	BufferedImage Canon, Body;

	
	public SpaceCanon(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,Corner goalDestination, int powerLvl) {
		super(corners,rotationPoint,rotationAngle,md,goalDestination, powerLvl);
		strenght = 2;

		try {
			Canon = ImageIO.read(new File("src/Icons/CanonCanon.png"));
			Canon = Game.resize(Canon,210, 210);
			Body = ImageIO.read(new File("src/Icons/CanonBody.png"));
			Body = Game.resize(Body,320, 320);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMoneyDropped(20);

	}
	
	
	public static SpaceCanon makeNewSpaceCanon(double x, double y, GameObject[] gameObjects, int powerLvl) {
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
	    InteractiveAttachment att = new InteractiveAttachment(new Corner[] {peakAIAt,botAIAt,canonAt1,canonAt2,canonAt3,canonAt4,rightCornerAIAt,leftCornerAIAt},new Corner(new double[] {x ,y}), new double[] {x ,y}, 0.6, wayPoint, 900, 45);
	    att.setRotateWithParentOb(false);
	    att.initialSetAttRangle(2);
	    att.setReloadLenght(120);
	    att.setDmg(2+powerLvl/3);
	    att.setInaccuracy(100-powerLvl*5);
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
	    SpaceCanon ai = new SpaceCanon(corners, new double[] {x,y}, 5, new Corner(new double[] {x,y+25}, new double[] {x,y}), goalCorner,powerLvl);
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.initialSetMaxSpeed(3.5);
	    ai.addAttachment(att);
	    ai.setHP(5+powerLvl);
	    ai.initialSetAcceleration(0.1);
	    ai.initialSetReflectedSpeed(6);
	    ai.setStoppingDistance(900);
	    ai.setShootForInteractiveAtts(true);
	    ai.findAndSetToClosestEnemy(gameObjects);
	    return ai;
	}
	
	public void updateAI(GameObject[] enemys, GameObject[] gos, AI[] ais) {

		if(isInStoppingDistance()) {
		    setShootForInteractiveAtts(true);
		}
		rotateCanon();
		super.updateAI(enemys, gos, ais);
		stopIfTooClose(getTargetedEnemy());

	}
	
	private void rotateCanon() {
		((InteractiveAttachment) getAttachments()[0]).rotateToCorner(((InteractiveAttachment) getAttachments()[0]).getAimCorner());
	}
	
	public void rotateOb() {
		super.rotateWithoutObject();
		
	}
	
	public void moveOb() {
		super.moveOb();
				
	}
	
	public void render(Graphics g) {
/*		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		rotateImage(g2, Body, 0,this.getRotationPoint(),159,143);		
	
		rotateImage(g2, Canon, ((InteractiveAttachment) getAttachments()[0]).getAttachmentAngleRotated(),((InteractiveAttachment) getAttachments()[0]).getRotationPoint(),109,70);		
*/		super.render(g);

	}
}
