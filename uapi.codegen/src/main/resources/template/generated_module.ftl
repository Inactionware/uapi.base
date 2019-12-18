module ${module.name} {
    <#list module.requires as require>
    requires ${require};
    </#list>

    <#list module.uses as use>
    uses ${use};
    </#list>

    <#list module.exports as export>
    exports ${export};
    </#list>

    <#list module.provides?keys as service>
    provides ${service} as <#list module.provides[service] as implementation>${implementation}<#sep>, </#sep></#list>;
    </#list>
}
