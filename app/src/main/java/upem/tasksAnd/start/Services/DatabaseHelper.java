package upem.tasksAnd.start.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import upem.tasksAnd.start.models.Attachement;
import upem.tasksAnd.start.models.Category;
import upem.tasksAnd.start.models.Geolocalisation;
import upem.tasksAnd.start.models.Task;
import upem.tasksAnd.start.models.Timer;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASENAME = "Tasks1.db";
    public final static String TABLE_NAME_Task = "Task";
    public final static String TABLE_NAME_Attachement = "Attachement";
    public final static String TABLE_NAME_Geolocalisation = "Geolocalisation";
    public final static String TABLE_NAME_Category = "Category";
    public final static String TABLE_NAME_Timer = "Timer";
    //public final static String TABLE_NAME_Category_task = "Category_Task";

    /*
    Types
     */
    public final static String primary_key = " INTEGER PRIMARY KEY ";
    public final static String integer = " INTEGER ";
    public final static String text = " TEXT ";
    public final static String date = " TEXT "; //Yep i know...
    public final static String autoincrement = " AUTOINCREMENT ";


    /*
     *Tasks Columns
     */
    public final static String Task_ID = "taskid";
    public final static String Task_NAME = "name";
    public final static String Task_DESCRIPTION = "description";
    public final static String Task_DATESTART = "datestart";
    public final static String Task_DATEEND = "dateend";
    public final static String Task_PRIORITY = "priority";
    public final static String Task_DIFFICULTY = "difficulty";
    public final static String Task_STATUS = "status";
    public final static String Task_PARENTID = "parentid";
    public final static String Task_done = "done";
    public final static String Task_categoryId = "categoryid";
    public final static String delimiter = ",";

    /*
     *Geolocalisation columns
     */
    public final static String Geolocalisation_ID = "geolocalisationid";
    public final static String GeoLocalisation_Adress = "adress";
    public final static String GeoLocalisation_LONGTITUDE = "longtitude";
    public final static String GeoLocalisation_LATITUDE = "latitude";
    public final static String GeoLocalisation_TASKID = "taskid";
    /*
     *Attachement Columns
     */
    public final static String Attachement_ID = "attachementid";
    public final static String Attachement_PATH = "attachpath";
    public final static String Attachement_TYPE = "attachtype";
    public final static String Attachement_TASKID = "taskid";
    public final static String Attachement_FileName = "filename";


    /*
     Category
     */
    public final static String categoryid = "categoryid";
    public final static String categoryname = "categoryname";
    public final static String categoryiconName = "iconName";

    /*
    Timer
     */
    public final static String timerid = "timerid";
    public final static String timerdatestart = "dateStart";
    public final static String timerdateend = "dateEnd";
    public final static String taskid_timer = "taskid_timer";
    public final static String timer_hour = "timer_hour";
    public final static String timer_activated = "isactivated";
    public final static String timer_minutes = "timer_minutes";
    public final static String timer_seconds = "timer_seconds";
    /*
    Linking Category and Tasks N-aire table[Category_task]

    public final static String C_T_idcategorytask = "categorytaskid";
    public final static String C_T_categoryid = "categoryid";
    public final static String C_T_taskid = "taskid";
*/

    public DatabaseHelper(Context context) {
        super(context, DATABASENAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table TASK (no autoincrement since we need to assign the id ourselves)

        db.execSQL("create table " + TABLE_NAME_Task + "(" + Task_ID + " INTEGER PRIMARY KEY  " + delimiter + Task_NAME + text + delimiter + Task_DESCRIPTION + text + delimiter + Task_DATESTART + date
                + delimiter + Task_DATEEND + date + delimiter + Task_PRIORITY + text + delimiter + Task_categoryId + integer + delimiter + Task_DIFFICULTY + text + delimiter + Task_STATUS + integer + delimiter
                + Task_PARENTID + integer +delimiter+Task_done+ text+")");
        //create category table
        createCategoryTable(db);

        //Create table Attachement(will be autoincremented)
        db.execSQL("create table " + TABLE_NAME_Attachement + " (" + Attachement_ID + primary_key + autoincrement + delimiter + Attachement_PATH + text + delimiter + Attachement_FileName + text + delimiter
                + Attachement_TYPE + text + delimiter + Attachement_TASKID + integer + ")");
        //Create table Geolocalisation (will be autoincremented)

    }

    public boolean toggleDone(int task) {
        Task t = getTaskById(task);
        if (t != null) {
            if (t.isDone()){
                Log.d("tdebugDb","the task is:"+t.isDone()+ " setting it to false");
                t.setDone(false);
            } else{
                Log.d("tdebugDb","the task is:"+t.isDone()+ " setting it to true");
                t.setDone(true);
            }
            updateTask(task, t);
        }
        return t.isDone();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Task);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Attachement);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Category);
        onCreate(db);
    }

    public boolean insertTask(Task t) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Task_ID, t.getTaskid());
            contentValues.put(Task_NAME, t.getName());
            contentValues.put(Task_DESCRIPTION, t.getDescription());
            contentValues.put(Task_DATESTART, t.getDateStart().toString());
            contentValues.put(Task_DATEEND, t.getDateEnd().toString());
            contentValues.put(Task_PRIORITY, t.getPriorityLevel());
            contentValues.put(Task_DIFFICULTY, t.getDifficulty());
            contentValues.put(Task_STATUS, 0);
            contentValues.put(Task_done,"No");
            contentValues.put(Task_PARENTID, t.getParentid());
            long ok = db.insert(TABLE_NAME_Task, null, contentValues);
            if (ok == -1) return false;
            else return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Task getTaskById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " where taskid=" + id, null);
        result.moveToFirst();
        int taskid = result.getInt(result.getColumnIndex(Task_ID));
        String name = result.getString(result.getColumnIndex(Task_NAME));
        String description = result.getString(result.getColumnIndex(Task_DESCRIPTION));
        int status = result.getInt(result.getColumnIndex(Task_STATUS));
        String priority = result.getString(result.getColumnIndex(Task_PRIORITY));
        String difficulty = result.getString(result.getColumnIndex(Task_DIFFICULTY));
        String dateStart = result.getString(result.getColumnIndex(Task_DATESTART));
        String dateEnd = result.getString(result.getColumnIndex(Task_DATEEND));
        int parentId = result.getInt(result.getColumnIndex(Task_PARENTID));
        boolean done = result.getString(result.getColumnIndex(Task_done)).equals("Yes") ? true : false;
        Task t = new Task(taskid, name, description, dateStart, dateEnd, priority, difficulty, status, null, parentId, done, null);
        result.close();
        db.close();
        return t;
    }


    public Cursor getTaskCursorById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " where taskid=" + id, null);

        return result;
    }

    public Category getCategoryById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Category + " where "+categoryid +"="+ id, null);
        result.moveToFirst();

        String catid = result.getString(result.getColumnIndex(categoryid));
        String name = result.getString(result.getColumnIndex(categoryname));
        String icon = result.getString(result.getColumnIndex(categoryiconName));
        Category cat = new Category(Integer.parseInt(catid),name,icon);
        return cat;
    }

    public boolean deleteTask(int taskid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME_Task, Task_ID + "=?", new String[]{Integer.toString(taskid)}) > 0;
        //  db.execSQL("DELETE FROM " + TABLE_NAME_Task + " WHERE " + Task_ID + "=" + taskid);
        // db.close();
    }

    public List<Category> getAllCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Category> categories = new ArrayList<>();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Category, null);
        if (result.moveToFirst()) {
            do {
                int catid = result.getInt(result.getColumnIndex(categoryid));
                String catname = result.getString(result.getColumnIndex(categoryname));
                String iconname = result.getString(result.getColumnIndex(categoryiconName));
                Category category = new Category(catid, catname, iconname);
                categories.add(category);
            } while (result.moveToNext());
        }
        return categories;
    }

    public List<Task> getAllTasksByParent(int parentid) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " where parentid=" + parentid + " order by " + Task_DATEEND + " desc", null);
        if (result.moveToFirst()) {
            do {
                int taskid = result.getInt(result.getColumnIndex(Task_ID));
                String name = result.getString(result.getColumnIndex(Task_NAME));
                String description = result.getString(result.getColumnIndex(Task_DESCRIPTION));
                int status = result.getInt(result.getColumnIndex(Task_STATUS));
                String priority = result.getString(result.getColumnIndex(Task_PRIORITY));
                String difficulty = result.getString(result.getColumnIndex(Task_DIFFICULTY));
                String dateStart = result.getString(result.getColumnIndex(Task_DATESTART));
                String dateEnd = result.getString(result.getColumnIndex(Task_DATEEND));
                boolean done = result.getString(result.getColumnIndex(Task_done)).equals("Yes") ? true : false;
                int parentId = result.getInt(result.getColumnIndex(Task_PARENTID));
                Task t = new Task(taskid, name, description, dateStart, dateEnd, priority, difficulty, status, null, parentId, done, null);
                tasks.add(t);
            } while (result.moveToNext());
        }
        result.close();
        return tasks;
    }

    public List<Task> getAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " order by " + Task_DATEEND + " desc", null);
        if (result.moveToFirst()) {
            do {
                int taskid = result.getInt(result.getColumnIndex(Task_ID));
                String name = result.getString(result.getColumnIndex(Task_NAME));
                String description = result.getString(result.getColumnIndex(Task_DESCRIPTION));
                int status = result.getInt(result.getColumnIndex(Task_STATUS));
                String priority = result.getString(result.getColumnIndex(Task_PRIORITY));
                String difficulty = result.getString(result.getColumnIndex(Task_DIFFICULTY));
                String dateStart = result.getString(result.getColumnIndex(Task_DATESTART));
                boolean done = result.getString(result.getColumnIndex(Task_done)).equals("Yes") ? true : false;
                String dateEnd = result.getString(result.getColumnIndex(Task_DATEEND));
                int parentId = result.getInt(result.getColumnIndex(Task_PARENTID));
                Task t = new Task(taskid, name, description, dateStart, dateEnd, priority, difficulty, status, null, parentId, done, null);
                tasks.add(t);
            } while (result.moveToNext());
        }
        result.close();
        return tasks;
    }

    public String getCategoriesDoneStatus(int catid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " where " + Task_categoryId + "="+catid,null);
       result.moveToFirst();
        int total = result.getCount();
        Cursor result2 = db.rawQuery("select * from " + TABLE_NAME_Task + " where " + Task_categoryId + "="+catid+" and "+Task_done+"='Yes' ",null);
        result2.moveToFirst();
        int totalDone=result2.getCount();
    return "("+totalDone+"/"+total+")";
    }

    public List<Task> getAllUrgentTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " where " + Task_PRIORITY + " ='Urgent' and parentid=0 order by " + Task_DATEEND + " desc", null);
        if (result.moveToFirst()) {
            do {
                int taskid = result.getInt(result.getColumnIndex(Task_ID));
                String name = result.getString(result.getColumnIndex(Task_NAME));
                String description = result.getString(result.getColumnIndex(Task_DESCRIPTION));
                boolean done = result.getString(result.getColumnIndex(Task_done)).equals("Yes") ? true : false;
                int status = result.getInt(result.getColumnIndex(Task_STATUS));
                String priority = result.getString(result.getColumnIndex(Task_PRIORITY));
                String difficulty = result.getString(result.getColumnIndex(Task_DIFFICULTY));
                String dateStart = result.getString(result.getColumnIndex(Task_DATESTART));
                String dateEnd = result.getString(result.getColumnIndex(Task_DATEEND));
                int parentId = result.getInt(result.getColumnIndex(Task_PARENTID));
                Task t = new Task(taskid, name, description, dateStart, dateEnd, priority, difficulty, status, null, parentId, done, null);
                tasks.add(t);
            } while (result.moveToNext());
        }
        result.close();
        return tasks;
    }
    /*
    public List<Task> getAllTasksByParent(int parentid,String name,String  description,String datestart,String dateend,String priority,String difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " where  parentid=" + parentid+" order by "+Task_DATEEND+" desc", null);
        if (result.moveToFirst()) {
            do {
                int taskid = result.getInt(result.getColumnIndex(Task_ID));
                String name = result.getString(result.getColumnIndex(Task_NAME));
                String description = result.getString(result.getColumnIndex(Task_DESCRIPTION));
                int status = result.getInt(result.getColumnIndex(Task_STATUS));
                String priority = result.getString(result.getColumnIndex(Task_PRIORITY));
                String difficulty = result.getString(result.getColumnIndex(Task_DIFFICULTY));
                String dateStart = result.getString(result.getColumnIndex(Task_DATESTART));
                String dateEnd = result.getString(result.getColumnIndex(Task_DATEEND));
                int parentId = result.getInt(result.getColumnIndex(Task_PARENTID));
                Task t = new Task(taskid, name, description, dateStart, dateEnd, priority, difficulty, status, null, parentId, null);
                tasks.add(t);
            } while (result.moveToNext());
        }
        result.close();
        return tasks;
    }

*/


    public List<Attachement> listAttachement(int taskid) {

        List<Attachement> attachements = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME_Attachement + " where " + Attachement_TASKID + "=" + taskid, null);
        if (cr.moveToFirst()) {
            do {
                String attachType = cr.getString(cr.getColumnIndex(Attachement_TYPE));
                String attachPath = cr.getString(cr.getColumnIndex(Attachement_PATH));
                String attachName = cr.getString(cr.getColumnIndex(Attachement_FileName));
                String attachTask = cr.getString(cr.getColumnIndex(Attachement_TASKID));
                Attachement a = new Attachement(attachTask, attachPath, attachName, attachType);
                Log.d("debugattach", "the attachement is " + a);

                attachements.add(a);

            } while (cr.moveToNext());
        }
        cr.close();
        return attachements;
    }

    public boolean updateTask(int taskid, Task t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task_ID, t.getTaskid());
        contentValues.put(Task_NAME, t.getName());
        contentValues.put(Task_DESCRIPTION, t.getDescription());
        contentValues.put(Task_DATESTART, t.getDateStart().toString());
        contentValues.put(Task_DATEEND, t.getDateEnd().toString());
        contentValues.put(Task_PRIORITY, t.getPriorityLevel());
        contentValues.put(Task_DIFFICULTY, t.getDifficulty());
        if(t.isDone())  contentValues.put(Task_done, "Yes");
        else  contentValues.put(Task_done, "No");


        contentValues.put(Task_STATUS, t.getStatus());
        contentValues.put(Task_PARENTID, t.getParentid());
        long ok = db.update(TABLE_NAME_Task, contentValues, Task_ID + "=" + taskid, null);
        if (ok == -1) return false;
        else return true;
    }

    /*
    Category Section
     */
    public void createCategoryTable(SQLiteDatabase db) {
        String createQuery = "create table " + TABLE_NAME_Category + " (" + categoryid + primary_key + autoincrement + delimiter
                + categoryiconName + text + delimiter + categoryname + text + ")";

        db.execSQL(createQuery);
    }
/*
    public void createCategoryTaskLinker(SQLiteDatabase db) {
        String createQuery = "create table " + TABLE_NAME_Category_task + "(" + C_T_idcategorytask + integer +
                primary_key + delimiter + C_T_categoryid + integer + delimiter + C_T_taskid + integer + ")";
        db.execSQL(createQuery);

    }
*/


    public boolean insertCategory(Category c) {
        //New Category
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(categoryname, c.getCategoryName());
        if (c.getIconName() == null) contentValues.put(categoryiconName, 0);
        else contentValues.put(categoryiconName, c.getIconName());
        long ok = db.insert(TABLE_NAME_Category, null, contentValues);
        if (ok == -1) return false;
        else return true;
    }


    public List<Task> getTasksByCategory(int catid) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME_Task + " where "+Task_categoryId+"="+catid+"  order by " + Task_DATEEND + " desc", null);
        if (result.moveToFirst()) {
            do {
                int taskid = result.getInt(result.getColumnIndex(Task_ID));
                String name = result.getString(result.getColumnIndex(Task_NAME));
                String description = result.getString(result.getColumnIndex(Task_DESCRIPTION));
                int status = result.getInt(result.getColumnIndex(Task_STATUS));
                String priority = result.getString(result.getColumnIndex(Task_PRIORITY));
                String difficulty = result.getString(result.getColumnIndex(Task_DIFFICULTY));
                String dateStart = result.getString(result.getColumnIndex(Task_DATESTART));
                boolean done = result.getString(result.getColumnIndex(Task_done)).equals("Yes") ? true : false;
                String dateEnd = result.getString(result.getColumnIndex(Task_DATEEND));
                int parentId = result.getInt(result.getColumnIndex(Task_PARENTID));
                Task t = new Task(taskid, name, description, dateStart, dateEnd, priority, difficulty, status, null, parentId, done, null);
                tasks.add(t);
            } while (result.moveToNext());
        }
        result.close();
        return tasks;
    }

    public boolean updateCategory(int category_id, Category c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(categoryid,c.getCategoryid());
        contentValues.put(categoryname, c.getCategoryName());
        contentValues.put(categoryiconName, c.getIconName());
        long ok = db.update(TABLE_NAME_Category, contentValues, categoryid + "=" + category_id, null);
        if (ok == -1) return false;
        else return true;
    }

    public boolean deleteCategory(int category_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //You need to update the stored categories id in the joint tables
        boolean deleted = db.delete(TABLE_NAME_Category, categoryid + "=" + category_id, null) > 0;
        db.close();
        return deleted;
    }


    public boolean moveTaskTocategory(int categoryid, Task t) {
        //update the task category to another category...
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task_categoryId, categoryid);
        long ok = db.update(TABLE_NAME_Task, contentValues, Task_ID + "=" + t.getTaskid(), null);
        if (ok == -1) return false;
        else return true;
    }

    public boolean removeTaskFromCategory(int task_id) {
        //assign 0 on the category where the taskid is located
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(categoryid, 0);
        long ok = db.update(TABLE_NAME_Task, contentValues, Task_ID + "=" + task_id, null);
        if (ok == -1) return false;
        else return true;
    }

    public int getCategoryIdByTask(int task_id) {
        //get the task old category
        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor c = db.rawQuery("select * from "+TABLE_NAME_Category)
        return 0;
    } //not done yet


    /*
    Attachement Section
     */
    public boolean insertAttachement(Attachement a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Attachement_PATH, a.getAttachmentPath());
        contentValues.put(Attachement_TYPE, a.getGetAttachmentType());
        contentValues.put(Attachement_TASKID, a.getTaskid());
        contentValues.put(Attachement_FileName, a.getAttachmentname());
        return db.insert(TABLE_NAME_Attachement, null, contentValues) > 0;
    }

    /*
    Timer section
     */
    public void createTimerTable(SQLiteDatabase db) {
        String createQuery = "create table " + TABLE_NAME_Timer + "(" + timerid + primary_key + autoincrement + delimiter + timerdatestart + text +
                delimiter + timerdateend + text + delimiter + timer_hour + integer + delimiter + timer_minutes + integer + delimiter
                + timer_seconds + delimiter + taskid_timer + integer + ")";
        db.execSQL(createQuery);
    }
/* REV 2 set Timer instant
    public boolean insertTimer(Timer t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(timerdatestart, t.getTimerdate());
        contentValues.put(timerdateend, t.getEndDate().toString());
        contentValues.put(timer_hour, t.getHour());
        contentValues.put(timer_minutes, t.getMin());
        contentValues.put(timer_seconds, t.getSec());
        contentValues.put(timer_activated, t.isActivated());
        contentValues.put(timer_hour, t.getHour());
        contentValues.put(timer_seconds, t.getSec());
        long ok = db.insert(TABLE_NAME_Category, null, contentValues);
        Log.w("cat", "the Timer set is:" + t.toString());
        if (ok == -1) return false;
        else return true;
    }

    public boolean updateTimer(int id, Timer t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(timerdatestart, t.getStartDate().toString());
        contentvalues.put(timerdateend, t.getEndDate().toString());
        contentvalues.put(taskid_timer, t.getTaskid());
        contentvalues.put(timer_activated, t.isActivated());
        contentvalues.put(timer_hour, t.getHour());
        contentvalues.put(timer_minutes, t.getMin());
        contentvalues.put(timer_seconds, t.getSec());
        long updated = db.update(TABLE_NAME_Timer, contentvalues, taskid_timer + "=" + id, null);
        if (updated == -1) return false;
        else return true;
    }

    public void deleteTimer(int id, Timer t) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_Timer + " WHERE " + timerid + "=" + id);
        db.close();
    }
*/

}
