package fr.rakambda.fallingtree.common.network;

import fr.rakambda.fallingtree.common.config.IConfiguration;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.wrapper.IFriendlyByteBuf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationPacket{
	private boolean dedicated;
	private double speedMultiplicand;
	private boolean forceToolUsage;
	private BreakMode breakMode;
	
	public static ConfigurationPacket get(boolean dedicated, @NonNull IConfiguration configuration){
		return builder()
				.dedicated(dedicated)
				.speedMultiplicand(configuration.getTools().getSpeedMultiplicand())
				.forceToolUsage(configuration.getTools().isForceToolUsage())
				.breakMode(configuration.getTrees().getBreakMode())
				.build();
	}
	
	public void write(IFriendlyByteBuf buf){
		buf.writeBoolean(isDedicated());
		buf.writeDouble(getSpeedMultiplicand());
		buf.writeBoolean(isForceToolUsage());
		buf.writeInteger(getBreakMode().ordinal());
	}
	
	public static ConfigurationPacket read(IFriendlyByteBuf buf){
		return builder()
				.dedicated(buf.readBoolean())
				.speedMultiplicand(buf.readDouble())
				.forceToolUsage(buf.readBoolean())
				.breakMode(BreakMode.getValues()[buf.readInteger()])
				.build();
	}
}
