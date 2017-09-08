package com.Example.iJam.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.Example.iJam.R;
import com.Example.iJam.models.User;
import com.Example.iJam.network.HttpImageTask;
import com.Example.iJam.network.InsertUserTask;
import com.Example.iJam.network.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_username, et_pass, et_confirm_pass, et_email, et_fname, et_lname;
    Button btn_signup;
    ImageView profileImage;
    String img_url = null;
    String email,password;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_signup);

        profileImage = (ImageView) findViewById(R.id.signup_img_userimage);
        et_username = (EditText) findViewById(R.id.signup_et_username);
        et_pass = (EditText) findViewById(R.id.signup_et_pass);
        et_confirm_pass = (EditText) findViewById(R.id.signup_et_conpass);
        et_email = (EditText) findViewById(R.id.signup_et_email);
        et_fname = (EditText) findViewById(R.id.signup_et_fname);
        et_lname = (EditText) findViewById(R.id.signup_et_lname);
        btn_signup = (Button) findViewById(R.id.signup_bt_signup);
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

    public void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 2);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_bt_signup:
                password = et_pass.getText().toString().trim();
                final String confirm_pass = et_confirm_pass.getText().toString().trim();
                final String user_name = et_username.getText().toString().trim();
                email = et_email.getText().toString().trim();
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
                        new HttpImageTask(ServerManager.getServerURL() + "/users/upload_image.php", getApplicationContext()) {
                            @Override
                            protected void onPostExecute(String s) {

                                if (s.equals(""))
                                    return;
                                try {
                                    JSONObject response = new JSONObject(s);
                                    if (response.getString("status").equals("success")) {
                                        img_url = "/users/" + response.getString("url");
                                        Toast.makeText(ctx, img_url, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ctx, response.getString("error"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //-------------------------------------------------------------------------------------------------------
                                //UPLOAD USER RECORD TO THE DATABASE
                                final User u = new User(user_name, password, fname, lname, img_url, email);

                                try {
                                    JSONObject user = u.toJSONObject();
                                    u.setImgUrl(ServerManager.getServerURL() + img_url);

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
                                                    SharedPreferences settings = getSharedPreferences(SignInActivity.PREFS_NAME, 0);
                                                    SharedPreferences.Editor editor = settings.edit();

                                                    editor.putString("user_name", u.getUser_name());
                                                    editor.putString("password", u.getPassword());
                                                    editor.putBoolean("signIn", false);
                                                    editor.commit();
                                                    Properties props = new Properties();
                                                    props.put("mail.smtp.host", "smtp.gmail.com");
                                                    props.put("mail.smtp.socketFactory.port", "465");
                                                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                                    props.put("mail.smtp.auth", "true");
                                                    props.put("mail.smtp.port", "465");
                                                    session = Session.getDefaultInstance(props, new Authenticator() {
                                                        @Override
                                                        protected PasswordAuthentication getPasswordAuthentication() {
                                                            return new PasswordAuthentication("jamhubapp@gmail.com", "jamhub123");
                                                        }
                                                    });

                                                    RetreiveFeedTask task = new RetreiveFeedTask();
                                                    task.execute();

                                                    Intent i = new Intent(ctx, MainActivity.class);
                                                    //inte.putExtra("user_id", uid);
                                                    i.putExtra("user", u);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    ctx.startActivity(i);
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri imageUri = data.getData();
                    //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    //final Bitmap img = BitmapFactory.decodeStream(imageStream);
                    performCrop(imageUri);
                    //profileImage.setImageBitmap(img);
                    break;
                case 2:
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");
                    profileImage.setImageBitmap(thePic);

                    break;
            }

        }
    }


    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("jamhub"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Welcome to JamHub");
                message.setContent("Welcome to JamHub, hope you enjoy our application", "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }
}