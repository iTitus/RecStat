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
			if (!event.player.worldObj.isRemote) {
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
		UUID uuid = RecStat.getUUID(event.entityPlayer);
		if (uuid != null) {
			IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
			if (playerStatus.isRecording()) {
				event.displayname = RecStat.getPlayerNamePrefix().getFormattedText() + event.displayname;
			}
		}
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entityLiving = event.entityLiving;
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
		if (event.modID.equalsIgnoreCase(RecStat.MOD_ID)) {
			ConfigHandler.loadConfig();
		}
	}

}
