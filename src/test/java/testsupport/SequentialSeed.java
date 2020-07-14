package testsupport;

import dev.marksman.kraftwerk.Seed;

public class SequentialSeed implements Seed {
    private final long n;

    public static SequentialSeed initialSequentialSeed() {
        return new SequentialSeed(0);
    }

    private SequentialSeed(long n) {
        this.n = n;
    }

    @Override
    public long getSeedValue() {
        return n;
    }

    @Override
    public Seed perturb(long value) {
        return this;
    }

    @Override
    public Seed setNextSeedValue(long value) {
        return new SequentialSeed(n + 1);
    }
}
