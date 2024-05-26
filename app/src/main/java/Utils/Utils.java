package Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils {
    public static void showAlertDialog(String message, Context context) {
        if (context != null) {
            if (context instanceof Activity) {
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                Log.e("Utils", "Provided context is not an Activity context. Showing Toast instead.");
            }
        } else {
            Log.e("Utils", "Context is null. Unable to show AlertDialog or Toast.");
        }
    }
}
