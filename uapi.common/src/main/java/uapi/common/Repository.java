package uapi.common;

import uapi.IIdentifiable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A repository can store any data which must be implemented IIdentifiable interface
 */
public class Repository<K, V extends IIdentifiable<K>> {

    private final Map<K, V> _store;

    public Repository() {
        this._store = new ConcurrentHashMap<>();
    }

    /**
     * Put specific data into the repository and return existing data if it has
     *
     * @param   data
     *          The data which will be put in the repository
     * @return  The existing data which has same id, return null if no existing data
     */
    public V put(final V data) {
        ArgumentChecker.required(data, "data");
        K key = data.getId();
        return this._store.put(key, data);
    }

    /**
     * Get data from the repository by specific id
     *
     * @param   id
     *          The id which is associated with one data in the repository
     * @return  Return specific data which associated with the id
     */
    public V get(K id) {
        return this._store.get(id);
    }

    /**
     * Return stored data count
     *
     * @return  Stored data count
     */
    public int count() {
        return this._store.size();
    }
}
