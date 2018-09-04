package uapi.common;

/**
 * Any class implements this interface means the class depends on certain class.
 *
 * @param   <T> The dependent class type
 */
public interface IDependent<T> {

    /**
     * Return dependent class object
     *
     * @return  The dependent class object
     */
    T dependsOn();
}
