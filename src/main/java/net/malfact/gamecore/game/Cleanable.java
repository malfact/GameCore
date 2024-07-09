package net.malfact.gamecore.game;

import java.util.function.Consumer;

public class Cleanable<T> {

    public static <T> Cleanable<T> of(T o, Consumer<T> clean) {
        return new Cleanable<>(o, clean);
    }

    private final T o;
    private final Consumer<T> clean;

    private Cleanable(T o, Consumer<T> clean) {
        this.o = o;
        this.clean = clean;
    }

    public void clean() {
        clean.accept(o);
    }
}
