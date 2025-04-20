package com.amethyst.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandManager {
    // Registers the AmethystCommand during server start
    public static void registerCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(new AmethystCommand());
    }
}
