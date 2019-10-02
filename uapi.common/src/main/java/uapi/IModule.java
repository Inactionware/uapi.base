package uapi;

public interface IModule {

    /**
     * Return the name of this module.
     *
     * @return  The module name
     */
    String name();

    /**
     * Set this module name
     *
     * @param   name
     *          The new name of this module
     */
    void setName(String name);

    /**
     * Get exported package list of this module.
     *
     * @return  Exported package list of this module
     */
    String[] exports();

    /**
     * Add exported package of this module.
     *
     * @param   export
     *          The exported package name
     */
    void addExport(String export);

    /**
     * Get required modules which are used in this module.
     *
     * @return  The required modules
     */
    String[] requires();

    /**
     * Add required module which is used in this module.
     *
     * @param   require
     *          The required module name
     */
    void addRequire(String require);

    /**
     * Get used services of this module.
     *
     * @return  Used services of this module
     */
    String[] uses();

    /**
     * Add used service for this module.
     *
     * @param   use
     *          The service name
     */
    void addUse(String use);

    /**
     * Get provides
     *
     * @return The provide list
     */
    Provide[] provides();

    /**
     * Add provide to this module.
     *
     * @param   provide
     *          The provide which will be added
     */
    void addProvide(Provide provide);
}
