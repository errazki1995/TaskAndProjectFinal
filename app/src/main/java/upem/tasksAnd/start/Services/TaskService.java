package upem.tasksAnd.start.Services;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import upem.tasksAnd.start.models.Attachement;
import upem.tasksAnd.start.models.MyTreeNode;
import upem.tasksAnd.start.models.Task;
import upem.tasksAnd.start.models.Node;

public class TaskService extends AppCompatActivity {
    public static final int duration = 0;
    Node task;
    IdAssigner idAssigner;
    static DatabaseHelper db;
    Context context;
    Json json;
    Map<String, String> map;

    void loadFormat() {
        map = new HashMap<String, String>();
        map.put("mp3", "audio");
        map.put("mp4", "video");
        map.put("wav", "audio");
        map.put("png", "image");
        map.put("jpg", "image");
        map.put("jpeg", "image");
        map.put("ogg", "audio");
        map.put("mov", "video");
        map.put("mov", "video");
        map.put("3gp", "video");
        map.put("3gpp", "video");


    }

    public TaskService(Context ctx) throws JSONException {
        task = new Node(new Task());
        this.context = ctx;
        loadFormat();
        db = new DatabaseHelper(ctx);
        idAssigner = new IdAssigner(db);
        //checkPermissions();

        //test();

    }

    public int taskIdGiver() {
        return idAssigner.assignTaskId();
    }

    public boolean addtask(int parentid, Task t) throws JSONException {
        t.setParentid(parentid);
        if (db.insertTask(t)) {
            if (parentid == 0)
                Toast("This is a Main Task, added successfully" + t.getTaskid(), duration);
            else
                Toast("This is a subtask with parentid :" + parentid + " added successfully" + t.getTaskid(), duration);
            return true;
        }
        Toast("Failure adding task", duration);
        return false;
    }

    public void Toast(String message, int duration) {
        int i = 0;
        while (i < duration) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            i++;
        }
    }

    /*

        void test()throws JSONException {
            Task t = new Task();
          //  JSONObject jsondata = new JSONObject(getJsonFile());
            //JSONArray array = jsondata.getJSONArray("tasks");
    JSONArray array = new JSONArray();
            JSONObject newtask =new JSONObject();
            JSONObject newattachement = new JSONObject();
            JSONObject multimedia = new JSONObject();

    //        newattachement.put("attachementid",t.getAttachement().getAttachementid());
      //      newattachement.put("insidevideo",t.getAttachement().isVideoinside());
        //    newattachement.put("insideaudio",t.getAttachement().isVideoinside());
          //  newattachement.put("insideimage",t.getAttachement().isVideoinside());
            //newattachement.put("insidegeo",t.getAttachement().isVideoinside());


            newtask.put("taskid",t.getTaskid());
            newtask.put("datestart",t.getDateStart());
            newtask.put("dateend",t.getDateEnd());
            newtask.put("prioritylevel",t.getPriorityLevel());
            newtask.put("difficulty",t.getStatus());
            //newtask.put("attachement",t.getA)
           // newtask.put("attachement",newattachement);
            array.put(newtask);
            JSONObject  finaljson= new JSONObject();
            finaljson.put("tasks",array.toString());
            writeToFile(finaljson.toString());

        }

    */
    public String getJsonFile() {
        return new Json(context).getTheJson();
    }

    public void writeToFile(String jsondata) {
        try {
            FileWriter file = new FileWriter(getJsonFile());
            BufferedWriter bufferedwriter = new BufferedWriter(file);
            bufferedwriter.write(jsondata);
            bufferedwriter.close();

        } catch (IOException e) {
            Log.i("ErrorOpenFileOrWrite", "Error openning file or write to it");
            e.printStackTrace();
        }
    }

    public void addSubtaskChild(int parentTaskid, Task t) {
        Node newsubtask = new Node(t);
        //search in json file the object with that parent id and place it as a parent
        addChild(parentTaskid, newsubtask);

    }

    public void addChild(int parentTaskid, Node child) {
        //search for parentTaskid
        //get the parent object
        Node parent = getParentObject(parentTaskid);
        child.setParent(task);
        task.getNodes().add(child);
        //put the tree with the new object
    }

    public Node getParentObject(int parentTasksid) {
        //search for the object based on the parentTasksid, populate object from json and return the sub-tree
        return null;
    }

    public List<Node> listTasks() {
        //get from Json file
        //return all tasks
        return null;
    }

    public List<Node> listsubtasks(int taskparentid) {
        //get subtasks from task
        //search in json
        //return the tasks
        return null;
    }

    public void modifyTask(int taskid, Task t) {
        //search id of task and replace the whole object with the new object

    }

    public boolean deleteTask(int taskid) {
        int attachementcount = getTaskAttachement(taskid).size();
      /*
              String imagespath = context.getExternalFilesDir(null).toString() + "/" + NewTask.pathTocontent;

        File directory = new File(imagespath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (int j = 0; j < files.length; j++) {
                // Log.w("FILESHIT", "the file name is:" + files[j].getName() + " the file split:" + files[j].getName().split("_")[2] + " and path is " + files[j].getAbsolutePath() + " you are inside");
                String finalfilename = files[j].getName().split("_")[2];
                if (finalfilename.split("\\.")[0] == taskid + "") {
                    if (files[j].exists()) {
                        files[j].delete();
                    }
                }
            }

        }
        */


        deleteTaskBranch(taskid);
        return db.deleteTask(taskid);
    }

    public static List<Task> listAllTasksByParent(int parentid) {
        return db.getAllTasksByParent(parentid);
    }

    public boolean updateTask(int taskid, Task t) {
        return db.updateTask(taskid, t);
    }

    public boolean setupAttachemennt(Attachement attachement) {
        return db.insertAttachement(attachement);
    }

    public List<Attachement> getTaskAttachement(int taskid) {
        return db.listAttachement(taskid);
    }


    public List<Task> listAllTasks() {
        return db.getAllTasks();
    }


    public boolean isExternableStoreWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public static void buildTree(MyTreeNode<Task> root) {
        List<MyTreeNode<Task>> children = getNodesByParent(root.getData().getTaskid());
        root.addChildren(children);
        for (int i = 0; i < root.getChildren().size(); i++) {
            buildTree(root.getChildAt(i));
        }
    }

    public MyTreeNode<Task> isParent(MyTreeNode<Task> somenode) {
        if (somenode.getParent() == null) return somenode;
        return isParent(somenode.getParent());
    }

    public MyTreeNode<Task> lastNode(MyTreeNode<Task> branch) {
        MyTreeNode<Task> lastnode = null;
        if (branch.getChildren().size() == 0) return lastnode = branch;
        for (int i = 0; i < branch.getChildren().size(); i++) {
            lastNode(branch);
        }
        return lastnode;
    }


    public static void walkTree(MyTreeNode<Task> obj, String indent) {
        if (obj != null) {
            Log.d("thetree", "" + indent + "+-" + obj.getData().getName());
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

    public Task getTaskByid(int task) {
        return db.getTaskById(task);
    }

    public List<Task> getUrgentTasks() {
        return db.getAllUrgentTasks();
    }

    public boolean setDone(int task) {
        return db.toggleDone(task);
    }

    public boolean deleteTaskBranch(int task) {
        Task taskObj = db.getTaskById(task);
        if (taskObj == null) return false;
        else {
            MyTreeNode<Task> tasknodeAsroot = new MyTreeNode<>(taskObj);
            buildTree(tasknodeAsroot);
            MyTreeNode<Task> lastnodeinbranch = lastNodeinBranch(tasknodeAsroot);
            deleteTasksTree(lastnodeinbranch, "");
            return true;
        }
    }


    void deleteTasksTree(MyTreeNode<Task> obj, String indent) {
        if (obj.getParent() == null) obj = null;
        else {
            MyTreeNode<Task> parent = obj.getParent();
            for (int i = 0; i < parent.getChildren().size(); i++) {
                deleteTask(parent.getChildAt(i).getData().getTaskid());
            }
            parent.setChildren(null);
            deleteTasksTree(parent, "");
        }
    }


    MyTreeNode<Task> lastNodeinBranch(MyTreeNode<Task> branch) {
        MyTreeNode<Task> inNextDepthNode = null;
        if (branch.getChildren().size() == 0) return branch;
        else {
             inNextDepthNode = lastNodeinBranch(branch.getChildren().get(0));
            return inNextDepthNode;
        }
    }

    static List<MyTreeNode<Task>> getNodesByParent(int parent) {
        List<MyTreeNode<Task>> nodes = new ArrayList<>();
        List<Task> nodesByParent = listAllTasksByParent(parent);
        for (Task task : nodesByParent) {
            MyTreeNode<Task> tasksNode = new MyTreeNode<Task>(task);
            nodes.add(tasksNode);
        }
        return nodes;
    }


}

