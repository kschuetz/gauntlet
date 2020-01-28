package dev.marksman.gauntlet;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;

final class UniversalDomainTestBuilder<A> implements DomainTestBuilder<A> {
    private final ImmutableFiniteIterable<A> domain;

    UniversalDomainTestBuilder(ImmutableFiniteIterable<A> domain) {
        this.domain = domain;
    }

    @Override
    public void mustSatisfy(Prop<A> prop) {

    }

    static <A> UniversalDomainTestBuilder<A> universalDomainTestBuilder(ImmutableFiniteIterable<A> domain) {
        return new UniversalDomainTestBuilder<>(domain);
    }

}
