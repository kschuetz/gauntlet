package software.kes.gauntlet;

interface SampleReader<A> {
    SampleBlock<A> readBlock(int size);
}
