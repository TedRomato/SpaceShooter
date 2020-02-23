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
	private boolean forward = false, turnRight = false, turnLeft = false;
	private Corner movePoint;
	private double maxSpeed = 2.5;
	private double acceleration = maxSpeed/200;
	private ObjectAttachment[] attachments;
	
	public LivingObject(Corner[] corners, double[] rotationPoint2, double rotationAngle, Corner md) {
		super(corners, rotationPoint2, rotationAngle, md);
		movePoint = new Corner(md, rotationPoint2);
		setReflectedSpeed(maxSpeed*2);
		setHP(10);
		// TODO Auto-generated constructor stub
	}
	
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
		for(Corner c : getCorners()) {
			c.rotateCorner(getRotationPoint(), getRotationAngle());
		}
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				for(Corner c : att.getCorners()) {
					c.rotateCorner(getRotationPoint(), getRotationAngle());
				}
				att.attachmentRP.rotateCorner(getRotationPoint(), getRotationAngle());

			}

		}
		
		movePoint.rotateCorner(getRotationPoint(), getRotationAngle());	
		}
	
	public void moveOb() {
		for(Corner c : getCorners()) {
			c.moveCorner(getVelX(),getVelY());
		}
		
		if(attachments != null) {
			for(ObjectAttachment att : attachments) {
				att.moveAttachment(getVelX(), getVelY());
			}
		}
		
		getRotationPoint().moveCorner(getVelX(), getVelY());


		movePoint.moveCorner(getVelX(),getVelY());
		moveDirection.moveCorner(getVelX(),getVelY());

	
	}
	
	public  void rotateAttachments() {
		if(turnRight || turnLeft) {
			for(ObjectAttachment att : attachments) {
				att.rotateAttachment(att.getAngleToRotateConstantly(getRotationAngle()));
			}
		}
		else {
			for(ObjectAttachment att : attachments) {
				att.rotateAttachment(att.getRotationAngle());
			}
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
		if(getCurrentSpeed() > maxSpeed) {
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
	
		return false;
		
	}
	
	public void checkAndHandleReflect(GameObject otherOb) {
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
			
			
			
			
			if(corners != null && corners.length == 2) {
				reflect(corners[0], corners[1]);
			}
		}
	}
	
	protected Corner getMP() {
		return movePoint;
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
	
	
	public void render(Graphics g) {
		for(int i = 0;i<getCorners().length;i++) {
			if(i<getCorners().length-1) {
				g.drawLine((int) Math.round(getCorners()[i].getX()),(int) Math.round(getCorners()[i].getY()),(int) Math.round(getCorners()[i+1].getX()),(int) Math.round(getCorners()[i+1].getY()));
			}
			else {
				g.drawLine((int) Math.round(getCorners()[i].getX()),(int) Math.round(getCorners()[i].getY()),(int) Math.round(getCorners()[0].getX()),(int) Math.round(getCorners()[0].getY()));
			}
		}
		
		if(attachments != null && attachments.length > 0) {
			for(int x = 0; x < attachments.length; x++) {
				for(int i = 0; i < attachments[x].getCorners().length;i++) {
					if(i < attachments[x].getCorners().length-1) {
						g.drawLine((int) Math.round(attachments[x].getCorners()[i].getX()),(int) Math.round(attachments[x].getCorners()[i].getY()),(int) Math.round(attachments[x].getCorners()[i+1].getX()),(int) Math.round(attachments[x].getCorners()[i+1].getY()));
					}
					else {
						g.drawLine((int) Math.round(attachments[x].getCorners()[i].getX()),(int) Math.round(attachments[x].getCorners()[i].getY()),(int) Math.round(attachments[x].getCorners()[0].getX()),(int) Math.round(attachments[x].getCorners()[0].getY()));
					}
				}
			}
		}
		g.setColor(Color.red);
		g.fillRect((int) Math.round(moveDirection.getX()),(int) Math.round(moveDirection.getY()), 10, 10);
		g.setColor(Color.darkGray);
		g.fillRect((int) Math.round(getRotationPoint().getX()),(int) Math.round(getRotationPoint().getY()), 9, 9);
		g.setColor(Color.BLUE);
		g.fillRect((int) Math.round(movePoint.getX()),(int) Math.round(movePoint.getY()), 8, 8);
		g.setColor(Color.BLACK);
		
		g.fillRect((int) Math.round(attachments[0].getAttachmentRP().getX()),(int) Math.round(attachments[0].getAttachmentRP().getY()), 8, 8);

		
		
	}


	public ObjectAttachment[] getAttachments() {
		// TODO Auto-generated method stub
		return attachments;
	}
} 
