package package1;

public class KeyChecker {
	int[] pressedChars = {};
	boolean leftMousePressed = false;
	boolean rightMousePressed = false;
	
	public boolean checkIfkeyIsPressed(int c) {
		for(int pressed : pressedChars) {
			if(pressed == c) {
				return true;
			}
		}
		return false;
	}
	
	public void keyPressed(int pressed) {
		boolean alreadyPressed = false;
		for(int c : pressedChars) {
			if(c == pressed) {
				alreadyPressed = true;
			}
		}
		if(alreadyPressed == false) {
			int[] newPressed = new int[pressedChars.length+1];
			for(int i = 0; i < pressedChars.length; i++) {
				newPressed[i] = pressedChars[i];
			}
			newPressed[newPressed.length-1] = pressed;
			pressedChars = newPressed;
		}
	}
	
	public void keyReleased(int released) {
		int i = -1;
		for(int c = 0; c < pressedChars.length; c++) {
			if(pressedChars[c] == released) {
				i = c;
				break;
			}
		}
		if(i != -1) {
			boolean afterReleasedint = false;
			int[] newPressed = new int[pressedChars.length-1];
			for(int b = 0; b < newPressed.length; b++) {
				if(b == i) {
					afterReleasedint = true;
				}
				if(afterReleasedint==false) {
					newPressed[b] = pressedChars[b];
				}else {
					newPressed[b] = pressedChars[b+1];
				}
			}
			pressedChars = newPressed;
		}
	}

	public boolean isLeftMousePressed() {
		return leftMousePressed;
	}

	public void setLeftMousePressed(boolean leftMousePressed) {
		this.leftMousePressed = leftMousePressed;
	}

	public boolean isRightMousePressed() {
		return rightMousePressed;
	}

	public void setRightMousePressed(boolean rightMousePressed) {
		this.rightMousePressed = rightMousePressed;
	}
	
	
}
