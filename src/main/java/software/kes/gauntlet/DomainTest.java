package software.kes.gauntlet;

public final class DomainTest<A> extends Test<A> {
    private final Quantifier quantifier;
    private final Domain<A> domain;
    private final Prop<A> property;
    private final DomainTestSettingsAdjustments settings;

    private DomainTest(Quantifier quantifier, Domain<A> domain, Prop<A> property, DomainTestSettingsAdjustments settings) {
        this.quantifier = quantifier;
        this.domain = domain;
        this.property = property;
        this.settings = settings;
    }

    static <A> DomainTest<A> domainTest(Quantifier quantifier,
                                        Domain<A> domain,
                                        Prop<A> property,
                                        DomainTestSettingsAdjustments settings) {
        return new DomainTest<>(quantifier, domain, property, settings);
    }

    public Quantifier getQuantifier() {
        return quantifier;
    }

    public Domain<A> getDomain() {
        return domain;
    }

    public Prop<A> getProperty() {
        return property;
    }

    public DomainTestSettingsAdjustments getSettingsAdjustments() {
        return settings;
    }

    @Override
    public PrettyPrinter<A> getPrettyPrinter() {
        return domain.getPrettyPrinter();
    }
}
