package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
public class DomainTestExecutionParameters {
    private final Executor executor;

    public static DomainTestExecutionParameters domainTestExecutionParameters(Executor executor) {
        return new DomainTestExecutionParameters(executor);
    }
}
