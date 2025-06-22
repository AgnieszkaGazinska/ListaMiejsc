package com.example.listamiejsc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogowanieActivity extends AppCompatActivity {
    private EditText poleEmail, poleHaslo;
    private Button btnZaloguj, btnZarejestruj, btnResetHasla;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logowanie);

        mAuth = FirebaseAuth.getInstance();

        poleEmail = findViewById(R.id.poleEmail);
        poleHaslo = findViewById(R.id.poleHaslo);
        btnZaloguj = findViewById(R.id.btnZaloguj);
        btnZarejestruj = findViewById(R.id.btnZarejestruj);
        btnResetHasla = findViewById(R.id.btnResetHasla);

        btnZaloguj.setOnClickListener(view -> zalogujUzytkownika());
        btnZarejestruj.setOnClickListener(view -> zarejestrujUzytkownika());
        btnResetHasla.setOnClickListener(view -> zresetujHaslo());
    }

    private void zalogujUzytkownika() {
        String email = poleEmail.getText().toString().trim();
        String haslo = poleHaslo.getText().toString().trim();

        if (email.isEmpty() || haslo.isEmpty()) {
            Toast.makeText(this, "Podaj e-mail i hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, haslo)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Zalogowano!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        String blad = task.getException() != null ? task.getException().getMessage() : "";
                        if (blad.contains("password is invalid") || blad.contains("The password is invalid")) {
                            Toast.makeText(this, "Nieprawidłowe hasło.", Toast.LENGTH_SHORT).show();
                        } else if (blad.contains("no user record") || blad.contains("There is no user record")) {
                            Toast.makeText(this, "Nie znaleziono użytkownika z tym adresem e-mail.", Toast.LENGTH_SHORT).show();
                        } else if (blad.contains("badly formatted") || blad.contains("The email address is badly formatted")) {
                            Toast.makeText(this, "Niepoprawny format adresu e-mail.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Błąd: " + blad, Toast.LENGTH_SHORT).show(); // fallback — na wypadek czegoś innego
                        }
                    }
                });
    }

    private void zarejestrujUzytkownika() {
        String email = poleEmail.getText().toString().trim();
        String haslo = poleHaslo.getText().toString().trim();

        if (email.isEmpty() || haslo.isEmpty()) {
            Toast.makeText(this, "Podaj e-mail i hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, haslo)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Rejestracja udana!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        String blad = task.getException() != null ? task.getException().getMessage() : "";

                        if (blad.contains("The email address is already in use") ||
                                blad.contains("email address is already in use")) {
                            Toast.makeText(this, "Ten adres e-mail jest już zarejestrowany.", Toast.LENGTH_SHORT).show();

                        } else if (blad.contains("badly formatted") ||
                                blad.contains("email address is badly formatted")) {
                            Toast.makeText(this, "Niepoprawny format adresu e-mail.", Toast.LENGTH_SHORT).show();

                        } else if (blad.contains("Password should be at least") ||
                                blad.contains("password must be")) {
                            Toast.makeText(this, "Hasło powinno mieć co najmniej 6 znaków.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this, "Błąd rejestracji. Spróbuj ponownie.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void zresetujHaslo() {
        String email = poleEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Podaj adres e-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Link do resetu hasła wysłany", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Błąd: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}