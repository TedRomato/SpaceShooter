package package1;

public class Meteor extends MovingObject {
	int size;
	double speed;
	public Meteor(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, double speed, int size) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
		this.speed = speed;
		this.size = size;
		setNewHp();
		setCurrentSpeed(speed);
		
	
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
