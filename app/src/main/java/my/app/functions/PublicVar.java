package my.app.functions;

import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class PublicVar extends AppCompatActivity {
    public static ProgressBar spinner;
    //public static InputStream caInput;
    public static String myusername;
    public static String userid;
    public static String guestusername;
    public static String login_endpoint="https://gulunodejs.myvnc.com/api/login";
    public static String firstname, lastname, mobile, emailid;
    public static boolean mainuser = true;
}
