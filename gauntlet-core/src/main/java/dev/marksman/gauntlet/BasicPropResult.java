package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.gauntlet.FailureReason.failureReason;
import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class BasicPropResult implements CoProduct2<Success, FailureReason, BasicPropResult> {
    private static BasicPropResult SUCCESS = new BasicPropResult(Choice2.a(Success.success()));

    Choice2<Success, FailureReason> underlying;

    @Override
    public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super FailureReason, ? extends R> bFn) {
        return underlying.match(aFn, bFn);
    }

    public static BasicPropResult success() {
        return SUCCESS;
    }

    public static BasicPropResult failureWithReason(String reason) {
        return new BasicPropResult(Choice2.b(failureReason(reason)));
    }

}
