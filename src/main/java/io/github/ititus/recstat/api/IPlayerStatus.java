package io.github.ititus.recstat.api;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerStatus extends INBTSerializable<NBTTagCompound> {

	boolean isRecording();

	void setRecording(boolean isRecording);

}
