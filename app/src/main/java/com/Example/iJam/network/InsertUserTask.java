package com.Example.iJam.network;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.Example.iJam.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mostafa on 7/6/2015.
 */
public class InsertUserTask extends HttpPostTask {

    public InsertUserTask(Context ctx) {
        super(ServerManager.getServerURL() + "/users/insert.php", ctx);
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            JSONObject response = new JSONObject(s);
            String status = response.getString("status");
            ServerManager.setServerStatus(status);
            if(status.equals("fail")) {
                Toast.makeText(ctx, "Sign up failed! " + response.getString("error"), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ctx, "Success", Toast.LENGTH_SHORT).show();
                int uid = response.getInt("user_id");
                Intent inte = new Intent(ctx, MainActivity.class);
                inte.putExtra("user_id", uid);
                inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(inte);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
