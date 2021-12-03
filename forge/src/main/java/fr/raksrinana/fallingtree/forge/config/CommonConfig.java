package fr.raksrinana.fallingtree.forge.config;

import lombok.Getter;
import net.minecraftforge.common.ForgeConfigSpec;

@Getter
public class CommonConfig{
	private static final String[] DESC_REVERSE_SNEAKING = {
			"When set to true, a tree will, only be chopped down if the player is sneaking."
	};
	private static final String[] DESC_BREAK_IN_CREATIVE = {
			"When set to true, the mod will, cut down trees in creative too."
	};
	private static final String[] DESC_NOTIFICATION_MODE = {
			"How messages are sent to the player.",
			"CHAT: Messages are sent in the chat.",
			"ACTION_BAR: Messages are displayed in the player's action bar.",
			"NONE: No notifications will appear."
	};
	private final TreeConfiguration trees;
	private final ToolConfiguration tools;
	private final ForgeConfigSpec.BooleanValue reverseSneaking;
	private final ForgeConfigSpec.BooleanValue breakInCreative;
	private final ForgeConfigSpec.ConfigValue<NotificationMode> notificationMode;
	
	public CommonConfig(ForgeConfigSpec.Builder builder){
		builder.comment("Falling Tree configuration");
		builder.push("trees");
		trees = new TreeConfiguration(builder);
		builder.pop();
		builder.push("tools");
		tools = new ToolConfiguration(builder);
		builder.pop();
		reverseSneaking = builder.comment(DESC_REVERSE_SNEAKING).define("reverse_sneaking", false);
		breakInCreative = builder.comment(DESC_BREAK_IN_CREATIVE).define("break_in_creative", false);
		notificationMode = builder.comment(DESC_NOTIFICATION_MODE).defineEnum("notification_mode", NotificationMode.ACTION_BAR);
	}
	
	public void setBreakInCreative(Boolean value){
		breakInCreative.set(value);
	}
	
	public void setReverseSneaking(Boolean value){
		reverseSneaking.set(value);
	}
	
	public void setNotificationMode(NotificationMode value){
		notificationMode.set(value);
	}
	
	public boolean isReverseSneaking(){
		return reverseSneaking.get();
	}
	
	public boolean isBreakInCreative(){
		return breakInCreative.get();
	}
	
	public NotificationMode getNotificationMode(){
		return notificationMode.get();
	}
}
