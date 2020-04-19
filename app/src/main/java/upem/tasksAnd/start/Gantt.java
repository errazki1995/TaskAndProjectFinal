package upem.tasksAnd.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.models.MyTreeNode;
import upem.tasksAnd.start.models.Node;
import upem.tasksAnd.start.models.Task;

public class Gantt extends AppCompatActivity implements View.OnClickListener {
    Map<Date, Integer> datemap;
    Map<Task, Integer> taskmap = new HashMap<Task, Integer>();
    public static  int counter = 0;
    String[] colors ={"#871F78","#FF0000","#F2F22C","#3D85C6","#67D428"}; //purple,red,yellow,blue,

    GridLayout ganttgrid;
    TaskService taskService;
    List<Task> tasks;
    int index = -1;
    int listsize = 0;

    public int getListsize() {
        return listsize;
    }

    public void setListsize(int listsize) {
        this.listsize = listsize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt);
        ganttgrid = (GridLayout) findViewById(R.id.ganttgrid);

        buildGantt();
    }



    void buildGantt() {

        LoadStringsAsync task = new LoadStringsAsync(this);
        task.execute();

    }



    public void debuglist(List<Date> toload) {
        for (Date d : toload) {
            Log.d("datedebug", "date :" + DatesUtilByme.convertToString(d));
        }
    }

    public void drawDates(int colscount, List<Date> dates) {
        Log.d("Scolscount", "thecols count are " + colscount);
        int i = 0;
        for (int cols = 0; cols < colscount - 1; cols++) {
            GridLayout.LayoutParams gridsets = new GridLayout.LayoutParams();
            gridsets.height = GridLayout.LayoutParams.WRAP_CONTENT;
            gridsets.width = GridLayout.LayoutParams.WRAP_CONTENT;
            gridsets.rowSpec = GridLayout.spec(0);
            if (cols + 1 == dates.size()) gridsets.columnSpec = GridLayout.spec(cols);
            else gridsets.columnSpec = GridLayout.spec(cols + 1);
            Log.d("colscount", "index i " + i);
            //gridsets.columnSpec = GridLayout.spec(cols);
            TextView txttask = new TextView(getApplicationContext());
            txttask.setLayoutParams(gridsets);
            txttask.setText(DatesUtilByme.convertToString(dates.get(cols)));


            txttask.setBackgroundResource(R.drawable.backgroundtextradiusorange);
            txttask.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            txttask.setTextSize(23);
            txttask.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            ganttgrid.addView(txttask);
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        }
    }


    void drawTasks(int tasksLength) {
        for (int rows = 0; rows < tasksLength; rows++) {
            GridLayout.LayoutParams gridsets = new GridLayout.LayoutParams();
            gridsets.height = GridLayout.LayoutParams.WRAP_CONTENT;
            gridsets.width = GridLayout.LayoutParams.WRAP_CONTENT;
            gridsets.rowSpec = GridLayout.spec(rows);
            gridsets.columnSpec = GridLayout.spec(0);
            TextView txttask = new TextView(getApplicationContext());
            txttask.setLayoutParams(gridsets);
            if (rows < tasks.size())
                txttask.setText(tasks.get(rows).getName());
            ganttgrid.addView(txttask);
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View v) {

    }


    void callTobuildTheTree() {
        Task root = new Task(0, "root");
        MyTreeNode<Task> node = new MyTreeNode<>(root);
        buildTree(node);
        walkTree(node, "");

    }


    public void drawSingleTask(Task task, int position) {
        if(task.getDateStart()!=null && task.getDateEnd() != null){
            GridLayout.LayoutParams gridsets = new GridLayout.LayoutParams();
        gridsets.height = GridLayout.LayoutParams.WRAP_CONTENT;
        gridsets.width = GridLayout.LayoutParams.WRAP_CONTENT;
        gridsets.rowSpec = GridLayout.spec(position);
        gridsets.columnSpec = GridLayout.spec(0);
        //taskmap.put(task,position);

 displayHash();

         if(datemap.get(DatesUtilByme.convertToDate(task.getDateEnd())) != null && datemap.get(DatesUtilByme.convertToDate(task.getDateStart())) !=null) {
                int posend = datemap.get(DatesUtilByme.convertToDate(task.getDateEnd())) + 1;
                int posstart = datemap.get(DatesUtilByme.convertToDate(task.getDateStart())) + 1;
                int weight = posend - posstart;
                int columnposition = posstart;
                 generateComposant(position, columnposition, weight + 1);
            }


        TextView txttask = new TextView(getApplicationContext());
        txttask.setLayoutParams(gridsets);
        txttask.setText(task.getName());
        //Marked
        txttask.setBackgroundResource(R.drawable.backgroundtextradius);
        txttask.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        txttask.setTextSize(29);
        txttask.setGravity(Gravity.CENTER_VERTICAL);
        txttask.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
       // txttask.setText("Do your homework");
        //txttask.setHeight(170);
        txttask.setWidth(400);
        ganttgrid.addView(txttask);
        }
        else return;


    }

  void displayHash(){

      for (Date key: datemap.keySet()) {
         Log.d("date_hash","DATE : " + DatesUtilByme.convertToString(key)+ " value : " + datemap.get(key));
      }
  }

  void fillHash(Date date,int columnposition){
        datemap.put(DatesUtilByme.convertToDate(DatesUtilByme.convertToString(date)),columnposition);
  }


    void generateComposant(int row,int col,int weight) {
        GridLayout.LayoutParams gridsets = new GridLayout.LayoutParams();
        gridsets.height = GridLayout.LayoutParams.WRAP_CONTENT;
        gridsets.width = GridLayout.LayoutParams.WRAP_CONTENT;
        gridsets.rowSpec = GridLayout.spec(row);

        gridsets.columnSpec = GridLayout.spec(col, weight);
        TextView b = new TextView(getApplicationContext());
        gridsets.setGravity(Gravity.FILL_HORIZONTAL);
        b.setBackgroundColor(Color.parseColor("#ff81a6"));
        b.setLayoutParams(gridsets);
        ganttgrid.addView(b);
    }


    public void buildTree(MyTreeNode<Task> root) {
        List<MyTreeNode<Task>> children = getNodesByParent(root.getData().getTaskid());
        root.addChildren(children);
        Log.d("taskDateinBuild","the date is"+ root.getData().getName()+" the date "+root.getData().getDateStart()+" date end: "+root.getData().getDateEnd());
        index++;

        drawSingleTask(root.getData(), index);

        for (int i = 0; i < root.getChildren().size(); i++) {
            buildTree(root.getChildAt(i));
        }
    }


    public MyTreeNode<Task> isParent(MyTreeNode<Task> somenode) {
        if (somenode.getParent() == null) return somenode;
        return isParent(somenode.getParent());
    }


    public static void walkTree(MyTreeNode<Task> obj, String indent) {
        if (obj != null) {
            Log.d("thetree", "" + indent + "+-" + obj.getData().getName());
            Log.d("TaskInTree", "the task dates are :" + obj.getData().getDateStart()+" end :"+ obj.getData().getDateEnd());
            if (obj.isLeaf()) indent += " ";
            else {
                indent += "| ";
                for (int i = 0; i < obj.getChildren().size(); i++) {
                    walkTree(obj.getChildren().get(i), indent);
                }
            }
        }
        return;
    }

    List<MyTreeNode<Task>> getNodesByParent(int parent) {
        List<MyTreeNode<Task>> nodes = new ArrayList<>();
        List<Task> nodesByParent = taskService.listAllTasksByParent(parent);
        for (Task task : nodesByParent) {
            MyTreeNode<Task> tasksNode = new MyTreeNode<Task>(task);
            nodes.add(tasksNode);
        }
        return nodes;
    }

    void fillhashmapFromList(List<Date> dates){
        datemap=new HashMap<Date, Integer>();
        int i=0;
        for(Date date:dates){
            fillHash(date,i);
            i++;
        }
    }


    public class LoadStringsAsync extends AsyncTask<Void, List<Date>, List<Date>> {
        private WeakReference<Gantt> activityweakrefrence;

        LoadStringsAsync(Gantt g) {
            activityweakrefrence = new WeakReference<Gantt>(g);
        }

        @Override
        protected List<Date> doInBackground(Void... voids) {
            // get the parent id , 0 for all
            int parentid = 0;
            DatesUtilByme datesUtilByme=new DatesUtilByme(parentid,getApplicationContext());
            List<Date> datesbetween = datesUtilByme.getDates();
            return datesbetween;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Gantt g = activityweakrefrence.get();
            if (g == null || g.isFinishing()) {
                return;
            }
            try {
                taskService = new TaskService(getApplicationContext());
                //Get the value parent here
                tasks = taskService.listAllTasks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(List<Date> dates) {
            super.onPostExecute(dates);

            Log.d("receivedList", "the list count is " + dates.size());
            debuglist(dates);
            ganttgrid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

            int colscount = dates.size();// size+1;
            setListsize(colscount);
            int rowscount = tasks.size() + 3;
            ganttgrid.setColumnCount(colscount);
            ganttgrid.setRowCount(rowscount);
            fillhashmapFromList(dates);
            drawDates(colscount, dates);
            //reformatHash(datemap);
            Log.d("Hashmapsize", "THE HASH SIZE" + datemap.size());
            callTobuildTheTree();

        }
    }
}
