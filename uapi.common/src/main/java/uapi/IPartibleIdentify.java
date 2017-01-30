package uapi;

/**
 * An identify which is combined by multiple parts
 */
public interface IPartibleIdentify<T> extends IIdentifiable<T> {

    /**
     * Get parts of this identify
     *
     * @return  The parts array
     */
    Object[] getParts();
}
