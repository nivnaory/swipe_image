package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;


public class MainActivity extends Activity {
    private SwipeDeck cardStack;
    private  ImageView imageView;
    private Stack<Uri> uriStack=new Stack<Uri>();
    float angle=0;
    SwipAdapter adapter;
    final ArrayList<Integer> cards = new ArrayList<>();
    Button addButton;
    Button rotateButton;

    // int orientation = getResources().getConfiguration().orientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cards.add(0);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        adapter = new SwipAdapter(cards, this);
        cardStack.setAdapter(adapter);
        addButton = (Button) findViewById(R.id.button3);
        rotateButton = (Button) findViewById(R.id.rotate);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, 1);

            }

        });


        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              adapter.rotate();
            }
        });


        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);

            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
            }
            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i("MainActivity", "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i("MainActivity", "cardActionUp");
            }
        });
        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       rotateButton.setEnabled(true);
        imageView = (ImageView)findViewById(R.id.offer_image);
        if (resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                try {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                for (int i = 0; i < count; i++) {
                    cards.add(i);
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    if (i==0) {
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                        adapter.setUriImage(imageUri);
                    }else{
                        adapter.getStk().push(imageUri);
                    }
                }
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Images loaded successfully!", Toast.LENGTH_LONG).show();
            }
        } else if (data.getData() != null) {
            String imagePath = data.getData().getPath();
        }
    }

}




