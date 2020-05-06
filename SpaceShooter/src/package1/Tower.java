package package1;

public class Tower extends LivingObject{

	public Tower(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	public static Tower makeNewTower(double x, double y){
		Tower tower = new Tower(GameObject.generatePeriodicObject(120, 8, new Corner(new double[] {x,y})).getCorners(), new Corner(new double[] {x,y}),0.0, new Corner(new double[] {x,y}));
		
		return tower;
	}
	
	public void updateAllTurrets(GameObject[] enemys) {
		if(getAttachments()!=null) {
			for(ObjectAttachment att : getAttachments()) {
				if(att instanceof AutomaticTurret) {
					((AutomaticTurret) att).updateAutoTurert(enemys);
				}
				if(att instanceof AutomaticExplosiveTurret) {
					((AutomaticExplosiveTurret) att).updateAutoTurret(enemys);
				}
			}
		}
	}
	public void addTurret() {
		this.addAttachment(AutomaticExplosiveTurret.MakeTurret(this.getRotationPoint().getX(), this.getRotationPoint().getY()));
	}

	
}
