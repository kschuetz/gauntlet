package dev.marksman.gauntlet;

interface SampleReader<A> {
    SampleBlock<A> readBlock(int size);
}
