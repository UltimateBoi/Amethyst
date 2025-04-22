package com.amethyst.commands;

import com.amethyst.AmethystClient;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class AmethystCommand extends CommandBase {

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
        return "/amethyst or /am to toggle the ClickGui";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            System.out.println("[Amethyst] Command executed, toggling GUI");
            
            // Schedule the GUI toggle in the next game tick to ensure chat is closed first
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc != null) {
                mc.addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        // Use the centralized method for opening the GUI
                        AmethystClient.toggleGui("command");
                    }
                });
            } else {
                sender.addChatMessage(new ChatComponentText("§c[Amethyst] Error: Minecraft instance is null"));
            }
        } catch (Exception e) {
            System.err.println("[Amethyst] Command error: " + e.getMessage());
            e.printStackTrace();
            sender.addChatMessage(new ChatComponentText("§c[Amethyst] Error: " + e.getMessage()));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
