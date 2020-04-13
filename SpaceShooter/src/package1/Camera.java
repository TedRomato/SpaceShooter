package package1;

public class Camera {
	Corner tlCorner;
	double cameraWidth, cameraHeight;
	int screenWidth, screenHeight;
	double zoom = 1;
	double toAddX = 0, toAddY = 0;
	double toMultiply = 1;
	
	public Camera(int screenWidth,int screenHeight,double zoom) { 
		tlCorner = new Corner(new double[] {0,0});
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.zoom = zoom;
		setCameraWidthAndHeight();
		updateCamera();
	}
	
	public void moveCam(double velX,double velY) {
		tlCorner.moveCorner(velX*Game.screenRatio, velY*Game.screenRatio);
		updateCameraLocation();

	}
	
	public void setCameraWidthAndHeight() {
		cameraWidth = screenWidth/zoom;
		cameraHeight = screenHeight/zoom;

	}
	
	public void setCameraToCorner(Corner c) {
		tlCorner.setX(c.getX() * Game.screenRatio - cameraWidth/2);
		tlCorner.setY(c.getY() * Game.screenRatio - cameraHeight/2);
		updateCameraLocation();
	}
	
	public void updateCameraLocation() {
		toAddX = -Game.camera.getX()*getZoom();
		toAddY = -Game.camera.getY()*getZoom();
	}
	
	public void updateCamera() {
		toMultiply = Game.screenRatio*getZoom();
		toAddX = -getX()*getZoom();
		toAddY = -getY()*getZoom();
	}
	
	public void setCameraCoords(double x, double y) {
		tlCorner.setX(x);
		tlCorner.setY(y);
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	public double getX() {
		return tlCorner.getX();
	}
	
	public double getY() {
		return tlCorner.getY();
	}
	
	public double toMultiply() {
		return toMultiply;
	}
	
	public double toAddX() {
		return toAddX;
	}
	
	public double toAddY() {
		return toAddY;
	}
}
