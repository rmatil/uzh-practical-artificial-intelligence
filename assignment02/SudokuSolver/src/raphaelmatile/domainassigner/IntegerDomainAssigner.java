package raphaelmatile.domainassigner;

public class IntegerDomainAssigner implements IDomainAssigner<Integer> {

    private int minDomain;
    private int maxDomain;

    private int currentDomain;

    public IntegerDomainAssigner(int minDomain, int maxDomain) {
        this.minDomain = minDomain;
        this.maxDomain = maxDomain;

        this.currentDomain = this.minDomain;
    }

    @Override
    public boolean hasNextDomainValue() {
        return this.currentDomain <= this.maxDomain;
    }

    @Override
    public Integer getNextDomainValue() {
        int tmp = this.currentDomain;
        this.currentDomain++;

        return tmp;
    }
}
