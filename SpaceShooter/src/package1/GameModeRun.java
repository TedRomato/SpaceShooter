package package1;

public class GameModeRun extends Game{
	
	Tower[] towers = new Tower[] {};
	int  towersIndex = arrayList.length; //12
	Grenade[] mines = new Grenade[] {};
	int minesIndex = arrayList.length+1; //13
	
	int towerAmount= 4;
	int aiAmount = 3;
	int mineAmount = 12;
	int meteorAmount = 8;
	
	double towerTimer = 0;
	double aiTimer = 0;
	double mineTimer = 0;
	double meteorTimer = 0;
	int pause = 60;
	
	
	int bonusSpawnLenght = 400;
	double bonusSpawnTimer = 0;
	
	
	double timeSurvived = 0;
	
	
	double zoneSpeed = 4*Game.tickMultiply;
	

	public GameModeRun(int sw, int sh, boolean softBorder) {
		super(sw, sh, softBorder);
		
		aiSpawningRange = new int[] {0,50};

		safeZoneHeight = 3500;
		safeZoneCorner = new Corner(new double[] {0, - 500});
		setRemoveSquareHeight(safeZoneHeight + getRemoveSquareBlock() + 200);
		setRemoveSquareWidth(safeZoneWidth + 2*getRemoveSquareBlock());
		setSpawnBlockCorner(new Corner(new double[] {0,-500}));
		setSpawnBlockHeight(safeZoneHeight / 3);
		setSpawnBlockRange(new int[] {50,60});
		
	}
	
	public void moveSoftBorders(double velX, double velY) {
		safeZoneCorner.moveCorner(velX, velY);
	}
	
	public void adjustSafeZone() {
		if(getDifference(p.getRotationPoint().getY(), safeZoneCorner.getY()) < 2000) {
			safeZoneCorner.moveCorner(0, -100);;
		}
	}
	
	public void adjustSpawnBlock() {
		getSpawnBlockCorner().moveCorner(0, p.getRotationPoint().getY() - 3000 - getSpawnBlockCorner().getY());
		
	}
	
	public void adjustRemoveSquare() {
		setRemoveSquareCornerY((int) (safeZoneCorner.getY()) - getRemoveSquareBlock());

	}
	
	public void tick() {
		super.tick();
		moveSoftBorders(0,-zoneSpeed);
		returnToOrigin();
		adjustSafeZone();
		adjustRemoveSquare();
		adjustSpawnBlock();
		handleSpawning();
		updateTimers();
		handleTowers();
		updateTime();
		updateGameAccordingToTimeSurvived();

	}
	
	public void updateGameAccordingToTimeSurvived() {
		towerAmount = (int) (timeSurvived / 2000) + 3;
		aiAmount = (int) (timeSurvived / 2000);
		mineAmount = (int) (timeSurvived / 800) + 5;
		meteorAmount = (int) (timeSurvived / 1000) + 5;
	}
			
	
	public void updateTime() {
		timeSurvived += Game.tickOne;
	}

	public void returnToOrigin() {
		if(safeZoneCorner.getY() < -3000) {
			moveAll(0,3000);
			safeZoneCorner.moveCorner(0, 3000);
			camera.setCameraToCorner(p.getRotationPoint());

		}
	}
	public void moveAll(int velX,int velY) {
		for(GameObject g : objects) {
			g.moveOb(velX, velY);
		}
	}
	
	public void handleTowers() {
		for(Tower t : towers) {
			t.updateAllTurrets(new GameObject[] {p});
		}
	}
	
	
	public void handleSpawning() {
		
		if(towers.length < towerAmount && towerTimer > pause) {
			towerTimer= 0;
			spawnTower();
		}
		
	
		if(ais.length< aiAmount && aiTimer > pause) {
			aiTimer = 0;
			spawnAI();
		} 
		
		
		if(mines.length < mineAmount && mineTimer > pause) {
			mineTimer = 0;
			spawnMine();
		}
		
		
		if(meteorTimer > pause){
			meteorTimer = 0;
			respawnMeteorsToAmount(meteorAmount);
		}
		
		if(bonusSpawnTimer >= bonusSpawnLenght) {
			bonusSpawnTimer = 0;
			spawnBonus();
		}
		
		
	}
	
	
	public void spawnAI() {
		super.spawnAI((int)GameObject.generateNumInRange(new double[] {1,5}),(int)GameObject.generateNumInRange(new double[] {1,3}),true);
	
	}
	
	public void spawnBonus() {	
		Corner rp;
		PlayerBonus b= null;
		boolean done = false;
		int i = 0;
		while(!done) {
			rp = GameObject.generateCornerInRect(getSpawnBlockCorner().getX(), getSpawnBlockCorner().getY(), getSpawnBlockWidth(), getSpawnBlockHeight());
			b = PlayerBonus.makeNewPlayerBonus(rp.getX(), rp.getY());
			done = checkIfSpawnCollision(b);
			if(i > 20) {
				return;
			}
			i++;

		}
		
		
		addObToRun(b, new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13});
	}
	
	public void spawnTower() {
		Corner rp;
		Tower t= null;
		boolean done = false;
		int i = 0;
		while(!done) {
			rp = GameObject.generateCornerInRect(getSpawnBlockCorner().getX(), getSpawnBlockCorner().getY(), getSpawnBlockWidth(), getSpawnBlockHeight());
			t = Tower.makeNewTower(rp.getX(), rp.getY());
			done = checkIfSpawnCollision(t);
			if(i > 20) {
				return;
			}
			i++;

		}
		
		t.addTurret();
		t.applyRandomUpgrade(40);
		
		addObToRun(t, new int[] {1,3,6,7,9,10,11,13});
	}
	
	
	
	public void spawnMine() {
		Corner rp;
		Grenade t= null;
		boolean done = false;
		int i = 0;
		while(!done) {
			rp = GameObject.generateCornerInRect(getSpawnBlockCorner().getX(), getSpawnBlockCorner().getY(), getSpawnBlockWidth(), getSpawnBlockHeight());
			t = Grenade.makeNewGrenade(rp.getX(), rp.getY(), new Corner(new double[] {0,0}));
			done = checkIfSpawnCollision(t);
			checkIfSpawnCollision(t);
			if(i > 20) {
				return;
			}
			i++;

			
		}
		t.setHP(4);
		t.setCurrentSpeed(0);
		addObToRun(t, new int[] {1,3,6,7,8,9,10,12});
	}
	
	
	protected void addObToRun(GameObject ob,int[] exclude) {
		super.addObToGame(ob, exclude);
		boolean exlcudeFromMines = false;;
		boolean excludeFromTowers = false;
		for(int i : exclude) {
			if(i == minesIndex) {
				exlcudeFromMines = true;
			}
			else if(i == towersIndex) {
				excludeFromTowers = true;
			}
		}
		if(!exlcudeFromMines) {
			mines = makeGamObArGrenadeAr(makeNewArrayWith(mines, ob));
		}
		if(!excludeFromTowers) {
			towers = makeGamObArTowerAr(makeNewArrayWith(towers, ob));
		}
	}
	
	protected void removeObFromGame(GameObject ob){
		super.removeObFromGame(ob);
		for(int i = 0; i < towers.length; i++) {
			if(ob == towers[i]) {
				towers = makeGamObArTowerAr(makeNewArrayWithout(towers, i));
			}
		}
		for(int i = 0; i < mines.length; i++) {
			if(ob == mines[i] ) {
				mines = makeGamObArGrenadeAr(makeNewArrayWithout(mines,i));
			}
			
		}
	}
	
	public void updateTimers() {
		towerTimer+=Game.tickOne;
		aiTimer+=Game.tickOne;
		mineTimer+=Game.tickOne;
		meteorTimer+=Game.tickOne;
		bonusSpawnTimer += Game.tickOne;
	}
	
	

	
	
	
	
			
}
