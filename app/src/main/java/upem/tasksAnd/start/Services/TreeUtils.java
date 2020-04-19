package upem.tasksAnd.start.Services;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import upem.tasksAnd.start.models.MyTreeNode;
import upem.tasksAnd.start.models.Task;

public class TreeUtils {
    static int index;
    String treetext;

    private Context contexte;
    public TaskService taskService;

    public static MyTreeNode<Task> Thetree;
    /*
     *  Fi check
     */
    public static JSONArray jsontree = new JSONArray();
    public static JSONArray testjsonarr = new JSONArray();


    public static List<MyTreeNode<Task>> tasksSource = new ArrayList<MyTreeNode<Task>>();

    public TreeUtils(Context ctx) {
        this.contexte = ctx;
        try {
            taskService = new TaskService(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<MyTreeNode<Task>> getSource() {
        return tasksSource;
    }

    public static void setRootNode(MyTreeNode<Task> roottree) {
        Thetree = roottree;
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


    public void buildTree(MyTreeNode<Task> root) {
        List<MyTreeNode<Task>> children = getNodesByParent(root.getData().getTaskid());
        root.addChildren(children);
        Log.d("taskDateinBuild", "the date is" + root.getData().getName() + " the date " + root.getData().getDateStart() + " date end: " + root.getData().getDateEnd());
        for (int i = 0; i < root.getChildren().size(); i++) {
            buildTree(root.getChildAt(i));
        }
    }

    void setTree(String tr) {
        if (tr != "null")
            this.treetext += tr;

    }

    public String getTreetext() {
        return treetext;
    }


    public static  String traverse(MyTreeNode<Task> obj,String indent) {
        if (obj != null ) {
            if(obj.getData().getTaskid()==0)System.out.println(indent+"+-"+obj.getData().getName()+" id :"+obj.getData().getTaskid());
            else System.out.println(indent+"+-"+obj.getData().getName()+" id :"+obj.getData().getTaskid()/*+" parent id:"+obj.getParent().getData().getTaskid()*/);
            tasksSource.add(obj);
            if(obj.isLeaf()) indent+=" ";
            else {
                indent+="| ";
                for (int i = 0; i < obj.getChildren().size(); i++) {


                    traverse(obj.getChildren().get(i),indent);
                }
            }
        }
        return "";
    }


    public String traverse(MyTreeNode<Task> obj, String indent, String tree) {
        if (obj != null) {
            if (obj.getData().getTaskid() == 0) {
                tree = "+-" + obj.getData().getName() + "\n";
                setTree(tree);
            } else {
                tree = "+-" + obj.getData().getName() + "\n";/*+" parent id:"+obj.getParent().getData().getTaskid()*/
                setTree(tree);
            }
            tasksSource.add(obj);
            if (obj.isLeaf()) {
                tree = " ";
                setTree(tree);
            } else {
                tree = "| ";
                setTree(tree);
                for (int i = 0; i < obj.getChildren().size(); i++) {
                    traverse(obj.getChildren().get(i), indent, tree);
                }
            }
        }
        return tree;
    }


    public JSONObject get_nodes(int nodeid) throws JSONException {
        //No surrender !
        JSONObject response = getchild(nodeid);
        JSONObject json = new JSONObject();
        try {
            if (nodeid == 0) {
                json.put("taskid", response.get("taskid"));
                json.put("name", response.get("name"));
                json.put("parentid", response.get("parentid"));

            } else {
                json.put("taskid", response.get("taskid"));
                json.put("name", response.get("name"));
                json.put("parentid", response.get("parentid"));
                json.put("description", response.get("description"));
                json.put("datestart", response.get("datestart"));
                json.put("dateend", response.get("dateend"));
                json.put("prioritylevel", response.get("prioritylevel"));
                json.put("status", response.get("status"));
            }
            JSONArray children = new JSONArray();
            for (int i = 0; i < getchildren(nodeid).length(); i++) {
                int child_id = getchildren(nodeid).getJSONObject(i).getInt("taskid");
                JSONObject childobj = get_nodes(child_id);
                children.put(childobj);
            }
            json.put("children", children);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static JSONObject toJSONObject(Task t) {
        //description , dateStart, dateEnd, priorityLevel,difficulty,status
        JSONObject json = new JSONObject();
        try {
            json.put("taskid", t.getTaskid());
            json.put("name", t.getName());
            json.put("parentid", t.getParentid());
            json.put("description", t.getDescription());
            json.put("datestart", t.getDateStart());
            json.put("dateend", t.getDateEnd());
            json.put("prioritylevel", t.getPriorityLevel());
            json.put("status", t.getStatus());
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public JSONObject getchild(int child_id) throws JSONException {
        JSONObject obj = new JSONObject();
        for (MyTreeNode<Task> t : tasksSource) {
            if (t.getData().getTaskid() == child_id) {
                obj = toJSONObject(t.getData());
                break;
            }
        }
        return obj;
    }

    JSONArray getchildren(int parent) throws JSONException {
        JSONArray ar = new JSONArray();

        for (MyTreeNode<Task> t : tasksSource) {
            if (t.getData().getParentid() == parent && !t.getData().getName().equals("Root")) {
                JSONObject json = new JSONObject();
                json.put("taskid", t.getData().getTaskid());
                json.put("name", t.getData().getName());
                json.put("parentid", t.getData().getParentid());
                /*
                json.put("description", t.getData().getDescription());
                json.put("datestart", t.getData().getDateStart());
                json.put("dateend", t.getData().getDateEnd());
                json.put("prioritylevel", t.getData().getPriorityLevel());
                json.put("status", t.getData().getStatus());*/
                ar.put(json);
            }
        }
        return ar;
    }

    public static void deleteTasksTree(MyTreeNode<Task> obj) {

        if (obj.getParent() == null) obj = null;
        else {
            MyTreeNode<Task> parent = obj.getParent();
            for (int i = 0; i < parent.getChildren().size(); i++) {
                getNodedeleteByid(parent.ChildAt(i).getData().getTaskid());
                System.out.println("deleting the node" + parent.ChildAt(i).getData().getName());
            }
            parent.setChildren(null);
            deleteTasksTree(parent);
        }
    }

    public static boolean getNodedeleteByid(int nodeid) {
        System.out.println("node (task" + nodeid + " is deleted");
        return true;
    }


}
