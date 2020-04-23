package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.RoundingMode;


//PRESUNOUT METODY LIVING OBJECTU Z HRACE 
public class LivingObject extends MovingObject{
	
	//is able to change its move direction based on move point 
	//does have acceleration and max Speed
	//can have attachments 
	//other than that same methods, but work for attachment as well
	GameObject[] shotImunes = new GameObject[] {};
	boolean solid = true; 
	private boolean forward = false, turnRight = false, turnLeft = false;
	private Corner movePoint;
	private double maxSpeed = 7;
	private double acceleration = maxSpeed/200;
	private ObjectAttachment[] attachments;
	boolean isStunned = false;
	int stunTimer;
	
	public LivingObject(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		movePoint = new Corner(md, rotationPoint);
		setReflectedSpeed(maxSpeed*2);
		setHP(10);
		makeSquare();
		addShotImune(this);
	}
	
	public LivingObject(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		movePoint = new Corner(md, rotationPoint);
		setReflectedSpeed(maxSpeed*2);
		setHP(10);
		makeSquare();	
		addShotImune(this);
		}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;

	}
	
	
	public void updateOb() {
		
		updateStun();
		
		updateSpeed();

		moveOb();

		if(turnRight || turnLeft ) {
			rotateOb();


		}
		
		updateForward();
				
		if(forward) {
			updateMDtoMP();			
			getNewRatios();
			setNewVels();


		}
		
	
	}
	

	private void updateForward() {
		if(getReflected()  == true) {
			forward = false;
		}
	}
	
	public void rotateOb() {
		if(!isStunned) {
			super.rotateOb();
			if(attachments != null) {
				for(ObjectAttachment att : attachments) {
					if(att.getRotateWithParentOb()) {
						att.rotateAttachment(this.getRotationAngle());
					}
		
				}
		
			}
			
			movePoint.rotateCorner(getRotationPoint(), getRotationAngle());	
			}
		}
	public void rotateOb(double angle) {
		super.rotateOb(angle);
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				if(att.getRotateWithParentOb()) {
					att.rotateAttachment(angle);
				}

			}

		}
		
		movePoint.rotateCorner(getRotationPoint(), angle);	
		}
	
	//rotates only mp, sp,sd
	public void rotateWithoutObject() {
		movePoint.rotateCorner(getRotationPoint(), getRotationAngle());	
		
	}
	
	public void moveOb() {
		super.moveOb();
		
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				att.moveAttachment(getVelX(), getVelY());
			}
		}
		
		movePoint.moveCorner(getVelX(),getVelY());
		



	
	}
	
	public  void rotateAttachments() {
		for(ObjectAttachment att : attachments) {
			att.rotateAttachment(att.getRotationAngle());
		}
	}
	
	public void rotateAtachmentsAroundItsCorner() {
		for(ObjectAttachment att : attachments) {
			att.rotateAttachmentAroundItsCorner(att.getRotationAngle());
		}
	}
	
	protected void updateMDtoMP() {
		
		moveDirection = new Corner(movePoint, getRotationPoint());
	
	}
	
	
	//handles acceleration 
	
	public void updateSpeed() {
		if(forward && getCurrentSpeed() < maxSpeed) {
			setCurrentSpeed(getCurrentSpeed() + acceleration);
		}
		if(getCurrentSpeed() > maxSpeed && getReflected() == false) {
			setCurrentSpeed(getCurrentSpeed()- acceleration);
		}
		else if(forward != true && getCurrentSpeed() > 0 - acceleration) {
			setCurrentSpeed(getCurrentSpeed() - acceleration);
			if(getCurrentSpeed() < 0) {
				setCurrentSpeed(0);
			}
			getNewRatios();
			setNewVels();
		}
	}
	
	

	public void updateReflection() {
		if(isReflected()) {
			setForward(false);
			setReflectedTimer(getReflectedTimer() + 1);
			if(getReflectedTimer() >= getReflectedLenght()) {
				setReflected(false);
				setReflectedTimer(0);
			}
		}
	}
	
	//adds attachment to ob
	
	public void addAttachment(ObjectAttachment att) {
		if(attachments == null) {
			attachments = new ObjectAttachment[] {att};
		} else {
			ObjectAttachment[] tempAtt = attachments;
			attachments = new ObjectAttachment[tempAtt.length +1 ];
			for(int i = 0; i < tempAtt.length ; i++) {
				attachments[i] = tempAtt[i];
			} 
			attachments[attachments.length - 1] = att;
		}
		makeSquare();
	}
	
	public boolean checkCollision(GameObject go) {
		if(go instanceof Explosives) {
			if(((Explosives) go).isShotImune(this)) {
				return false;
			}
		}
		if(go instanceof Shield) {
			return false;
		}
		if(super.checkCollision(go)) {
			return true;
		} 
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				if(att.checkCollisionInside(go) || att.getCrossedLineCorners(go).length == 2) { //TODO prepsat na check collision
					return true;
				}
			}
		}
		
		if(go instanceof LivingObject){
		    if(((LivingObject) go).getAttachments() != null) {
		    	for(ObjectAttachment att : ((LivingObject) go).getAttachments()) {
					if(att.checkCollisionInside(this) || att.getCrossedLineCorners(this).length == 2) {
						return true;
					}
				}
		    }
		}
	
		return false;
		
	}
	
	public void checkAndHandleReflect(GameObject otherOb) {
		if(otherOb instanceof Explosives) {
			if(((Explosives) otherOb).isShotImune(this)) {
				return;
			}
		}
		
		if(getCollisionSquare().squareCollision(otherOb.getCollisionSquare())) {
			if(otherOb != this) {
				Corner[] corners = new Corner[] {};
				
				
				corners = getCrossedLineCorners(otherOb);
				
				if(attachments != null) {
					for(ObjectAttachment att : attachments) {
						if(corners.length != 2) {
						corners = att.getCrossedLineCorners(otherOb);
						}
					}
				}
				
				if(otherOb instanceof LivingObject) {
					if(((LivingObject) otherOb).getAttachments() != null) {
						if(((LivingObject) otherOb).getAttachments().length > 0) {
							for(ObjectAttachment att : ((LivingObject) otherOb).getAttachments()) {
								if(corners.length != 2) {
									corners = this.getCrossedLineCorners(att);
								}
								
								if(attachments != null) {
									for(ObjectAttachment thisatt : attachments) {
										if(corners.length != 2) {
										corners = thisatt.getCrossedLineCorners(att);
										}
									}
								}
								
								
							}
						}
					}
				}
				
				if(corners != null && corners.length == 2) {
					reflect(corners[0], corners[1]);
				}
			}
		}
	}

	
	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public void makeSquare() {
		double biggest = 0;
		biggest = this.getFurthestDistance();
		if(attachments!=null) {
			for(ObjectAttachment at : attachments) {
				if(at.getFurthestDistance() > biggest) {
					biggest = at.getFurthestDistance();
				}
			}
		}
		super.makeSquare(biggest);
	}
	
	
	public void setShootForInteractiveAtts(boolean shoot) {
		if(this.getAttachments() != null && this.getAttachments().length > 0) {
			for(ObjectAttachment att : this.getAttachments()) {
				if(att instanceof InteractiveAttachment) {
					((InteractiveAttachment) att).setShoot(shoot);
				}
			}
		}
	}
	
	
	

	protected Corner getMP() {
		return movePoint;
	}
	
	protected Corner getMD() {
		return moveDirection;
	}
	protected void setForward(boolean b) {
		forward = b;
	}
	protected void setRight(boolean b) {
		turnRight = b;	
	}
	protected void setLeft(boolean b) {
		turnLeft = b;
	}
	
	protected boolean getForward() {
		return forward;
	}
	protected boolean getTurnLeft() {
		return turnLeft;
	}protected boolean getTurnRight() {
		return turnRight;
	}
	
	public void setAcceleration(double ac) {
		this.acceleration = ac;
	}
	
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	
	public void render(Graphics g) {
		super.render(g);
		
		if(attachments != null && attachments.length > 0) {
			for(int x = 0; x < attachments.length; x++) {
				attachments[x].render(g);
			}
		}


//		g.setColor(Color.red);
//		getRotationPoint().renderCorner(g, 10);
//		g.setColor(Color.GREEN);
//		g.fillRect((int) Math.round(getRotationPoint().getX()*Game.screenRatio),(int) Math.round(getRotationPoint().getY()*Game.screenRatio), 9, 9);
//		g.setColor(Color.BLUE);
//		g.fillRect((int) Math.round(movePoint.getX()*Game.screenRatio),(int) Math.round(movePoint.getY()*Game.screenRatio), 8, 8);
//		g.setColor(Color.black);	
//
	//	g.fillRect((int) Math.round(attachments[0].getAttachmentRP().getX()*Game.screenRatio),(int) Math.round(attachments[0].getAttachmentRP().getY()*Game.screenRatio), 8, 8);
	//	getCollisionSquare().render(g);
				
	}
	
	public void addShotImune(GameObject toAdd) {
		GameObject[] arr = shotImunes;
		shotImunes = new GameObject[arr.length+1];
		for(int i = 0; i < arr.length; i++) {
			shotImunes[i] = arr[i];
		}
		shotImunes[shotImunes.length-1] = toAdd;
	}
	
	public void addShotImunes(GameObject[] toAdds) {
		GameObject[] arr = shotImunes;
		shotImunes = new GameObject[arr.length+toAdds.length];
		for(int i = 0; i < arr.length; i++) {
			shotImunes[i] = arr[i];
			if(i < toAdds.length) {
				shotImunes[i+arr.length] = toAdds[i];
			}
		}
	}
	
	public Missile[] makePeriodicExplosion(int distance, Corner rp, int chunks, GameObject[] im){
		Missile[] m = new Missile[chunks];
		Corner[] rpList = GameObject.generatePeriodicObject(50, chunks, rp).getCorners();
		Corner[] mdList = GameObject.generatePeriodicObject(70, chunks, rp).getCorners();
		
		for(int i = 0; i < chunks; i++) {
			m[i] = Missile.makeNewMissile(rpList[i], 1, mdList[i], im);
		}
		
		return m;
	}

	public ObjectAttachment[] getAttachments() {
		
		return attachments;
	}
	
	public GameObject[] getShotImunes() {
		return shotImunes;
	}
	
	public boolean isShotImune(GameObject go) {
		for(int i = 0; i < getShotImunes().length; i++) {
			if(go == getShotImunes()[i]) {
				return true;
			}
		}
		return false;
	}
	
	public double getVelToGoDistance(double distance) {
		return Math.sqrt(2*distance*acceleration);
	}
	
	
	
	public void startStun(int stunLenght) {
		stunTimer = stunLenght;
		isStunned = true;

	}
	
	public void updateStun() {
		if(isStunned == true) {
			setForward(false);
			stunTimer--;
			if(stunTimer <= 0) {
				isStunned = false;
			}
		}
	}
	
	public boolean getIsStunned() {
		return isStunned;
	}
} 
