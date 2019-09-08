package uapi.rx;

import uapi.InvalidArgumentException;

/**
 * The SkipMapper will skip data by specific count
 */
public class SkipMapper<T> extends Mapper<T> {

    private final int _count;
    private int _skipCount = 0;

    public SkipMapper(Mapper<T> previously, int count) {
        super(previously);
        if (count < 0) {
            throw new InvalidArgumentException("The skip count must be more then 0");
        }
        this._count = count;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem() throws NoItemException {
        while (this._skipCount < this._count) {
            if (! hasItem()) {
                throw new NoItemException();
            }
            getPreviously().getItem();
            this._skipCount++;
        }
        if (! hasItem()) {
            throw new NoItemException();
        }
        return (T) getPreviously().getItem();
    }
}
