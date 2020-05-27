package dev.marksman.gauntlet;

import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;

import java.util.Iterator;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.SampleBlock.sampleBlock;

final class IteratorSampleReader<A> implements SampleReader<A> {
    private final Iterator<A> source;

    private IteratorSampleReader(Iterator<A> source) {
        this.source = source;
    }

    public static <A> IteratorSampleReader<A> iteratorSampleReader(Iterator<A> source) {
        return new IteratorSampleReader<>(source);
    }

    @Override
    public SampleBlock<A> readBlock(int size) {
        VectorBuilder<A> builder = Vector.builder(size);
        int i = 0;
        while (i < size && source.hasNext()) {
            builder = builder.add(source.next());
            i += 1;
        }
        return sampleBlock(builder.build(), nothing());
    }
}
