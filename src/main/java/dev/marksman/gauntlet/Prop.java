package dev.marksman.gauntlet;

public interface Prop<A> {
    EvalResult<A> test(A data);
}
