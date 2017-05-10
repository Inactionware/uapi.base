package uapi.codegen;

/**
 * All generated class must be implemented this interface
 */
public interface IGenerated {

    /**
     * Get original type which is generated class extends for
     *
     * @return  The original type
     */
    Class<?> originalType();
}
