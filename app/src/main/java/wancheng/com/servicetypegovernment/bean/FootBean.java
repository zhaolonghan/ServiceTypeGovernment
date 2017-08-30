package wancheng.com.servicetypegovernment.bean;


public class FootBean {
    private String foot_1 = "首页";
    private String foot_2="新闻动态";
    private String foot_3="通知公告";
    private String foot_4="法律法规";
    private String foot_5="个人中心";
    public FootBean(String foot_1,String foot_2,String foot_3,String foot_4,String foot_5){
        this.foot_1=foot_1;
        this.foot_2=foot_2;
        this.foot_3=foot_3;
        this.foot_4=foot_4;
        this.foot_5=foot_5;


    }
    public String getFoot_1() {
        return foot_1;
    }

    public void setFoot_1(String foot_1) {
        this.foot_1 = foot_1;
    }

    public String getFoot_2() {
        return foot_2;
    }

    public void setFoot_2(String foot_2) {
        this.foot_2 = foot_2;
    }

    public String getFoot_3() {
        return foot_3;
    }

    public void setFoot_3(String foot_3) {
        this.foot_3 = foot_3;
    }

    public String getFoot_4() {
        return foot_4;
    }

    public void setFoot_4(String foot_4) {
        this.foot_4 = foot_4;
    }

    public String getFoot_5() {
        return foot_5;
    }

    public void setFoot_5(String foot_5) {
        this.foot_5 = foot_5;
    }
}
