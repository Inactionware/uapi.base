package uapi.state;

/**
 * Check state
 * @param <T>
 */
public interface IChecker<T> {

    /**
     * Check current state can be executed by the operation.
     *
     * @param   currentState
     *          Current state
     * @param   operation
     *          The operation that will be executed
     * @return  Return temporary state when the operation is executing, or return null if the operation cannot be
     *          applied on current state.
     */
    T check(T currentState, IOperation operation);
}
