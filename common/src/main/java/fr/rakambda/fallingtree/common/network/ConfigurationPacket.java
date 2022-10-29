package fr.rakambda.fallingtree.common.network;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
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
