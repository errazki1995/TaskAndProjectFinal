package upem.tasksAnd.start;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import upem.tasksAnd.start.Adapter.Adapter;
import upem.tasksAnd.start.Services.CategoryService;
import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.models.Category;
import upem.tasksAnd.start.models.Task;

public class UrgentTasks extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private CategoryService Catservice;
    private TaskService taskservice;
    String haveAparent = null;  //is this task a parent  ?
    private ListView listviewTasks;
    private TextView txtnotask;
    private upem.tasksAnd.start.Adapter.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            taskservice = new TaskService(this);
            listviewTasks = (ListView) findViewById(R.id.urgentlist);
            List<Task> urgenttasks = taskservice.getUrgentTasks();
            txtnotask= (TextView) findViewById(R.id.notasks);
            adapter = new Adapter(this, urgenttasks);
            if(urgenttasks.size()==0) txtnotask.setVisibility(View.VISIBLE);
            listviewTasks.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }

}
/*
BESIDE THE DESTROYING OF THE INTENT INSIDE THE ONCREATE IT SHOULD BE DESTROYED WHEN THERE IS ANY MIUSE OF
THE ACTIVITY , LIKE PRRESSING HOME OR BUTTON BACK
 */