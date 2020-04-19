/*
package upem.tasksAnd.start.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.io.InputStream;
import java.util.ArrayList;

import upem.tasksAnd.start.R;

public class ImageAdapter extends PagerAdapter {
    private ArrayList<String> images;
    private LayoutInflater outflat;
    private Context context;

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        int i=0;
        while(i<20){
            Log.w("insideItem","inside  adapter");
            i++;
        }
        View imageLayout = outflat.inflate(R.layout.img_layout, view, false);
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imgok);
        InputStream is;
        try{
            is =context.getContentResolver().openInputStream(Uri.parse(images.get(position).toString()));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
            is.close();
            if(HasImage(imageView))
            Log.d("imgSet","the imgview has an image");
            else{
                Log.d("imgSet","the imgview DOESNT have an image");
            }
        }catch (Exception e){e.printStackTrace();}

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    public ImageAdapter(Context context, ArrayList<String> images) {
        int i=0;
        while(i<20){
            Log.w("insideAdapter","inside  adapter");
            i++;
        }
        this.context = context;
        this.images = images;
        outflat = LayoutInflater.from(context);
    }

    public void destroyItem(ViewGroup container, int position, Object ok) {
        container.removeView((View) ok);
    }

    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
    private boolean HasImage(ImageView imageVieww){
        Drawable drawable= imageVieww.getDrawable();
        boolean imageisthere = drawable !=null;
        if (imageisthere && (drawable instanceof BitmapDrawable)) {
            imageisthere = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return imageisthere;
    }
}
*/



package upem.tasksAnd.start.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import upem.tasksAnd.start.R;
import upem.tasksAnd.start.models.Attachement;

public class ImageAdapter extends PagerAdapter {
    private ArrayList<Attachement> images;
    private LayoutInflater outflat;
    private Context context;

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        Log.d("imageInstantiateItem","YOU ARE INSIDE INSTANTIATE ITEM");

        View imageLayout = outflat.inflate(R.layout.img_layout, view, false);
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imgok);
        try{
            //InputStream is =context.getContentResolver().openInputStream(Uri.parse(images.get(position).toString()));
           // InputStream is =context.getContentResolver().openInputStream(Uri.parse(images.get(position).toString()));
             //
            //is.close();

            //

            File image = new File(context.getExternalFilesDir(null)+"/"+images.get(position).getAttachmentPath(),images.get(position).getAttachmentname());
            if(image.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
            else{
                Log.d("filenotexist","file does not exists");
            }



        }
        catch (Exception e){e.printStackTrace();}


        view.addView(imageLayout, 0);
        return imageLayout;
    }

    public ImageAdapter(Context context, ArrayList<Attachement> images) {
        Log.d("IMAGEADAPTER","YOU ARE INSIDE image adapter");

        this.context = context;
        this.images = images;
        outflat = LayoutInflater.from(context);
    }

    public void destroyItem(ViewGroup container, int position, Object ok) {
        container.removeView((View) ok);
    }

    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }




}


