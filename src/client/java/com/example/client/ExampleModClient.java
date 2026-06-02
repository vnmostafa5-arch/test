package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class ExampleModClient implements ClientModInitializer {
    private static int ticksActive = 0;
    private static boolean wasInWorld = false;

    @Override
    public void onInitializeClient() {
        // عداد التيكس لحساب الثواني
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && !client.isPaused()) {
                if (!wasInWorld) {
                    wasInWorld = true;
                    ticksActive = 0; 
                }
                ticksActive++;
            } else if (client.world == null) {
                wasInWorld = false;
            }
        });

        // رسم الوقت أعلى الشاشة
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null) return;

            TextRenderer textRenderer = client.textRenderer;
            
            int totalSeconds = ticksActive / 20;
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            String timeDisplay = String.format("Time in World: %02d:%02d:%02d", hours, minutes, seconds);
            
            // رسم النص في أعلى اليسار
            drawContext.drawText(textRenderer, Text.literal(timeDisplay), 10, 10, 0xFFFFFF, true);
        });
    }
}
