package org.readutf.ui;

import team.unnamed.creative.ResourcePack;

@FunctionalInterface
public interface Module {

    /**
     * Applies the module to the given resource pack.
     * @param resourcePack the resource pack to apply the module to
     */
    void apply(ResourcePack resourcePack);
}
