package Utils;

import Utils.Type;
import us.codecraft.xsoup.XElements;

public class Tag {
    private String name;
    private String content;
    private Type type;
    private String voice;
    private String url;
    private XElements xPath;

    public Tag(String name, String content, Type type) {
        this.name = name;
        this.content = content;
        this.type = type;
        this.voice = null;
        this.url = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String fileName) {
        this.name = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    @Override
    public boolean equals(Object obj) {
        Tag other = (Tag) obj;
        return this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public XElements getxPath() {
        return xPath;
    }

    public void setxPath(XElements xPath) {
        this.xPath = xPath;
    }
}
