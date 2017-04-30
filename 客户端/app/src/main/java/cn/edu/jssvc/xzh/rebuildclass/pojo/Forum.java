package cn.edu.jssvc.xzh.rebuildclass.pojo;

/**
 * Created by xzh on 2017/2/10.
 *      论坛部分实体类
 */

public class Forum {

    private int id;     // id
    private String username;    // 用户名
    private String imageId;    // 用户头像
    private String time;    // 时间
    private String title;   //  标题
    private String content;     // 内容
    private int times;   // 次数
    private String f_img = "";       //图片

    public Forum(int id, String imageId, String username, String time, String title, String content, int times) {
        this.id = id;
        this.imageId = imageId;
        this.username = username;
        this.time = time;
        this.title = title;
        this.content = content;
        this.times = times;
    }

      public Forum(int id, String imageId, String username, String time, String content) {
        this.id = id;
        this.imageId = imageId;
        this.username = username;
        this.time = time;
        this.content = content;
    }
    public Forum(String time, String title,int times) {
        this.time = time;
        this.title = title;
        this.times = times;
    }

    public Forum(int id, String imageId, String username, String time, String title, String content, int times, String f_img) {
        this.id = id;
        this.imageId = imageId;
        this.username = username;
        this.time = time;
        this.title = title;
        this.content = content;
        this.times = times;
        this.f_img = f_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getF_img() {
        return f_img;
    }

    public void setF_img(String f_img) {
        this.f_img = f_img;
    }
}
