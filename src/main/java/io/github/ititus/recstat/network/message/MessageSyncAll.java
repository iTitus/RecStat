package io.github.ititus.recstat.network.message;

import io.github.ititus.recstat.RecStat;
import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncAll implements IMessage, IMessageHandler<MessageSyncAll, IMessage> {

	NBTTagCompound nbt;

	public MessageSyncAll() {
	}

	public MessageSyncAll(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public IMessage onMessage(MessageSyncAll message, MessageContext ctx) {
		RecStat.getPlayerTracker().deserializeNBT(message.nbt);
		return null;
	}
}
