package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Seed;

public final class GeneratedSample<A> {
    private final Seed inputSeed;
    private final A value;

    public static <A> GeneratedSample<A> generatedSample(Seed inputSeed, A value) {
        return new GeneratedSample<>(inputSeed, value);
    }

    private GeneratedSample(Seed inputSeed, A value) {
        this.inputSeed = inputSeed;
        this.value = value;
    }

    public Seed getInputSeed() {
        return inputSeed;
    }

    public A getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratedSample<?> that = (GeneratedSample<?>) o;

        if (!inputSeed.equals(that.inputSeed)) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = inputSeed.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
