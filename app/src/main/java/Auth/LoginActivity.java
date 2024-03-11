package Auth;

import static asseter.StatusBarUtils.setStatusBarColorAndIcons;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sastauts.MainActivity;
import com.example.sastauts.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setStatusBarColorAndIcons(getWindow(),LoginActivity.this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Check if email and password are valid (this is a dummy check)
                if (email.equals("admin") && password.equals("password123")) {
                    // Login successful, navigate to main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed, display an error message
                    Toast.makeText(LoginActivity.this, "Invalid email or password.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
