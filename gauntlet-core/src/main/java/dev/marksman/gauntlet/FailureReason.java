package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class FailureReason {
    String reason;

    public static FailureReason failureReason(String reason) {
        return new FailureReason(reason);
    }
}
