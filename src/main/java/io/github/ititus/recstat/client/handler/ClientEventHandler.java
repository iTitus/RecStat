package io.github.ititus.recstat.client.handler;

import java.util.UUID;

import org.lwjgl.input.Keyboard;

import io.github.ititus.recstat.RecStat;
import io.github.ititus.recstat.api.IPlayerStatus;
import io.github.ititus.recstat.handler.ConfigHandler;
import io.github.ititus.recstat.network.NetworkHandler;
import io.github.ititus.recstat.network.message.MessageSetPlayerStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.text.translation.I18n;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ClientEventHandler {

	private final KeyBinding changeRecordStatus;

	public ClientEventHandler() {
		changeRecordStatus = new KeyBinding("key.recstat:changeRecordStatus", Keyboard.KEY_R, "key.categories.recstat:recstat");
		ClientRegistry.registerKeyBinding(changeRecordStatus);
	}

	private void onInput() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.inGameHasFocus) {
			if (changeRecordStatus.isPressed()) {
				UUID uuid = RecStat.getUUID(mc.thePlayer);
				if (uuid != null) {
					NetworkHandler.instance.sendToServer(new MessageSetPlayerStatus(RecStat.getToggledPlayerStatus(uuid)));
				}
			}
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		onInput();
	}

	@SubscribeEvent
	public void onMouseInput(InputEvent.MouseInputEvent event) {
		onInput();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderGameOverPost(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {

			Minecraft mc = Minecraft.getMinecraft();
			FontRenderer fr = mc.fontRendererObj;
			Profiler p = mc.mcProfiler;

			p.startSection("recstat-hud");
			{
				ScaledResolution resolution = event.getResolution();
				if (fr != null && resolution != null) {
					UUID uuid = RecStat.getUUID(mc.thePlayer);
					if (uuid != null) {
						IPlayerStatus playerStatus = RecStat.getPlayerTracker().getPlayerStatus(uuid);
						if (playerStatus.isRecording()) {
							String text = I18n.translateToLocal("text.recstat:recordingHudText." + ConfigHandler.recordingHudText);
							int color = 0xFFFFFF;
							int textWidth = fr.getStringWidth(text);
							int textHeight = fr.FONT_HEIGHT;
							int width = resolution.getScaledWidth();
							int height = resolution.getScaledHeight();

							int x, y;

							switch (ConfigHandler.recordingHudPosition) {
								case 0: {
									x = 1;
									y = 1;
									break;
								}
								case 1: {
									x = width - textWidth - 1;
									y = 1;
									break;
								}
								case 2: {
									x = 1;
									y = height - textHeight - 1;
									break;
								}
								default:
								case 3: {
									x = width - textWidth - 1;
									y = height - textHeight - 1;
									break;
								}
							}

							fr.drawString(text, x, y, color);
						}
					}
				}
			}
			p.endSection();
		}
	}

}
