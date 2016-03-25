package io.github.ititus.recstat.handler;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

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
		if (event.player != null) {
			GameProfile gameProfile = event.player.getGameProfile();
			if (gameProfile != null) {
				UUID uuid = gameProfile.getId();
				if (uuid != null) {
					RecStat.getPlayerTracker().getPlayerStatus(uuid);
					if (!event.player.worldObj.isRemote) {
						RecStat.getPlayerTracker().sync();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.player != null) {
			GameProfile gameProfile = event.player.getGameProfile();
			if (gameProfile != null) {
				UUID uuid = gameProfile.getId();
				if (uuid != null) {
					RecStat.getPlayerTracker().removePlayerStatus(uuid);
					RecStat.getPlayerTracker().sync();
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerGetDisplayName(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player != null) {
			GameProfile gameProfile = player.getGameProfile();
			if (gameProfile != null) {
				UUID uuid = gameProfile.getId();
				if (uuid != null) {
					IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
					if (playerStatus.isRecording()) {
						event.setDisplayname(RecStat.getPlayerNamePrefix().getFormattedText() + event.getDisplayname());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entityLiving = event.getEntityLiving();
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			GameProfile gameProfile = player.getGameProfile();
			if (gameProfile != null) {
				UUID uuid = gameProfile.getId();
				if (uuid != null) {
					player.refreshDisplayName();
				}
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
