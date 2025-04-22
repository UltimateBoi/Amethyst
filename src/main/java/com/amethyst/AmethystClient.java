package com.amethyst;

import com.amethyst.gui.GameInfoGui;
import com.amethyst.gui.ClickGui;
import com.amethyst.module.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import com.amethyst.commands.AmethystCommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import net.minecraftforge.client.event.MouseEvent;

@Mod(modid = AmethystClient.MODID, version = AmethystClient.VERSION, name = AmethystClient.NAME)
public class AmethystClient {
    public static final String MODID = "amethyst";
    public static final String VERSION = "1.0";
    public static final String NAME = "Amethyst Client";

    private GameInfoGui gameInfoGui;
    private static final KeyBinding openGuiKey = new KeyBinding("ClickGUI", Keyboard.KEY_G, "key.categories.amethyst");
    public static final ModuleManager moduleManager = new ModuleManager();
    
    // Single instance of ClickGui
    private static ClickGui clickGui = null;
    
    // Flag to control GUI visibility
    public static boolean guiVisible = false;
    
    public static ClickGui getClickGui() {
        if (clickGui == null) {
            System.out.println("[Amethyst] Creating new ClickGui instance");
            clickGui = new ClickGui();
        }
        return clickGui;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Initialize and register the ClickGui to receive events
        clickGui = getClickGui();
        MinecraftForge.EVENT_BUS.register(clickGui);
        
        gameInfoGui = new GameInfoGui();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(gameInfoGui);
        ClientRegistry.registerKeyBinding(openGuiKey);
        ClientCommandHandler.instance.registerCommand(new AmethystCommand());
        
        System.out.println("[Amethyst] Client initialized successfully!");
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        com.amethyst.commands.CommandManager.registerCommands(event);
    }

    // Use HIGH priority to catch the event before other handlers
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Allow the GUI toggle key to work even when GUI is open
        if (Keyboard.getEventKey() == openGuiKey.getKeyCode() && Keyboard.getEventKeyState()) {
            toggleGui("key press");
            return;
        }

        // Do not attempt to cancel the event; instead, handle GUI visibility logic here
        if (guiVisible) {
            // Prevent game actions by ignoring further processing of the key input
            KeyBinding.unPressAllKeys(); // Release all keys to avoid unintended actions
        }
    }
    
    // Add a new mouse event handler with proper cancellation
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseEvent(MouseEvent event) {
        if (guiVisible) {
            event.setCanceled(true); // Block all mouse events while GUI is open
        }
    }
    
    // Modified mouse input handler that doesn't try to cancel the event
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        // This event is not cancellable, but we have the MouseEvent handler above
        // that properly cancels mouse interactions with the game world
        
        // If GUI is open, we still want to handle mouse movements for GUI interaction
        if (guiVisible) {
            // Reset all MouseButtons when the GUI is open to prevent actions leaking through
            for (int i = 0; i < Mouse.getButtonCount(); i++) {
                // Force game to think no mouse buttons are pressed
                Mouse.poll();
            }
        }
    }
    
    // Add a new event handler to block keyboard typing events
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onKeyTyped(net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent event) {
        if (guiVisible) {
            int key = Keyboard.getEventKey();
            boolean keyState = Keyboard.getEventKeyState();

            if (key == Keyboard.KEY_ESCAPE && keyState) {
                // Close the GUI when ESC is pressed
                toggleGui("escape key");
                event.setCanceled(true); // Prevent the pause menu from opening
            } else if (key == Keyboard.KEY_T && keyState) {
                // Allow the chat to open when T is pressed
                Minecraft.getMinecraft().displayGuiScreen(new net.minecraft.client.gui.GuiChat());
                event.setCanceled(true); // Prevent other actions
            } else {
                // Cancel all other key inputs
                event.setCanceled(true);
            }
        }
    }
    
    // Toggle GUI visibility instead of opening a screen
    public static void toggleGui(String source) {
        try {
            guiVisible = !guiVisible;
            Minecraft mc = Minecraft.getMinecraft();
            
            if (guiVisible) {
                System.out.println("[Amethyst] GUI shown from: " + source);
                mc.inGameHasFocus = false; // Allow mouse interaction without affecting game
                mc.mouseHelper.ungrabMouseCursor(); // Show the mouse cursor

                // Pause the game when GUI is open
                mc.displayGuiScreen(null); // Close any open GUI
                mc.setIngameNotInFocus(); // Pause the game

                // Force release of all currently pressed keys to prevent stuck inputs
                KeyBinding.unPressAllKeys();
                
                // Clear mouse button states
                while (Mouse.next()) {
                    // Consume any pending mouse events
                }
            } else {
                System.out.println("[Amethyst] GUI hidden from: " + source);
                mc.inGameHasFocus = true; // Return focus to game
                mc.mouseHelper.grabMouseCursor(); // Hide the mouse cursor
                mc.setIngameFocus(); // Resume the game
            }
        } catch (Exception e) {
            System.err.println("[Amethyst] Critical error toggling GUI from " + source + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
