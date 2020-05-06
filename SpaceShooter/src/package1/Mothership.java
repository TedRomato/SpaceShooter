package package1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Mothership extends Summoner{
	
	
	BufferedImage Body;
	
	public Mothership(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,
			Corner goalDestination) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination);
		try {

			Body = ImageIO.read(new File("src/Icons/MothershipBody.png"));
			Body = Game.resize(Body,180, 180);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	
	public static Mothership  makeNewMothership(double x, double y, GameObject[] gameObjects) {
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
	     
		ai = new Mothership(body, new double[] {x,y}, 1, md, gd);
		ai.setSummoningPoint(new Corner[] {summoningPoint1,summoningPoint2,summoningPoint3,summoningPoint4});
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});
	    ai.setMaxSpeed(1.5);
	    ai.setReflectedSpeed(6);
	    ai.setStoppingDistance(800);

	    ai.setStoppingDistance(600);
	    ai.setHP(12);
	    ai.findAndSetToClosestEnemy(gameObjects);
	    ai.setSummoningSpeed(200);
	    ai.setPlayerFocus(true);
	    return ai;
	}
	
	public void rotateOb() {
		super.rotateWithoutObject();
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//Game.rotateImage(g2, Body, 0,this.getRotationPoint(),80,80);		
		super.render(g);
	}
}
