package dev.marksman.gauntlet.filter;

import dev.marksman.gauntlet.Arbitraries;
import dev.marksman.gauntlet.GauntletApiBase;
import dev.marksman.gauntlet.Prop;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Not.not;
import static dev.marksman.gauntlet.Arbitraries.boxedPrimitives;
import static dev.marksman.gauntlet.Prop.allOf;
import static dev.marksman.gauntlet.Prop.named;
import static dev.marksman.gauntlet.Prop.predicate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.IterateN.iterateN;

final class FilterTest extends GauntletApiBase {
    @Test
    void emptyFilter() {
        Filter<Object> filter = Filter.emptyFilter();
        assertThat(all(boxedPrimitives())
                .satisfy(predicate("always true", filter::apply)));
    }

    @Test
    void basicFilter() {
        Filter<Integer> isEven = Filter.filter(n -> n % 2 == 0);
        assertThat(all(Arbitraries.ints())
                .satisfy(allOf(predicate("true for even numbers", n -> isEven.apply(n * 2)),
                        predicate("false for odd numbers", n -> !isEven.apply(n * 2 + 1)),
                        predicate(isEven).xor(predicate(not(isEven))))));
    }

    @Test
    void compositeFilter() {
        Filter<Integer> div3 = Filter.filter(n -> n % 3 == 0);
        Filter<Integer> div5 = Filter.filter(n -> n % 5 == 0);
        Filter<Integer> div35 = div3.add(div5);
        Filter<Integer> div53 = div5.add(div3);

        Prop<Integer> divisibleBy3 = predicate(div3);
        Prop<Integer> divisibleBy5 = predicate(div5);
        Prop<Integer> divisibleBy3and5 = predicate(div35);
        Prop<Integer> divisibleBy5and3 = predicate(div53);

        assertThat(all(Arbitraries.ints())
                .satisfy(allOf(
                        named("combines all components with 'and'", divisibleBy3and5.iff(divisibleBy3.and(divisibleBy5))),
                        named("commutative", divisibleBy3and5.iff(divisibleBy5and3)))));
    }

    @Test
    void mapFilter() {
        Filter<Integer> divBy3and5 = Filter.<Integer>filter(n -> n % 3 == 0).add(n -> n % 5 == 0);
        Filter<Integer> minus1isDivBy3and5 = divBy3and5.contraMap((Integer n) -> n - 1);

        assertTrue(minus1isDivBy3and5.apply(16));
        assertFalse(minus1isDivBy3and5.apply(15));
    }

    @Test
    void filterMapFilter() {
        Filter<Integer> divBy3and5 = Filter.<Integer>filter(n -> n % 3 == 0).add(n -> n % 5 == 0);
        Filter<Integer> minus1isDivBy3and5 = divBy3and5.contraMap((Integer n) -> n - 1);
        Filter<Integer> minus1IsDivBy3and5andNegative = minus1isDivBy3and5.add(n -> n < 0);

        assertTrue(minus1IsDivBy3and5andNegative.apply(-14));
        assertFalse(minus1IsDivBy3and5andNegative.apply(16));
    }

    @Test
    void stackSafeCompose() {
        int bigNumber = 50_000;
        Filter<Integer> filter = Filter.filter(n -> n % 2 == 0);
        Filter<Integer> megaFilter = iterateN(bigNumber, filter, acc -> acc.add(filter));
        assertTrue(megaFilter.apply(2));
    }

    @Test
    void stackSafeMap() {
        int bigNumber = 50_000;
        Filter<Integer> mapped = iterateN(bigNumber, Filter.filter(n1 -> n1 == 0), acc -> acc.contraMap(n -> n - 1));
        assertTrue(mapped.apply(bigNumber));
    }
}