package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

import static software.kes.gauntlet.Reasons.reasons;

public abstract class SimpleResult implements CoProduct2<SimpleResult.Pass, SimpleResult.Fail, SimpleResult> {
    public static Pass pass() {
        return Pass.INSTANCE;
    }

    public static Fail fail(String primaryReason) {
        return new Fail(reasons(primaryReason));
    }

    public static Fail fail(Reasons reasons) {
        return new Fail(reasons);
    }

    public static SimpleResult test(boolean condition, Fn0<String> reasonForFailure) {
        return condition
                ? pass()
                : fail(reasonForFailure.apply());
    }

    public static SimpleResult test(boolean condition, String reasonForFailure) {
        return condition
                ? pass()
                : fail(reasonForFailure);
    }

    public static Monoid<SimpleResult> and() {
        return And.INSTANCE;
    }

    public abstract SimpleResult and(SimpleResult other);

    public static final class Pass extends SimpleResult {
        private static final Pass INSTANCE = new Pass();

        private Pass() {
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return aFn.apply(this);
        }

        @Override
        public SimpleResult and(SimpleResult other) {
            return other;
        }

        public String toString() {
            return "SimpleResult.Pass()";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Pass)) return false;
            final Pass other = (Pass) o;
            if (!other.canEqual(this)) return false;
            return super.equals(o);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Pass;
        }
    }

    public static final class Fail extends SimpleResult {
        private final Reasons reasons;

        private Fail(Reasons reasons) {
            this.reasons = reasons;
        }

        public String getPrimaryReason() {
            return reasons.getPrimary();
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return bFn.apply(this);
        }

        @Override
        public SimpleResult and(SimpleResult other) {
            return other.match(__ -> this,
                    fail2 -> fail(this.reasons.concat(fail2.getReasons())));
        }

        public Reasons getReasons() {
            return this.reasons;
        }

        public String toString() {
            return "SimpleResult.Fail(reasons=" + this.getReasons() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Fail)) return false;
            final Fail other = (Fail) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$reasons = this.getReasons();
            final Object other$reasons = other.getReasons();
            return this$reasons == null ? other$reasons == null : this$reasons.equals(other$reasons);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Fail;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $reasons = this.getReasons();
            result = result * PRIME + ($reasons == null ? 43 : $reasons.hashCode());
            return result;
        }
    }

    private static class And implements Monoid<SimpleResult> {
        private static final And INSTANCE = new And();

        @Override
        public SimpleResult identity() {
            return pass();
        }

        @Override
        public SimpleResult checkedApply(SimpleResult result1, SimpleResult result2) {
            return result1.and(result2);
        }
    }
}
