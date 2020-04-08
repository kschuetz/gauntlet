package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
public class DiscreteDomainTestExecutionParameters {
    private final Executor executor;
    private final Duration defaultTimeout;

    public static DiscreteDomainTestExecutionParameters generatorTestExecutionParameters(Executor executor,
                                                                                         Duration defaultTimeout) {
        return new DiscreteDomainTestExecutionParameters(executor, defaultTimeout);
    }
}
