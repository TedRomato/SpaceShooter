package package1;

public class Player extends GameObject {
	private int velUp, velDown, velLeft, velRight;
	private boolean goUp, goDown, goLeft, goRight;
	private int maxSpeed;
	private int acceleration;
	public Player(Corner[] corners, int[] rotationPoint, int rotationAngle) {
		super(corners, rotationPoint, rotationAngle);
		
	}
}
