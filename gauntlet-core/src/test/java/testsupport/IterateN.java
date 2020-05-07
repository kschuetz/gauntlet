package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;

public final class IterateN {
    private IterateN() {
    }

    public static <A> A iterateN(int n, A seed, Fn1<? super A, ? extends A> fn) {
        A result = seed;
        for (int i = 0; i < n; i++) {
            result = fn.apply(result);
        }
        return result;
    }
}
