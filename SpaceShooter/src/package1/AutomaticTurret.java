package package1;

public class AutomaticTurret extends MagazineAttachment{

	public AutomaticTurret(Corner[] corners, Corner rp, double[] attachmentRP, double rotationAngle, Corner wayPoint,
			double lenght, double width) {
		super(corners, rp, attachmentRP, rotationAngle, wayPoint, lenght, width);
		// TODO Auto-generated constructor stub
	}
	
	public void updateAutoTurert(GameObject[] enemys) {
		if(enemys != null && enemys.length > 0) {
			setAimCorner(getNewAimCorner(getClosestEnemy(enemys)));
			rotateToCorner(getAimCorner());
		}
	}
	
	public static AutomaticTurret test(double x, double y) {
		Corner c1 = new Corner(new double[] {x + 10, y}, new double[] {x ,y });
		Corner c2 = new Corner(new double[] {x - 10 ,y }, new double[] {x ,y });
		Corner c3 = new Corner(new double[] {x - 10,y + 40}, new double[] {x ,y });
		Corner c4 = new Corner(new double[] {x + 10,y + 40}, new double[] {x ,y });
		AutomaticTurret at = new AutomaticTurret(new Corner[] {c1,c2,c3,c4}, new Corner(new double[] {x,y}), new double[] {x,y}, 60, new Corner(new double[] {x, y + 50}, new double[] {x,y}), 2000, 1);
		at.setAttRangle(4);
		at.setMagazineParameters(1, 100);
		at.setShoot(true);
		return at; 
	}
	
	
	

	

}
