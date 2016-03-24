package io.github.ititus.recstat.tracker;

import io.github.ititus.recstat.api.IPlayerStatus;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.common.util.INBTSerializable;

public class PlayerStatus implements IPlayerStatus {

	private boolean isRecording;

	public PlayerStatus() {
		this(false);
	}

	public PlayerStatus(boolean isRecording) {
		this.isRecording = isRecording;
	}

	@Override
	public boolean isRecording() {
		return isRecording;
	}

	@Override
	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setBoolean("isRecording", isRecording);

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.isRecording = nbt.getBoolean("isRecording");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		PlayerStatus other = (PlayerStatus) o;
		return isRecording == other.isRecording;
	}

	@Override
	public int hashCode() {
		return Boolean.hashCode(isRecording);
	}

	@Override
	public String toString() {
		return "PlayerStatus{" + "isRecording=" + isRecording + '}';
	}

}
