module uapi.codegen {
    requires java.compiler;
    requires freemarker;
    requires auto.service.annotations;
    requires org.objectweb.asm;
    requires uapi.common;

    exports uapi.codegen;
}