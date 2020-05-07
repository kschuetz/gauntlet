package dev.marksman.gauntlet;

final class GeneratorTestResult<A> {
    private final TestResult<A> result;
    private final long initialSeedValue;

    private GeneratorTestResult(TestResult<A> result, long initialSeedValue) {
        this.result = result;
        this.initialSeedValue = initialSeedValue;
    }

    static <A> GeneratorTestResult<A> generatorTestResult(TestResult<A> testResult,
                                                          long initialSeedValue) {
        return new GeneratorTestResult<>(testResult, initialSeedValue);
    }

    public GeneratorTestResult<A> withResult(TestResult<A> result) {
        return new GeneratorTestResult<>(result, initialSeedValue);
    }

    public TestResult<A> getResult() {
        return this.result;
    }

    public long getInitialSeedValue() {
        return this.initialSeedValue;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GeneratorTestResult)) return false;
        final GeneratorTestResult<?> other = (GeneratorTestResult<?>) o;
        final Object this$result = this.getResult();
        final Object other$result = other.getResult();
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) return false;
        return this.getInitialSeedValue() == other.getInitialSeedValue();
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $result = this.getResult();
        result = result * PRIME + ($result == null ? 43 : $result.hashCode());
        final long $initialSeedValue = this.getInitialSeedValue();
        result = result * PRIME + (int) ($initialSeedValue >>> 32 ^ $initialSeedValue);
        return result;
    }

    public String toString() {
        return "GeneratorTestResult(result=" + this.getResult() + ", initialSeedValue=" + this.getInitialSeedValue() + ")";
    }
}
