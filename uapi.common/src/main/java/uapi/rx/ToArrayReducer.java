package uapi.rx;

import java.util.LinkedList;
import java.util.List;

/**
 * The ToArrayReducer will collect all of element and put into a List
 */
public class ToArrayReducer<T> extends Reducer<T[]> {

    private List<T> _list;
    private final T[] _temp;

    @SuppressWarnings("unchecked")
    ToArrayReducer(Mapper<T> previously, T[] t) {
        super(previously);
        if (t == null) {
            t = (T[]) new Object[0];
        }
        this._temp = t;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] getItem() {
        if (this._list == null) {
            this._list = new LinkedList<>();
        }
        while (hasItem()) {
            try {
                T item = (T) getPreviously().getItem();
                this._list.add(item);
            } catch (NoItemException ex) {
                // do nothing
            }
        }
        var result = this._list;
        this._list = null;
        return result.toArray(this._temp);
    }
}
