package upem.tasksAnd.start.Services;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class IdAssigner {
    DatabaseHelper dbhelp;

    public IdAssigner(DatabaseHelper databaseHelper) {
        this.dbhelp = databaseHelper;
    }

    public int assignTaskId() {
        int threshold = 500;
        int taskid = 0, i = 1;
        while (i <= threshold) {
            if (i == threshold && dbhelp.getTaskCursorById(i).getCount() > 0) threshold += 500;
            else if(dbhelp.getTaskCursorById(i).getCount()>0) i++;
            else{
                dbhelp.getTaskCursorById(i).close();
                taskid=i;
                return taskid;
            }
        }
        dbhelp.getTaskCursorById(i).close();
        return taskid;
    }

}
