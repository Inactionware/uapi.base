module uapi.codegen {
    requires java.compiler;
    requires freemarker;
    requires auto.service.annotations;
    requires uapi.common;

    exports uapi.codegen;
}