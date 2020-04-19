package upem.tasksAnd.start.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import upem.tasksAnd.start.CategoriesActivity;
import upem.tasksAnd.start.NewTask;
import upem.tasksAnd.start.R;
import upem.tasksAnd.start.Services.CategoryService;
import upem.tasksAnd.start.Services.Display;
import upem.tasksAnd.start.TasksAfterCategory;
import upem.tasksAnd.start.models.Category;
import upem.tasksAnd.start.models.Task;

public class CatAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    List<Category> categories;
    Context context;
    CategoryService categoryService;
    static LayoutInflater layoutInflater = null;

    public CatAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            categoryService = new CategoryService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = parent.getPositionForView(view);
        Toast.makeText(context.getApplicationContext(), pos + "", Toast.LENGTH_LONG).show();
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.categorymenu, popupMenu.getMenu());
        popupMenu.show();

    }

    public class ViewHolder {
        TextView catName;
        TextView doneTasks;
        Button movebtn;
        ImageView imgmorecat;

    }

    @Override
    public int getCount() {
        return categories.size();

    }

    public Category getItem(int i) {
        return categories.get(i);
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
        ViewHolder viewHolder = new ViewHolder();
        itemview = (itemview == null) ? layoutInflater.inflate(R.layout.category_item, null) : itemview;
        viewHolder.catName = (TextView) itemview.findViewById(R.id.catName);
        viewHolder.imgmorecat = (ImageView) itemview.findViewById(R.id.morecat);
        viewHolder.movebtn = (Button) itemview.findViewById(R.id.movehere);
        viewHolder.doneTasks=(TextView) itemview.findViewById(R.id.catdone);
        Category c = getItem(position);
        String status= categoryService.categoryStatus(c.getCategoryid());
        viewHolder.doneTasks.setText(status);
        viewHolder.catName.setText(c.getCategoryName());
        viewHolder.imgmorecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu((Activity) context, v, position);
            }
        });
        final Task task = getTaskIncaseOfMove();
        if(task!=null){
            viewHolder.movebtn.setVisibility(View.VISIBLE);
            viewHolder.imgmorecat.setVisibility(View.GONE);
            viewHolder.movebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryService service = new CategoryService(context);
                    Log.d("OKTEST","You are inside the onclick:"+getItem(position).getCategoryName());
                    Category category = getItem(position);
                    if(service.moveTasktoList(category.getCategoryid(),task))
                        confirmpanel("Task Moved","Task '"+task.getName()+"' successfully moved to list "+getItem(position).getCategoryName());
                    else confirmpanel("Error moving task","Failure moving your task to list "+category.getCategoryName());

                }
            });
        }
        else viewHolder.movebtn.setVisibility(View.GONE);
        return itemview;
    }


    private void showPopMenu(final Activity activity, View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.categorymenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.catdelete:
                        deleteCategory(activity, position);
                        break;
                    case R.id.catedit:
                        editItemContent(activity,position);
                        break;

                    case R.id.catlist:
                        listTasksbycat(activity,position);
                    default:
                        break;

                }
                return true;
            }
        });
        popupMenu.show();
    }


    private void editItemContent(final Activity activity, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), CategoriesActivity.class);
        Category c = getItem(position);
        intent.putExtra("catedit", getItem(position).getCategoryid());
        context.startActivity(intent);
    }


    private void listTasksbycat(final Activity activity,int position){
        Intent in = new Intent(activity.getApplicationContext(), TasksAfterCategory.class);
        Category c = getItem(position);
        in.putExtra("catitemslistid", getItem(position).getCategoryid());
        context.startActivity(in);
    }



    private void deleteCategory(final Activity activity, int position) {
        Category c = getItem(position);
        String category = c.getCategoryName();
        if(categoryService.deleteCategory(c.getCategoryid())) Toast.makeText(activity.getApplicationContext(), "The category" + category + " has been successfuly deleted !", Toast.LENGTH_LONG).show();
        else Toast.makeText(activity.getApplicationContext(), "Failed to delete the Category", Toast.LENGTH_LONG).show();
        Intent i = new Intent(activity.getApplicationContext(), CategoriesActivity.class);
        activity.startActivity(i);
    }

    Task getTaskIncaseOfMove(){
        Intent intent = ((Activity) context).getIntent();
        Bundle bundle= intent.getExtras();
        Task task=null;
        if(bundle!=null) task = (Task) bundle.get("theTaskCategory");
        return task;
    }
    void confirmpanel(String title,String message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message).setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = ((Activity) context).getIntent();
                intent.removeExtra("theTaskCategory");
                Intent i =new Intent(context.getApplicationContext(),CategoriesActivity.class);
                context.startActivity(i);
            }
        }).show();
    }




}