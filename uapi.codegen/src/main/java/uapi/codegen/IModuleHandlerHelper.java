package uapi.codegen;

/**
 * A Helper to maintain this module definition which will be used to build module-info file at build-time
 */
public interface IModuleHandlerHelper extends IHandlerHelper {

    /**
     * This helper's name
     */
    String name = "ModuleHelper";

    default String getName() {
        return name;
    }

    /**
     * Set this module name
     *
     * @param   name
     *          The name of the module
     */
    void setModuleName(String name);

    /**
     * Add required module for this module
     *
     * @param   require
     *          The required module
     */
    void addRequire(String require);

    /**
     * Add used service for this module
     *
     * @param   use
     *          The used service
     */
    void addUse(String use);

    /**
     * Add the exported package for this module
     *
     * @param   export
     *          The exported package
     */
    void addExport(String export);

    /**
     * Add provide service for this module
     *
     * @param   service
     *          The service interface
     * @param   implementation
     *          The service implementation
     */
    void addProvide(String service, String implementation);
}
