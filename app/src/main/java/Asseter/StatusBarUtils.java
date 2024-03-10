package asseter;

import android.content.Context;
import android.os.Build;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.core.view.WindowCompat;

import com.example.sastauts.R;

public class StatusBarUtils {

    public static void setStatusBarColorAndIcons(Window window, Context context) {
        window.setStatusBarColor(context.getResources().getColor(R.color.primary, context.getTheme()));

        WindowCompat.setDecorFitsSystemWindows(window, false);
    }
}
