package io.github.ititus.recstat.network.message;

import java.util.UUID;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.api.IPlayerStatus;
import io.github.ititus.recstat.tracker.PlayerStatus;
import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetPlayerStatus extends BaseMessage<MessageSetPlayerStatus> {

	IPlayerStatus playerStatus;

	public MessageSetPlayerStatus() {
	}

	public MessageSetPlayerStatus(IPlayerStatus playerStatus) {
		this.playerStatus = playerStatus;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.playerStatus = new PlayerStatus(buf.readBoolean());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(playerStatus.isRecording());
	}

	@Override
	public IMessage onMessage(MessageSetPlayerStatus message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		if (player != null) {
			MinecraftServer server = player.mcServer;
			if (server != null) {
				UUID uuid = RecStat.getUUID(player);
				if (uuid != null) {
					RecStat.getPlayerTracker().setPlayerStatus(uuid, message.playerStatus);
					RecStat.getPlayerTracker().sync();

					IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
					IChatComponent msg = new ChatComponentTranslation("text.recstat:record." + (playerStatus.isRecording() ? "true" : "false"), player.getDisplayName());
					server.getConfigurationManager().sendChatMsg(RecStat.getWithPrefix(msg));
				}
			}
		}
		return null;
	}
}
