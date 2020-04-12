package package1;

public class Camera {
	double x = 0, y = 0;
	double cameraWidth, cameraHeight;
	int screenWidth, screenHeight;
	double zoom = 1;
	double toAddX = 0, toAddY = 0;
	double toMultiply = 1;
	
	public Camera(int screenWidth,int screenHeight,double zoom) { 
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.zoom = zoom;
		setCameraWidthAndHeight();
		updateCamera();
	}
	
	public void setCameraWidthAndHeight() {
		cameraWidth = screenWidth/zoom;
		cameraHeight = screenHeight/zoom;

	}
	
	public void setCameraToCorner(Corner c) {
		x = c.getX() * Game.screenRatio - cameraWidth/2;
		y = c.getY() * Game.screenRatio - cameraHeight/2;
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
		this.x = x;
		this.y = y;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
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
