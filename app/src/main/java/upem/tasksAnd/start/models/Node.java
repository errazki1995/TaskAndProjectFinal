package upem.tasksAnd.start.models;

import java.util.ArrayList;
import java.util.List;

public class Node extends Task {
 private int id;
 private Task t= null;
 private List<Node> children = new ArrayList<>();
 private Node parent=null;

public Node(){

}
    public Node(Task t){
    this.t = t;
     }


    public Task getT() {
        return t;
    }

    public void setT(Task t) {
        this.t = t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Node> getTasks() {
        return children;
    }

   /* public void setTask(List<Node> task) {
        this.nodes = task;
    }
*/

   public void setTask(Task t){
       this.t= t;
   }
   public Task getTask(){return t;}
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addChildrenTasks(Node t){
        t.setParent(this);
        this.children.add(t);
    }

    public List<Node> getNodes() {
        return children;
    }

    public void setNodes(List<Node> nodes) {
       for(Node node:nodes){
           node.setParent(this);
       }
        this.children.addAll(nodes);
        //this.children = nodes;
    }
}
