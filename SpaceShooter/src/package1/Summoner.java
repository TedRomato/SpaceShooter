package package1;

import java.awt.Graphics;

public class Summoner extends AI{
	double w8Length = 100;
	double summonTimer = w8Length;
	boolean onCooldown = false;
	Corner[] summoningDestinations;
	double runningDistance = 400;

	public Summoner(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, Corner goalDestination) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination);
		// TODO Auto-generated constructor stub
	}
	
	public void updateAI(Player p, GameObject[] gos) {
		runIfTooClose(p);
		super.updateAI(p, gos);
		stopIfTooClose(p);
	}
	
	public void stopIfTooClose(GameObject enemy) {
		if(this.getRotationPoint().getPointDistance(enemy.getRotationPoint()) < stoppingDistance && this.getRotationPoint().getPointDistance(enemy.getRotationPoint()) > runningDistance) {
			setForward(false);
		}else {
			setForward(true);
		}
	}
	
	public void runIfTooClose(Player p) {
		if(p.getRotationPoint().getPointDistance(getRotationPoint()) < 500) {
			Corner newGD = new Corner(p.getRotationPoint(), p.getRotationPoint());
			newGD.turnAround('b', getRotationPoint());
			setGoalDestination(newGD);
		}
	}
	
	public AI handleSummoner() {
		updateTimer();
		if(onCooldown == false) {
			onCooldown = true;
			return summonAI();
		}else {
			return null;
		}
	}
	
	public AI summonAI() {
		int whichOne = (int) (Math.floor(Math.random()*4));
		HuntingMine ai = HuntingMine.makeNewHuntingMine(summoningDestinations[whichOne].getX(), summoningDestinations[whichOne].getY());
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
	
	public void render(Graphics g) {
		super.render(g);
		
		
	}
}
