package dev.marksman.gauntlet;

final class DomainTestResult<A> {
    private final TestResult<A> result;

    private DomainTestResult(TestResult<A> result) {
        this.result = result;
    }

    static <A> DomainTestResult<A> domainTestResult(TestResult<A> testResult) {
        return new DomainTestResult<>(testResult);
    }

    public TestResult<A> getResult() {
        return this.result;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DomainTestResult)) return false;
        final DomainTestResult<?> other = (DomainTestResult<?>) o;
        final Object this$result = this.getResult();
        final Object other$result = other.getResult();
        return this$result == null ? other$result == null : this$result.equals(other$result);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $result = this.getResult();
        result = result * PRIME + ($result == null ? 43 : $result.hashCode());
        return result;
    }

    public String toString() {
        return "DomainTestResult(result=" + this.getResult() + ")";
    }
}
