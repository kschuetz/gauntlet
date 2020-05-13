package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Seed;

public interface StatefulSupply<A> {
    GeneratorOutput<A> getNext(Seed input);
}
