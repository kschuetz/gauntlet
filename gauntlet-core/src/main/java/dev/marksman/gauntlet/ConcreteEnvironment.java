package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
class ConcreteEnvironment implements GauntletEnvironment {

    @Getter
    Executor executor;

    @Getter
    GeneratorTestRunner generatorTestRunner;

    @Getter
    Reporter reporter;

    static ConcreteEnvironment concreteEnvironment(Executor executor, GeneratorTestRunner generatorTestRunner, Reporter reporter) {
        return new ConcreteEnvironment(executor, generatorTestRunner, reporter);
    }
}
