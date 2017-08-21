package wancheng.com.servicetypegovernment.bean;

/**
 * Created by HANZHAOLONG on 2017/8/21.
 */
public class TopBean {
    private String title="首页";
    private String left="返回";
    private String right="";
    private boolean isLeft=true;
    private boolean isRight=true;

    public TopBean() {
    }

    public TopBean(String title, String left, String right, boolean isLeft, boolean isRight) {
        this.title = title;
        this.left = left;
        this.right = right;
        this.isRight = isRight;
        this.isLeft = isLeft;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }
}
