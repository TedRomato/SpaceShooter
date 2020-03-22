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
	protected int reloadLenght;
	protected int reloadTimer;
	private boolean forward = false, turnRight = false, turnLeft = false,shoot = false;
	private Corner movePoint, shootDirection, shootPoint;
	private double maxSpeed = 7;
	private double acceleration = maxSpeed/200;
	private ObjectAttachment[] attachments;
	
	public LivingObject(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,int reloadTimer) {
		super(corners, rotationPoint, rotationAngle, md);
		movePoint = new Corner(md, rotationPoint);
		setReflectedSpeed(maxSpeed*2);
		shootPoint = new Corner(new double[] {movePoint.getX(),movePoint.getY()+20}, rotationPoint);
		shootDirection = new Corner(new double[] {movePoint.getX(),movePoint.getY()+40}, rotationPoint);
		this.reloadTimer= reloadTimer;
		setHP(10);
		makeSquare();
	}
	
	public LivingObject(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md,int reloadTimer) {
		super(corners, rotationPoint, rotationAngle, md);
		movePoint = new Corner(md, rotationPoint);
		setReflectedSpeed(maxSpeed*2);
		shootPoint = new Corner(new double[] {movePoint.getX(),movePoint.getY()+20}, rotationPoint);
		shootDirection = new Corner(new double[] {movePoint.getX(),movePoint.getY()+40}, rotationPoint);
		this.reloadTimer= reloadTimer;
		setHP(10);
		makeSquare();	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;

	}
	
	
	public void updateOb() {
			
		updateSpeed();

		moveOb();

		if(turnRight || turnLeft) {
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
		super.rotateOb();
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				if(att.getRotateWithParentOb()) {
					for(Corner c : att.getCorners()) {
						c.rotateCorner(getRotationPoint(), getRotationAngle());
					}
					att.attachmentRP.rotateCorner(getRotationPoint(), getRotationAngle());
				}

			}

		}
		
		movePoint.rotateCorner(getRotationPoint(), getRotationAngle());	
		shootDirection.rotateCorner(getRotationPoint(), getRotationAngle());
		shootPoint.rotateCorner(getRotationPoint(), getRotationAngle());
		}
	
	//rotates only mp, sp,sd
	public void rotateWithoutObject() {
		movePoint.rotateCorner(getRotationPoint(), getRotationAngle());	
		shootDirection.rotateCorner(getRotationPoint(), getRotationAngle());
		shootPoint.rotateCorner(getRotationPoint(), getRotationAngle());
	}
	
	public void moveOb() {
		super.moveOb();
		
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				att.moveAttachment(getVelX(), getVelY());
			}
		}
		
		movePoint.moveCorner(getVelX(),getVelY());
		shootDirection.moveCorner(getVelX(), getVelY());
		shootPoint.moveCorner(getVelX(), getVelY());



	
	}
	
	public  void rotateAttachments() {
		for(ObjectAttachment att : attachments) {
			att.rotateAttachment(att.getRotationAngle());
		}
	}
	
	private void updateMDtoMP() {
		
		moveDirection = new Corner(movePoint, getRotationPoint());
	
	}
	
	
	//handles acceleration 
	
	public void updateSpeed() {
		if(forward && getCurrentSpeed() < maxSpeed) {
			setCurrentSpeed(getCurrentSpeed() + acceleration);
		}
		if(getCurrentSpeed() > maxSpeed && getReflected() == false) {
			setCurrentSpeed(maxSpeed);
		}if(forward != true && getCurrentSpeed() > 0 - acceleration) {
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
		if(checkCollisionInside(go) || getCrossedLineCorners(go).length == 2) {
			return true;
		}
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				if(att.checkCollisionInside(go) || att.getCrossedLineCorners(go).length == 2) {
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
		if(getCollisionSquare().squareCollision(otherOb.getCollisionSquare())) {
			if(otherOb != this) {
				Corner[] corners = null;
				
				
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
	protected Corner getSD() {
		return shootDirection;
	}
	public Missile shoot() {
		Corner rp = new Corner(new double[] {getSP().getX(),getSP().getY()});
		Corner TopLeft = new Corner(new double[] {getSP().getX()-5,getSP().getY()-5}, rp);
		Corner BotLeft = new Corner(new double[] {getSP().getX()-5,getSP().getY()+5}, rp);
		Corner BotRight = new Corner(new double[] {getSP().getX()+5,getSP().getY()+5}, rp);
		Corner TopRight = new Corner(new double[] {getSP().getX()+5,getSP().getY()-5}, rp);
		Corner md = new Corner(new double[] {getSD().getX(), getSD().getY()}, rp);
		if(getShoot()) {
			Missile m = new Missile(new Corner[] {TopLeft, BotLeft, BotRight, TopRight}, rp, 0,md,12);
			m.getNewRatios();
			m.setNewVels();
			return m;
		}
		else return null;
	}
	
	public int getReloadLenght() {
		return reloadLenght;
	}

	public void setReloadLenght(int reloadLenght) {
		this.reloadLenght = reloadLenght;
	}

	public int getReloadTimer() {
		return reloadTimer;
	}

	public void setReloadTimer(int reloadTimer) {
		this.reloadTimer = reloadTimer;
	}

	protected Corner getMP() {
		return movePoint;
	}
	protected Corner getSP() {
		return shootPoint;
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
	protected void setShoot(boolean b) {
		shoot = b;
	}
	protected boolean getShoot() {
		return shoot;
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

		

	/*

		g.setColor(Color.red);
		g.fillRect((int) Math.round(moveDirection.getX()),(int) Math.round(moveDirection.getY()), 10, 10);
		g.setColor(Color.darkGray);
		g.fillRect((int) Math.round(getRotationPoint().getX()),(int) Math.round(getRotationPoint().getY()), 9, 9);
		g.setColor(Color.BLUE);
		g.fillRect((int) Math.round(movePoint.getX()),(int) Math.round(movePoint.getY()), 8, 8);
		g.setColor(Color.GREEN);
		g.fillRect((int) Math.round(shootDirection.getX()),(int) Math.round(shootDirection.getY()), 9,9);
		g.setColor(Color.YELLOW);
		g.fillRect((int) Math.round(shootPoint.getX()),(int) Math.round(shootPoint.getY()), 9,9);
		g.setColor(Color.BLACK);

		
		g.fillRect((int) Math.round(attachments[0].getAttachmentRP().getX()),(int) Math.round(attachments[0].getAttachmentRP().getY()), 8, 8);
		 */
		
		getCollisionSquare().render(g);
		
		
	}
	


	public ObjectAttachment[] getAttachments() {
		
		return attachments;
	}
} 
