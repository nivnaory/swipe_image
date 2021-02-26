package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import java.io.InputStream;
import java.util.List;
import java.util.Stack;

public class SwipAdapter  extends BaseAdapter {
    public void setData(List<Integer> data) {
        this.data = data;
    }

    private List<Integer> data;
    private Context context;
    private Stack<Uri> stk;



    Uri uriImage;
    View v;
    float angle;

    public SwipAdapter(List<Integer> data, Context context) {
        this.data = data;
        this.context = context;
        stk = new Stack<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.tets_card2, parent, false);
            ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);
            if (this.stk.empty()) {
                Toast.makeText(context, " please Select images to show", Toast.LENGTH_LONG).show();
            } else {
                try {
                    this.uriImage= stk.pop();
                    final InputStream imageStream = context.getContentResolver().openInputStream(this.uriImage);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
            }

        }
        return v;
    }
    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }

    public Stack<Uri> getStk() {
        return this.stk;
    }

    public void rotate() {
        try {
            angle += 90;
            final ImageView image = (ImageView) v.findViewById(R.id.offer_image);
            System.out.println(this.uriImage);
            final InputStream imageStream = context.getContentResolver().openInputStream(this.uriImage);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Bitmap rotatedImage = rotateImage(selectedImage, angle);
            image.setImageBitmap(rotatedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    public static Bitmap rotateImage(Bitmap sourceImage, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(sourceImage, 0, 0, sourceImage.getWidth(), sourceImage.getHeight(), matrix, true);
    }
}
