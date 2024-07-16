package net.malfact.gamecore;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings({"LoggingSimilarMessage", "unused"})
public class LuaScript {
    private final String path;
    private final String name;

    private LuaValue chunk;

    LuaScript(File file) {
        this.path = file.getPath();
        this.name = file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    public String getName() {
        return name;
    }

    void load(Globals globals, LuaTable environment) {
        Path path = Paths.get(this.path);

        InputStream fileStream;
        try {
            fileStream = Files.newInputStream(path);
            InputStreamReader fileReader = new InputStreamReader(fileStream);

            chunk = globals.load(fileReader, name, environment);
        } catch (IOException e) {
            GameCore.logger().error("Unable to load {}:\n\t{}", name, e.getMessage());
            chunk = null;
            return;
        } catch (LuaError e) {
            GameCore.logger().error("Error in {}:\n\t{}", this, e.getMessage());
            chunk = null;
            return;
        }

        GameCore.logger().info("Loaded Script {}", this);
    }

    public boolean run() {
        if (chunk == null || chunk.isnil()) {
            GameCore.logger().error("Error in {}: No Chunk!", this);
            return false;
        }

        try {
            chunk.call();
        } catch (LuaError e) {
            GameCore.logger().error("Error in {}:\n{}", this, e.getMessage());
            return false;
        }

        return true;
    }

    public boolean isValid() {
        return chunk != null;
    }

    @Override
    public String toString() {
        return this.name + ".lua";
    }
}
