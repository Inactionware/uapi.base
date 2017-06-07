package uapi.rx;

import uapi.common.Functionals;

/**
 * The SingleSelectReducerReducer return only one item based on specific filter
 */
public class SelectReducer<T> extends Reducer<T> {

    private final Functionals.FilterOne<T> _filter;

    SelectReducer(Mapper previously, Functionals.FilterOne<T> filter) {
        super(previously);
        this._filter = filter;
    }

    @Override
    public T getItem() throws NoItemException {
        T item = null;
        T selected = null;
        while (hasItem()) {
            try {
                item = (T) getPreviously().getItem();
            } catch (NoItemException ex) {
                // Do nothing
            }
            if (this._filter.accept(item, selected)) {
                selected = item;
            }
        }

        return selected;
    }
}
