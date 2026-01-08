package donutsmpstatistics;

import donutsmpstatistics.utils.ConfigManager;
import donutsmpstatistics.utils.ModRegistries;
import net.fabricmc.api.ClientModInitializer;

public class DonutSMPStatisticsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ConfigManager.load();
		ModRegistries.registerCommands();
	}
}