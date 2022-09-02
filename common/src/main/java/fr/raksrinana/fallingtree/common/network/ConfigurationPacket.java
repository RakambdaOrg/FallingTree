package fr.raksrinana.fallingtree.common.network;

import fr.raksrinana.fallingtree.common.config.enums.BreakMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationPacket{
	private double speedMultiplicand;
	private boolean forceToolUsage;
	private BreakMode breakMode;
}
