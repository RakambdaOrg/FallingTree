package fr.rakambda.fallingtree.common.wrapper;

public interface IFriendlyByteBuf extends IWrapper{
	void writeDouble(double value);
	
	void writeInteger(int value);
	
	void writeBoolean(boolean value);
	
	double readDouble();
	
	int readInteger();
	
	boolean readBoolean();
}
