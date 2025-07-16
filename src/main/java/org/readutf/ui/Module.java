package org.readutf.ui;

import team.unnamed.creative.ResourcePack;

@FunctionalInterface
public interface Module {

    void apply(ResourcePack resourcePack);

}
