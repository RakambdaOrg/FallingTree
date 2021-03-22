package fr.raksrinana.fallingtree.utils;

public enum TreePartType{
	LOG(true),
	NETHER_WART(true),
	LEAF_NEED_BREAK(true),
	OTHER(false);
	private final boolean breakable;
	
	TreePartType(boolean breakable){
		this.breakable = breakable;
	}
	
	public boolean isBreakable(){
		return breakable;
	}
}
