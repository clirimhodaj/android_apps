package com.clirimhodaj.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PhotoDetailActivity extends BaseActivity {
    private static final String TAG = "PhotoDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null){
            Resources resources = getResources();
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            String text = resources.getString(R.string.photo_title_text, photo.getTitle());
            photoTitle.setText(text);

            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());
            Log.d(TAG, "onCreate: " + photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.get().load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }
    }
}