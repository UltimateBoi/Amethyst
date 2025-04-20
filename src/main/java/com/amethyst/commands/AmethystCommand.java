package com.amethyst.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import java.util.Collections;
import java.util.List;
import com.amethyst.AmethystMessage;
import com.amethyst.Format;
import com.amethyst.gui.ClickGui; // Import ClickGui

public class AmethystCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "amethyst";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("am");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/amethyst or /am";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
        } else {
            ClickGui.open(); // Open the ClickGUI
        }
    }

    private void showHelp(ICommandSender sender) {
        String formattedMessage = Format.format("&7Amethyst Client v1.0 &8- &dA simple dungeons qol by &lUltimate&r&d!");
        AmethystMessage.sendClientMessage(sender, formattedMessage);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
