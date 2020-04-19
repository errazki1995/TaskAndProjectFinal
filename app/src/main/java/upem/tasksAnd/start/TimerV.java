package upem.tasksAnd.start;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TimerV extends AppCompatActivity implements Chronometer.OnChronometerTickListener {
    Button btnstart, btnhold;
    ImageView icanchor;
    Animation roundingalone;
    Chronometer timerHere;
    TextView taskname;
    String specifiedTime = "";
    static long stoppedhere = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_v);

        btnstart = findViewById(R.id.btnstart);
        icanchor = findViewById(R.id.icanchor);
        btnhold = findViewById(R.id.btnstop);
        timerHere = findViewById(R.id.timerHere);
        taskname=findViewById(R.id.tasknametimer2);
        btnhold.setAlpha(0);
        roundingalone = AnimationUtils.loadAnimation(this, R.anim.roundingalone);
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MMedium.ttf");
        btnhold.setTypeface(MMedium);
        triggerChronometer();
        timerHere.setOnChronometerTickListener(this);
        // pauseChronometer();
    }


    void triggerChronometer() {
        icanchor.startAnimation(roundingalone);

        btnhold.animate().alpha(1).translationY(-80).setDuration(300).start();
        btnhold.animate().alpha(1).setDuration(300).start();
        //start time
        timerHere.setBase(SystemClock.elapsedRealtime());
        timerHere.start();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            specifiedTime= bundle.getString("specifiedTime");
            Log.d("spectime","the specified time is "+ specifiedTime);
            String gettaskname= bundle.getString("tasktimer");
            taskname.setText(gettaskname);
            Log.d("tasknameontimerV","the task name is:"+gettaskname);
        }
    }

    void pauseChronometer() {
        timerHere.stop();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        long time = SystemClock.elapsedRealtime()-timerHere.getBase();
        int h   = (int)(time /3600000);
        int m = (int)(time - h*3600000)/60000;
        int s= (int)(time - h*3600000- m*60000)/1000 ;
        String hour="";
        String minutes="";
        String seconds = "";
        hour= h<10?"0"+h:h+"";
        minutes=m<10?"0"+m:m+"";
        seconds=s<10?"0"+s:s+"";
        //Weird behavior as the seconds are skipping 2 or 1 sec late

        String timetick=hour+":"+minutes+":"+seconds;

        if(!specifiedTime.isEmpty()){
            if(specifiedTime.equals(timetick)){
                Intent i=new Intent(getApplicationContext(),Timesup.class);
                    startActivity(i);
                    this.finish();
                }
        }
    }
}