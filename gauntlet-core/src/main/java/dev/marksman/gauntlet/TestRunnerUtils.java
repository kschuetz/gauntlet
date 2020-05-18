package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;

import static com.jnape.palatable.lambda.io.IO.io;

final class TestRunnerUtils {
    static <A> ImmutableVector<A> readBlock(int size, Iterator<A> source) {
        VectorBuilder<A> builder = Vector.builder(size);
        int i = 0;
        while (i < size && source.hasNext()) {
            builder = builder.add(source.next());
            i += 1;
        }
        return builder.build();
    }

    static Duration getBlockTimeout(LocalDateTime deadline, LocalDateTime now) {
        return now.isBefore(deadline)
                ? Duration.between(now, deadline)
                : Duration.ZERO;
    }

    static IO<LocalDateTime> getDeadline(Duration timeout) {
        return io(() -> LocalDateTime.now().plus(timeout));
    }
}
