package com.amethyst.gui;

import com.amethyst.AmethystClient;
import com.amethyst.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ClickGui extends GuiScreen {

    private final int guiWidth = (int) (300 * 1.75); // Increased size
    private final int guiHeight = (int) (200 * 1.75); // Increased size
    private final int categoryWidth = (int) (80 * 1.75); // Adjusted for scaling
    private final int titleBarHeight = (int) (20 * 1.75); // Adjusted for scaling
    private final int padding = 5; // Reduced padding

    private String selectedCategory = "Combat"; // Default selected category
    private String selectedModule = null;

    private final Map<String, String[]> categories = new HashMap<>();
    // Add a map to track the state of each module
    private final Map<String, Boolean> moduleStates = new HashMap<>();

    // Switch dimensions
    private final int switchWidth = 30;
    private final int switchHeight = 12;

    public ClickGui() {
        // Initialize categories with example modules
        categories.put("Combat", new String[]{"KillAura", "AutoClicker"});
        categories.put("Visual", new String[]{"ESP", "Shaders"});
        categories.put("Player", new String[]{"AutoSprint", "NoFall"});
        categories.put("Dungeons", new String[]{"DungeonSolver", "AutoPuzzle"});

        // Initialize module states
        for (String[] modules : categories.values()) {
            for (String module : modules) {
                moduleStates.put(module, false); // Default state is off
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int centerX = width / 2;
        int centerY = height / 2;

        // Draw main panel
        RenderUtil.drawRect(centerX - guiWidth / 2f, centerY - guiHeight / 2f, guiWidth, guiHeight, new Color(0, 0, 0, 200).getRGB(), 10f);

        // Draw title bar
        RenderUtil.drawRect(centerX - guiWidth / 2f, centerY - guiHeight / 2f, guiWidth, titleBarHeight, new Color(50, 50, 50, 255).getRGB(), 0f);
        fontRendererObj.drawString("Amethyst", centerX - guiWidth / 2f + padding, centerY - guiHeight / 2f + padding, new Color(138, 43, 226).getRGB(), true);
        RenderUtil.drawScaledString(AmethystClient.VERSION, centerX - guiWidth / 2 + 50, centerY - guiHeight / 2 + padding + 3, Color.GRAY.getRGB(), 0.8f);

        // Separator below title bar
        RenderUtil.drawRect(centerX - guiWidth / 2f, centerY - guiHeight / 2f + titleBarHeight, guiWidth, 2, new Color(100, 100, 100, 255).getRGB(), 0f);

        // Draw categories
        int categoryY = centerY - guiHeight / 2 + titleBarHeight + padding;
        for (String category : categories.keySet()) {
            boolean isSelected = category.equals(selectedCategory);
            RenderUtil.drawRect(centerX - guiWidth / 2f + padding, categoryY, categoryWidth, 20, isSelected ? new Color(50, 50, 50, 200).getRGB() : new Color(30, 30, 30, 200).getRGB(), 5f);
            fontRendererObj.drawString(category, centerX - guiWidth / 2 + padding + 5, categoryY + 5, Color.WHITE.getRGB());
            categoryY += 25;

            // Separator between categories
            RenderUtil.drawRect(centerX - guiWidth / 2f + padding, categoryY - 2, categoryWidth, 2, new Color(100, 100, 100, 255).getRGB(), 0f);
        }

        // Draw modules in the selected category
        int moduleY = centerY - guiHeight / 2 + titleBarHeight + padding;
        if (selectedCategory != null) {
            String[] modules = categories.get(selectedCategory);
            if (modules != null) {
                for (String module : modules) {
                    boolean isSelected = module.equals(selectedModule);
                    int moduleHeight = 20;
                    float moduleWidth = guiWidth - categoryWidth - padding * 4 - 120;
                    RenderUtil.drawRect(centerX - guiWidth / 2f + categoryWidth + padding * 2, moduleY, moduleWidth, moduleHeight, isSelected ? new Color(50, 50, 50, 200).getRGB() : new Color(30, 30, 30, 200).getRGB(), 5f);
                    fontRendererObj.drawString(module, centerX - guiWidth / 2 + categoryWidth + padding * 3, moduleY + 5, Color.WHITE.getRGB());

                    // Draw switch for the module - adjust position to center it vertically and move it right
                    boolean isOn = moduleStates.getOrDefault(module, false);
                    
                    // Calculate switch position inside module button
                    float switchX = centerX - guiWidth / 2f + categoryWidth + padding * 2 + moduleWidth - switchWidth - 10; // 10px margin from right edge
                    float switchY = moduleY + (moduleHeight - switchHeight) / 2; // Centered vertically
                    
                    RenderUtil.drawSwitch(switchX, switchY, switchWidth, switchHeight, isOn);
                    moduleY += 25;

                    // Separator between modules
                    RenderUtil.drawRect(centerX - guiWidth / 2f + categoryWidth + padding * 2, moduleY - 2, guiWidth - categoryWidth - padding * 4 - 120, 2, new Color(100, 100, 100, 255).getRGB(), 0f);
                }
            }
        }

        // Draw module configuration panel
        RenderUtil.drawRect(centerX + guiWidth / 2f - 120, centerY - guiHeight / 2f + titleBarHeight + padding, 100, guiHeight - titleBarHeight - padding * 2, new Color(30, 30, 30, 200).getRGB(), 5f);
        if (selectedModule != null) {
            fontRendererObj.drawString(selectedModule, centerX + guiWidth / 2 - 115, centerY - guiHeight / 2 + titleBarHeight + padding + 5, Color.WHITE.getRGB());
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int centerX = width / 2;
        int centerY = height / 2;

        // Check if a category was clicked
        int categoryY = centerY - guiHeight / 2 + titleBarHeight + padding;
        for (String category : categories.keySet()) {
            if (mouseX >= centerX - guiWidth / 2 + padding && mouseX <= centerX - guiWidth / 2 + padding + categoryWidth &&
                mouseY >= categoryY && mouseY <= categoryY + 20) {
                selectedCategory = category;
                selectedModule = null;
                return;
            }
            categoryY += 25;
        }

        // Check if a module was clicked
        int moduleY = centerY - guiHeight / 2 + titleBarHeight + padding;
        if (selectedCategory != null) {
            String[] modules = categories.get(selectedCategory);
            if (modules != null) {
                for (String module : modules) {
                    float moduleWidth = guiWidth - categoryWidth - padding * 4 - 120;
                    
                    // Check for clicks on the module entry
                    if (mouseX >= centerX - guiWidth / 2 + categoryWidth + padding * 2 && mouseX <= centerX - guiWidth / 2 + categoryWidth + padding * 2 + moduleWidth &&
                        mouseY >= moduleY && mouseY <= moduleY + 20) {
                            
                        // Check if click was on the switch area
                        float switchX = centerX - guiWidth / 2f + categoryWidth + padding * 2 + moduleWidth - switchWidth - 10;
                        float switchY = moduleY + (20 - switchHeight) / 2;
                        
                        if (mouseX >= switchX && mouseX <= switchX + switchWidth && 
                            mouseY >= switchY && mouseY <= switchY + switchHeight) {
                            // Toggle the module's state
                            moduleStates.put(module, !moduleStates.getOrDefault(module, false));
                        } else {
                            // Select the module if not clicking on switch
                            selectedModule = module;
                        }
                        return;
                    }
                    moduleY += 25;
                }
            }
        }
    }
}
