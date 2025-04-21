package com.amethyst.commands;

import com.amethyst.gui.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

public class AmethystCommand extends CommandBase {

    private final ClickGui clickGui = new ClickGui();

    @Override
    public String getCommandName() {
        return "amethyst";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("am");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/amethyst or /am to open the ClickGui";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Minecraft.getMinecraft().displayGuiScreen(clickGui);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
