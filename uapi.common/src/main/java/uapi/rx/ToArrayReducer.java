package uapi.rx;

import java.util.LinkedList;
import java.util.List;

/**
 * The ToArrayReducer will collect all of element and put into a List
 */
public class ToArrayReducer<T> extends Reducer<T[]> {

    private List<T> _list;

    ToArrayReducer(Mapper<T> previously) {
        super(previously);
    }

    @Override
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
        List<T> result = this._list;
        this._list = null;
        return result.toArray((T[]) new Object[result.size()]);
    }
}
