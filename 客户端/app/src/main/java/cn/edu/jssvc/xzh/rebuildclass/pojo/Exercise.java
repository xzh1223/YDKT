package cn.edu.jssvc.xzh.rebuildclass.pojo;

/**
 * Created by xzh on 2017/3/20.
 * <p>
 * 练习实体类
 */

public class Exercise {
    private int id;
    private String question;
    private String achoose;
    private String bchoose;
    private String cchoose;
    private String dchoose;
    private String answer;

    public Exercise(int id, String question, String achoose, String bchoose, String cchoose, String dchoose, String answer) {
        this.id = id;
        this.question = question;
        this.achoose = achoose;
        this.bchoose = bchoose;
        this.cchoose = cchoose;
        this.dchoose = dchoose;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDchoose() {
        return dchoose;
    }

    public void setDchoose(String dchoose) {
        this.dchoose = dchoose;
    }

    public String getCchoose() {
        return cchoose;
    }

    public void setCchoose(String cchoose) {
        this.cchoose = cchoose;
    }

    public String getBchoose() {
        return bchoose;
    }

    public void setBchoose(String bchoose) {
        this.bchoose = bchoose;
    }

    public String getAchoose() {
        return achoose;
    }

    public void setAchoose(String achoose) {
        this.achoose = achoose;
    }
}
