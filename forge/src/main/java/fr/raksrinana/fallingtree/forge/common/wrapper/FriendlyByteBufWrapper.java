package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.raksrinana.fallingtree.common.wrapper.IFriendlyByteBuf;
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
	public double readDouble(){
		return raw.readDouble();
	}
	
	@Override
	public int readInteger(){
		return raw.readInt();
	}
}
