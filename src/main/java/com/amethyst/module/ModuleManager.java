package com.amethyst.module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // TODO: Initialize modules
        // Example: modules.add(new ExampleModule());
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public void toggleModule(String name) {
        Module module = getModuleByName(name);
        if (module != null) {
            module.toggle();
        }
    }

    public List<Module.Category> getCategories() {
        return modules.stream()
                      .map(Module::getCategory)
                      .distinct()                      .distinct()
                      .collect(Collectors.toList());
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        return modules.stream()
                      .filter(module -> module.getCategory() == category)
                      .collect(Collectors.toList());
    }
}
