package com.amethyst;

import com.amethyst.gui.GameInfoGui;
import com.amethyst.gui.ClickGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@Mod(modid = AmethystClient.MODID, version = AmethystClient.VERSION, name = AmethystClient.NAME)
public class AmethystClient {
    public static final String MODID = "amethyst";
    public static final String VERSION = "1.0";
    public static final String NAME = "Amethyst Client";

    private GameInfoGui gameInfoGui;
    private static final KeyBinding openGuiKey = new KeyBinding("key.openGui", Keyboard.KEY_G, "key.categories.amethyst");

    @EventHandler
    public void init(FMLInitializationEvent event) {
        gameInfoGui = new GameInfoGui();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(gameInfoGui);
        ClientRegistry.registerKeyBinding(openGuiKey);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        com.amethyst.commands.CommandManager.registerCommands(event);
    }
}
