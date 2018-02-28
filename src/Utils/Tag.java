package Utils;

import Utils.Type;

public class Tag {
    private String fileName;
    private String content;
    private Type type;

    public Tag(String fileName, String content, Type type){
        this.fileName = fileName;
        this.content = content;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Type getType(){
        return type;
    }

    public void setType(Type type){
        this.type = type;
    }
}
