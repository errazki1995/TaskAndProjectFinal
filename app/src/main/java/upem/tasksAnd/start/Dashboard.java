package upem.tasksAnd.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private CardView newMainTask, listTasks, urgentTasks, gantt, categories;
    private ImageView categorieslist, jsonexp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initCards();
    }

    void initCards() {
        newMainTask = (CardView) findViewById(R.id.newtaskCard);
        listTasks = (CardView) findViewById(R.id.listtaskCard);
        urgentTasks = (CardView) findViewById(R.id.urgenttaskCard);
        gantt = (CardView) findViewById(R.id.ganttcard);
        categories = (CardView) findViewById(R.id.categoriesCard);
        jsonexp = (ImageView) findViewById(R.id.jsonexport);
        newMainTask.setOnClickListener(this);
        listTasks.setOnClickListener(this);
        urgentTasks.setOnClickListener(this);
        gantt.setOnClickListener(this);
        categorieslist = (ImageView) findViewById(R.id.categorydashboard);


        categorieslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), CategoriesActivity.class);
                startActivity(i);
            }
        });
        jsonexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), JsonExporter.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {

            case R.id.newtaskCard:
                Log.d("inHere", "New activity");
                i = new Intent(this, NewTask.class);
                break;
            case R.id.ganttcard:
                Log.d("inHere", "New activity");
                i = new Intent(this, Gantt.class);
                break;
            case R.id.listtaskCard:
                Log.d("inHere", "listActivity activity");

                i = new Intent(this, TasksList.class);
                break;
            case R.id.categoriesCard:
                Log.d("inHere", "Cat activity");
                i = new Intent(this, CategoriesActivity.class);
                break;
            case R.id.urgenttaskCard:
                Log.d("inHere", "Urgent activity");
                i = new Intent(this, UrgentTasks.class);
                break;

            default:
                break;
        }
        if (i != null)
            startActivity(i);
    }
}
