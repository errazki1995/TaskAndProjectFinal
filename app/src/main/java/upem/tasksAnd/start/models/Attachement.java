package upem.tasksAnd.start.models;

import android.graphics.Bitmap;

public class Attachement {
//atachement id equals task id or subtask-id

private int attachementid;
private Bitmap content;
private String attachmentPath;
private String attachmentType;
private String taskid;
private String attachmentname;


    public Attachement(String attachmentPath, String attachType){
    this.attachmentPath=attachmentPath;
    this.attachmentType= attachType;
}
    public Attachement(String taskid,String attachmentPath,String attachementname,String attachType){
        this.attachmentPath=attachmentPath;
        this.attachmentname= attachementname;
        this.attachmentType= attachType;
        this.taskid = taskid;
    }
    public String getAttachmentname() {
        return attachmentname;
    }

    public void setAttachmentname(String attachmentname) {
        this.attachmentname = attachmentname;
    }

    public int getAttachementid() {
        return attachementid;
    }

    public void setAttachementid(int attachementid) {
        this.attachementid = attachementid;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getGetAttachmentType() {
        return attachmentType;
    }

    public void setGetAttachmentType(String getAttachmentType) {
        this.attachmentType = getAttachmentType;
    }

    public String getTaskid() {
        return taskid;
    }

    public Bitmap getContent() {
        return content;
    }

    public void setContent(Bitmap content) {
        this.content = content;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Override
    public String toString() {
        return "Attachement{" +
                "attachementid=" + attachementid +
                ", content=" + content +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentType='" + attachmentType + '\'' +
                ", taskid='" + taskid + '\'' +
                ", attachmentname='" + attachmentname + '\'' +
                '}';
    }
}
