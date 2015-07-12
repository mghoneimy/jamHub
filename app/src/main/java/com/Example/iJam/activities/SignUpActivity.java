package com.Example.iJam.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.Example.iJam.network.HttpImageTask;
import com.Example.iJam.network.InsertUserTask;
import com.Example.iJam.R;
import com.Example.iJam.network.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_username, et_pass, et_confirm_pass, et_email, et_fname, et_lname;
    Button btn_signup;
    ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        profileImage =(ImageView)findViewById(R.id.signup_img_userimage);
        et_username=(EditText) findViewById(R.id.signup_et_username);
        et_pass=(EditText) findViewById(R.id.signup_et_pass);
        et_confirm_pass=(EditText) findViewById(R.id.signup_et_conpass);
        et_email=(EditText) findViewById(R.id.signup_et_email);
        et_fname=(EditText) findViewById(R.id.signup_et_fname);
        et_lname=(EditText) findViewById(R.id.signup_et_lname);
        btn_signup =(Button) findViewById(R.id.signup_bt_signup);
        btn_signup.setOnClickListener(this);
        profileImage.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_bt_signup:
                final String password = et_pass.getText().toString().trim();
                final String confirm_pass = et_confirm_pass.getText().toString().trim();
                final String user_name = et_username.getText().toString().trim();
                final String email = et_email.getText().toString().trim();
                final String fname = et_fname.getText().toString().trim();
                final String lname = et_lname.getText().toString().trim();

                if (password.equals("") || confirm_pass.equals("") || user_name.equals("") || email.equals("")
                        || fname.equals("") || lname.equals(""))
                    Toast.makeText(getApplicationContext(), "one or more of the fields is empyt!", Toast.LENGTH_SHORT).show();
                else {
                    if (password.equals(confirm_pass)) {
                        //-------------------------------------------------------------------------------------------------------
                        //UPLOAD USER IMAGE TO THE SERVER
                        Bitmap img_bmp = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                        new HttpImageTask(ServerManager.getServerURL() + "/users/upload_image.php", getApplicationContext()){
                            @Override
                            protected void onPostExecute(String s) {
                                String img_url = null;
                                if(s.equals(""))
                                    return;
                                try {
                                    JSONObject response = new JSONObject(s);
                                    if (response.getString("status").equals("success")) {
                                        img_url = ServerManager.getServerURL() + "/users/" + response.getString("url");
                                        Toast.makeText(ctx, img_url, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ctx, response.getString("error"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //-------------------------------------------------------------------------------------------------------
                                //UPLOAD USER RECORD TO THE DATABASE
                                JSONObject user = new JSONObject();
                                try {
                                    user.put("user_name", user_name);
                                    user.put("password", password);
                                    user.put("first_name", fname);
                                    user.put("last_name", lname);
                                    user.put("email", email);
                                    user.put("img_url", img_url);

                                    new InsertUserTask(getApplicationContext()) {
                                        @Override
                                        protected void onPostExecute(String s) {
                                            try {
                                                JSONObject response = new JSONObject(s);
                                                String status = response.getString("status");
                                                if (status.equals("fail")) {
                                                    Toast.makeText(ctx, "Sign up failed! " + response.getString("error"), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ctx, "Success", Toast.LENGTH_SHORT).show();
                                                    //int uid = response.getInt("user_id");
                                                    Intent inte = new Intent(ctx, MainActivity.class);
                                                    //inte.putExtra("user_id", uid);
                                                    inte.putExtra("user_name", user_name);
                                                    inte.putExtra("password", password);
                                                    inte.putExtra("first_name", fname);
                                                    inte.putExtra("last_name", lname);
                                                    inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    ctx.startActivity(inte);
                                                    finish();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.execute(user);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.execute(img_bmp);
                        //-------------------------------------------------------------------------------------------------------
                    } else
                        Toast.makeText(getApplicationContext(), "passwords mismatch!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.signup_img_userimage:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap img = BitmapFactory.decodeStream(imageStream);
                        profileImage.setImageBitmap(img);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}