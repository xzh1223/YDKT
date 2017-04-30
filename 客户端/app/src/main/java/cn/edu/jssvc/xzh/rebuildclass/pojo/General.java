package cn.edu.jssvc.xzh.rebuildclass.pojo;

/**
 * Created by xzh on 2017/2/7.
 *      通用实体类
 */

public class General {
    private int id;
    private String name;    // 名称
    private String imageId;    // 图片
    private String sex;     // 性别
    private int age;     // 年龄
    private String profession;  // 专业
    private String phone;   // 手机号
    private String eamil;   // 邮箱
    private String constellation;   // 星座
    private String home;    // 家乡
    private String description;     // 个人说明
    private int progressText;   // 进度条
    private int allProgress;     //总进度

    public General(int id, String name, String imageId) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
    }

    public General(int id, String imageId) {
        this.id = id;
        this.imageId = imageId;
    }

    public General(int id, String name, String imageId, int progressText, int allProgress) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
        this.progressText = progressText;
        this.allProgress = allProgress;
    }

    public General(String sex, int age, String profession, String phone, String eamil, String constellation, String home, String description) {
        this.sex = sex;
        this.age = age;
        this.profession = profession;
        this.phone = phone;
        this.eamil = eamil;
        this.constellation = constellation;
        this.home = home;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEamil() {
        return eamil;
    }

    public void setEamil(String eamil) {
        this.eamil = eamil;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgressText() {
        return progressText;
    }

    public void setProgressText(int progressText) {
        this.progressText = progressText;
    }

    public int getAllProgress() {
        return allProgress;
    }

    public void setAllProgress(int allProgress) {
        this.allProgress = allProgress;
    }
}
