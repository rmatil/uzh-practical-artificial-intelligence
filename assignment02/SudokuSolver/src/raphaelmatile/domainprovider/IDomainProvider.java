package raphaelmatile.domainprovider;

/**
 * Represents a data structure which can be seen as a provider for
 * values on the defined domain. There is no restriction about which
 * values of the specified domain can be returned but that the may only provided once.
 *
 * @param <T> The domain for which to provide values
 */
public interface IDomainProvider<T> {

    /**
     * Returns true, if a new value for th domain can be provided.
     *
     * @return True, if a new value can be fetched by this provider
     */
    boolean hasNextDomainValue();

    /**
     * Returns the next available value for the domain.
     *
     * @return The next available value
     *
     * @throws ProviderException If no value is available
     */
    T getNextDomainValue()
            throws ProviderException;
}
