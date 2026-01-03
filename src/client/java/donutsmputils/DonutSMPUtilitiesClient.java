package donutsmputils;

import donutsmputils.utils.ConfigManager;
import donutsmputils.utils.ModRegistries;
import net.fabricmc.api.ClientModInitializer;

public class DonutSMPUtilitiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ConfigManager.load();
		ModRegistries.registerCommands();
	}
}