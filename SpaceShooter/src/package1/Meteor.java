package package1;

public class Meteor extends MovingObject {
	//Moving object that reflects differently from borders of screen to make for a more interesting gameplay
	//comes in three basic types
	int size;
	public Meteor(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, double speed, int size) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
		setReflectedSpeed(speed);
		this.size = size;
		setNewHp();
		setCurrentSpeed(speed);
	}
	public Meteor(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md, double speed, int size) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
		setReflectedSpeed(speed);
		this.size = size;
		setNewHp();
		setCurrentSpeed(speed);
	}
	
	
	
	public void reflectMeteorFromSide(int border, Corner rp) {

		switch(border) {
		case 0 :
			if(this.moveDirection.isUnderCorner(rp) == true) {
				moveDirection.turnAround('y', this.getRotationPoint());
				}
			break;
		case 1 :
			if(this.moveDirection.isCornerOnRigth(rp) == false) {
				moveDirection.turnAround('x', this.getRotationPoint());
				}
			break;
		case 2 :
			if(this.moveDirection.isUnderCorner(rp) == false) {
				moveDirection.turnAround('y', this.getRotationPoint());
				}
			break;
		case 3 : 
			if(this.moveDirection.isCornerOnRigth(rp) == true) {
				moveDirection.turnAround('x', this.getRotationPoint());
				}
			break;
		}
		moveDirection.updateNoRotation(getRotationPoint());
		setNewVels();
	}
	
	
	
	
	private void setNewHp() {
		switch(size) {
			case 1:  
				setHP(1);
				break;
			case 2:  
				setHP(3);
				break;
			case 3:  
				setHP(20);
				break;
			default:
				System.out.println("Spatne v meteor konstruktoru");
			
		}
	}
	

	public int getSize() {
		return size;
	}
	
}
