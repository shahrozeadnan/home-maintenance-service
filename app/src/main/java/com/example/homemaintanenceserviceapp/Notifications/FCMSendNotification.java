package com.example.homemaintanenceserviceapp.Notifications;
import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class FCMSendNotification {
        private static final String Base_url = "https://fcm.googleapis.com/fcm/send";
        private static final String SERVER_KEY = "YOUR_KEY";

        public static void pushNotification (Context context , String token , String title , String message ) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RequestQueue queue = Volley.newRequestQueue(context);
            try {
                JSONObject json = new JSONObject();
                json.put("to", token);
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", message);
                json.put("notification", notification);
                JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST, Base_url, json, response -> {
                }, error -> {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", SERVER_KEY);
                        return params;
                    }
                };
                queue.add(jsonobjectRequest);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
}
