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
    FailureReasons failureReasons;
    ImmutableFiniteIterable<EvalFailure> causes;

    @Override
    public boolean isSuccess() {
        return false;
    }

    public EvalFailure addCause(EvalFailure failure) {
        return new EvalFailure(propertyName, failureReasons, causes.append(failure));
    }

    @Override
    public <R> R match(Fn1<? super EvalSuccess, ? extends R> aFn, Fn1<? super EvalFailure, ? extends R> bFn) {
        return bFn.apply(this);
    }

    public static EvalFailure evalFailure(Named propertyName,
                                          FailureReasons failureReasons) {
        return new EvalFailure(propertyName, failureReasons, Vector.empty());
    }

    public static EvalFailure evalFailure(Named propertyName,
                                          FailureReasons failureReasons,
                                          Iterable<EvalFailure> causes) {
        return new EvalFailure(propertyName, failureReasons, Vector.copyFrom(causes));
    }
}
