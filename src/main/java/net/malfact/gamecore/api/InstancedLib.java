package net.malfact.gamecore.api;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.script.Instance;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.Objects;

public abstract class InstancedLib extends TwoArgFunction {

    /**
     * The {@code Game} instance.
     */
    protected final Instance instance;

    public InstancedLib(Instance instance) {
        this.instance = Objects.requireNonNull(instance);
        GameCore.logDebug("--> Created " + this.getClass().getSimpleName());
    }
}
