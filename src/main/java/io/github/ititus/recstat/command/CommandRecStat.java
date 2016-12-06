package io.github.ititus.recstat.command;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Nullable;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.api.IPlayerStatus;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandRecStat extends CommandBase {

	@Override
	public String getName() {
		return "recstat";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.recstat:recstat.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1 || args.length > 3) {
			throw new WrongUsageException(getUsage(sender));
		}
		String subCommand = args[0].toLowerCase(Locale.ENGLISH).trim();
		switch (subCommand) {
			case "get": {
				EntityPlayerMP player;
				if (args.length == 1) {
					player = getCommandSenderAsPlayer(sender);
				} else if (args.length == 2) {
					player = getPlayer(server, sender, args[1]);
				} else {
					throw new WrongUsageException(getUsage(sender));
				}
				UUID uuid = RecStat.getUUID(player);
				if (uuid != null) {
					IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);

					ITextComponent msg = new TextComponentTranslation("text.recstat:status." + (playerStatus.isRecording() ? "true" : "false"), sender.getDisplayName());
					sender.sendMessage(RecStat.getWithPrefix(msg));
				}
				break;
			}
			case "toggle": {
				EntityPlayerMP player;
				if (args.length == 1) {
					player = getCommandSenderAsPlayer(sender);
				} else if (args.length == 2) {
					player = getPlayer(server, sender, args[1]);
					if (player != sender && !sender.canUseCommand(2, getName())) {
						ITextComponent msg = new TextComponentTranslation("commands.generic.permission");
						msg.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(msg);
						break;
					}
				} else {
					throw new WrongUsageException(getUsage(sender));
				}
				UUID uuid = RecStat.getUUID(player);
				if (uuid != null) {
					RecStat.getPlayerTracker().setPlayerStatus(uuid, RecStat.getToggledPlayerStatus(uuid));
					IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
					RecStat.getPlayerTracker().sync();

					ITextComponent msg = new TextComponentTranslation("text.recstat:record." + (playerStatus.isRecording() ? "true" : "false"), sender.getDisplayName());
					server.getPlayerList().sendMessage(RecStat.getWithPrefix(msg));
				}
				break;
			}
			case "set": {
				EntityPlayerMP player;
				if (args.length == 2) {
					player = getCommandSenderAsPlayer(sender);
				} else if (args.length == 3) {
					player = getPlayer(server, sender, args[2]);
					if (player != sender && !sender.canUseCommand(2, getName())) {
						ITextComponent msg = new TextComponentTranslation("commands.generic.permission");
						msg.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(msg);
						break;
					}
				} else {
					throw new WrongUsageException(getUsage(sender));
				}
				UUID uuid = RecStat.getUUID(player);
				if (uuid != null) {
					String set = args[1].trim();
					boolean isRecording;
					if (set.equalsIgnoreCase("true")) {
						isRecording = true;
					} else if (set.equalsIgnoreCase("false")) {
						isRecording = false;
					} else {
						throw new CommandException("commands.generic.boolean.invalid", set);
					}

					IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
					if (playerStatus.isRecording() == isRecording) {
						ITextComponent msg = new TextComponentTranslation("text.recstat:alreadyRecording." + (playerStatus.isRecording() ? "true" : "false"), sender.getDisplayName());
						msg.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(RecStat.getWithPrefix(msg));
					} else {
						playerStatus.setRecording(isRecording);
						RecStat.getPlayerTracker().sync();

						ITextComponent msg = new TextComponentTranslation("text.recstat:record." + (playerStatus.isRecording() ? "true" : "false"), sender.getDisplayName());
						server.getPlayerList().sendMessage(RecStat.getWithPrefix(msg));
					}
				}
				break;
			}
			default: {
				throw new WrongUsageException(getUsage(sender));
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		if (args.length == 2) {
			String subCommand = args[0].trim();
			if (subCommand.equalsIgnoreCase("get") || subCommand.equalsIgnoreCase("toggle")) {
				return index == 1;
			}
		}
		if (args.length == 3) {
			String subCommand = args[0].trim();
			if (subCommand.equalsIgnoreCase("set")) {
				return index == 2;
			}
		}
		return false;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, new String[]{"get", "set", "toggle"});
		}
		if (isUsernameIndex(args, args.length - 1)) {
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		if (args.length == 2) {
			return getListOfStringsMatchingLastWord(args, new String[]{"true", "false"});
		}
		return null;
	}
}
