package com.amethyst.settings;

import com.amethyst.features.Feature; // Assuming Feature base class

import java.util.function.Predicate;

public class Setting<T> {

    private final String name;
    private final T defaultValue;
    private T value;
    private T plannedValue;
    private T min;
    private T max;
    private boolean hasRestriction;
    private Predicate<T> visibility;
    private String description;
    private Feature feature; // The module this setting belongs to
    private boolean shouldRenderStringName = false; // For ClickGUI rendering

    // Constructor for unrestricted settings
    public Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.plannedValue = defaultValue;
        this.description = "";
        this.hasRestriction = false;
    }

    // Constructor for restricted settings (e.g., sliders)
    public Setting(String name, T defaultValue, T min, T max) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.description = "";
        this.hasRestriction = true;
    }

    // Constructor with visibility predicate
    public Setting(String name, T defaultValue, Predicate<T> visibility) {
        this(name, defaultValue);
        this.visibility = visibility;
    }

    // Constructor with restriction and visibility
    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility) {
        this(name, defaultValue, min, max);
        this.visibility = visibility;
    }

    // Constructor with description
    public Setting(String name, T defaultValue, String description) {
        this(name, defaultValue);
        this.description = description;
    }

    // Full constructor
    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility, String description) {
        this(name, defaultValue, min, max, visibility);
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.setPlannedValue(value);
        // Optional: Add logic here to immediately apply the change or wait for confirmation
        // For simplicity, apply immediately:
        if (this.hasRestriction) {
            this.value = clampValue(value);
        } else {
            this.value = value;
        }
        // Optional: Trigger an event or update mechanism
        // ClientEvent event = new ClientEvent(this);
        // AmethystClient.EVENT_BUS.post(event);
    }

    public T getPlannedValue() {
        return this.plannedValue;
    }

    public void setPlannedValue(T value) {
        this.plannedValue = this.hasRestriction ? clampValue(value) : value;
    }

    private T clampValue(T value) {
        if (this.min instanceof Number && this.max instanceof Number && value instanceof Number) {
            Number val = (Number) value;
            Number minVal = (Number) this.min;
            Number maxVal = (Number) this.max;
            if (val.doubleValue() < minVal.doubleValue()) {
                return (T) this.min;
            }
            if (val.doubleValue() > maxVal.doubleValue()) {
                return (T) this.max;
            }
        }
        return value;
    }

    public T getMin() {
        return this.min;
    }

    public T getMax() {
        return this.max;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return this.feature;
    }

    // --- Type Checks (similar to PigHack) ---

    public boolean isNumberSetting() {
        return this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float;
    }

    public boolean isEnumSetting() {
        return this.value instanceof Enum;
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }

    public boolean isBooleanSetting() {
        return this.value instanceof Boolean;
    }

    public boolean isBindSetting() {
        return this.value instanceof Bind;
    }

    // --- Other Methods ---

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean hasRestriction() {
        return this.hasRestriction;
    }

    public void setVisibility(Predicate<T> visibility) {
        this.visibility = visibility;
    }

    public boolean isVisible() {
        if (this.visibility == null) {
            return true;
        }
        return this.visibility.test(this.getValue());
    }

    // Method for ClickGUI rendering hints
    public Setting<T> setRenderName(boolean renderName) {
        this.shouldRenderStringName = renderName;
        return this;
    }

    public boolean shouldRenderName() {
        // By default, render name unless it's a string setting without the flag
        return this.isStringSetting() ? this.shouldRenderStringName : true;
    }

    public String getDescription() {
        return description != null ? description : "";
    }
}
