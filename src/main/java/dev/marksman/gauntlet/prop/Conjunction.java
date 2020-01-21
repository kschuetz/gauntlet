package dev.marksman.gauntlet.prop;

import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
class Conjunction<A> implements Prop<A> {
    private final ImmutableNonEmptyFiniteIterable<Prop<A>> operands;

    @Override
    public Prop<A> and(Prop<A> other) {
        if (other instanceof Conjunction<?>) {
            return new Conjunction<>(operands.concat(((Conjunction<A>) other).operands));
        } else {
            return new Conjunction<>(operands.append(other));
        }
    }

    @Override
    public EvalResult test(A data) {
        throw new UnsupportedOperationException("todo");
    }
}
