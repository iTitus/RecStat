package io.github.ititus.recstat;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import io.github.ititus.recstat.api.IPlayerStatus;
import io.github.ititus.recstat.api.IPlayerTracker;
import io.github.ititus.recstat.command.CommandRecStat;
import io.github.ititus.recstat.handler.ConfigHandler;
import io.github.ititus.recstat.proxy.CommonProxy;
import io.github.ititus.recstat.tracker.PlayerStatus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = RecStat.MOD_ID, name = RecStat.MOD_NAME, version = RecStat.MOD_VERSION, guiFactory = RecStat.GUI_FACTORY)
public class RecStat {

	// TODO: Fancier graphics (No "[Recording]" but a red dot)
	// TODO: Show recording state above name tag (if using [Recording])

	public static final String MOD_ID = "recstat";
	public static final String MOD_NAME = "RecStat";
	public static final String MOD_VERSION = "@MODVERSION@";
	public static final String MOD_AUTHOR = "iTitus";
	public static final String CLIENT_PROXY = "io.github.ititus.recstat.proxy.ClientProxy";
	public static final String SERVER_PROXY = "io.github.ititus.recstat.proxy.ServerProxy";
	public static final String GUI_FACTORY = "io.github.ititus.recstat.client.gui.RecStatGuiFactory";

	@Mod.Instance
	public static RecStat instance;

	@SidedProxy(clientSide = RecStat.CLIENT_PROXY, serverSide = RecStat.SERVER_PROXY)
	public static CommonProxy proxy;

	public static ITextComponent getWithPrefix(ITextComponent msg) {
		ITextComponent prefix = new TextComponentString(TextFormatting.GRAY + "[" + TextFormatting.DARK_RED + MOD_NAME + TextFormatting.GRAY + "]");
		prefix.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("text.recstat:info", MOD_NAME, MOD_AUTHOR)));
		ITextComponent text = new TextComponentTranslation("text.recstat:prefix", prefix, msg);
		return text;
	}

	public static ITextComponent getPlayerNamePrefix() {
		ITextComponent prefix = new TextComponentTranslation("text.recstat:playerPrefixText." + ConfigHandler.playerPrefixText);
		prefix.appendSibling(new TextComponentString(" "));
		return prefix;
	}

	public static IPlayerStatus getToggledPlayerStatus(UUID uuid) {
		IPlayerTracker playerTracker = getPlayerTracker();
		IPlayerStatus playerStatus = playerTracker.getPlayerStatus(uuid);
		return new PlayerStatus(!playerStatus.isRecording());
	}

	public static UUID getUUID(EntityPlayer player) {
		if (player != null) {
			GameProfile gameProfile = player.getGameProfile();
			if (gameProfile != null) {
				return gameProfile.getId();
			}
		}
		return null;
	}

	public static IPlayerTracker getPlayerTracker() {
		return proxy.getPlayerTracker();
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandRecStat());
	}
}