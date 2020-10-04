package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResult;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import static dev.marksman.gauntlet.shrink.ShrinkResultBuilder.shrinkResultBuilder;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkCharacter.shrinkCharacter;

final class ShrinkString {
    private ShrinkString() {

    }

    static ShrinkStrategy<String> shrinkString() {
        return shrinkString(0, shrinkCharacter());
    }

    static ShrinkStrategy<String> shrinkString(int minimumLength) {
        return shrinkString(minimumLength, shrinkCharacter());
    }

    static ShrinkStrategy<String> shrinkString(ShrinkStrategy<Character> elements) {
        return shrinkString(0, elements);
    }

    static ShrinkStrategy<String> shrinkString(int minimumLength, ShrinkStrategy<Character> elements) {
        if (minimumLength < 0) {
            throw new IllegalArgumentException("minimumLength must be >= 0");
        }
        return input -> {
            int length = input.length();
            if (length < minimumLength) {
                return ShrinkResult.empty();
            }
            ShrinkResultBuilder<String> builder = shrinkResultBuilder();
            if (length > minimumLength) {
                if (minimumLength == 0) builder = builder.append("");
                final int single = Math.max(1, minimumLength);
                if (single < length) {
                    String front = input.substring(0, single);
                    String back = input.substring(length - single);

                    // first character
                    builder = builder.append(front);

                    // last character
                    if (!front.equals(back)) {
                        builder = builder.append(back);
                    }
                }
                final int quarter = Math.max(length / 4, minimumLength);
                if (quarter > single) {
                    // first quarter
                    builder = builder.append(input.substring(0, quarter));

                    // last quarter
                    builder = builder.append(input.substring(length - quarter));
                }
                if (length >= 4 && length / 2 >= minimumLength) {
                    // even elements
                    builder = builder.lazyAppend(() -> evenElements(input));

                    // odd elements
                    builder = builder.lazyAppend(() -> oddElements(input));
                }
                final int half = Math.max(length / 2, minimumLength);
                if (half > quarter) {
                    // first half
                    builder = builder.lazyAppend(() -> input.substring(0, half));

                    // second half
                    builder = builder.lazyAppend(() -> input.substring(length - half));
                }
                final int init = length - single;
                if (init >= minimumLength) {
                    // init
                    builder = builder.lazyAppend(() -> input.substring(0, init));

                    // tail
                    builder = builder.lazyAppend(() -> input.substring(single));
                }
            }
            builder = builder.lazyConcat(() -> compact(minimumLength, input));
            builder = builder.lazyConcat(() -> removeWhitespace(minimumLength, input));
            // individual elements
            builder = builder.lazyConcat(() -> shrinkIndividualElements(0, elements, input));
            return builder.build();
        };
    }

    private static String evenElements(String s) {
        return skipElements(0, s);
    }

    private static String oddElements(String s) {
        return skipElements(1, s);
    }

    private static String skipElements(int offset, String s) {
        int length = s.length();
        StringBuilder output = new StringBuilder(length / 2);
        for (int i = offset; i < length; i += 2) {
            output.append(s.charAt(i));
        }
        return output.toString();
    }

    private static ImmutableFiniteIterable<String> shrinkIndividualElements(final int n, ShrinkStrategy<Character> shrinkCharacter, String input) {
        final int length = input.length();
        if (n >= length) {
            return ShrinkResult.empty();
        } else {
            char element = input.charAt(n);
            ImmutableFiniteIterable<Character> nthShrinks = shrinkCharacter.apply(element);
            if (nthShrinks.isEmpty()) {
                return shrinkIndividualElements(n + 1, shrinkCharacter, input);
            } else {
                String front = input.substring(0, n);
                String back = input.substring(n + 1);
                ImmutableFiniteIterable<String> spliced = nthShrinks.fmap(newElement -> front +
                        newElement +
                        back);
                return spliced.concat(() -> shrinkIndividualElements(n + 1, shrinkCharacter, input).iterator());
            }
        }
    }

    private static ImmutableFiniteIterable<String> compact(int minimumLength, String input) {
        int length = input.length();
        if (length < 2 || length < minimumLength) {
            return ShrinkResult.empty();
        } else {
            boolean changed = false;
            int outputLength = 1;
            StringBuilder output = new StringBuilder(input.length());
            char prev = input.charAt(0);
            output.append(prev);
            for (int i = 1; i < length; i++) {
                char c = input.charAt(i);
                if (c == prev) {
                    changed = true;
                } else {
                    prev = c;
                    output.append(c);
                    outputLength += 1;
                }
            }
            if (changed && outputLength >= minimumLength) {
                return ShrinkResult.singleton(output.toString());
            } else {
                return ShrinkResult.empty();
            }
        }
    }

    private static ImmutableFiniteIterable<String> removeWhitespace(int minimumLength, String input) {
        int length = input.length();
        if (length < 2 || length < minimumLength) {
            return ShrinkResult.empty();
        } else {
            boolean changed = false;
            boolean hasNonWhitespace = false;
            int outputLength = 0;
            StringBuilder output = new StringBuilder(input.length());
            for (int i = 0; i < length; i++) {
                char c = input.charAt(i);
                if (Character.isWhitespace(c)) {
                    changed = true;
                } else {
                    hasNonWhitespace = true;
                    output.append(c);
                    outputLength += 1;
                }
            }
            if (changed && hasNonWhitespace && outputLength >= minimumLength) {
                return ShrinkResult.singleton(output.toString());
            } else {
                return ShrinkResult.empty();
            }
        }
    }
}
