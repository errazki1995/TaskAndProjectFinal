package upem.tasksAnd.start.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {

    private int taskid;
    private String name;
    private String description;
    private String dateStart;
    private String dateEnd;
    private String priorityLevel;
    private String difficulty;
    private int status;
    private boolean done;
    private Geolocalisation geolocalisation;
    private int parentid;
    private Timer timer;
    private Attachement attachement;

    public Task() {

    }

    public Task(int taskid,String name){
        this.taskid=taskid;
        this.name=name;
    }

    public Task(int taskid,Timer m){
      this.timer = timer;
    }

    public Task(int taskid,String name,String description){
        this.taskid= taskid;
        this.name=name;
        this.description=description;
    }

    public Task(int taskid, String name, String description,
                String dateStart, String dateEnd, String priorityLevel,
                String difficulty, int status,
                Geolocalisation geolocalisation, int parentid,boolean done, Attachement attachement){
        this.taskid = taskid;
        this.name = name;
        this.description = description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.priorityLevel = priorityLevel;
        this.difficulty = difficulty;
        this.status = status;
        this.geolocalisation = geolocalisation;
        this.parentid = parentid;
        this.attachement = attachement;
        this.done=done;
    }




    public Task(int taskid, String name, String description,
                String dateStart, String dateEnd, String priorityLevel,
                String difficulty, int status,
                Geolocalisation geolocalisation, int parentid, Attachement attachement) {
        this.taskid = taskid;
        this.name = name;
        this.description = description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.priorityLevel = priorityLevel;
        this.difficulty = difficulty;
        this.status = status;
        this.geolocalisation = geolocalisation;
        this.parentid = parentid;
        this.attachement = attachement;
    }

    public Geolocalisation getGeolocalisation() {
        return geolocalisation;
    }

    public String getName() {
        return name;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeolocalisation(Geolocalisation geolocalisation) {
        this.geolocalisation = geolocalisation;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAttachement(Attachement attachement) {
        this.attachement = attachement;
    }

    public Attachement getAttachement() {
        return attachement;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskid=" + taskid +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", priorityLevel='" + priorityLevel + '\'' +
                "description: "+description+
                ", difficulty='" + difficulty + '\'' +
                ", status=" + status +
                ", attachement=" + attachement +
                '}';
    }
}