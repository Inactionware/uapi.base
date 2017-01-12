package uapi.rx;

import uapi.GeneralException;
import uapi.common.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * The ToMapReducer will collect all of element and put into a Map
 * The previously operator must generate a value which is instance of Pair
 */
class ToMapReducer<K, V> extends Reducer<Map<K, V>> {

    ToMapReducer(Mapper<Pair<K, V>> previously) {
        super(previously);
    }

    @Override
    public Map<K, V> getItem() throws NoItemException {
        Map<K, V> items = new HashMap<>();
        while (hasItem()) {
            try {
                Pair<K, V> item = (Pair<K, V>) getPreviously().getItem();
                if (item == null) {
                    continue;
                }
                items.put(item.getLeftValue(), item.getRightValue());
            } catch (NoItemException ex) {
                // do nothing
            } catch (ClassCastException ex) {
                throw new GeneralException(
                        "The ToMapReducer requires previously operator generated item is instance of Pair");
            }
        }
        return items;
    }
}
