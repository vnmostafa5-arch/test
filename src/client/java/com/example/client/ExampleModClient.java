package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class ExampleModClient implements ClientModInitializer {
    private static int ticksActive = 0;

    @Override
    public void onInitializeClient() {
        // عداد يحسب الوقت بدقة طالما أنت داخل العالم واللعبة مش متوقفة
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && !client.isPaused()) {
                ticksActive++;
            }
        });

        // رسم الوقت الصافي فقط على الشاشة
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null) return;

            TextRenderer textRenderer = client.textRenderer;
            
            int totalSeconds = ticksActive / 20;
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            // هنا التعديل: يعرض الوقت فقط كأرقام نقية مثل (00:15:30) بدون أي كلمات بجانبها
            String timeDisplay = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            
            // رسم النص في أعلى اليسار بلون أبيض واضح وظل خلفي
            drawContext.drawText(textRenderer, Text.literal(timeDisplay), 10, 10, 0xFFFFFF, true);
        });
    }
}
