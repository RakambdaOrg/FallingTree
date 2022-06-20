package fr.raksrinana.fallingtree.common.wrapper;

public interface IFriendlyByteBuf extends IWrapper{
	void writeDouble(double value);
	
	double readDouble();
}
