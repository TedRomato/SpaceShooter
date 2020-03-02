package package1;

import java.util.LinkedList;

public class RandomMeteorGenerator {
	Corner[] corners;
	int meteorSize;
	Corner rotationPoint;
	double chunkSize;
	int chunkAmount;
	int chunkPointer = 0;
	int layer;
	double clearSpace;
	double distanceChunk;
	
	public RandomMeteorGenerator() {
		
	}


	
	public Meteor generateMeteor(int size, int cornerAmount, Corner rotationPoint, double distanceChunk) {
		meteorSize = size;
		chunkAmount = cornerAmount;
		this.rotationPoint = rotationPoint;
		this.distanceChunk = distanceChunk;
		setNewVariables(cornerAmount);
		System.out.println("New meteor");
		while(chunkPointer < chunkAmount) {
			addNewCorner();
		}
		Corner md = new Corner(new double[] {0,0}, rotationPoint);
		return new Meteor(corners,this.rotationPoint, 1.0, new Corner(md, this.rotationPoint), 0.4,meteorSize);
	}
	
	private void addNewCorner() {
		decideNewLayer();
		if(chunkPointer == 0) {
			double newAngle = generateAngle();
			System.out.println("angle : " + newAngle);
			corners[chunkPointer] = Corner.makeCornerUsinAngle(layer*distanceChunk + distanceChunk, newAngle, rotationPoint);
		}else if(chunkPointer == chunkAmount - 1) {
			while(true) {
				double newAngle = generateAngle();
				if(checkIfInClearSpace(newAngle, corners[chunkPointer-1].getAngle(rotationPoint)) == false && checkIfInClearSpace(corners[chunkPointer-1].getAngle(rotationPoint), newAngle)) {
					System.out.println("angle : " + newAngle);
					corners[chunkPointer] = Corner.makeCornerUsinAngle(layer*distanceChunk + distanceChunk, newAngle, rotationPoint);
					break;
				}
			}
		}
		else {
			while(true) {
				double newAngle = generateAngle();
				if(checkIfInClearSpace(newAngle, corners[chunkPointer-1].getAngle(rotationPoint)) == false) {
					System.out.println("angle : " + newAngle);
					corners[chunkPointer] = Corner.makeCornerUsinAngle(layer*distanceChunk + distanceChunk, newAngle, rotationPoint);
					break;
				}
			}
		}
		chunkPointer ++;
		
	}
	
	private void decideNewLayer() {
		double random = Math.random();
		if(layer == 3) {
			if(random > 0.8) {
				layer = 2;
			}else if(random < 0.1) {
				layer = 1;
			}
		}else if(layer == 2) {
			if(random > 0.4) {
				layer = 3;
			}else if(random < 0.2) {
				layer = 1;
			}
		}else{
			if(random < 0.85) {
				layer = 2;
			}else if(random > 0.9 ) {
				layer = 3;
			}
		}
	}
	
	private boolean checkIfInClearSpace(double angle, double previousAngle) {
		if(previousAngle + clearSpace >= 360) {
			if(previousAngle + clearSpace - 360 > angle) {
				return true;
			}else {
				return false;
			}
		}else {
			if(previousAngle + clearSpace > angle) {
				return true;
			}else {
				return false;
			}
		}
		
		
	}
	
	private double generateAngle() {
		return addChunksBefore(generateRandomAngle());
	}
	
	private double addChunksBefore(double angle) {
		return angle + chunkPointer*chunkSize;
	}
	
	private double generateRandomAngle() {
		return Math.random()*chunkSize;
	}
	
	private void setNewVariables(int cornerAmount) {
		getNewChunkAmount(cornerAmount);
		getNewChunkSize();
		getNewClearSpace();
		setDistanceChunk();
		chunkPointer = 0;
		corners = new Corner[chunkAmount];
	}
	
	
	
	private void setDistanceChunk() {
		switch(meteorSize) {
			case 1:
				distanceChunk = 8;
				break;
			case 2:
				distanceChunk = 14;
				break;
			case 3:
				distanceChunk = 22;
				break;

		}
	}
	
	private void getNewChunkAmount(int cornerAmount) {
		chunkAmount = cornerAmount;
	}
	
	private void getNewChunkSize() {
		chunkSize = 360 / chunkAmount;
	}
	
	private void getNewClearSpace() {
		clearSpace = chunkSize / 4;
	}
	
	
	
}

