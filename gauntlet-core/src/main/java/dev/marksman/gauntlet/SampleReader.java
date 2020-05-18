package dev.marksman.gauntlet;

public interface SampleReader<A> {
    SampleBlock<A> readBlock(int size);
}
