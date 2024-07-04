package net.malfact.gamecore.api.userdata;

import net.malfact.gamecore.script.Instance;

public abstract class InstancedUserdataProvider implements UserdataProvider {

    protected final Instance instance;

    public InstancedUserdataProvider(Instance instance) {
        this.instance = instance;
    }
}
