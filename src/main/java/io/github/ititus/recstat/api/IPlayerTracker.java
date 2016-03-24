package io.github.ititus.recstat.api;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerTracker extends INBTSerializable<NBTTagCompound> {

	IPlayerStatus getPlayerStatus(UUID uuid);

	void removePlayerStatus(UUID uuid);

	void sync();

	void togglePlayerStatus(UUID uuid);

}
