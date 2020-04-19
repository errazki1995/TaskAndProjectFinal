package upem.tasksAnd.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Timerconfigure extends AppCompatActivity {
    NumberPicker nphours;
    NumberPicker minutes;
    NumberPicker seconds;
    String txthours="00:";
    String txtminutes="00:";
    String txtseconds="00";
    String time = "";

    TextView showhours;
    TextView showminutes;
    TextView showseconds;

    TextView tasknametimer;
    Button timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timerconfigure);
        //Get the widgets reference from XML layout
        // final TextView tv = (TextView) findViewById(R.id.tv);
        nphours = (NumberPicker) findViewById(R.id.nphours);
        minutes = (NumberPicker) findViewById(R.id.npminutes);
        seconds = (NumberPicker) findViewById(R.id.npseconds);
        showhours = (TextView) findViewById(R.id.showhours);
        showminutes = (TextView) findViewById(R.id.showminutes);
        showseconds = (TextView) findViewById(R.id.showseconds);
        tasknametimer=(TextView) findViewById(R.id.tasknametimer);
        //tasktimer

        timer=(Button) findViewById(R.id.starttimer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
          String  task= bundle.getString("tasktimer");
          tasknametimer.setText(task);
        }

        initnumPickers();
        pickersListeners();
        setTimer();
    }

    void initnumPickers() {
        NumberPicker nphours = (NumberPicker) findViewById(R.id.nphours);
        nphours.setMinValue(0);
        nphours.setMaxValue(12);
        nphours.setWrapSelectorWheel(true);
        NumberPicker minutes = (NumberPicker) findViewById(R.id.npminutes);
        minutes.setMinValue(0);
        minutes.setMaxValue(60);
        minutes.setWrapSelectorWheel(true);
        NumberPicker seconds = (NumberPicker) findViewById(R.id.npseconds);
        seconds.setMinValue(0);
        seconds.setMaxValue(60);
        seconds.setWrapSelectorWheel(true);

    }

    public String getTime(String hourschanged, String minuteschanged, String secondschanged) {
        if (hourschanged == null && minuteschanged == null) {
            time.split(":")[2] = secondschanged;
        } else if (minuteschanged == null && secondschanged == null) {
            time.split(":")[0] = hourschanged;
        } else if (hourschanged == null && secondschanged == null) {
            time.split(":")[1] = minuteschanged;
        }

        return time.split(":")[0] + ":" + time.split(":")[1] + ":" + time.split(":")[2];

    }

    void pickersListeners() {

        nphours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txthours=(newVal<10?"0"+newVal:newVal)+":";
                showhours.setText(txthours);
            }
        });
        minutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtminutes=(newVal<10?"0"+newVal:newVal)+":";
                showminutes.setText(txtminutes);
            }
        });
        seconds.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtseconds=newVal<10?"0"+newVal:newVal+"";
                showseconds.setText(txtseconds);

            }
        });
    }

    void setTimer(){
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=txthours+txtminutes+txtseconds;
                Toast.makeText(getApplicationContext(),"the final time is"+ time+"",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),TimerV.class);
                //load the timer needed
                i.putExtra("specifiedTime",time);
                startActivity(i);

            }
        });

    }
}