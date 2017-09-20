package wancheng.com.servicetypegovernment.bean;

import java.util.List;

/**
 * Created by HANZHAOLONG on 2017/9/17.
 */
public class ImageUpload {
    private int indexP;
    private int indexC;
    private String path;;
    private List<ImagesBean> paths;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageUpload(int indexP, int indexC, String path) {
        this.indexP = indexP;
        this.indexC = indexC;
        this.path = path;
    }

    public int getIndexP() {
        return indexP;
    }

    public void setIndexP(int indexP) {
        this.indexP = indexP;
    }

    public int getIndexC() {
        return indexC;
    }

    public void setIndexC(int indexC) {
        this.indexC = indexC;
    }

    public List<ImagesBean> getPaths() {
        return paths;
    }

    public void setPaths(List<ImagesBean> paths) {
        this.paths = paths;
    }

    public ImageUpload(int indexP, int indexC, List<ImagesBean> paths) {
        this.indexP = indexP;
        this.indexC = indexC;
        this.paths = paths;
    }
}
