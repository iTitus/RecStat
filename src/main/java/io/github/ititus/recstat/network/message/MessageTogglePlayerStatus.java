package io.github.ititus.recstat.network.message;

import java.util.UUID;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.api.IPlayerStatus;
import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTogglePlayerStatus implements IMessage, IMessageHandler<MessageTogglePlayerStatus, IMessage> {

	UUID uuid;

	public MessageTogglePlayerStatus() {
	}

	public MessageTogglePlayerStatus(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.uuid = new UUID(buf.readLong(), buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}

	@Override
	public IMessage onMessage(MessageTogglePlayerStatus message, MessageContext ctx) {
		MinecraftServer server = ctx.getServerHandler().playerEntity.mcServer;

		if (server != null) {
			UUID uuid = message.uuid;
			EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(uuid);

			if (player != null) {
				RecStat.getPlayerTracker().togglePlayerStatus(uuid);
				RecStat.getPlayerTracker().sync();

				IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
				ITextComponent msg = new TextComponentTranslation("text.recstat:record." + (playerStatus.isRecording() ? "true" : "false"), player.getDisplayName());
				server.getPlayerList().sendChatMsg(RecStat.getWithPrefix(msg));
			}
		}
		return null;
	}
}
