package raphaelmatile.domainprovider;

/**
 * Provides integer values between a particular range.
 * Values are returned starting from the minimum value and are incremented by one up to the maximum value (including).
 */
public class IntegerDomainProvider implements IDomainProvider<Integer> {

    private int minDomain;
    private int maxDomain;

    private int currentDomain;

    /**
     * Creates a new integer provider with the given limits.
     *
     * @param minDomain The minimum value (including)
     * @param maxDomain The maximum value (including)
     */
    public IntegerDomainProvider(int minDomain, int maxDomain) {
        this.minDomain = minDomain;
        this.maxDomain = maxDomain;

        this.currentDomain = this.minDomain;
    }

    @Override
    public boolean hasNextDomainValue() {
        return this.currentDomain <= this.maxDomain;
    }

    @Override
    public Integer getNextDomainValue()
            throws ProviderException {
        if (this.currentDomain > this.maxDomain) {
            throw new ProviderException("The current domain '" + this.currentDomain + "' reached the maximum available domain of '" + this.maxDomain + "'");
        }

        int tmp = this.currentDomain;
        this.currentDomain++;

        return tmp;
    }
}
