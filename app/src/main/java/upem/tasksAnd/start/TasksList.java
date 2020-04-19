package upem.tasksAnd.start;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import upem.tasksAnd.start.Adapter.Adapter;
import upem.tasksAnd.start.Services.CategoryService;
import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.models.Category;
import upem.tasksAnd.start.models.Task;

public class TasksList extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private CategoryService Catservice;
    private TaskService taskservice;
    String haveAparent = null;  //is this task a parent  ?
    private ListView listviewTasks;
    TextView title;
    TextView notasks;
    Button backhome;

    private List<Task> tasks = new ArrayList<>();
    private upem.tasksAnd.start.Adapter.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    String task = bundle.getString("parentidL");
                }
                Intent i = new Intent(getApplicationContext(), NewTask.class);
                startActivity(i);
            }
        });
        notasks = (TextView) findViewById(R.id.notaskslist);
        backhome = (Button) findViewById(R.id.backhomecategory);
        backhomeListener();


        try {
            taskservice = new TaskService(this);
            listviewTasks = (ListView) findViewById(R.id.tasklist);
            title = (TextView) findViewById(R.id.intro);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) haveAparent = bundle.getString("parentidL");
            if (haveAparent != null) {
                tasks = taskservice.listAllTasksByParent(Integer.parseInt(haveAparent));
                fab.setEnabled(false);
                fab.setVisibility(View.GONE);
                Log.d("valuetask", "the value " + haveAparent);
                int theparent = Integer.parseInt(haveAparent);
                if (theparent != 0) {
                    title.setText(taskservice.getTaskByid(theparent).getName());
                    title.setTextColor(Color.parseColor("#000000"));
                    title.setTextSize(22);
                } else {
                    fab.setEnabled(true);
                    fab.setVisibility(View.VISIBLE);
                }
            } else tasks = taskservice.listAllTasksByParent(0);
            if (tasks.size() == 0) notasks.setVisibility(View.VISIBLE);

            adapter = new Adapter(this, tasks);
            listviewTasks.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }

    void backhomeListener() {
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), Dashboard.class);
                startActivity(i);

            }
        });
    }

    void loadDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        DrawerLayout drawer = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //  NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // Catservice = new CategoryService(getApplicationContext());
        //   Catservice.newCategory();
        addTodoListInNavView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("infab", "you are in fab button");
            }
        });
        addTodoListInNavView();
    }

    //Drawer filler
    public void addTodoListInNavView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.addSubMenu("Your lists");
        //Menu submenu= menu.addSubMenu("Your Lists");
        List<Category> allcategories = Catservice.getAllCategories();
        if (allcategories.size() > 0) Log.i("sizeok", "the category list is full");
        else Log.i("sizeProblem", "the category list is Empty");
        int i = 0;
        for (Category c : allcategories) {
            Log.i("tag", "the category is +" + c.getCategoryName());
            menu.getItem(i).setIcon(ContextCompat.getDrawable(this, R.drawable.fullstar));
            menu.add(c.getCategoryName());
            i++;
        }
    }
}
/*
BESIDE THE DESTROYING OF THE INTENT INSIDE THE ONCREATE IT SHOULD BE DESTROYED WHEN THERE IS ANY MIUSE OF
THE ACTIVITY , LIKE PRRESSING HOME OR BUTTON BACK
 */