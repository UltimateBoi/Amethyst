package com.amethyst.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class RenderUtil {
    // Animation speed (lower = faster)
    private static final float ANIMATION_SPEED = 0.15f;
    // Map to track switch animation progress (key = x,y position as string, value = animation progress 0 to 1)
    private static final Map<String, Float> switchAnimationMap = new HashMap<>();

    // Interpolate between two values based on a progress factor (0-1)
    public static float interpolate(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    // Interpolate between two colors based on a progress factor (0-1)
    public static Color interpolateColor(Color color1, Color color2, float progress) {
        int red = (int) interpolate(color1.getRed(), color2.getRed(), progress);
        int green = (int) interpolate(color1.getGreen(), color2.getGreen(), progress);
        int blue = (int) interpolate(color1.getBlue(), color2.getBlue(), progress);
        return new Color(red, green, blue);
    }

    // Get the current animation progress for a switch, update it based on the target state
    private static float getAnimationProgress(String key, boolean targetState) {
        float progress = switchAnimationMap.getOrDefault(key, targetState ? 0.0f : 1.0f);
        
        // Update progress based on target state
        if (targetState) {
            progress += ANIMATION_SPEED;
            if (progress > 1.0f) progress = 1.0f;
        } else {
            progress -= ANIMATION_SPEED;
            if (progress < 0.0f) progress = 0.0f;
        }
        
        // Store updated progress
        switchAnimationMap.put(key, progress);
        return progress;
    }

    // Draws a rectangle with the specified color and optional rounded corners
    public static void drawRect(float x, float y, float width, float height, int color, float radius) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        if (radius > 0) {
            // Draw rounded corners using arcs
            GL11.glEnable(GL11.GL_BLEND);
            drawArc(x + width - radius, y + height - radius, radius, 0.0f, 90.0f, 16);
            drawArc(x + radius, y + height - radius, radius, 90.0f, 180.0f, 16);
            drawArc(x + radius, y + radius, radius, 180.0f, 270.0f, 16);
            drawArc(x + width - radius, y + radius, radius, 270.0f, 360.0f, 16);

            // Draw rectangles to fill the remaining areas
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2d(x + width - radius, y);
            GL11.glVertex2d(x + radius, y);
            GL11.glVertex2d(x + width - radius, y + radius);

            GL11.glVertex2d(x + width - radius, y + radius);
            GL11.glVertex2d(x + radius, y);
            GL11.glVertex2d(x + radius, y + radius);

            GL11.glVertex2d(x + width, y + radius);
            GL11.glVertex2d(x, y + radius);
            GL11.glVertex2d(x, y + height - radius);

            GL11.glVertex2d(x + width, y + radius);
            GL11.glVertex2d(x, y + height - radius);
            GL11.glVertex2d(x + width, y + height - radius);

            GL11.glVertex2d(x + width - radius, y + height - radius);
            GL11.glVertex2d(x + radius, y + height - radius);
            GL11.glVertex2d(x + width - radius, y + height);

            GL11.glVertex2d(x + width - radius, y + height);
            GL11.glVertex2d(x + radius, y + height - radius);
            GL11.glVertex2d(x + radius, y + height);
            GL11.glEnd();
        } else {
            // Draw a regular rectangle
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x, y + height);
            GL11.glEnd();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    // Replacing the existing private drawArc method with the new public one
    public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
        GL11.glBegin(4); // GL_TRIANGLES
        int i = (int) ((float) num_segments / (360.0f / start_angle)) + 1;
        while ((float) i <= (float) num_segments / (360.0f / end_angle)) {
            double previousangle = Math.PI * 2 * (double) (i - 1) / (double) num_segments;
            double angle = Math.PI * 2 * (double) i / (double) num_segments;
            GL11.glVertex2d(cx, cy);
            GL11.glVertex2d((double) cx + Math.cos(angle) * (double) r, (double) cy + Math.sin(angle) * (double) r);
            GL11.glVertex2d((double) cx + Math.cos(previousangle) * (double) r, (double) cy + Math.sin(previousangle) * (double) r);
            ++i;
        }
        GL11.glEnd();
    }

    // Draws text at the specified position
    public static void drawText(String text, float x, float y, int color) {
        // Assuming you have a font renderer instance
        // Replace `fontRenderer` with your actual font renderer
        net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.drawString(text, (int) x, (int) y, color);
    }

    // Draws text with scaling
    public static void drawScaledString(String text, int x, int y, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        Minecraft.getMinecraft().fontRendererObj.drawString(text, (int) (x / scale), (int) (y / scale), color, true);
        GlStateManager.popMatrix();
    }
    // Draws a checkbox
    public static void drawCheckBox(float x, float y, float size, boolean checked, int borderColor, int fillColor) {
        drawRect(x, y, size, size, borderColor, 5f); // Draw border
        if (checked) {
            drawRect(x + 2, y + 2, size - 4, size - 4, fillColor, 0f); // Draw check mark
        }
    }

    // Draws a slider
    public static void drawSlider(float x, float y, float width, float height, float progress, int backgroundColor, int progressColor) {
        drawRect(x, y, width, height, backgroundColor, 2); // Draw slider background
        drawRect(x, y, width * progress, height, progressColor, 2); // Draw progress
    }

    public static void drawSwitch(float x, float y, float width, float height, boolean isOn) {
        // Create a unique identifier for this switch based on position
        String switchId = x + "," + y;
        
        // Get the current animation progress (0 = off, 1 = on)
        float progress = getAnimationProgress(switchId, isOn);
        
        // Define the on and off colors
        Color offColor = new Color(244, 67, 54);
        Color onColor = new Color(76, 175, 80);
        
        // Interpolate the color
        Color currentColor = interpolateColor(offColor, onColor, progress);
        
        // Draw switch background
        drawRect(x, y, width, height, currentColor.getRGB(), 5f);
        
        // Calculate knob position with smooth interpolation
        float offX = x + (height - (height - 6) * 1.3f) / 2;
        float onX = x + width - height + (height - (height - 6) * 1.3f) / 2;
        float currentX = interpolate(offX, onX, progress);
        float knobY = y + (height - (height - 6) * 1.3f) / 2;
        
        // Draw switch knob
        drawRect(currentX, knobY, (height - 6) * 1.3f, (height - 6) * 1.3f, 
                new Color(255, 255, 255).getRGB(), ((height - 6) * 1.3f) / 2f);
    }
}
