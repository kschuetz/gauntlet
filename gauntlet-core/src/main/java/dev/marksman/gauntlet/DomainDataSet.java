package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainDataSet<A> implements DataSet<A> {
    private final ImmutableVector<A> values;

    @Override
    public Maybe<SupplyFailure> getSupplyFailure() {
        return nothing();
    }

    public static <A> DomainDataSet<A> domainDataSet(Iterable<A> values) {
        return new DomainDataSet<>(Vector.copyFrom(values));
    }
}
