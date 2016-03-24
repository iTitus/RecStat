package io.github.ititus.recstat.proxy;

import io.github.ititus.recstat.api.IPlayerTracker;
import io.github.ititus.recstat.api.RecStatAPI;
import io.github.ititus.recstat.handler.ConfigHandler;
import io.github.ititus.recstat.handler.EventHandler;
import io.github.ititus.recstat.network.NetworkHandler;
import io.github.ititus.recstat.tracker.PlayerTracker;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		RecStatAPI.playerTracker = new PlayerTracker();
		NetworkHandler.init();
		ConfigHandler.preInit(event);
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public IPlayerTracker getPlayerTracker() {
		return RecStatAPI.playerTracker;
	}

}
