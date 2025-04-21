package com.amethyst.features;

import net.minecraft.client.Minecraft;

// Base class for Modules and potentially other features
public class Feature {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    private String name;

    public Feature(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Optional lifecycle methods - can be overridden by modules
    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onTick() {}
    // Add other common methods or properties needed by features
}
