package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode(callSuper = true)
@Value
@AllArgsConstructor(access = PRIVATE)
public class EvalFailure extends EvalResult {
    Named propertyName;
    Reasons reasons;
    ImmutableFiniteIterable<Cause> causes;

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

    public static EvalFailure evalFailure(Named propertyName,
                                          Reasons reasons) {
        return new EvalFailure(propertyName, reasons, Vector.empty());
    }

    public static EvalFailure evalFailure(Named propertyName,
                                          Reasons reasons,
                                          Iterable<Cause> causes) {
        return new EvalFailure(propertyName, reasons, Vector.copyFrom(causes));
    }
}
