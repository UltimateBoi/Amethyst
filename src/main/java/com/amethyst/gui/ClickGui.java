package com.amethyst.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends GuiScreen {
    private static final int GUI_WIDTH = 200;
    private static final int GUI_HEIGHT = 150;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;

    private List<GuiButton> solversButtons;
    private List<GuiButton> terminalsButtons;
    private List<GuiButton> devicesButtons;
    private List<GuiButton> qolButtons;

    private GuiTextField solversTextField;
    private GuiTextField terminalsTextField;
    private GuiTextField devicesTextField;
    private GuiTextField qolTextField;

    private static final KeyBinding openGuiKey = new KeyBinding("key.openGui", Keyboard.KEY_G, "key.categories.amethyst");

    public ClickGui() {
        solversButtons = new ArrayList<>();
        terminalsButtons = new ArrayList<>();
        devicesButtons = new ArrayList<>();
        qolButtons = new ArrayList<>();
    }

    @Override
    public void initGui() {
        int centerX = (width - GUI_WIDTH) / 2;
        int centerY = (height - GUI_HEIGHT) / 2;

        // Add buttons for solvers section
        solversButtons.add(new GuiButton(0, centerX + PADDING, centerY + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "Solver 1"));
        solversButtons.add(new GuiButton(1, centerX + PADDING, centerY + PADDING + BUTTON_HEIGHT + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "Solver 2"));

        // Add buttons for terminals section
        terminalsButtons.add(new GuiButton(2, centerX + PADDING, centerY + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "Terminal 1"));
        terminalsButtons.add(new GuiButton(3, centerX + PADDING, centerY + PADDING + BUTTON_HEIGHT + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "Terminal 2"));

        // Add buttons for devices section
        devicesButtons.add(new GuiButton(4, centerX + PADDING, centerY + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "Device 1"));
        devicesButtons.add(new GuiButton(5, centerX + PADDING, centerY + PADDING + BUTTON_HEIGHT + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "Device 2"));

        // Add buttons for qol section
        qolButtons.add(new GuiButton(6, centerX + PADDING, centerY + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "QoL 1"));
        qolButtons.add(new GuiButton(7, centerX + PADDING, centerY + PADDING + BUTTON_HEIGHT + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, "QoL 2"));

        // Add text fields
        solversTextField = new GuiTextField(8, fontRendererObj, centerX + PADDING, centerY + PADDING + 2 * (BUTTON_HEIGHT + PADDING), BUTTON_WIDTH, BUTTON_HEIGHT);
        terminalsTextField = new GuiTextField(9, fontRendererObj, centerX + PADDING, centerY + PADDING + 2 * (BUTTON_HEIGHT + PADDING), BUTTON_WIDTH, BUTTON_HEIGHT);
        devicesTextField = new GuiTextField(10, fontRendererObj, centerX + PADDING, centerY + PADDING + 2 * (BUTTON_HEIGHT + PADDING), BUTTON_WIDTH, BUTTON_HEIGHT);
        qolTextField = new GuiTextField(11, fontRendererObj, centerX + PADDING, centerY + PADDING + 2 * (BUTTON_HEIGHT + PADDING), BUTTON_WIDTH, BUTTON_HEIGHT);

        // Register key binding
        ClientRegistry.registerKeyBinding(openGuiKey);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        // Handle button actions
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        solversTextField.textboxKeyTyped(typedChar, keyCode);
        terminalsTextField.textboxKeyTyped(typedChar, keyCode);
        devicesTextField.textboxKeyTyped(typedChar, keyCode);
        qolTextField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        solversTextField.mouseClicked(mouseX, mouseY, mouseButton);
        terminalsTextField.mouseClicked(mouseX, mouseY, mouseButton);
        devicesTextField.mouseClicked(mouseX, mouseY, mouseButton);
        qolTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int centerX = (width - GUI_WIDTH) / 2;
        int centerY = (height - GUI_HEIGHT) / 2;
        drawRect(centerX, centerY, centerX + GUI_WIDTH, centerY + GUI_HEIGHT, 0x80000000); // Transparent background

        for (GuiButton button : solversButtons) {
            button.drawButton(mc, mouseX, mouseY);
        }
        for (GuiButton button : terminalsButtons) {
            button.drawButton(mc, mouseX, mouseY);
        }
        for (GuiButton button : devicesButtons) {
            button.drawButton(mc, mouseX, mouseY);
        }
        for (GuiButton button : qolButtons) {
            button.drawButton(mc, mouseX, mouseY);
        }

        solversTextField.drawTextBox();
        terminalsTextField.drawTextBox();
        devicesTextField.drawTextBox();
        qolTextField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static void open() {
        Minecraft.getMinecraft().displayGuiScreen(new ClickGui());
    }
}
