package uapi.codegen;

import javax.tools.JavaFileManager;

public interface IResourceFile {

//    void setLocation(JavaFileManager.Location location);
//
//    void setFileName(String fileName);
//
//    void setEncoder(String encoder);

    void appendContent(String content);
}
