package com.amethyst;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import com.amethyst.Format; // adjust the package as needed

public class AmethystMessage {

    public static void sendClientMessage(ICommandSender sender, String message) {
        String prefix = Format.format("&c[&dAmethyst&c]&r");
        sender.addChatMessage(new ChatComponentText(prefix + " " + Format.format(message)));
    }
}
