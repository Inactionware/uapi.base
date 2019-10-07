module ${module.name} {
    <#list module.requires as require>
    requires ${require};
    </#list>

    <#list module.uses as use>
    uses ${use};
    </list>

    <#list module.exports as export>
    exports ${export};
    </#list>

    <#list module.provides as provide>
    provides ${provide.service} as ${provide.implementation};
    </#list>
}
