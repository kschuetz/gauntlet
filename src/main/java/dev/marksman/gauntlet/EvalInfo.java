package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

@Wither
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EvalInfo<A> {
    private final Prop<A> property;
    private final A data;
    private final EvalState evalState;
    private final Context context;
    private final int index;
}
