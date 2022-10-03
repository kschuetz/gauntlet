package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;

public abstract class Cause implements CoProduct2<Cause.Explanation, Cause.PropertyFailed, Cause> {
    public static Explanation explanation(Named propertyName, Reasons reasons) {
        return new Explanation(propertyName, reasons);
    }

    public static PropertyFailed propertyFailed(EvalFailure failure) {
        return new PropertyFailed(failure);
    }

    public static Explanation propertyPassed(Named propertyName) {
        return new Explanation(propertyName, Reasons.reasons("passed"));
    }

    public static final class Explanation extends Cause {
        private final Named propertyName;
        private final Reasons reasons;

        private Explanation(Named propertyName, Reasons reasons) {
            this.propertyName = propertyName;
            this.reasons = reasons;
        }

        @Override
        public <R> R match(Fn1<? super Explanation, ? extends R> aFn, Fn1<? super PropertyFailed, ? extends R> bFn) {
            return aFn.apply(this);
        }

        public Named getPropertyName() {
            return this.propertyName;
        }

        public Reasons getReasons() {
            return this.reasons;
        }

        public String toString() {
            return "Cause.Explanation(propertyName=" + this.getPropertyName() + ", reasons=" + this.getReasons() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Explanation)) return false;
            final Explanation other = (Explanation) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$propertyName = this.getPropertyName();
            final Object other$propertyName = other.getPropertyName();
            if (this$propertyName == null ? other$propertyName != null : !this$propertyName.equals(other$propertyName))
                return false;
            final Object this$reasons = this.getReasons();
            final Object other$reasons = other.getReasons();
            return this$reasons == null ? other$reasons == null : this$reasons.equals(other$reasons);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Explanation;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $propertyName = this.getPropertyName();
            result = result * PRIME + ($propertyName == null ? 43 : $propertyName.hashCode());
            final Object $reasons = this.getReasons();
            result = result * PRIME + ($reasons == null ? 43 : $reasons.hashCode());
            return result;
        }
    }

    public static final class PropertyFailed extends Cause {
        private final EvalFailure failure;

        private PropertyFailed(EvalFailure failure) {
            this.failure = failure;
        }

        @Override
        public <R> R match(Fn1<? super Explanation, ? extends R> aFn, Fn1<? super PropertyFailed, ? extends R> bFn) {
            return bFn.apply(this);
        }

        public EvalFailure getFailure() {
            return this.failure;
        }

        public String toString() {
            return "Cause.PropertyFailed(failure=" + this.getFailure() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof PropertyFailed)) return false;
            final PropertyFailed other = (PropertyFailed) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$failure = this.getFailure();
            final Object other$failure = other.getFailure();
            return this$failure == null ? other$failure == null : this$failure.equals(other$failure);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof PropertyFailed;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $failure = this.getFailure();
            result = result * PRIME + ($failure == null ? 43 : $failure.hashCode());
            return result;
        }
    }
}
