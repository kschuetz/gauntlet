package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

public interface ValueSupplier<A> {

    Result<Seed, Maybe<A>> getNext(Seed input);

}
