package io.github.ititus.recstat.handler;

import java.util.UUID;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.api.IPlayerStatus;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventHandler {

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		UUID uuid = RecStat.getUUID(event.player);
		if (uuid != null) {
			RecStat.getPlayerTracker().getPlayerStatus(uuid);
			if (!event.player.world.isRemote) {
				RecStat.getPlayerTracker().sync();
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		UUID uuid = RecStat.getUUID(event.player);
		if (uuid != null) {
			RecStat.getPlayerTracker().removePlayerStatus(uuid);
			RecStat.getPlayerTracker().sync();
		}
	}

	@SubscribeEvent
	public void onPlayerGetDisplayName(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
		UUID uuid = RecStat.getUUID(event.getEntityPlayer());
		if (uuid != null) {
			IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
			if (playerStatus.isRecording()) {
				event.setDisplayname(RecStat.getPlayerNamePrefix().getFormattedText() + event.getDisplayname());
			}
		}
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entityLiving = event.getEntityLiving();
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			UUID uuid = RecStat.getUUID(player);
			if (uuid != null) {
				player.refreshDisplayName();
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (RecStat.MOD_ID.equalsIgnoreCase(event.getModID())) {
			ConfigHandler.loadConfig();
		}
	}

}
