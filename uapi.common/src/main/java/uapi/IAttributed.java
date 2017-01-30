package uapi;

import java.util.Map;

/**
 * An class implement this interface will have one or more attributes
 */
public interface IAttributed {

    /**
     * Get attribute value by specific attribute key
     *
     * @param   key
     *          The attribute key which associated with attribute value
     * @return  The attribute value or null if no attribute value associated with the key
     */
    Object get(Object key);

    /**
     * Check this object contains specific attribute key and value
     *
     * @param   key
     *          The attribute key which will be checked
     * @param   value
     *          The attribute value which will be checked
     * @return  True means this object contains specific attribute otherwise return false
     */
    boolean contains(Object key, Object value);

    /**
     * Check this object contains specific attributes
     *
     * @param   attributes
     *          The attributes map
     * @return  True means this object contains specific attributes otherwise return false
     */
    boolean contains(Map<Object, Object> attributes);
}
