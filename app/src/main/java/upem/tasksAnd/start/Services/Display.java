package upem.tasksAnd.start.Services;

import android.content.Context;
import android.widget.Toast;

public class Display {
    public static void Toast(Context ctx,String message, int duration) {
        int i=0;
        while(i<duration){
            Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
            i++;
        }
    }
}
