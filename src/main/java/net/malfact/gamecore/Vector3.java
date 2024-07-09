package net.malfact.gamecore;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

@SuppressWarnings("unused")
public class Vector3 {

    public static final Vector3 Zero = new Vector3(0, 0, 0);
    public static final Vector3 Up = new Vector3(0, 1.0, 0);
    public static final Vector3 Down = new Vector3(0, -1.0, 0);

    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int blockX() {
        return (int) Math.floor(x);
    }

    public int blockY() {
        return (int) Math.floor(y);
    }

    public int blockZ() {
        return (int) Math.floor(z);
    }

    @NotNull
    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    @NotNull
    public Vector3 add(@NotNull Vector3 other) {
        return new Vector3(
            this.x + other.x,
            this.y + other.y,
            this.z + other.z
        );
    }

    @NotNull
    public Vector3 subtract(@NotNull Vector3 other) {
        return new Vector3(
            this.x - other.x,
            this.y - other.y,
            this.z - other.z
        );
    }

    @NotNull
    public Vector3 multiply(@NotNull Vector3 other) {
        return new Vector3(
            this.x * other.x,
            this.y * other.y,
            this.z * other.z
        );
    }

    @NotNull
    public Vector3 multiply(double scalar) {
        return new Vector3(
            this.x * scalar,
            this.y * scalar,
            this.z * scalar
        );
    }

    @NotNull
    public Vector3 divide(@NotNull Vector3 other) {
        return new Vector3(
            this.x / other.x,
            this.y / other.y,
            this.z / other.z
        );
    }

    @NotNull
    public Vector3 divide(double scalar) {
        return new Vector3(
            this.x / scalar,
            this.y / scalar,
            this.z / scalar
        );
    }

    @NotNull
    public Vector3 negate() {
        return this.multiply(-1.0);
    }

    public double lengthSquared() {
        return (x * x) + (y * y) + (z * z);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double distanceSquared(@NotNull Vector3 other) {
        return Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2) + Math.pow(this.z - other.z, 2);
    }

    public double distance(@NotNull Vector3 other) {
        return Math.sqrt(distanceSquared(other));
    }

    public double dot(@NotNull Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z + other.z;
    }

    public float angle(@NotNull Vector3 other) {
        return (float) Math.acos(dot(other));
    }

    @NotNull
    public Vector3 midpoint(@NotNull Vector3 other) {
        return new Vector3(
            (x + other.x) / 2.0,
            (y + other.y) / 2.0,
            (z + other.z) / 2.0
        );
    }

    @NotNull
    public Vector3 crossProduct(@NotNull Vector3 other) {
        return new Vector3(
            y * other.z - other.y * z,
            z * other.x - other.z * x,
            x * other.y - other.x * y
        );
    }

    @NotNull
    public Vector3 floor() {
        return unaryOp(this, Math::floor);
    }

    @NotNull
    public Vector3 ceil() {
        return unaryOp(this, Math::ceil);
    }

    @NotNull
    public Vector3 normalize() {
        return this.divide(length());
    }

    @Override
    public String toString() {
        return "<" + x + "," + y + "," + z + ">";
    }

    @NotNull
    public static Vector3 max(@NotNull Vector3 vec1, @NotNull Vector3 vec2) {
        return binaryOp(vec1, vec2, Math::max);
    }

    @NotNull
    public static Vector3 min(@NotNull Vector3 vec1, @NotNull Vector3 vec2) {
        return binaryOp(vec1, vec2, Math::min);
    }

    private static final Random random = new Random();

    @NotNull
    public static Vector3 random() {
        return new Vector3(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    @NotNull
    public static Vector3 unaryOp(@NotNull Vector3 vec, DoubleUnaryOperator operator) {
        return new Vector3(
            operator.applyAsDouble(vec.x),
            operator.applyAsDouble(vec.y),
            operator.applyAsDouble(vec.z)
        );
    }

    @NotNull
    public static Vector3 binaryOp(@NotNull Vector3 vec1, @NotNull Vector3 vec2, DoubleBinaryOperator operator) {
        return new Vector3(
            operator.applyAsDouble(vec1.x, vec2.x),
            operator.applyAsDouble(vec1.y, vec2.y),
            operator.applyAsDouble(vec1.z, vec2.z)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3 vec))
            return false;

        double e = 0.000001;
        return Math.abs(x - vec.x) < e && Math.abs(y - vec.y) < e && Math.abs(z - vec.z) < e;
    }
}
