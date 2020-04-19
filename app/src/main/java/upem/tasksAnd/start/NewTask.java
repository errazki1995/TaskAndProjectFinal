package upem.tasksAnd.start;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import upem.tasksAnd.start.Services.TaskService;
import upem.tasksAnd.start.models.Attachement;
import upem.tasksAnd.start.models.Task;

public class NewTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    int startOrEndDatePicker = 0; //Tricky method since i can't put 2 onDateSets listener (have 2 Datepicker in the same activity) 1 for startDate and 2 for end date
    int ThenewTaskid = -1;
    Typeface MMedium;
    public static final int READ_REQUEST = 1010;
    public static final int READ_WRITE_REQUEST = 2020;
    boolean permissionTowrite = false;
    boolean permissionToRead = false;
    public static String pathTocontent = "AppStorage";
    TaskService taskService;
    Intent fileintent;
    String haveAparent = null;  //Does this task have a parent  ?
    Task toUpdateTask;
    Spinner priorlist;
    Spinner difflist;
    ImageView btnattach;
    ImageView imgcontainer1;
    ImageView imgcontainer2;
    ImageView imgcontainer3;
    Button backhome;

    //Image Cancel buttons
    ImageView imgcancel1;
    ImageView imgcancel2;
    ImageView imgcancel3;
    ImageView imageviewarr[] = {imgcontainer1, imgcontainer2, imgcontainer3};
    ImageView imagecancelarr[] = {imgcancel1, imgcancel2, imgcancel3};


    VideoView videocontainer;
    ArrayAdapter<String> priorAdapter;
    ArrayAdapter<String> difficultyAdapter;
    List<ImageView> imagesviews;
    Map<String, Integer> toGetPriorityPosition = new HashMap<String, Integer>();
    Map<String, Integer> toGetDifficultyPosition = new HashMap<String, Integer>();
    Map<Integer, String> attachementImgViewLinker = new HashMap<>();
    List<Attachement> attachements = new ArrayList<>();
    Button addBtn;
    Button btnCancel;

    Button btndatestart;
    Button btndateend;

    EditText txtTaskname;
    EditText txtDescription;
    EditText txtStartDate;
    EditText txtEndDate;
    List<String> spinnerPriority = new ArrayList<String>();
    List<String> spinnerDifficulty = new ArrayList<String>();
    DatePickerDialog.OnDateSetListener startDateListener, endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task1);


        checkPermissions();
        initTaskService();
        populateHashMap();
        initComponents();
        initSpinners();
        attachListener();
        imageCancelListener();
        if (isExternableStoreWritable()) Log.d("WriteRight", "It is writable");
        else Log.d("WriteRight", "It is not writable");
        //Toast("The Next ID IS: "+taskService.taskIdGiver(),taskService.duration);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            haveAparent = bundle.getString("parentidN");
            //Toast("The redirected parent is :"+haveAparent+" ",taskService.duration);
            toUpdateTask = (Task) getIntent().getExtras().get("tasktoEdit");
            if (toUpdateTask != null) {
                addBtn.setText("Edit");
                populateTheForm();
                displayImagesInCaseOfEdit();
            }
        }


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (validateDates()) {
                        if (toUpdateTask == null && formValidator()) {
                            newTask();
                        } else if (toUpdateTask != null && formValidator()) {
                            changeEditedData();
                            updateTheTask();
                        } else {/*SHOW ERROR DETAILS HERE */ }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please verify your dates", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });
        backHome();//back listener
        showDateSTARTpickerStartListener();//date pickers listener

    }

    boolean validateDates() {

        if (DatesUtilByme.convertToDate(txtEndDate.getText().toString()).before(DatesUtilByme.convertToDate(txtStartDate.getText().toString()))) {
            dateErrorUi();
            Log.d("checkone","the date end before start");

            return false;
        } else if (DatesUtilByme.convertToDate(txtStartDate.getText().toString()).before(DatesUtilByme.convertToDate(DatesUtilByme.convertToString(DatesUtilByme.getActualDate()))) ||
                DatesUtilByme.convertToDate(txtEndDate.getText().toString()).before(DatesUtilByme.convertToDate(DatesUtilByme.convertToString(DatesUtilByme.getActualDate())))) {
            Log.d("checkone2","the date  before date"+" "+DatesUtilByme.convertToString(DatesUtilByme.getActualDate()));
            //&&( !DatesUtilByme.convertToDate(txtEndDate.getText().toString()).equals(DatesUtilByme.getActualDate())  ||  !DatesUtilByme.convertToDate(txtEndDate.getText().toString()).equals(DatesUtilByme.getActualDate())

            dateErrorUi();
            return false;
        } else return true;
    }

    void dateErrorUi() {
        txtStartDate.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        //txtStartDate.setTextColor(getResources().getColor(R.color.red));
        //txtEndDate.setTextColor(getResources().getColor(R.color.red));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.clear();
        }
    }


    void initComponents() {
        MMedium = Typeface.createFromAsset(getAssets(), "fonts/MMedium.ttf");
        addBtn = (Button) findViewById(R.id.addBtn);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtTaskname = (EditText) findViewById(R.id.txtTaskName);
        txtDescription = (EditText) findViewById(R.id.txtTaskDescription);
        txtStartDate = (EditText) findViewById(R.id.txtstartDate);
        txtEndDate = (EditText) findViewById(R.id.txtEndDate);
        btnattach = (ImageView) findViewById(R.id.btnattach);
        //SETTING UP IMAGE CONTAINERS
        imgcontainer1 = (ImageView) findViewById(R.id.img1);
        imgcontainer2 = (ImageView) findViewById(R.id.img2);
        imgcontainer3 = (ImageView) findViewById(R.id.img3);
        //SETTING UP VIDEO CONTAINER
        videocontainer = (VideoView) findViewById(R.id.vid1);
        //SETTING UP CANCEL ICONS ON TOP OF LOADED IMAGES
        imgcancel1 = (ImageView) findViewById(R.id.imgcancel1);
        imgcancel2 = (ImageView) findViewById(R.id.imgcancel2);
        imgcancel3 = (ImageView) findViewById(R.id.imgcancel3);
        //SETTING UP BUTTONS
        btndatestart = (Button) findViewById(R.id.showstartPicker);
        btndateend = (Button) findViewById(R.id.showEndPicker);

        //font
        txtTaskname.setTypeface(MMedium);
        txtDescription.setTypeface(MMedium);
        txtStartDate.setTypeface(MMedium);
        txtEndDate.setTypeface(MMedium);


    }

    void initTaskService() {
        try {
            taskService = new TaskService(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initSpinners() {
        spinnerPriority.add("Not urgent");
        spinnerPriority.add("Urgent");
        spinnerDifficulty.add("Simple");
        spinnerDifficulty.add("Medium");
        spinnerDifficulty.add("Hard");
        priorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerPriority);
        difficultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerDifficulty);
        priorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorlist = (Spinner) findViewById(R.id.priorityList);
        difflist = (Spinner) findViewById(R.id.difficultyList);
        priorlist.setAdapter(priorAdapter);
        difflist.setAdapter(difficultyAdapter);
    }

    void populateTheForm() {
        Task st = toUpdateTask;
        txtTaskname.setText(toUpdateTask.getName());
        txtDescription.setText(toUpdateTask.getDescription());
        txtStartDate.setText(toUpdateTask.getDateStart());
        txtEndDate.setText(toUpdateTask.getDateEnd());
        if (!toUpdateTask.getDifficulty().isEmpty() || !toUpdateTask.getPriorityLevel().isEmpty()) {
            priorlist.setSelection(toGetPriorityPosition.get(toUpdateTask.getPriorityLevel()));
            difflist.setSelection(toGetDifficultyPosition.get(toUpdateTask.getDifficulty()));
        }
        //don't forget to add the other elements [Status..]
    }

    void populateHashMap() {
        //HASH MAP IS TO LINK THE DATA WITH THE ITEM POSITION INSIDE THE SPINNER (IE: WHEN DATA IS URGENT GET 1 TO DISPLAY ON SPINNER)
        toGetPriorityPosition.put("Not urgent", 0);
        toGetPriorityPosition.put("Urgent", 1);

        toGetDifficultyPosition.put("Simple", 0);
        toGetDifficultyPosition.put("Medium", 1);
        toGetDifficultyPosition.put("Hard", 2);
    }


    void newTask() throws Exception {
        String priorSelect = priorlist.getSelectedItem().toString();
        String diffSelect = difflist.getSelectedItem().toString();
        /*
        GIVES A SAFE ID TO THE NEW TASK, WE NEED AN ID SINCE THE ATTACHEMENTS NEEDS TO BE LINKED WITH THE TASK WHEN ADDING
        AT THE SAME
         */
        ThenewTaskid = taskService.taskIdGiver();
        //FEEDING THE OBJECT
        Task t = new Task(ThenewTaskid, txtTaskname.getText().toString(),
                txtDescription.getText().toString()
                , txtStartDate.getText().toString(),
                txtEndDate.getText().toString(),
                priorSelect, diffSelect, 0, null, 0, null);



        /*
        linking the id of the task with the file.
         */
        if (attachements.size() > 0) {
            int index = 0;
            for (Attachement a : attachements) {
                a.setTaskid(ThenewTaskid + "");
                String filename = "img_" + index + "_" + a.getTaskid() + "." + a.getAttachmentType();
                a.setAttachmentname(filename);
                storetheImage(filename, a.getContent());
                taskService.setupAttachemennt(a);
                index++;
            }
        }
        //Notice the parameter parentid as 0 to show this is a root task
        if (haveAparent == null) taskService.addtask(0, t);
            //On the other hand we have the value haveAparent which points on the parent task
        else taskService.addtask(Integer.parseInt(haveAparent), t);
        // getIntent().removeExtra("parentidN"); IMPORTANT
        Intent i = new Intent(getApplicationContext(), TasksList.class);
        startActivity(i);
    }


    //In case of edit on the form we need  to get the new values then store in database
    void changeEditedData() {
        toUpdateTask.setName(txtTaskname.getText().toString());
        toUpdateTask.setDescription(txtDescription.getText().toString());
        toUpdateTask.setDateStart(txtStartDate.getText().toString());
        toUpdateTask.setDateEnd(txtEndDate.getText().toString());
        String priority = priorlist.getSelectedItem().toString();
        String difficulty = difflist.getSelectedItem().toString();
        toUpdateTask.setPriorityLevel(priority);
        toUpdateTask.setDifficulty(difficulty);

    }

    //function update
    void updateTheTask() {
        if (taskService.updateTask(toUpdateTask.getTaskid(), toUpdateTask)) {
            //remove intent of the object
            //getIntent().removeExtra("tasktoEdit");
            Intent i = new Intent(getApplicationContext(), TasksList.class);
            startActivity(i);
        } else {
            //SHOW ERROR MESSAGE HERE
        }

    }


    void backHome() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bundle bundle = getIntent().getExtras();
                //if(bundle!=null) bundle.clear();
            }
        });
    }

    void attachListener() {
        btnattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("*/*");
                startActivityForResult(fileintent, READ_REQUEST);
            }
        });
    }


    void GetTheAttachement(Uri uri) throws Exception {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(contentResolver.getType(uri));

        if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("jpg")) {
            if (!HasImage(imgcontainer1))
                linkTheImage(imgcontainer1, imgcancel1, uri, 1);
            else if (!HasImage(imgcontainer2))
                linkTheImage(imgcontainer2, imgcancel2, uri, 2);
            else if (!HasImage(imgcontainer3))
                linkTheImage(imgcontainer3, imgcancel3, uri, 3);
        }
    }

    private boolean HasImage(ImageView imageVieww) {
        Drawable drawable = imageVieww.getDrawable();
        boolean image_is_there = drawable != null;
        if (image_is_there && (drawable instanceof BitmapDrawable)) {
            image_is_there = ((BitmapDrawable) drawable).getBitmap() != null;

        }
        return image_is_there;
    }


    private void linkTheImage(ImageView imgview, ImageView imgcancel, Uri uri, int whichimageview) throws Exception {
        InputStream is = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();
        imgview.setImageBitmap(bitmap);
        imgview.setVisibility(View.VISIBLE);
        imgcancel.setVisibility(View.VISIBLE);
        Attachement a = new Attachement(pathTocontent, "jpeg");
        a.setContent(bitmap);
        attachementImgViewLinker.put(whichimageview, a.getGetAttachmentType());
        attachements.add(a);

    }

    //Overrided function to get the path from a hardcoded path and not Uri
    private void displayImageFromPath(ImageView imgview, ImageView imgcancel, String pathtofile) throws Exception {
        Bitmap bitmap = BitmapFactory.decodeFile(pathtofile);

        imgview.setImageBitmap(bitmap);
        imgview.setVisibility(View.VISIBLE);
        imgcancel.setVisibility(View.VISIBLE);
        //Attachement a= new Attachement(pathTocontent,"jpeg");
        //a.setContent(bitmap);
        //attachementImgViewLinker.put(whichimageview,a.getGetAttachmentType());
        //attachements.add(a);

    }

    //test to validate the form
    boolean formValidator() {
        if (txtTaskname.getText().toString().isEmpty() || txtDescription.getText().toString().isEmpty() || txtStartDate.getText().toString().isEmpty() || txtEndDate.getText().toString().isEmpty())
            return false;
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    GetTheAttachement(data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    void imageCancelListener() {
        imgcancel1.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                if (attachementImgViewLinker.get(1) != null) {
                    String uri = attachementImgViewLinker.get(1);
                    removeAttachement(uri, imgcontainer1, imgcancel1);

                }
            }
        });

        imgcancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachementImgViewLinker.get(2) != null) {
                    String uri = attachementImgViewLinker.get(2);
                    removeAttachement(uri, imgcontainer2, imgcancel2);
                }
            }
        });

        imgcancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachementImgViewLinker.get(3) != null) {
                    String uri = attachementImgViewLinker.get(3);
                    removeAttachement(uri, imgcontainer3, imgcancel3);

                }
            }
        });
    }

    void removeAttachement(String whichone, ImageView imageView, ImageView cancelImage) {
        for (Attachement a : attachements) {
            if (whichone.equalsIgnoreCase(a.getAttachmentPath())) {
                attachements.remove(a);
            }
            imageView.setVisibility(View.GONE);
            cancelImage.setVisibility(View.GONE);
            imageView.setImageDrawable(null);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            /* IN CASE OF NO BUG PLEASE DELETE THE CODE
            case READ_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("insidereadrequest","Permissions Read are granted");

                }
                //break;
            }
            */

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

    void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void showDateSTARTpickerStartListener() {

        btndatestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startOrEndDatePicker = 1;
                showDatePickerDialog();

            }
        });
        btndateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrEndDatePicker = 2;
                showDatePickerDialog();

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        if (startOrEndDatePicker == 1) {
            String date = formatDateOnPicker(dayOfMonth, month, year);
            txtStartDate.setText(date);
        } else if (startOrEndDatePicker == 2) {
            String date = formatDateOnPicker(dayOfMonth, month, year);
            txtEndDate.setText(date);
        }
    }


    public void storetheImage(String thefile, Bitmap image) {
        try {
            File file = null;
            if (storagefolderExists()) {
                file = new File(getExternalFilesDir(null) + "/" + pathTocontent, thefile);
                FileOutputStream out = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                Log.d("InsideExistsFolder", "You are inside please verify the  path");
            } else {
                createFolder(pathTocontent);
                Log.d("CreatingStorage", "No private storage  , setting up one for you");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void displayImagesInCaseOfEdit() {
        String imagespath = getExternalFilesDir(null).toString() + "/" + pathTocontent;
        File directory = new File(imagespath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                Log.w("FILESHIT", "the file name is:" + files[i].getName() + " the file split:" + files[i].getName().split("_")[2] + " and path is " + files[i].getAbsolutePath() + " you are inside");
                String finalfilename = files[i].getName().split("_")[2];
                if (finalfilename.split("\\.")[0] == toUpdateTask.getTaskid() + "") {
                    for (int j = 0; j < imageviewarr.length; j++) {
                        Log.w("inside dispimageout", "the file path is " + files[i].getAbsolutePath() + " you are inside");

                        if (!HasImage(imageviewarr[j])) {
                            try {
                                displayImageFromPath(imageviewarr[j], imagecancelarr[j], files[i].getAbsolutePath());
                                Log.w("inside dispimage", "the file path is " + files[i].getAbsolutePath() + " you are inside");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean createFolder(String folder) {
        boolean confirmed = true;
        File file = new File(getExternalFilesDir(null), folder);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.w("FolderNotCreated", "Create folder problem creating the folder");
                confirmed = false;
            }
        }
        return true;
    }

    public boolean storagefolderExists() {
        boolean exists = true;
        File file = new File(getExternalFilesDir(null), pathTocontent);
        if (!file.exists()) {
            exists = false;
        }
        return exists;
    }

    String formatDateOnPicker(int dayofmonth, int month, int year) {
        String day = (dayofmonth < 10) ? "0" + dayofmonth : dayofmonth + "";
        String monthok = (month < 10) ? "0" + month : month + "";
        return day + "/" + monthok + "/" + year;
    }


    public boolean isExternableStoreWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
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
}

/*
BESIDE THE DESTROYING OF THE INTENT INSIDE THE ONCREATE IT SHOULD BE DESTROYED WHEN THERE IS ANY MIUSE OF
THE ACTIVITY , LIKE PRRESSING HOME OR BUTTON BACK
 */