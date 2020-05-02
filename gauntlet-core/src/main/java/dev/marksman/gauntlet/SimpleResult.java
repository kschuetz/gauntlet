package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;

import static dev.marksman.gauntlet.Reasons.reasons;

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

    public static final class Pass extends SimpleResult {
        private static final Pass INSTANCE = new Pass();

        private Pass() {
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return aFn.apply(this);
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

}
