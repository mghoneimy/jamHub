package com.Example.iJam.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Example.iJam.R;
import com.Example.iJam.network.HttpImageTask;
import com.Example.iJam.network.HttpPostTask;
import com.Example.iJam.network.NetworkManager;
import com.Example.iJam.network.ServerManager;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class EdittProfileActivity extends AppCompatActivity implements View.OnClickListener {

    final String lName = MainActivity.user.getLast_name();
    final String fName = MainActivity.user.getFirst_name();
    final String mail = MainActivity.user.getEmail();
    final String pass = MainActivity.user.getPassword();
    String img_url = MainActivity.user.getImgUrl();

    EditText firstname,lastname,email,oldpassword,newpassword,confpassword;
    NetworkImageView image;
    String editfname,editlname,editemail,editoldpass,editnewpass,editconfpass;
    Button submit;
    boolean imgChanged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editt_profile);

        firstname = (EditText)findViewById(R.id.Editprofile_edittxt_fname);
        lastname = (EditText)findViewById(R.id.Editprofile_edittxt_lname);
        email = (EditText)findViewById(R.id.Editprofile_edittxt_email);
        oldpassword = (EditText)findViewById(R.id.Editprofile_edittxt_pass);
        newpassword = (EditText)findViewById(R.id.Editprofile_edittxt_newpass);
        confpassword = (EditText)findViewById(R.id.Editprofile_edittxt_confpass);

        submit = (Button)findViewById(R.id.Editprofile_btn_submit);
        image = (NetworkImageView)findViewById(R.id.editprofile_img_profile);
        submit.setOnClickListener(this);
        image.setOnClickListener(this);

        firstname.setText(fName);
        lastname.setText(lName);
        email.setText(mail);
        image.setImageUrl(img_url, NetworkManager.getInstance(this).getImageLoader());
        img_url="";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri imageUri = data.getData();
                    performCrop(imageUri);
                    break;
                case 2:
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");
                    image.setImageBitmap(thePic);
                    imgChanged = true;
                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.Editprofile_btn_submit):
                editfname = firstname.getText().toString().trim();
                editlname = lastname.getText().toString().trim();
                editemail = email.getText().toString().trim();
                editoldpass = oldpassword.getText().toString().trim();
                editnewpass = newpassword.getText().toString().trim();
                editconfpass = confpassword.getText().toString().trim();

                if(editfname.isEmpty() || editlname.isEmpty() || editemail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please fill all fields with the values you want updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(imgChanged) {
                        Bitmap img_bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
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
                                UpdateUser();
                            }
                        }.execute(img_bmp);
                    }
                    else
                        UpdateUser();
                }

                /*if((!editnewpass.isEmpty())&& (editoldpass.isEmpty())){
                        Toast.makeText(getApplicationContext(), "Please enter old password", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(editoldpass.equals(pass)){
                            if(editnewpass.equals(editconfpass)){
                                //update database with new password
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Confirm password doesn't match new password", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                    }*/

                break;
            case R.id.editprofile_img_profile:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
        }
    }

    private void UpdateUser(){
        try {
            JSONObject updates = new JSONObject();
            updates.put("user_name", MainActivity.user.getUser_name());
            updates.put("first_name", editfname);
            updates.put("last_name", editlname);
            updates.put("email", editemail);
            updates.put("img_url", img_url);

            new HttpPostTask(ServerManager.getServerURL()+"/users/update.php", getApplicationContext()) {
                @Override
                protected void onPostExecute(String s) {
                    try {
                        JSONObject response = new JSONObject(s);
                        String status = response.getString("status");
                        if (status.equals("fail")) {
                            Toast.makeText(ctx, "Update failed! " + response.getString("error"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ctx, "Success", Toast.LENGTH_SHORT).show();
                            //THIS IS TEMPORARY AND NEEDS TO BE CHANGED SO SOON!!!!
                            MainActivity.user.setFirst_name(editfname);
                            MainActivity.user.setLast_name(editlname);
                            MainActivity.user.setEmail(editemail);
                            MainActivity.user.setImgUrl(ServerManager.getServerURL() + response.getString("url"));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(updates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
