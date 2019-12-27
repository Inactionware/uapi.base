package uapi;

/**
 * Module portal
 */
public interface IModulePortal {

    /**
     * Get module name
     *
     * @return  Module name
     */
    default String moduleName() {
        return this.getClass().getModule().getName();
    }
}
