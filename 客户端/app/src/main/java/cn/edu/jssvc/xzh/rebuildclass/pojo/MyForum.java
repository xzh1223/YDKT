package cn.edu.jssvc.xzh.rebuildclass.pojo;

/**
 * Created by xzh on 2017/3/23.
 *
 *      我的论坛实体类
 */

public class MyForum {
    private int id;
    private String imageId;
    private String time;
    private String username;
    private String f_title;
    private String f_content;

    public MyForum(int id, String username, String iamgeId) {
        this.id = id;
        this.username = username;
        this.imageId = iamgeId;
    }

    public MyForum() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public String getF_content() {
        return f_content;
    }

    public void setF_content(String f_content) {
        this.f_content = f_content;
    }
}
