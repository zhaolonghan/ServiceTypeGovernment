package wancheng.com.servicetypegovernment.bean;

import java.io.Serializable;

/**
 * Created by HANZHAOLONG on 2017/8/24.
 */
public class ImagesBean implements Serializable {
    private int id;
    private String type;//netImage ����ͼƬ localImage ����ͼƬ  defaultImage Ĭ��ͼƬ
    private String path;//��ַ

    public ImagesBean(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
