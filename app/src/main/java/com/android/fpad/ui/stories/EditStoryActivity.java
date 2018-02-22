package com.android.fpad.ui.stories;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.fpad.R;
import com.android.fpad.adapter.KategoriAdapter;
import com.android.fpad.adapter.ViewPagerAdapter;
import com.android.fpad.adapter.stories.StoryPagerAdapter;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.KategoriList;
import com.android.fpad.retrofit.StoryRespond;
import com.android.fpad.ui.LoginActivity;
import com.bumptech.glide.Glide;
import com.scrat.app.richtext.RichEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.input_title)
    EditText title;
    @BindView(R.id.input_description)
    EditText description;
    @BindView(R.id.var_kategori)
    EditText var_kategori;
    @BindView(R.id.input_kategori)
    EditText inputkategori;
    @BindView(R.id.editor)
    RichEditText editor;

    APIInterface apiInterface;
    Activity context = this;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    String usrmail;
    String story_id,story_title,story_description,story_kategori,story_content,story_status;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editstory);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        setTitle(getResources().getString(R.string.create_title_activity));



        verifyStoragePermissions(EditStoryActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        usrmail = sharedpreferences.getString("email", null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        Intent intent = getIntent();
        story_id = intent.getExtras().getString("story_id");
        story_title = intent.getExtras().getString("story_title");
        story_description = intent.getExtras().getString("story_description");
        story_kategori = intent.getExtras().getString("story_kategori");
        story_content = intent.getExtras().getString("story_content");
        story_status = intent.getExtras().getString("story_status");



        title.setText(story_title);
        description.setText(story_description);
        inputkategori.setText(story_kategori);
        editor.fromHtml(story_content);


        var_kategori.setInputType(InputType.TYPE_NULL);
        kategori(0);
        var_kategori.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    var_kategori.setInputType(0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(var_kategori.getWindowToken(), 0);
                    kategori(1);
                }
            }
        });
        var_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var_kategori.setInputType(0);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(var_kategori.getWindowToken(), 0);
                kategori(1);
            }
        });






        editor.setTextColor(Color.BLACK);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem publish = menu.findItem(R.id.publish);
        if(story_status.equals("Drafts"))
        {
            publish.setVisible(true);
        }
        else
        {
            publish.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_story, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.simpan) {
            simpan(story_status);
            Intent i = new Intent(EditStoryActivity.this, StoryActivity.class);
            startActivityForResult(i,1);
            return true;
        }
        else if (item.getItemId() == R.id.publish) {
            simpan("waitingapproval");
            Intent i = new Intent(EditStoryActivity.this, StoryActivity.class);
            startActivityForResult(i,1);
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }





        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }



    private void simpan(String status) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(EditStoryActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loadingprogress));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestBody val_id = RequestBody.create(MediaType.parse("multipart/form-data"), story_id);
        RequestBody val_title = RequestBody.create(MediaType.parse("multipart/form-data"), title.getText().toString());
        RequestBody val_description = RequestBody.create(MediaType.parse("multipart/form-data"), description.getText().toString());
        RequestBody val_content = RequestBody.create(MediaType.parse("multipart/form-data"), editor.toHtml());
        RequestBody val_email = RequestBody.create(MediaType.parse("multipart/form-data"), usrmail);
        RequestBody val_kategori =RequestBody.create(MediaType.parse("multipart/form-data"), inputkategori.getText().toString());
        RequestBody val_status =RequestBody.create(MediaType.parse("multipart/form-data"), status);


        Call<StoryRespond> resultCall = apiInterface.simpan(val_title, val_description, val_content, val_email, val_kategori, val_id, val_status);


        resultCall.enqueue(new Callback<StoryRespond>() {
            @Override
            public void onResponse(Call<StoryRespond> call, Response<StoryRespond> response) {

                progressDialog.dismiss();

                // Response Success or Fail

                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();}


            }

            @Override
            public void onFailure(Call<StoryRespond> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();

        if(!title.getText().toString().equals(story_title) || !description.getText().toString().equals(story_description) || !editor.toHtml().equals(story_content) || !inputkategori.getText().toString().equals(story_kategori)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(EditStoryActivity.this);
            builder.setTitle(getResources().getString(R.string.edit_header_builder));
            builder.setMessage(getResources().getString(R.string.edit_body_builder));

            //Yes Button
            builder.setPositiveButton(getResources().getString(R.string.yes_builder), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   startActivity(new Intent(EditStoryActivity.this, StoryActivity.class));
                   finish();
                    //EditStoryActivity.this.onSuperBackPressed();
                }
            });

            //No Button
            builder.setNegativeButton(getResources().getString(R.string.no_builder), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();



        }else{
            EditStoryActivity.this.onSuperBackPressed();
        }

    }
    public void onSuperBackPressed(){
        super.onBackPressed();
    }

    public void kategori(final int first){
        final List<String> kategori = new ArrayList<>();
        final List<String> kategori_id = new ArrayList<>();


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(EditStoryActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();


        Call<List<KategoriList>> call3 = apiInterface.doGetKategoriList();
        call3.enqueue(new Callback<List<KategoriList>>() {
            @Override
            public void onResponse(Call<List<KategoriList>> call, Response<List<KategoriList>> response) {
                int no = -1;
                List<KategoriList> jsonResponse = response.body();
                for (int i = 0; i < jsonResponse.size(); i++) {
                    //dataModels.add(new KategoriList(jsonResponse.get(i).getId(),jsonResponse.get(i).getNama_kategori()));
                    //test[]=1;
                    kategori.add(jsonResponse.get(i).getNama_kategori());
                    kategori_id.add(jsonResponse.get(i).getId());
                    if(inputkategori.getText().toString().equals(jsonResponse.get(i).getId())){
                        no=i;
                        var_kategori.setText(kategori.get(i));
                        inputkategori.setText(kategori_id.get(i));
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.header_kategori_builder));

                int checkedItem = no;
                // cow
                builder.setSingleChoiceItems(kategori.toArray(new String[kategori.size()]), checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                        inputkategori.setText(kategori_id.get(which));
                        var_kategori.setText(kategori.get(which));
                    }
                });

// add OK and Cancel buttons
                builder.setPositiveButton(getResources().getString(R.string.ok_builder), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK


                    }
                });

                AlertDialog dialog = builder.create();
                if(first==1) {
                    dialog.show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<KategoriList>> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
            }
        });

    }



    public void setBold(View v) {
        editor.bold(!editor.contains(RichEditText.FORMAT_BOLD));
    }
    public void setItalic(View v) {
        editor.italic(!editor.contains(RichEditText.FORMAT_ITALIC));
    }
    public void setUnderline(View v) {
        editor.underline(!editor.contains(RichEditText.FORMAT_UNDERLINED));
    }
    public void setStrikethrough(View v) {
        editor.strikethrough(!editor.contains(RichEditText.FORMAT_STRIKETHROUGH));
    }
    public void setBullet(View v) {
        editor.bullet(!editor.contains(RichEditText.FORMAT_BULLET));
    }
    public void setQuote(View v) {
        editor.quote(!editor.contains(RichEditText.FORMAT_QUOTE));
    }
    public void undo(View v) {
        editor.undo();
    }
    public void redo(View v) {
        editor.redo();
    }









}
