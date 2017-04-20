package raphaelmatile.constraint;

public interface IConstraint<T> {

    void addConstraint(T constraint);

    void removeConstraint(T constraint);

    boolean isConstrainedBy(T constraint);

    boolean isAssignable(T value);

    int size();
}
