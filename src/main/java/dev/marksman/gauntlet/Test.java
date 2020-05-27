package dev.marksman.gauntlet;

/**
 * Consider this sealed.  Do not inherit from outside of gauntlet.
 */
public abstract class Test<A> {
    public abstract Prop<A> getProperty();

    public abstract PrettyPrinter<A> getPrettyPrinter();
}
