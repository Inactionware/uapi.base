package ${packageName};

<#list imports as import>
${import};
</#list>

<#list annotations as annotation>
@${annotation.name}(<#list annotation.arguments as argument>${argument.name}=<#if argument.isString>"${argument.value}"<#else>${argument.value}</#if></#list>)
</#list>
public class ${generatedClassName}
<#if parentClassName??>
extends ${parentClassName}
<#elseif className??>
extends ${className}
</#if>
<#list implements>implements <#items as implement>${implement}<#sep>, </#sep></#items></#list> {

<#list fields as field>
    <#if field.isList>
    ${field.modifiers} java.util.List<${field.typeName}> ${field.name} = new <#if field.value??>${field.value}<#else>java.util.ArrayList<>()</#if>;
    <#elseif field.isMap>
    ${field.modifiers} java.util.Map<${field.keyTypeName}, ${field.typeName}> ${field.name} = new <#if field.value??>${field.value}<#else>java.util.HashMap<>()</#if>;
    <#else>
    ${field.modifiers} ${field.typeName} ${field.name}<#if field.value??> = ${field.value}</#if>;
    </#if>

</#list>

<#list properties as property>
    <#if property.generateField()>
    private ${property.fieldType()} ${property.fieldName()};
    </#if>

    <#if property.generateSetter()>
    public void ${property.setterName()}(final ${property.fieldType()} value) {
        this.${property.fieldName()} = value;
    }
    </#if>

    <#if property.generateGetter()>
    public ${property.fieldType()} ${property.getterName()}() {
        return this.${property.fieldName()};
    }
    </#if>
</#list>

<#list methods as methodInfo>
    <#list methodInfo.annotations as annotation>
    @${annotation.name}<#list annotation.arguments>(<#items as argument>${argument.name}=<#if argument.isString>"${argument.value}"<#else>${argument.value}</#if></#items>)</#list>
    </#list>
    ${methodInfo.modifiers} <#if methodInfo.returnTypeName??>${methodInfo.returnTypeName}</#if> ${methodInfo.name} (
    <#list methodInfo.parameters as parameter>
            ${parameter.modifiers} ${parameter.type} ${parameter.name}<#sep>, </#sep>
    </#list>
    ) <#list methodInfo.throwTypeNames>throws <#items as throw>${throw}<#sep>, </#sep></#items></#list> {
    <#if methodInfo.invokeSuperBefore>
        super.${methodInfo.name}(<#list methodInfo.parameters as parameter>${parameter.name}<#sep>, </#sep></#list>);
    </#if>
    <#list methodInfo.codes as code>
        ${code.code}
    </#list>
    <#if methodInfo.invokeSuperAfter>
        super.${methodInfo.name}(<#list methodInfo.parameters as parameter>${parameter.name}<#sep>, </#sep></#list>);
    </#if>
    }

</#list>
}
