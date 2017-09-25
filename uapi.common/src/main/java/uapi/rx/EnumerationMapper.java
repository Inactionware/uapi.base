package uapi.rx;

import uapi.common.ArgumentChecker;

import java.util.Enumeration;

/**
 * he data source which is enumeration
 */
class EnumerationMapper<T> extends Mapper<T> {

    private final Enumeration<T> _itemEnum;

    EnumerationMapper(final Enumeration<T> itemEnum) {
        ArgumentChecker.required(itemEnum, "itemEnum");
        this._itemEnum = itemEnum;
    }

    @Override
    public boolean hasItem() {
        return this._itemEnum.hasMoreElements();
    }

    @Override
    public T getItem() throws NoItemException {
        if (hasItem()) {
            return this._itemEnum.nextElement();
        }
        throw new NoItemException();
    }

    @Override
    public void end() {
        // do nothing
    }
}
