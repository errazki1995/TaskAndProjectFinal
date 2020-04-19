package upem.tasksAnd.start.models;

import java.util.Date;

public class Timer {
    private int timerid;
    private int taskid;
    private Date trigger;
    private int hour;
    private int min;
    private boolean activated;
    private int sec;

    public Timer(int timerid,int taskid,Date triggerdate, boolean activated,int hour, int min, int sec) {
        this.timerid = timerid;
        this.taskid=taskid;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.activated=activated;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getTimerid() {
        return timerid;
    }

    public void setTimerid(int timerid) {
        this.timerid = timerid;
    }

    public Date getTriggerDate() {
        return trigger;
    }

    public void setTriggerDate(Date endDate) {
        this.trigger = trigger;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }
}
