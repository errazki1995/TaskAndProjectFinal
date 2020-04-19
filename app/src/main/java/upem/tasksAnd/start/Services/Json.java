package upem.tasksAnd.start.Services;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import upem.tasksAnd.start.models.Task;

public class Json {
    Context context;

    public Json(Context ctx){
        context=ctx;
    }

    public String filename="tasks.json";

    public  String getTheJson(){
        try {
            InputStream stream = context.getAssets().open(filename);
            int size =stream.available();
            byte[] tampon = new byte[size];
            stream.read(tampon);
            stream.close();
            return new String(tampon,"UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static JSONObject toJSONObject(Task t){
        //description , dateStart, dateEnd, priorityLevel,difficulty,status
        JSONObject json = new JSONObject();
        try{
            json.put("taskid",t.getTaskid());
            json.put("name",t.getName());
            json.put("parentid",t.getParentid());
            json.put("description",t.getDescription());
            json.put("datestart",t.getDateStart());
            json.put("dateend",t.getDateEnd());
            json.put("prioritylevel",t.getPriorityLevel());
            json.put("status",t.getStatus());
            return json;
        }catch (Exception e){e.printStackTrace(); return null;}
    }



    public String PutTotheJson(String data){
        File f = context.getFilesDir();
        OutputStreamWriter streamWriter;
        try {
            File file =new File(f.getPath()+"/"+filename);
            streamWriter= new OutputStreamWriter(context.openFileOutput(file.getPath(),Context.MODE_PRIVATE));
            streamWriter.write(data);
            Log.d("JSONPATH",f.getAbsolutePath()+" canonical path:"+f.getCanonicalPath());
            return "Json OK!";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

}
