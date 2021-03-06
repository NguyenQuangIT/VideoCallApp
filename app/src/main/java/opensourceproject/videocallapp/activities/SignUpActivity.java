package opensourceproject.videocallapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import opensourceproject.videocallapp.R;
import opensourceproject.videocallapp.utilities.Constants;
import opensourceproject.videocallapp.utilities.PreferenceManager;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;
    private MaterialButton buttonSignup;
    private ProgressBar signUpProgressBar;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferenceManager = new PreferenceManager(getApplicationContext());

        findViewById(R.id.signInText).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.backImage).setOnClickListener(view -> onBackPressed());

        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.confirmPassword);
        buttonSignup=findViewById(R.id.signUpButton);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);

        buttonSignup.setOnClickListener(view -> {
            if(inputEmail.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
            }else if(inputLastName.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Họ không được để trống!", Toast.LENGTH_SHORT).show();
            }else if(inputFirstName.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
            }else if(inputPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
            }else if(inputConfirmPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Vui lòng xác nhận mật khẩu!", Toast.LENGTH_SHORT).show();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()){
                Toast.makeText(SignUpActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
            }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                Toast.makeText(SignUpActivity.this, "Mật khẩu và xác nhận mật khẩu phải giống nhau!", Toast.LENGTH_SHORT).show();
            }else{
                signUp();
            }
        });
    }

    private void signUp(){
        buttonSignup.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString());
        user.put(Constants.KEY_LAST_NAME, inputLastName.getText().toString());
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());


        database.collection(Constants.KEY_COLLECTION_USERS).add(user).addOnSuccessListener(documentReference -> {
            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
            preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
            preferenceManager.putString(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString());
            preferenceManager.putString(Constants.KEY_LAST_NAME, inputLastName.getText().toString());
            preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString());

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }).addOnFailureListener(e -> {
            signUpProgressBar.setVisibility(View.INVISIBLE);
            buttonSignup.setVisibility(View.VISIBLE);
            Toast.makeText(SignUpActivity.this, "Đã có lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
        }

        public boolean testSignUp(){
            return true;
        }
    }
