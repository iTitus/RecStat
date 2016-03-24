package io.github.ititus.recstat.network;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.network.message.MessageSyncAll;
import io.github.ititus.recstat.network.message.MessageTogglePlayerStatus;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

	public static SimpleNetworkWrapper instance;

	public static void init() {
		instance = new SimpleNetworkWrapper(RecStat.MOD_ID);

		instance.registerMessage(MessageSyncAll.class, MessageSyncAll.class, 0, Side.CLIENT);
		instance.registerMessage(MessageTogglePlayerStatus.class, MessageTogglePlayerStatus.class, 1, Side.SERVER);
	}

}
