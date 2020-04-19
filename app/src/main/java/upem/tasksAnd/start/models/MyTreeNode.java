package upem.tasksAnd.start.models;

import java.util.ArrayList;
import java.util.List;

public class MyTreeNode<T>{
    private T data = null;
    private List<MyTreeNode<T>> children = new ArrayList<>();
    private MyTreeNode<T> parent = null;

    public MyTreeNode(T data) {
        this.data = data;
    }

    public MyTreeNode() {}
    public void addChild(MyTreeNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public MyTreeNode addChild(T data) {
        MyTreeNode<T> newChild = new MyTreeNode<>(data);
        this.addChild(newChild);
        return this;
    }
    public MyTreeNode<T> getChildAt(int i){
        return children.get(i);
    }
    public MyTreeNode addChildren(List<MyTreeNode<T>> children) {
        for(MyTreeNode<T> t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
        return this;
    }
    public MyTreeNode<T> ChildAt(int pos){
        return children.get(pos);
    }
    public List<MyTreeNode<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(MyTreeNode<T> parent) {
        this.parent = parent;
    }

    public MyTreeNode<T> getParent() {
        return parent;
    }

    public void setChildren(List<MyTreeNode<T>> children) {
        this.children = children;
    }

    public boolean isRoot(){
        return (this.parent==null);
    }
    public boolean isLeaf(){
        return this.children.size() ==0;
    }
    public void removeParent(){
        this.parent = null;
    }
}