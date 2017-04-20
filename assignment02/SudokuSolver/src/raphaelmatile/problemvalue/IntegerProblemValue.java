package raphaelmatile.problemvalue;

public class IntegerProblemValue implements IProblemValue<Integer> {

    private Integer value;

    public IntegerProblemValue(Integer value) {
        this.value = value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
