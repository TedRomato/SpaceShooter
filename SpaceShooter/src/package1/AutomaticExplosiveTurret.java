package package1;

public class AutomaticExplosiveTurret extends ExplosiveShootingAtt {

	public AutomaticExplosiveTurret(Corner[] corners, Corner rp, double[] attachmentRP, double rotationAngle,
			Corner wayPoint, double lenght, double width) {
		super(corners, rp, attachmentRP, rotationAngle, wayPoint, lenght, width);
		// TODO Auto-generated constructor stub
	}
	public void updateAutoTurret(GameObject[] enemys) {
		if(enemys != null && enemys.length > 0) {
			setAimCorner(getNewAimCorner(getClosestEnemy(enemys)));
			rotateToCorner(getAimCorner());
		}
	}
	
	public static AutomaticExplosiveTurret MakeTurret(double x, double y) {
		Corner c1 = new Corner(new double[] {x + 10, y}, new double[] {x ,y });
		Corner c2 = new Corner(new double[] {x - 10 ,y }, new double[] {x ,y });
		Corner c3 = new Corner(new double[] {x - 10,y + 40}, new double[] {x ,y });
		Corner c4 = new Corner(new double[] {x + 10,y + 40}, new double[] {x ,y });
		AutomaticExplosiveTurret at = new AutomaticExplosiveTurret(new Corner[] {c1,c2,c3,c4}, new Corner(new double[] {x,y}), new double[] {x,y}, 60, new Corner(new double[] {x, y + 50}, new double[] {x,y}), 2000, 1);
		at.setFireGrenade(true);
		at.initialSetAttRangle(60);
		at.setMagazineParameters(1, 100);
		at.setShoot(true);
		return at; 
	}
}
