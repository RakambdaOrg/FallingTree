package fr.raksrinana.fallingtree.fabric.config;

public enum BreakMode{
	INSTANTANEOUS(true), SHIFT_DOWN(false);
	private final boolean checkLeavesAround;
	
	BreakMode(boolean checkLeavesAround){
		this.checkLeavesAround = checkLeavesAround;
	}
	
	public boolean shouldCheckLeavesAround(){
		return checkLeavesAround;
	}
}
