package dev.marksman.gauntlet;

final class ExistentialDomainTestBuilder<A> implements DomainTestBuilder<A> {
    private final Iterable<A> domain;

    ExistentialDomainTestBuilder(Iterable<A> domain) {
        this.domain = domain;
    }

    @Override
    public void mustSatisfy(Prop<A> prop) {

    }

    static <A> ExistentialDomainTestBuilder<A> existentialDomainTestBuilder(Iterable<A> domain) {
        return new ExistentialDomainTestBuilder<>(domain);
    }

}
