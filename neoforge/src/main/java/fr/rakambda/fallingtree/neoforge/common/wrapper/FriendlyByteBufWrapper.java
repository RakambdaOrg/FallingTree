package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IFriendlyByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@ToString
public class FriendlyByteBufWrapper implements IFriendlyByteBuf{
	@NotNull
	@Getter
	private final FriendlyByteBuf raw;
	
	@Override
	public void writeDouble(double value){
		raw.writeDouble(value);
	}
	
	@Override
	public void writeInteger(int value){
		raw.writeInt(value);
	}
	
	@Override
	public void writeBoolean(boolean value){
		raw.writeBoolean(value);
	}
	
	@Override
	public double readDouble(){
		return raw.readDouble();
	}
	
	@Override
	public int readInteger(){
		return raw.readInt();
	}
	
	@Override
	public boolean readBoolean(){
		return raw.readBoolean();
	}
}
