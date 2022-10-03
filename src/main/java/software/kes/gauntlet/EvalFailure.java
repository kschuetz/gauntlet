package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.collectionviews.Vector;
import software.kes.enhancediterables.ImmutableFiniteIterable;

public final class EvalFailure extends EvalResult {
    private final Named propertyName;
    private final Reasons reasons;
    private final ImmutableFiniteIterable<Cause> causes;

    private EvalFailure(Named propertyName, Reasons reasons, ImmutableFiniteIterable<Cause> causes) {
        this.propertyName = propertyName;
        this.reasons = reasons;
        this.causes = causes;
    }

    public static EvalFailure evalFailure(Named propertyName,
                                          Reasons reasons) {
        return new EvalFailure(propertyName, reasons, Vector.empty());
    }

    public static EvalFailure evalFailure(Named propertyName,
                                          Reasons reasons,
                                          Iterable<Cause> causes) {
        return new EvalFailure(propertyName, reasons, Vector.copyFrom(causes));
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    public EvalFailure addCause(Cause failure) {
        return new EvalFailure(propertyName, reasons, causes.append(failure));
    }

    @Override
    public <R> R match(Fn1<? super EvalSuccess, ? extends R> aFn, Fn1<? super EvalFailure, ? extends R> bFn) {
        return bFn.apply(this);
    }

    public Named getPropertyName() {
        return this.propertyName;
    }

    public Reasons getReasons() {
        return this.reasons;
    }

    public ImmutableFiniteIterable<Cause> getCauses() {
        return this.causes;
    }

    public String toString() {
        return "EvalFailure(propertyName=" + this.getPropertyName() + ", reasons=" + this.getReasons() + ", causes=" + this.getCauses() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EvalFailure)) return false;
        final EvalFailure other = (EvalFailure) o;
        if (!other.canEqual(this)) return false;
        if (!super.equals(o)) return false;
        final Object this$propertyName = this.getPropertyName();
        final Object other$propertyName = other.getPropertyName();
        if (this$propertyName == null ? other$propertyName != null : !this$propertyName.equals(other$propertyName))
            return false;
        final Object this$reasons = this.getReasons();
        final Object other$reasons = other.getReasons();
        if (this$reasons == null ? other$reasons != null : !this$reasons.equals(other$reasons)) return false;
        final Object this$causes = this.getCauses();
        final Object other$causes = other.getCauses();
        return this$causes == null ? other$causes == null : this$causes.equals(other$causes);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EvalFailure;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $propertyName = this.getPropertyName();
        result = result * PRIME + ($propertyName == null ? 43 : $propertyName.hashCode());
        final Object $reasons = this.getReasons();
        result = result * PRIME + ($reasons == null ? 43 : $reasons.hashCode());
        final Object $causes = this.getCauses();
        result = result * PRIME + ($causes == null ? 43 : $causes.hashCode());
        return result;
    }
}
