package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import java.util.function.Consumer;

final class ConsumerProp<A> implements Prop<A> {
    private final String name;
    private final Consumer<A> consumer;

    ConsumerProp(String name, Consumer<A> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    @Override
    public EvalResult test(Context context, A data) {
        consumer.accept(data);
        return EvalResult.success();
    }

    @Override
    public String getName() {
        return name;
    }

    static <A> ConsumerProp<A> consumerProp(String name, Consumer<A> consumer) {
        return new ConsumerProp<>(name, consumer);
    }
}
