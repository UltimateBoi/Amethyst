package com.amethyst.module;

import com.amethyst.features.Feature;
import com.amethyst.settings.Setting; // Assuming Setting class exists
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Module extends Feature {

    private String description;
    private Category category;
    private int keybind;
    private boolean enabled;
    public boolean hidden; // Used by ClickGui to hide modules
    private List<Setting> settings = new ArrayList<>(); // Placeholder for settings

    public Module(String name, String description, Category category) {
        super(name);
        this.description = description;
        this.category = category;
        this.keybind = Keyboard.KEY_NONE; // Default to no keybind
        this.enabled = false;
        this.hidden = false; // Modules are visible by default
    }

    // --- Enable / Disable / Toggle ---

    public void enable() {
        this.enabled = true;
        // Optional: Register listeners or perform actions on enable
        // AmethystClient.EVENT_BUS.register(this); // Example
        onEnable(); // Call lifecycle method
    }

    public void disable() {
        this.enabled = false;
        // Optional: Unregister listeners or perform actions on disable
        // AmethystClient.EVENT_BUS.unregister(this); // Example
        onDisable(); // Call lifecycle method
    }

    public void toggle() {
        if (this.enabled) {
            disable();
        } else {
            enable();
        }
    }

    // --- Getters --- 

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeybind() {
        return keybind;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    // --- Setters --- 

    public void setEnabled(boolean enabled) {
        if(enabled) enable(); else disable();
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    // --- Settings Management --- 

    // Example: Method to add a setting
    protected void addSetting(Setting setting) {
        this.settings.add(setting);
    }

    // --- Category Enum --- 
    // Matches the categories used in the previous Library.java example
    // Add/remove categories as needed for Amethyst
    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        MISC("Misc"),
        PLAYER("Player"),
        VISUAL("Visual"); // Added Visual for the Gui module

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
