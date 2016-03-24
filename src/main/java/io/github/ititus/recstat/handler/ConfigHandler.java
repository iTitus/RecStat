package io.github.ititus.recstat.handler;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {

	public static int playerPrefixText = 0;
	public static int recordingHudPosition = 3;
	public static int recordingHudText = 1;

	private static Configuration configuration;

	public static void preInit(FMLPreInitializationEvent event) {
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		loadConfig();
	}

	public static void loadConfig() {

		playerPrefixText = configuration.getInt("playerPrefixText", Configuration.CATEGORY_GENERAL, playerPrefixText, 0, 3, "0: red dot, 1: \"Recording\", 2: \"R\", 3: \"r\"");
		if (playerPrefixText < 0 || playerPrefixText > 3) {
			playerPrefixText = 0;
		}

		recordingHudPosition = configuration.getInt("recordingHudPosition", Configuration.CATEGORY_CLIENT, recordingHudPosition, 0, 3, "0: top-left, 1: top-right, 2: bottom-left, 3: bottom-right");
		if (recordingHudPosition < 0 || recordingHudPosition > 3) {
			recordingHudPosition = 3;
		}

		recordingHudText = configuration.getInt("recordingHudText", Configuration.CATEGORY_CLIENT, recordingHudText, 0, 3, "0: red dot, 1: \"Recording\", 2: \"R\", 3: \"r\"");
		if (recordingHudText < 0 || recordingHudText > 3) {
			recordingHudText = 1;
		}

		if (configuration.hasChanged()) {
			configuration.save();
		}
	}

	public static Configuration getConfiguration() {
		return configuration;
	}
}
