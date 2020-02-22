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
	
	//TODO reflectMeteorFromSide FIX IT!!!
	
	public void reflectMeteorFromSide(int border) {

		switch(border) {
		case 0 :
			moveDirection.turnAround('y', this.getRotationPoint());
			break;
		case 1 :
			moveDirection.turnAround('x', this.getRotationPoint());
			break;
		case 2 :
			moveDirection.turnAround('y', this.getRotationPoint());
			break;
		case 3 : 
			moveDirection.turnAround('x', this.getRotationPoint());
			break;
		}
		moveDirection.updateNoRotation(getRotationPoint());
		setCurrentSpeed(getCurrentSpeed()+8);
		setNewVels();
		moveOb();
		setCurrentSpeed(getCurrentSpeed()-8);
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
