package uapi.rx;

/**
 * A CountReducer is used to count item number
 */
public class CountReducer<T> extends Reducer<T> {

    private int _counter = 0;

    CountReducer(Mapper<T> previously) {
        super(previously);
    }

    @Override
    public T getItem() throws NoItemException {
        while (hasItem()) {
            try {
                getPreviously().getItem();
                this._counter++;
            } catch (NoItemException ex) {
                break;
            }
        }
        return (T) new Integer(this._counter);
    }
}
