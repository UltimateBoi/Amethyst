package util; // Corrected package

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui; // For drawRect
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11; // Import OpenGL for line width

import java.awt.Color; // If needed for color conversions

// Simplified RenderUtil based on PigHack structure, focusing on GUI needs
public class RenderUtil {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    // --- Basic Drawing ---

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format).
     * Uses vanilla Gui.drawRect for simplicity and compatibility.
     */
    public static void drawRect(float left, float top, float right, float bottom, int color) {
        System.out.println("RenderUtil: drawRect called with color: " + color); // Debug log
        // Ensure correct order for Gui.drawRect if width/height are negative
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }
        // Updated to use correct GlStateManager methods
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771); // Replaced SourceFactor and DestFactor with constants
        setLineWidth(1.5f); // Corrected method usage
        // Delegate to vanilla drawRect
        Gui.drawRect((int)left, (int)top, (int)right, (int)bottom, color);
        // Re-enable alpha blend which drawRect disables
        GlStateManager.enableBlend();
    }

    // Overload for AWT Color
    public static void drawRect(float left, float top, float right, float bottom, Color color) {
        drawRect(left, top, right, bottom, color.getRGB());
    }


    // --- GL State Management Helpers (Adapted from PigHack) ---

    /** Prepares GL state for 2D rendering (disables texture, depth, enables blend). */
    public static void prepareGL() {
        GlStateManager.pushMatrix(); // Added pushMatrix
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.blendFunc(770, 771); // Replaced SourceFactor and DestFactor with constants
        setLineWidth(1.5f); // Default line width
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
    }

    /** Releases GL state changes made by prepareGL. */
    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        // GlStateManager.enableBlend(); // Blend is often kept enabled
        GlStateManager.enableDepth();
        GlStateManager.enableLighting(); // Re-enable lighting if it was enabled before
        GlStateManager.disableAlpha(); // Often disabled after GUI
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
        GlStateManager.popMatrix(); // Added popMatrix
    }

     /** Simplified GL setup for drawing shapes. */
    public static void glrendermethod() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        setLineWidth(1.0f); // Use 1.0f for consistency unless specified otherwise
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING); // Disable lighting for 2D
        GL11.glDisable(GL11.GL_CULL_FACE); // Usually disable culling for 2D
        GL11.glEnable(GL11.GL_ALPHA_TEST); // Enable alpha testing
        GL11.glPushMatrix();
        // Translate based on render manager if needed for world rendering, not usually for GUI
    }

    /** Restores GL state after glrendermethod. */
    public static void glEnd() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // GL11.glDisable(GL11.GL_LINE_SMOOTH); // Often left enabled
        GL11.glDisable(GL11.GL_BLEND);
        // GL11.glEnable(GL11.GL_LIGHTING); // Re-enable if needed
        // GL11.glEnable(GL11.GL_CULL_FACE); // Re-enable if needed
        GL11.glPopMatrix();
    }

    // --- Color Helpers (Optional) ---

    public static void setColor(Color color) {
        GlStateManager.color((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public static void setLineWidth(float width) {
        GL11.glLineWidth(width); // Use OpenGL's glLineWidth
    }

    // Add other utility methods from PigHack's RenderUtil as needed,
    // ensuring they are adapted for 1.8.9 and don't rely on removed PigHack classes.
    // Examples: drawOutline, drawBox, drawTexturedRect, glScissor, etc.

}
