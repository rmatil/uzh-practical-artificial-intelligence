package raphaelmatile.domainassigner;

public interface IDomainAssigner<T> {

    boolean hasNextDomainValue();

    T getNextDomainValue();
}
