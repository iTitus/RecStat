package io.github.ititus.recstat;

import io.github.ititus.recstat.api.IPlayerTracker;
import io.github.ititus.recstat.command.CommandRecStat;
import io.github.ititus.recstat.handler.ConfigHandler;
import io.github.ititus.recstat.proxy.CommonProxy;

import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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

	public static IChatComponent getWithPrefix(IChatComponent msg) {
		IChatComponent prefix = new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.DARK_RED + MOD_NAME + EnumChatFormatting.GRAY + "]");
		prefix.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentTranslation("text.recstat:info", MOD_NAME, MOD_AUTHOR)));

		IChatComponent text = new ChatComponentTranslation("text.recstat:prefix", prefix, msg);

		return text;

	}

	public static IChatComponent getPlayerNamePrefix() {
		IChatComponent prefix = new ChatComponentTranslation("text.recstat:playerPrefixText." + ConfigHandler.playerPrefixText);

		prefix.appendSibling(new ChatComponentText(" "));
		return prefix;
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