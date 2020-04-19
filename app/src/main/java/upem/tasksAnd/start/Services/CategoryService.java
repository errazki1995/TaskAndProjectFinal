package upem.tasksAnd.start.Services;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import upem.tasksAnd.start.models.Category;
import upem.tasksAnd.start.models.Task;

public class CategoryService {
    public DatabaseHelper db;
    TaskService taskService;
    Context context;

    public CategoryService(Context context) {
        this.context = context;
        this.db = new DatabaseHelper(context);
    }

    public boolean newCategory(Category c) {
        return db.insertCategory(c);
    }

    public List<Category> getAllCategories() {
        List<Category> cats = new ArrayList<>();
        return db.getAllCategories();
    }

    public String categoryStatus(int categoryid) {
        return db.getCategoriesDoneStatus(categoryid);
    }

    public boolean deleteCategory(int id) {
        return db.deleteCategory(id);
    }

    public boolean editCategory(int id, Category c) {
        return db.updateCategory(id, c);
    }

    public boolean moveTasktoList(int categoryid, Task t) {
        return db.moveTaskTocategory(categoryid, t);
    }

    public Category getCategoryByid(int catid) {
        return db.getCategoryById(catid);
    }

    public List<Task> getTasksByCategory(int cat){
        return db.getTasksByCategory(cat);
    }
    public boolean updateCategory(Category c){
        return db.updateCategory(c.getCategoryid(),c);
    }

}