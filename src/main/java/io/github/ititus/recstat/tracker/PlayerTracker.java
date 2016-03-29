package io.github.ititus.recstat.tracker;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.api.IPlayerStatus;
import io.github.ititus.recstat.api.IPlayerTracker;
import io.github.ititus.recstat.network.NetworkHandler;
import io.github.ititus.recstat.network.message.MessageSyncAll;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import net.minecraftforge.common.util.Constants;

public class PlayerTracker implements IPlayerTracker {

	private final Map<UUID, IPlayerStatus> playerStatusMap = Maps.newConcurrentMap();

	private IPlayerStatus getPlayerStatusRaw(UUID uuid) {
		if (uuid == null) {
			throw new NullPointerException();
		}
		return playerStatusMap.get(uuid);
	}

	@Override
	public IPlayerStatus getPlayerStatus(UUID uuid) {
		if (uuid == null) {
			throw new NullPointerException();
		}
		if (!playerStatusMap.containsKey(uuid)) {
			IPlayerStatus playerStatus = new PlayerStatus();
			playerStatusMap.put(uuid, playerStatus);
			return playerStatus;
		}
		return playerStatusMap.get(uuid);
	}

	@Override
	public void removePlayerStatus(UUID uuid) {
		if (uuid == null) {
			throw new NullPointerException();
		}
		playerStatusMap.remove(uuid);
	}

	@Override
	public void setPlayerStatus(UUID uuid, IPlayerStatus playerStatus) {
		if (uuid == null || playerStatus == null) {
			throw new NullPointerException();
		}
		IPlayerStatus existingPlayerStatus = getPlayerStatus(uuid);
		existingPlayerStatus.setRecording(playerStatus.isRecording());
	}

	@Override
	public void sync() {
		NetworkHandler.instance.sendToAll(new MessageSyncAll(RecStat.getPlayerTracker().serializeNBT()));
	}

	private NBTTagCompound serializeNBT(UUID uuid) {
		if (uuid == null) {
			throw new NullPointerException();
		}

		IPlayerStatus playerStatus = getPlayerStatusRaw(uuid);
		if (playerStatus == null) {
			throw new NullPointerException();
		}

		NBTTagCompound compound = new NBTTagCompound();

		compound.setLong("UUIDLeast", uuid.getLeastSignificantBits());
		compound.setLong("UUIDMost", uuid.getMostSignificantBits());
		compound.setTag("Status", playerStatus.serializeNBT());

		return compound;
	}

	private void deserializeStatusNBT(NBTTagCompound compound) {
		UUID uuid = new UUID(compound.getLong("UUIDMost"), compound.getLong("UUIDLeast"));

		IPlayerStatus playerStatus = new PlayerStatus();
		playerStatus.deserializeNBT(compound.getCompoundTag("Status"));

		playerStatusMap.put(uuid, playerStatus);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		NBTTagList tagList = new NBTTagList();
		for (UUID uuid : playerStatusMap.keySet()) {
			tagList.appendTag(serializeNBT(uuid));
		}
		nbt.setTag("PlayerStatus", tagList);

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		playerStatusMap.clear();
		NBTTagList tagList = nbt.getTagList("PlayerStatus", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++) {
			deserializeStatusNBT(tagList.getCompoundTagAt(i));
		}
	}
}
