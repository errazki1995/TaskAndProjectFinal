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

public class TasksAfterCategory extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private CategoryService categoryService;
    private TaskService taskService;
    int category;
    private ListView listviewTasks;
    TextView title;
    TextView notasks;
    Button backhome;

    private List<Task> tasks = new ArrayList<>();
    private upem.tasksAnd.start.Adapter.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_after_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        notasks = (TextView) findViewById(R.id.notaskslistcat);
        backhome = (Button) findViewById(R.id.backhomecategory);
        backhomeListener();


        try {
            categoryService = new CategoryService(this);
            listviewTasks = (ListView) findViewById(R.id.tasklistcat);
            title = (TextView) findViewById(R.id.introcat);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) category = bundle.getInt("catitemslistid");
            Log.d("categoryAfter", "the category id :" + category);
            if (category > 0) {
                Category c = categoryService.getCategoryByid(category);
                title.setText(c.getCategoryName());
                title.setTextSize(30);
                title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tasks = categoryService.getTasksByCategory(category);
            if (tasks.size() == 0) notasks.setVisibility(View.VISIBLE);

            adapter = new Adapter(this, tasks);
            listviewTasks.setAdapter(adapter);

            fab.setVisibility(View.GONE);
            if (tasks.size() == 0) notasks.setVisibility(View.VISIBLE);

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
        List<Category> allcategories = categoryService.getAllCategories();
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