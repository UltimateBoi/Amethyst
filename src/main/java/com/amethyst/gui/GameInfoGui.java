package com.amethyst.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation; // Added import
import org.lwjgl.opengl.GL12; // for GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL14; // for GL14.GL_MIRRORED_REPEAT

// Renamed class back to GameInfoGui
public class GameInfoGui extends Gui {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fontRenderer;
    private static final int TEXT_COLOR = 0xFFFFFFFF;              // White text
    private static final int PADDING = 5; // Reduced padding for "gamesense style"
    
    // Added ResourceLocation for the gradient texture
    private static final ResourceLocation GRADIENT_TEXTURE = new ResourceLocation("amethyst", "image/gradient1.png");
    private static final float BACKGROUND_ALPHA = 0.65f; // Opacity for the gradient background

    public GameInfoGui() {
        this.fontRenderer = mc.fontRendererObj;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();

        // Get game info
        String username = mc.thePlayer.getName();
        String serverIp = mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "Singleplayer";
        // Format time without seconds for brevity
        String time = String.format("%02d:%02d",
            (int)((mc.theWorld.getWorldTime() / 1000 + 6) % 24), // Adjust for Minecraft time offset
            (int)((mc.theWorld.getWorldTime() % 1000) * 60 / 1000));

        // Combine info into one string
        String infoText = String.format("[ %s | %s | %s ]", username, serverIp, time);

        // Calculate dimensions
        int textWidth = fontRenderer.getStringWidth(infoText);
        int boxWidth = textWidth + PADDING * 2;
        int boxHeight = fontRenderer.FONT_HEIGHT + PADDING * 2; // Single line of text
        int x = screenWidth - boxWidth - PADDING; // Position X to the right
        int y = PADDING; // Position Y to the top

        // Draw textured background
        drawTexturedBackground(x, y, boxWidth, boxHeight);

        // Draw text centered vertically
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        int textY = y + PADDING + (boxHeight - PADDING * 2 - fontRenderer.FONT_HEIGHT) / 2; // Center text vertically
        // Ensure text is drawn *after* the background and with blending enabled
        fontRenderer.drawString(infoText, x + PADDING, textY, TEXT_COLOR);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    // Updated to animate gradient texture with mirrored repeat and inner overlay
    private void drawTexturedBackground(int x, int y, int width, int height) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.enableTexture2D();

        mc.getTextureManager().bindTexture(GRADIENT_TEXTURE);
        // Enable mirrored repeat on horizontal axis and clamp to edge vertically
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        // Calculate UV offset based on time for animation (4s loop)
        float time = (System.currentTimeMillis() % 4000L) / 4000f;
        float uMin = time;
        float uMax = time + 1f;

        // Apply transparency tint to gradient background
        GlStateManager.color(1.0F, 1.0F, 1.0F, BACKGROUND_ALPHA);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer buffer = tessellator.getWorldRenderer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0D).tex(uMin, 1.0D).endVertex();
        buffer.pos(x + width, y + height, 0.0D).tex(uMax, 1.0D).endVertex();
        buffer.pos(x + width, y, 0.0D).tex(uMax, 0.0D).endVertex();
        buffer.pos(x, y, 0.0D).tex(uMin, 0.0D).endVertex();
        tessellator.draw();

        // Draw inner black overlay for border effect
        GlStateManager.disableTexture2D();
        int overlayColor = 0x55000000; // semi-transparent black
        drawRect(x + 1, y + 1, x + width - 1, y + height - 1, overlayColor);
        GlStateManager.enableTexture2D();

        GlStateManager.disableBlend();
    }
}