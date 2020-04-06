package package1;

import java.awt.Graphics;

public class Summoner extends AI{
	double w8Length = 50;
	double summonTimer = w8Length;
	boolean onCooldown = false;
	Corner[] summoningDestinations;
	double runningDistance = 400;

	public Summoner(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, Corner goalDestination) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination);
		// TODO Auto-generated constructor stub
	}
	
	public void updateAI(GameObject[] enemys, GameObject[] gos, AI[] ais) {
		runIfTooClose(getTargetedEnemy());
		super.updateAI(enemys, gos, ais);
		stopIfTooClose(getTargetedEnemy());
	}
	
	public void stopIfTooClose(GameObject enemy) {
		if(this.getRotationPoint().getPointDistance(enemy.getRotationPoint()) < stoppingDistance && this.getRotationPoint().getPointDistance(enemy.getRotationPoint()) > runningDistance) {
			setForward(false);
		}else {
			setForward(true);
		}
	}
	
	public void runIfTooClose(GameObject p) {
		if(p.getRotationPoint().getPointDistance(getRotationPoint()) < 500) {
			Corner newGD = new Corner(p.getRotationPoint(), p.getRotationPoint());
			newGD.turnAround('b', getRotationPoint());
			setGoalDestination(newGD);
		}
	}
	
	public AI handleSummoner(GameObject[] enemys) {
		updateTimer();
		if(onCooldown == false) {
			onCooldown = true;
			return summonAI(enemys);
		}else {
			return null;
		}
	}
	
	public AI summonAI(GameObject[] enemys) {
		int whichOne = (int) (Math.floor(Math.random()*4));
		HuntingMine ai = HuntingMine.makeNewHuntingMine(summoningDestinations[whichOne].getX(), summoningDestinations[whichOne].getY(), enemys);
		return ai;
	}
	
	public void moveOb() {
		super.moveOb();
		for(Corner x : summoningDestinations) {
			x.moveCorner(getVelX(), getVelY());
		}
	}
	
	public void rotateOb() {
		super.rotateOb();
		for(Corner x : summoningDestinations) {
			x.rotateCorner(getRotationPoint(), getRotationAngle());
		}
	}
	public void rotateWithoutObject(){
		super.rotateWithoutObject();
	}
	
	public void setSummoningPoint(Corner[] summoningP) {
		summoningDestinations = summoningP;
	}
	
	private void updateTimer() {
		if(onCooldown) {
			summonTimer--;
			if(summonTimer <= 0){
				summonTimer = w8Length;
				onCooldown=false;
			}
		}
	}
	
	public void setSummoningSpeed(int i) {
		w8Length = i;
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		
	}
}
