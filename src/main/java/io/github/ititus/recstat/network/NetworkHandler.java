package io.github.ititus.recstat.network;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.network.message.MessageSetPlayerStatus;
import io.github.ititus.recstat.network.message.MessageSyncAll;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

	public static SimpleNetworkWrapper instance;
	private static int discriminator = 0;

	public static void init() {
		instance = new SimpleNetworkWrapper(RecStat.MOD_ID);

		registerMessage(MessageSyncAll.class, Side.CLIENT);
		registerMessage(MessageSetPlayerStatus.class, Side.SERVER);
	}

	private static void registerMessage(Class clazz, Side handlerSide) {
		instance.registerMessage(clazz, clazz, discriminator++, handlerSide);
	}

}
