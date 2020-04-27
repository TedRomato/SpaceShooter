package package1;

public class Tower extends LivingObject{

	public Tower(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	public Tower makeNewTower(int x, int y){
		Tower tower = new Tower(GameObject.generatePeriodicObject(50, 8, new Corner(new double[] {x,y})).getCorners(), new Corner(new double[] {x,y}),0.0, new Corner(new double[] {x,y}));
		return tower;
	}
	
}
