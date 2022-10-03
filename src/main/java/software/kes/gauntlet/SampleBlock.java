package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;

final class SampleBlock<A> {
    private final ImmutableVector<A> samples;
    private final Maybe<SupplyFailure> supplyFailure;

    public static <A> SampleBlock<A> sampleBlock(Iterable<A> samples, Maybe<SupplyFailure> supplyFailure) {
        return new SampleBlock<>(Vector.copyFrom(samples), supplyFailure);
    }

    private SampleBlock(ImmutableVector<A> samples, Maybe<SupplyFailure> supplyFailure) {
        this.samples = samples;
        this.supplyFailure = supplyFailure;
    }

    public ImmutableVector<A> getSamples() {
        return samples;
    }

    public Maybe<SupplyFailure> getSupplyFailure() {
        return supplyFailure;
    }

    public boolean isEmpty() {
        return samples.isEmpty();
    }
}
