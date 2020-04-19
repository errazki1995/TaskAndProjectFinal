package upem.tasksAnd.start;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.models.Task;

public class DatesUtilByme {
    private DatesUtilByme taskgenerator;
    public  TaskService taskservice;
    private   Context context;
    private  int parentid=0;
    public DatesUtilByme(int parentid, Context context){
                this.parentid=parentid;
                this.context = context;

    }


    public  List<Date> getDates() {
        try {
            taskservice = new TaskService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date datefirst = getMinDateTask(parentid);
        Log.d("datefiest", "the date first " + datefirst);

        Date dateend = somedays(getMaxDateTask(parentid));
        List<Date> datesbetween = generateDatesBetweenTwoDates(datefirst, dateend);
        //debuglist(datesbetween);
        Log.d("letseehere", "the items size " + datesbetween.size());
        return datesbetween;
    }

    public static List<Date> generateDatesBetweenTwoDates(Date start, Date end) {
        Calendar calendarOne = Calendar.getInstance();
        Calendar calendarTwo = Calendar.getInstance();
        List<Date> dates = new ArrayList<Date>();
        calendarOne.setTime(start);
        calendarTwo.setTime(end);
        Log.d("shouldenter", "entered generated");
        while (calendarOne.after(calendarTwo) == false) {
            dates.add(calendarOne.getTime());
            calendarOne.add(Calendar.DATE, 1);
        }
        Iterator<Date> iterator = dates.iterator();
        int position = 0;
        for (int i = 0; i < dates.size(); i++) {
            Date date = dates.get(position);
            int pos = position;
            position++;
        }
        //datemap.put(convertToDate(convertToString(date)), pos);

        Log.d("datelistsize", "the date list size :" + dates.size());
        return dates;

    }

    public static String convertToString(Date date) {
        Calendar tempcalendar = Calendar.getInstance();
        tempcalendar.setTime(date);
        String day = tempcalendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + tempcalendar.get(Calendar.DAY_OF_MONTH) : tempcalendar.get(Calendar.DAY_OF_MONTH) + "";
        String month = tempcalendar.get(Calendar.MONTH) < 10 ? "0" + tempcalendar.get(Calendar.MONTH) : tempcalendar.get(Calendar.MONTH) + "";
        String year = tempcalendar.get(Calendar.YEAR) + "";

        return day + "/" + month + "/" + year;

    }

    public  Date getMinDateTask(int parentid) { // 0 for all the tasks
        List<Task> tasks = taskservice.listAllTasksByParent(parentid);
        Date min = convertToDate(tasks.get(0).getDateStart());
        for (Task t : tasks) {
            if (convertToDate(t.getDateStart()).before(min) || convertToDate(t.getDateStart()).equals(min)) {

                min = convertToDate(t.getDateStart());
            }
        }
        Log.d("dateMIN", "the task date is: " + convertToString(min));

        return setThenewDate(min);
    }

    public static Date somedays(Date fromdate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(fromdate);
        cal.add(Calendar.DAY_OF_MONTH, +2);
        Date somedaysafter = cal.getTime();
        return somedaysafter;
    }

    public  Date getMaxDateTask(int parentid) {
        List<Task> tasks = taskservice.listAllTasksByParent(parentid);
        Date max = convertToDate(tasks.get(0).getDateEnd());
        for (Task t : tasks) {
            if (convertToDate(t.getDateEnd()).after(max) || convertToDate(t.getDateEnd()).equals(max)) {
                max = convertToDate(t.getDateEnd());
            }
        }
        max = setThenewDate(max);
        Log.d("dateMAX", "the task date is: " + convertToString(max));

        return setThenewDate(max);
    }

    public static Date convertToDate(String date) {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = formater.parse(date);
            return date1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date setThenewDate(Date d) {
        Calendar somecal = Calendar.getInstance();
        somecal.setTime(d);
        somecal.add(Calendar.MONTH, 1);
        return somecal.getTime();
    }


    public static Date getActualDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.add(Calendar.MONTH, 1);
        Date curr = c.getTime();
        return curr;
    }
}
