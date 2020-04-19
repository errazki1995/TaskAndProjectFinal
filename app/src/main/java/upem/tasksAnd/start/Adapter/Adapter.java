package upem.tasksAnd.start.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.Inflater;

import upem.tasksAnd.start.CategoriesActivity;
import upem.tasksAnd.start.NewTask;
import upem.tasksAnd.start.R;
import upem.tasksAnd.start.Services.Display;
import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.TasksList;
import upem.tasksAnd.start.Timerconfigure;
import upem.tasksAnd.start.ViewTask;
import upem.tasksAnd.start.models.MyTreeNode;
import upem.tasksAnd.start.models.Task;

public class Adapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    List<Task> tasks;
    Context context;
    TextView dateEnd;
    TextView txtname;
    TextView txtdescription;


    TaskService taskService;
    static LayoutInflater layoutInflater = null;

    public Adapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            taskService = new TaskService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = parent.getPositionForView(view);
        Toast.makeText(context.getApplicationContext(), pos + "", Toast.LENGTH_LONG).show();
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.show();

    }

    public class ViewHolder {
        TextView txtstatus;
        TextView txtTaskNumber;
        ImageButton btnNewSubtask;
        ImageButton btnEditSubtask;
        ImageButton btnListSubtasks;
        ImageView imageViewmore;
        RadioButton isdone;
        TextView txtelement;

    }

    @Override
    public int getCount() {
        return tasks.size();

    }

    public Task getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition(int i) {
        return i;
    }


    public View getView(final int position, View v, ViewGroup parent) {
        View itemview = v;
        final ViewHolder viewHolder = new ViewHolder();
        itemview = (itemview == null) ? layoutInflater.inflate(R.layout.taskitem1, null) : itemview;

        txtname = (TextView) itemview.findViewById(R.id.lbltaskName);
        viewHolder.txtelement = (TextView) itemview.findViewById(R.id.sideelement);
        viewHolder.txtstatus = (TextView) itemview.findViewById(R.id.lblstatus);
        viewHolder.txtTaskNumber = (TextView) itemview.findViewById(R.id.lbltasknumber);
        viewHolder.btnNewSubtask = (ImageButton) itemview.findViewById(R.id.btnNewsubtask);
        txtdescription = (TextView) itemview.findViewById(R.id.lbltaskDescription);
        dateEnd = (TextView) itemview.findViewById(R.id.datedoes);
        viewHolder.btnEditSubtask = (ImageButton) itemview.findViewById(R.id.btnEditsubtask);
        viewHolder.btnListSubtasks = (ImageButton) itemview.findViewById(R.id.btnListsubtasks);
        viewHolder.imageViewmore = (ImageView) itemview.findViewById(R.id.imgviewMore);
        viewHolder.isdone = (RadioButton) itemview.findViewById(R.id.setdone);




        final Task t = getItem(position);
        if(t.isDone()){
            //strikeTask();
           viewHolder.isdone.setChecked(true);
           viewHolder.txtelement.setBackgroundResource(R.drawable.elementsidegray);

        }else{
            viewHolder.isdone.setChecked(false);
            viewHolder.txtelement.setBackgroundResource(R.drawable.elementsideblue);

        }



        txtname.setText(t.getName());
        txtdescription.setText(t.getDescription());
        dateEnd.setText(dateCalculator(t));
        viewHolder.imageViewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu((Activity) context, v, position);
            }
        });
        viewHolder.isdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if(viewHolder.isdone.isChecked()){
                if(t.isDone()){
                    viewHolder.isdone.setChecked(false);
                    taskService.setDone(t.getTaskid());

                }else {
                    viewHolder.isdone.setChecked(true);
                    taskService.setDone(t.getTaskid());
                   }
                reFreshSameList((Activity)context,position);
            }
        });

        return itemview;
    }


    private void showPopMenu(final Activity activity, View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemhuhadd:
                        addItemContent(activity, position);
                        break;
                    case R.id.itemhuhdelete:
                        deleteItemContent(activity, position);
                        break;
                    case R.id.itemhuhlistsubtask:
                        listItemContent(activity, position);
                        break;
                    case R.id.itemhuhedit:
                        editItemContent(activity, position);
                        break;
                    case R.id.itemhuhview:
                        viewItemContent(activity, position);
                        break;
                    case R.id.itemhuhmovetolist:
                        movetoAnotherItemContent(activity, position);
                        break;
                    case R.id.itemhuhsettimer:
                        timerItem(activity,position);
                        break;
                    default:
                        break;

                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void addItemContent(final Activity activity, int position) {
        Toast.makeText(context, "This task id is:" + getItem(position).getTaskid() + " and the parent id of this task is :" + getItem(position).getParentid(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity.getApplicationContext(), NewTask.class);
        intent.putExtra("parentidN", getItem(position).getTaskid() + "");
        context.startActivity(intent);

    }

    private void reFreshSameList(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), TasksList.class);
        intent.putExtra("parentidL", getItem(position).getParentid() + "");
        activity.startActivity(intent);
    }


    private void editItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), NewTask.class);
        intent.putExtra("tasktoEdit", getItem(position));
        context.startActivity(intent);
    }

    private void timerItem(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), Timerconfigure.class);
        intent.putExtra("tasktimer", getItem(position).getName());
        context.startActivity(intent);
    }

    private void listItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), TasksList.class);
        intent.putExtra("parentidL", getItem(position).getTaskid() + "");
        activity.startActivity(intent);
    }

    private void deleteItemContent(final Activity activity, int position) {
        Task t = getItem(position);
        String taskname = t.getName();
        confirmpanel("Warning!","delete the main task leads to delete the secondary tasks");
        if (taskService.deleteTask(t.getTaskid()))
            Toast.makeText(activity.getApplicationContext(), "The task " + taskname + " has been successfuly deleted !", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(activity.getApplicationContext(), "Failed to delete the task", Toast.LENGTH_LONG).show();
        Intent i = new Intent(activity.getApplicationContext(), TasksList.class);
        activity.startActivity(i);
    }

    private void viewItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), ViewTask.class);
        intent.putExtra("viewTask", getItem(position));
        context.startActivity(intent);
    }

    void confirmpanel(String title,String message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message).setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_input_delete).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
               //deletesubtasks(taskparent);
            }
        }).setCancelable(true).show();
    }
public String deletesubtasks(MyTreeNode<Task> taskparent){
return "";
}

    private void movetoAnotherItemContent(final Activity activity, int position) {

        Intent intent = new Intent(activity.getApplicationContext(), CategoriesActivity.class);
        intent.putExtra("theTaskCategory", getItem(position));
        context.startActivity(intent);
    }

    private String dateCalculator(Task t) {
        long diff = convertToDate(t.getDateEnd()).getTime()-getActualDate().getTime();
        String dateMessage="";
        int daysbetween =(int) (diff/(1000*60*60*24));
        if(daysbetween==0){
            statusDateColor("#eb8676");
            dateMessage="Today";
        }
        else if(daysbetween==1) {
            statusDateColor("#e9df0b");
            dateMessage= "Tomorrow";
        }
        else if(daysbetween>1) {
            statusDateColor("#39b7e7");
            dateMessage ="In "+daysbetween+" days" ;
        }
        else {
            statusDateColor("#ff002b");
           //strikeTask();
            dateMessage="Expired";
        }
    return dateMessage;
    }
    void statusDateColor(String color){
        dateEnd.setTextColor(Color.parseColor(color));    }

    Date convertToDate(String date){
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = formater.parse(date);
            return date1;
        }catch (Exception e){e.printStackTrace();return null;}
   }
   void strikeTask(){
       txtname.setPaintFlags(txtname.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
       txtdescription.setPaintFlags(txtdescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

   }
    void removestrikeTask(){

        txtname.setPaintFlags(txtname.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        txtdescription.setPaintFlags(txtdescription.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

    }

    public Date getActualDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return today;
    }

}