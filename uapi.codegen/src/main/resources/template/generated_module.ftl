module ${moduleName} {
    <#list requires as require>
    requires ${require};
    </#list>

    <#list uses as use>
    uses ${use};
    </list>

    <#list exports as export>
    exports ${export};
    </#list>

    <#list provides as provide>
    provides ${provide.service} as ${provide.implementation};
    </#list>
}
