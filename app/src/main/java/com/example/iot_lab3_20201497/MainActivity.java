package com.example.iot_lab3_20201497;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iot_lab3_20201497.api.ApiService;
import com.example.iot_lab3_20201497.api.RetrofitClient;
import com.example.iot_lab3_20201497.models.LoginRequest;
import com.example.iot_lab3_20201497.models.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Activity que se usará para el login
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Captura del botón de iniciar sesión
        MaterialButton loginButton = findViewById(R.id.loginButton);

        // Configurar botón de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {
        /*Código realizado con ayuda de chatgpt, se modificaron nombres de las clases y
        declaraciones para que concuerden con los creados en el proyecto*/
        // Obtener texto de los campos
        TextInputEditText usernameEditText = findViewById(R.id.usernameEditText);
        TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);
        String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

        // Validar campos
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto LoginRequest
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Obtener instancia de ApiService
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Realizar llamada a la API
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // Mostrar mensaje de éxito
                    Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                    // Aquí puedes guardar los datos del usuario si es necesario, por ejemplo, en SharedPreferences

                    // Navegar a la siguiente Activity (por ejemplo, TimerActivity)
                    Intent intent = new Intent(MainActivity.this, PomodoroTimerActivity.class);
                    // Puedes pasar los datos del usuario a la siguiente Activity si lo deseas
                    intent.putExtra("firstName", loginResponse.getFirstName());
                    intent.putExtra("lastName", loginResponse.getLastName());
                    intent.putExtra("email", loginResponse.getEmail());
                    intent.putExtra("gender", loginResponse.getGender());
                    intent.putExtra("userId", loginResponse.getId());
                    startActivity(intent);
                    finish(); // Finaliza la actividad de login para que no se pueda regresar con el botón "Atrás"
                } else {
                    // Manejar errores de la respuesta
                    Toast.makeText(MainActivity.this, "Login fallido: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Manejar fallos de la llamada
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}