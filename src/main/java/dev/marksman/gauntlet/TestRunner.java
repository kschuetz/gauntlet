package dev.marksman.gauntlet;

class TestRunner {

    static <A> TestResult<A> runTest(TestData<A> testData) {
        return new TestResult<>(testData.getInitialSeedValue());
    }

}
