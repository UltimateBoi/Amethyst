package com.amethyst;

import com.amethyst.gui.GameInfoGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = AmethystClient.MODID, version = AmethystClient.VERSION, name = AmethystClient.NAME)
public class AmethystClient {
    public static final String MODID = "amethyst";
    public static final String VERSION = "1.0";
    public static final String NAME = "Amethyst Client";

    private GameInfoGui gameInfoGui;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        gameInfoGui = new GameInfoGui();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(gameInfoGui);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        com.amethyst.commands.CommandManager.registerCommands(event);
    }
}