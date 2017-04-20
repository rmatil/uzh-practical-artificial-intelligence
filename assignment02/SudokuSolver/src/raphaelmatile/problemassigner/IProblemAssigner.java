package raphaelmatile.problemassigner;

public interface IProblemAssigner<T> {

    void addProblem(T problem);

    void removeProblem(T problem);

    boolean hasProblemsToSolve();

    T getNextProblemToSolve();
}
