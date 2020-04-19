package upem.tasksAnd.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import upem.tasksAnd.start.Services.TreeUtils;
import upem.tasksAnd.start.models.MyTreeNode;
import upem.tasksAnd.start.models.Task;

public class JsonExporter extends AppCompatActivity {
    TextView status;
    TextView pathok;
    ImageView checkmark;
    ImageView errormark;
    public String filename = "tasks.json";
    public static final int READ_WRITE_REQUEST = 2020;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    boolean permissionTowrite = false;
    boolean permissionToRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_exporter);
        verifyStoragePermissions(this);
        checkPermissions();
        initcomponents();
        letsTest();
    }

    void initcomponents() {
        checkmark = (ImageView) findViewById(R.id.checkmark);
        errormark = (ImageView) findViewById(R.id.errormark);
        status = (TextView) findViewById(R.id.jsonstatus);
        pathok = (TextView) findViewById(R.id.filepathok);

    }

    //Building the tasks hiearchy and formatting to json
    void letsTest() {
        TreeUtils utils = new TreeUtils(this);
        Log.d("afterinstanciation", "ok");

        Task root = new Task(0, "Root", "Root of the tree");
        MyTreeNode<Task> Root = new MyTreeNode<Task>(root);
        utils.buildTree(Root);
        utils.traverse(Root, "","");

        try {
            String json = utils.get_nodes(Root.getData().getTaskid()).toString(4);
            Log.d("finaljson", json);
            PutTotheJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Storing the json string to the file
    public void PutTotheJson(String data) {
        File f = getExternalFilesDir(null);
        FileOutputStream streamWriter;
        try {
            File file = new File(f.getPath() + "/" + filename);
            // streamWriter = new OutputStreamWriter(openFileOutput(file.getPath(), Context.MODE_PRIVATE));
            //streamWriter = new FileOutputStream(new File(file.getPath()),false);
            streamWriter = new FileOutputStream((new File(getExternalFilesDir(null), filename)));
            streamWriter.write(data.getBytes());
            errormark.setVisibility(View.GONE);
            checkmark.setVisibility(View.VISIBLE);
            String exported = "/storage/self/primary/Android/data/" + getApplicationContext().getPackageName() + "/files/" + filename;
            pathok.setText(exported);
            pathok.setTextColor(Color.BLUE);
        } catch (Exception e) {
            errormark.setVisibility(View.VISIBLE);
            checkmark.setVisibility(View.GONE);
            status.setText("Error Exporting json");
            e.printStackTrace();
        }
    }

    public boolean isExternableStoreWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_WRITE_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("readPermissionResult", "Permissions Read are granted");
                    permissionToRead = true;
                } else if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionTowrite = true;
                    Log.d("writePermissionResult", "Permissions Write are granted");
                } else {
                    Log.d("elsepermission", "Permissions Write are  NOT granted");
                }
                break;
            }
        }
    }

    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternableStoreWritable()) {
            Log.d("checkPermissions", "you should allow the rights in order to read and write on the device");
            Toast.makeText(this, "you should allow the rights in order to read and write on the device",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_WRITE_REQUEST);
            return false;
        } else {
            return true;
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionread = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            Log.d("writePermission", "no");
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            Log.d("writePermission", "yes");

        }
        if (permissionread != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            Log.d("readPermission", "no");
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            Log.d("readPermission", "yes");

        }
    }
}

