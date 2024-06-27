package net.malfact.gamecore.script;

import net.malfact.gamecore.GameCore;
import org.luaj.vm2.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LuaScript {
    private final String path;
    private final String name;

    private LuaValue chunk;
    private LuaValue environment;

    LuaScript(File file) {
        this.path = file.getPath();
        this.name = file.getName().substring(0, file.getName().lastIndexOf('.'));
        GameCore.getInstance().logInfo(path);
    }

    public String getName() {
        return name;
    }

    void load(Globals globals, LuaTable environment) {
        Path path = Paths.get(this.path);

        if (!Files.exists(path) || Files.isDirectory(path)) {
            GameCore.getInstance().logError("Unable to load " + this + ": File does not exist!");
            chunk = null;
            this.environment = LuaConstant.NIL;
            return;
        }

        if (environment == null || environment.isnil())
            this.environment = LuaTable.tableOf();

        InputStream fileStream;
        try {
            fileStream = Files.newInputStream(path);
            InputStreamReader fileReader = new InputStreamReader(fileStream);

            this.environment = environment;
            chunk = globals.load(fileReader, name, environment);

        } catch (IOException e) {
            GameCore.getInstance().logError("Unable to load " + name + ":\n" + e.getMessage());
            chunk = null;
            this.environment = LuaConstant.NIL;
            return;
        } catch (LuaError e) {
            GameCore.getInstance().logError("Error in " + this + ":\n" + e.getMessage());
            chunk = null;
            this.environment = LuaConstant.NIL;
            return;
        }

        GameCore.getInstance().logInfo("Loaded Script " + this);
    }

    public LuaValue getEnvironment() {
        return this.environment;
    }

    public void run() {
        if (chunk == null || chunk.isnil())
            return;
        try {
            chunk.call();
        } catch (LuaError e) {
            GameCore.getInstance().logError("Error in " + this + ":\n" + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return this.name + ".lua";
    }
}
