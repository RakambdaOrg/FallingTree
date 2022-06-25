package fr.raksrinana.fallingtree.common.wrapper;

public interface IFriendlyByteBuf extends IWrapper{
	void writeDouble(double value);
	
	void writeInteger(int value);
	
	double readDouble();
	
	int readInteger();
}
