package package1;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
//TODO fixnout meteor odrazy pro malej sklon  -- > at se mi neodrazi spatnym smerem
//TODO udelat metodu add to game at to nemusim pripisovat dovsech listu jak autak

public class Game extends JPanel{
	private int screenWidth, screenHeight;
	private Player p;
	private AI ai, ai2, ai3, ai4, ai5;
	private Meteor pes, les;
	private GameObject[] borders;
	private GameObject[] objects;
	private MovingObject[] reflectableObs;
	private MovingObject[] reflectingObs;
	private LivingObject[] livingObsReflectUpdate;
	private MovingObject[] borderSensitive;
	private AI[] ais;
	private GameObject[][] arrayList;
	private ObjectAttachment attachmentTry;
	private GameObject[] aiVisible;
	//public static JPanel gp = new GamePanel();
	private boolean running = true;
	
	public Game(int sw,int sh) {
		this.screenHeight = sh;
		this.screenWidth = sw;
		
		objects = new GameObject[] {};
	    reflectableObs = new MovingObject[] {};
	    reflectingObs = new MovingObject[] {};
	    livingObsReflectUpdate = new LivingObject[] {};
	    borderSensitive = new MovingObject[] {};
	    aiVisible = new GameObject[] {};
	    ais = new AI[] {};
		arrayList = new GameObject[][] {objects, reflectableObs, reflectingObs, livingObsReflectUpdate, borderSensitive, aiVisible, ais};

		
	
	    ai = AI.makeNewAI(400,400);
	    ai2 = AI.makeNewAI(600, 600);
	    ai3 = AI.makeNewAI(800, 400);
	    ai4 = AI.makeNewAI(800, 100);
	    ai5 = AI.makeNewAI(800, 600);



		Corner peakA = new Corner(new double[] {400,100}, new double[] {400,175});
	    Corner rightCornerA = new Corner(new double[] {390,150}, new double[] {400,175});
	    Corner leftCornerA = new Corner(new double[] {410,150}, new double[] {400,175});
	    attachmentTry = new ObjectAttachment(new Corner[] {peakA, rightCornerA, leftCornerA}, new double[] {400,175},new double[] {400,150},-5);
		//TEST
		Corner peak = new Corner(new double[] {400,200}, new double[] {400,175});
	    Corner rightCorner = new Corner(new double[] {375,150}, new double[] {400,175});
	    Corner leftCorner = new Corner(new double[] {425,150}, new double[] {400,175});
	    p = new Player(new Corner[] {peak, rightCorner, leftCorner},new double[] {400,175}, 1, new Corner(new double[] {400,200}, new double[] {400,175}));
	    p.addAttachment(attachmentTry);
	    //trojuhelnik s vnitrnim rohem
	   /*
	    Corner top = new Corner(new double[] {200,200}, new double[] {200,250});
	    Corner left = new Corner(new double[] {150,250}, new double[] {200,250});
	    Corner right = new Corner(new double[] {250,250}, new double[] {200,250});
	    Corner bot = new Corner(new double[] {200,300}, new double[] {300,275});*/
	    //ctverec
	    
	    
	    Corner rightBotC = new Corner(new double[] {screenWidth,screenHeight}, new double[] {500,400});
	    Corner leftBotC = new Corner(new double[] {0,screenHeight}, new double[] {500,400});
	    Corner rightTopC = new Corner(new double[] {screenWidth,0}, new double[] {500,400});
	    Corner leftTopC = new Corner(new double[] {0,0}, new double[] {500,400});
	    
	    GameObject rightBorder = new GameObject(new Corner[] {rightTopC, rightBotC}, new double[] {500,400}, 0);
	    GameObject leftBorder = new GameObject(new Corner[] {leftTopC, leftBotC}, new double[] {500,400}, 0);
	    GameObject topBorder = new GameObject(new Corner[] {rightTopC, leftTopC}, new double[] {500,400}, 0);
	    GameObject botBorder = new GameObject(new Corner[] {leftBotC, rightBotC}, new double[] {500,400}, 0);
	    
	    borders = new GameObject[] {botBorder,leftBorder,topBorder,rightBorder};
	    
	    Corner leftTop = new Corner(new double[] {450,350}, new double[] {500,400});
	    Corner leftBot = new Corner(new double[] {450,450}, new double[] {500,400});
	    Corner rightBot = new Corner(new double[] {550,450}, new double[] {500,400});
	    Corner rightTop = new Corner(new double[] {550,350}, new double[] {500,400});
	    les = new Meteor(new Corner[] {leftTop, leftBot, rightBot, rightTop},new double[] {500,400}, -0.5, new Corner(new double[] {450,400}, new double[] {500,400}), 0.3, 3);
	    
	    //kosoctverec
	    Corner top = new Corner(new double[] {200,200}, new double[] {200,250});
	    Corner left = new Corner(new double[] {150,250}, new double[] {200,250});
	    Corner right = new Corner(new double[] {250,250}, new double[] {200,250});
	    Corner bot = new Corner(new double[] {200,300}, new double[] {200,250});
	    
	    pes = new Meteor(new Corner[] {top, left, bot, right},new double[] {200,250}, 0.6, new Corner(new double[] {250,300}, new double[] {200,250}), 0.5, 2);
	    
	    addObToGame(les, new int[] {3,6});
	    addObToGame(pes, new int[] {3,6});
	    addObToGame(ai, new int[] {4});
	    addObToGame(ai2, new int[] {4});
	    addObToGame(ai3, new int[] {4});
	    addObToGame(ai4, new int[] {4});
	    addObToGame(ai5, new int[] {4});
	    addObToGame(p, new int[] {5,6});
	/*  objects = new GameObject[] {pes,les,p,ai};
	    reflectableObs = new MovingObject[] {pes,les,p,ai};
	    reflectingObs = new MovingObject[] {pes,les,p,ai};
	    livingObsReflectUpdate = new LivingObject[] {p,ai};
	    borderSensitive = new MovingObject[] {p, les, pes};
	    aiVisible = new GameObject[] {pes,les};
	    
	    */
	    
	    
	    
	}
	public void keyTyped(KeyEvent e) {
		
	}
	public void keyPressed(KeyEvent e) {
	
		p.keyPressed(e);
	}
	public void keyReleased(KeyEvent e) {
		p.keyReleased(e);
	}
	
	
	public void start() {
		long lastTime = System.nanoTime();
        double amountOfTicks = 150;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
        	long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            this.repaint();
            while(delta >=1)
            	{
                tick();
                delta--;
                }
                
                frames++;
                            
                if(System.currentTimeMillis() - timer > 1000)
                {
                	timer += 1000;
            //      System.out.println("FPS: "+ frames);
                    frames = 0;
                    }
        }
	}
	public void stop() {
		if(running) {
			running = false;	
		}
	}
	
	public void tick() {	

		handleAI();
		updateAllObs();
    	checkAndHandleCollision();
    	updateLivingObsReflect();
        checkAndHandleAllRefs();
        deleteNoHpObs();
    	updateAllInvs();
        reflectFromSides();


	}
	
	
	private void updateLivingObsReflect() {
		if(livingObsReflectUpdate != null) {	
			for(LivingObject livingOb : livingObsReflectUpdate) {
				livingOb.updateReflection();
			}
		}
	}
	
	private void handleAI() {
		for(AI ai : ais) {
			ai.updateAI(p, aiVisible);
		}
	}
    	
	//handle reflections from objects with attachments
	private void checkAndHandleAllRefs() {
		for(MovingObject mob : reflectableObs) {
			for(MovingObject ob : reflectingObs) {
				if(mob != ob) {
					mob.checkAndHandleReflect(ob);
					if(ob.getClass().getSimpleName().equals("LivingObject") || ob.getClass().getSimpleName().equals("Player")) {
						if(((LivingObject) ob).getAttachments().length > 0) {
							for(ObjectAttachment att : ((LivingObject) ob).getAttachments()) {
								mob.checkAndHandleReflect(att);
							}
						}
					}
				}
			}
		}
	}
	
	private void checkAndHandleCollision() {
		GameObject[] compareArray = objects; 
		for(int i = 0; i < objects.length; i++) {
			for(int x = 0; x < compareArray.length; x++) {
				
				if(objects[i] != compareArray[x]) {
					
					if(objects[i].checkCollision(compareArray[x])) {
						if(objects[i].getInvulnurability() == false) {
							objects[i].setHP(objects[i].getHP()-1);
							objects[i].startInvulnurability();
						}
					}
				}
	
			}
		}
		
	}
	
	private void updateAllInvs() {
		for(GameObject ob : objects) {
			ob.updateInvulnurability();
		}
	}
	
	private void deleteNoHpObs() {
		for(GameObject ob : objects) {
			if(ob.getHP() <= 0) {
				removeObFromGame(ob);
			}
		}
		
	}
	
	private void reflectFromSides() {
		for(int i = 0; i < borders.length; i++) {
			for(MovingObject go : borderSensitive) {
				if(go.getClass().getSimpleName().equals("Meteor") ) {
					if(go.checkCollision(borders[i])) {
						((Meteor) go).reflectMeteorFromSide(i,go.getRotationPoint());
					}
				}else {
					go.checkAndHandleReflect(borders[i]);
				}
			}
		}
	}
	
	
	private void addObToGame(GameObject ob) {
		for(int i = 0; i < arrayList.length; i++) {
			arrayList[i] =  makeNewArrayWith(arrayList[i], ob);
		}
		fixGameArrays();
	}
	
	private void addObToGame(GameObject ob,int[] exclude) {
		for(int i = 0; i < arrayList.length; i++) {
			boolean excludedArr = false;
			for(int x = 0; x < exclude.length; x++){
				if(exclude[x] == i) {
					excludedArr = true;
				}
			}
			if(excludedArr == false) {
				arrayList[i] =  makeNewArrayWith(arrayList[i], ob);
			}
		}
		fixGameArrays();
	}
	
	
	private void removeObFromGame(GameObject ob){
		for(int i = 0; i < arrayList.length; i++) {
			for(int index = 0; index < arrayList[i].length; index++) {
				if(arrayList[i][index] == ob) {
					arrayList[i] =  makeNewArrayWithout(arrayList[i], index);
				}
			}
		}
		fixGameArrays();
		
	}
	
	private void fixGameArrays() {
		objects = arrayList[0];
	    reflectableObs = makeGameObArMovingArr(arrayList[1]);
	    reflectingObs = makeGameObArMovingArr(arrayList[2]);
	    livingObsReflectUpdate = makeGameObArLivingArr(arrayList[3]);
	    borderSensitive = makeGameObArMovingArr(arrayList[4]);
	    aiVisible = arrayList[5];
	    ais = makeGameObArAIArr(arrayList[6]);
	
	}
	
	private AI[] makeGameObArAIArr(GameObject[] arr) {
		AI[] newArr = new AI[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (AI) arr[i];
		}
		return newArr;
	}
	
	private MovingObject[] makeGameObArMovingArr(GameObject[] arr) {
		MovingObject[] newArr = new MovingObject[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (MovingObject) arr[i];
		}
		return newArr;
	}
	private LivingObject[] makeGameObArLivingArr(GameObject[] arr) {
		LivingObject[] newArr = new LivingObject[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (LivingObject) arr[i];
		}
		return newArr;
	}
	
	private GameObject[] makeNewArrayWith(GameObject[] arr, GameObject ob) {
		if(ob != null) {
			GameObject[] newArray = new GameObject[arr.length+1];
			for(int i = 0; i < arr.length;i++) {
				newArray[i] = arr[i];
			}
			newArray[newArray.length-1] = ob;
			return newArray;
		}
		return arr;
		
	}
		
	private GameObject[] makeNewArrayWithout(GameObject[] array ,int index){
		GameObject[] newArray = new GameObject[array.length - 1];
		
		int add = 0;
		for(int i = 0; i < newArray.length; i++) {
			if(i == index) {
				add = 1;
			}
			newArray[i] = array[i + add];
		}
		return newArray;
	}
	
	private void updateAllObs() {
		for(GameObject go : objects) {
			go.updateOb();
		}
	}
	
	
	
	private void renderAll(Graphics g) {
		if(objects != null) {
			if(objects.length > 0) {
				for(GameObject ob : objects) {
					ob.render(g);
				}
			}
		}
		for(GameObject ob : borders) {
			ob.render(g);
		}
	}
	
	
	
	
	
	public Dimension getPreferredSize() {
		return new Dimension(1920,1080);
	}
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderAll(g2);

	}

	
}