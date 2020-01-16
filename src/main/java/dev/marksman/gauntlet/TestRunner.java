package dev.marksman.gauntlet;

class TestRunner<A> {

    static <A> TestResult<A> runTest(TestData<A> testData) {
        return new TestResult<>(testData.getInitialSeedValue());
    }

}
