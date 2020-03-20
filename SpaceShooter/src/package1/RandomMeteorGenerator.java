package package1;

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
	
	public Meteor generateRandomMeteorOutside(int width, int height) {
		Corner rp = GameObject.generateCornerOutsideMapInRange(width, height, new int[] {70,100});
		Corner md = GameObject.generateCornerInRect(616, 338, width-616, height-338 );
		md.setToNewRP(rp);
		Meteor m;
		double[] atribs = generateRandomAtributes();
		m = generateMeteor((int) atribs[0],(int) atribs[1], atribs[2], atribs[3],rp);
		m.setCurrentSpeed(atribs[2]);
		m.setReflectedSpeed(atribs[2]);
		m.setMoveDirection(md);
		m.updateAfterReflect();
		return m;
		
	}
	
	
	//size, vel, cAmount
	private double[] generateRandomAtributes() {
		double size = pickSize();
		double vel = generateNumInRange(new double[] {0.6,4})*2.5/size;
		double cornerAmount = (int) Math.floor(generateNumInRange(new double[] {13,18}));
		double rotationAngle = generateNumInRange(new double[] {0.4,2.5})*2/size;
		if(Math.random() > 0.5) {
			rotationAngle = - rotationAngle;
		}
		return new double[] {size,cornerAmount,vel,rotationAngle};
	}
	
	private int pickSize() {
		double number = generateNumInRange(new double[] {1,101});
		if(number > 80) {
			return 3;
		}else if(number < 30) {
			return 1;
		}else {
			return 2;
		}
	}
	
	private double generateNumInRange(double[] range) {
		return Math.random()*(range[1] - range[0])+range[0];
	}
	
	public Meteor generateMeteor(int size, int cornerAmount, double speed, double rotationAngle, Corner rotationPoint) {
		meteorSize = size;
		chunkAmount = cornerAmount;
		this.rotationPoint = rotationPoint;
		this.distanceChunk = 10;
		setNewVariables(cornerAmount);

		while(chunkPointer < chunkAmount) {
			addNewCorner();
		}
		Corner md = new Corner(new double[] {0,0}, rotationPoint);
		return new Meteor(corners,this.rotationPoint, rotationAngle, new Corner(md, this.rotationPoint), 0.4,meteorSize);
	}
	
	private void addNewCorner() {
		decideNewLayer();
		if(chunkPointer == 0) {
			double newAngle = generateAngle();
			corners[chunkPointer] = Corner.makeCornerUsinAngle(layer*distanceChunk + distanceChunk, newAngle, rotationPoint);
		}else if(chunkPointer == chunkAmount - 1) {
			while(true) {
				double newAngle = generateAngle();
				if(checkIfInClearSpace(newAngle, corners[chunkPointer-1].getAngle(rotationPoint)) == false && checkIfInClearSpace(corners[chunkPointer-1].getAngle(rotationPoint), newAngle)) {
					corners[chunkPointer] = Corner.makeCornerUsinAngle(layer*distanceChunk + distanceChunk, newAngle, rotationPoint);
					break;
				}
			}
		}
		else {
			while(true) {
				double newAngle = generateAngle();
				if(checkIfInClearSpace(newAngle, corners[chunkPointer-1].getAngle(rotationPoint)) == false) {
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
			}else if(random < 0.15) {
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

