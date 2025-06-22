package com.example.listamiejsc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMapa;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker aktualnyZnacznik;

    private FirebaseAuth mAutoryzacja;
    private FirebaseFirestore bazaDanych;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Button btnDodajMiejsce;

    private Map<Marker, String> markeryZDokumentami = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAutoryzacja = FirebaseAuth.getInstance();
        bazaDanych = FirebaseFirestore.getInstance();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnDodajMiejsce = findViewById(R.id.btnDodajMiejsce);
        btnDodajMiejsce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dodajMiejsceNaZnaczniku();
            }
        });
        if (mAutoryzacja.getCurrentUser() == null) {
            mAutoryzacja.signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Auth", "Zalogowano anonimowo");
                            zaladujMape();
                        } else {
                            Log.e("Auth", "Błąd logowania", task.getException());
                            Toast.makeText(this, "Błąd logowania do Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            zaladujMape();
        }
        FirebaseFirestore.getInstance().collection("test")
                .add(new HashMap<>())
                .addOnSuccessListener(docRef -> Log.d("TEST", "Działa!"))
                .addOnFailureListener(e -> Log.e("TEST", "Błąd Firestore", e));
    }

    private void zaladujMape() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Pozwolenie na korzystanie z lokalizacji
    private void sprawdzPozwolenieNaLokalizacje() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            wlaczLokalizacjeUzytkowika();
        }
    }
    private void wlaczLokalizacjeUzytkowika() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMapa.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng polozenieUzytkownika = new LatLng(location.getLatitude(), location.getLongitude());
                            mMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(polozenieUzytkownika, 15));
                        } else {
                            LatLng polska = new LatLng(52.0, 19.0);
                            mMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(polska, 5.5f));
                        }
                    });
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMapa = googleMap;
        sprawdzPozwolenieNaLokalizacje();
        wlaczLokalizacjeUzytkowika();

        LatLng polska = new LatLng(52.0, 19.0);
        float zoom = 5.5f;
        mMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(polska, zoom));

        mMapa.setOnMapClickListener(latLng -> {
            if (aktualnyZnacznik != null) {
                aktualnyZnacznik.remove();
            }
            aktualnyZnacznik = mMapa.addMarker(new MarkerOptions().position(latLng).title("Nowe miejsce"));
        });

        bazaDanych.collection("miejsca")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (var doc : queryDocumentSnapshots) {
                        Double szerokoscGeo = doc.getDouble("szerokoscGeo");
                        Double dlugoscGeo = doc.getDouble("dlugoscGeo");
                        String nazwa = doc.getString("nazwa");
                        String kategoria = doc.getString("kategoria");

                        if (szerokoscGeo != null && dlugoscGeo != null) {
                            LatLng polozenie = new LatLng(szerokoscGeo, dlugoscGeo);

                            MarkerOptions opcjeMarkera = new MarkerOptions()
                                    .position(polozenie)
                                    .title(nazwa + " (" + kategoria + ")")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                            Marker marker = mMapa.addMarker(opcjeMarkera);

                            if (marker != null) {
                                markeryZDokumentami.put(marker, doc.getId());
                            }
                        }
                    }
                });

        mMapa.setOnMarkerClickListener(marker -> {
            String dokumentId = markeryZDokumentami.get(marker);
            if (dokumentId != null) {
                pokazSzczegolyMiejsca(marker, dokumentId);
            }
            return true;
        });
    }
    private void zaladujKategorieZBazy(LinearLayout kontenerCheckboxow, Runnable poZaladowniu) {
        bazaDanych.collection("kategorie")
                .get()
                .addOnSuccessListener(wynik -> {
                    kontenerCheckboxow.removeAllViews();
                    for (var dokument : wynik) {
                        String nazwa = dokument.getString("nazwa");
                        Log.d("KATEGORIE", "Z Firestore: " + nazwa);
                        if (nazwa != null) {
                            dodajCheckboxKategorie(kontenerCheckboxow, nazwa);
                        }
                    }
                    if (poZaladowniu != null) {
                        poZaladowniu.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Błąd ładowania kategorii", Toast.LENGTH_SHORT).show();
                });
    }
    private void dodajMiejsceNaZnaczniku() {
        if (aktualnyZnacznik == null) {
            Toast.makeText(this, "Kliknij na mapie, aby wybrać miejsce", Toast.LENGTH_SHORT).show();
            return;
        }
        pokazDialogDodawania(aktualnyZnacznik.getPosition());
    }

    private void pokazDialogDodawania(LatLng latLng) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Dodaj miejsce");

        View widokDialogu = getLayoutInflater().inflate(R.layout.dodaj_miejsce, null);
        builder.setView(widokDialogu);

        EditText poleNazwa = widokDialogu.findViewById(R.id.etNazwa);
        EditText poleNowaKategoria = widokDialogu.findViewById(R.id.etNowaKategoria);
        Button btnDodajKategorie = widokDialogu.findViewById(R.id.btnDodajKategorie);
        LinearLayout kontenerCheckboxow = widokDialogu.findViewById(R.id.llKategorieCheckboxy);

        zaladujKategorieZBazy(kontenerCheckboxow, null);

        btnDodajKategorie.setOnClickListener(v -> {
            String nowaKategoria = poleNowaKategoria.getText().toString().trim();
            boolean juzIstnieje = false;
            for (int i = 0; i < kontenerCheckboxow.getChildCount(); i++) {
                View child = kontenerCheckboxow.getChildAt(i);
                if (child instanceof LinearLayout) {
                    CheckBox cb = (CheckBox) ((LinearLayout) child).getChildAt(0);
                    if (cb.getText().toString().equalsIgnoreCase(nowaKategoria)) {
                        juzIstnieje = true;
                        break;
                    }
                }
            }
            if (!juzIstnieje) {
                dodajCheckboxKategorie(kontenerCheckboxow, nowaKategoria);
                zapiszNowaKategorie(nowaKategoria);
                poleNowaKategoria.setText("");
            } else {
                Toast.makeText(this, "Taka kategoria już istnieje", Toast.LENGTH_SHORT).show();
            }

        });

        builder.setPositiveButton("Zapisz", null);
        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dlg -> {
            Button btnZapisz = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnZapisz.setOnClickListener(v -> {
                String nazwa = poleNazwa.getText().toString().trim();
                List<String> wybraneKategorie = new ArrayList<>();

                for (int i = 0; i < kontenerCheckboxow.getChildCount(); i++) {
                    View child = kontenerCheckboxow.getChildAt(i);
                    if (child instanceof LinearLayout) {
                        LinearLayout wiersz = (LinearLayout) child;
                        for (int j = 0; j < wiersz.getChildCount(); j++) {
                            View sub = wiersz.getChildAt(j);
                            if (sub instanceof CheckBox && ((CheckBox) sub).isChecked()) {
                                wybraneKategorie.add(((CheckBox) sub).getText().toString());
                            }
                        }
                    }
                }

                if (nazwa.isEmpty() || wybraneKategorie.isEmpty()) {
                    Toast.makeText(this, "Podaj nazwę i wybierz co najmniej jedną kategorię", Toast.LENGTH_SHORT).show();
                    return;
                }

                String kategorieString = String.join(", ", wybraneKategorie);
                zapiszMiejsceFirestore(nazwa, kategorieString, latLng);

                if (aktualnyZnacznik != null) {
                    aktualnyZnacznik.remove();
                    aktualnyZnacznik = null;
                }

                odswiezMape();
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private void dodajCheckboxKategorie(LinearLayout kontener, String nazwa) {
        LinearLayout wiersz = new LinearLayout(this);
        wiersz.setOrientation(LinearLayout.HORIZONTAL);

        CheckBox cb = new CheckBox(this);
        cb.setText(nazwa);
        cb.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        ImageButton btnUsun = new ImageButton(this);
        btnUsun.setImageResource(android.R.drawable.ic_delete);
        btnUsun.setBackgroundColor(Color.TRANSPARENT);
        btnUsun.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Potwierdzenie")
                    .setMessage("Czy na pewno chcesz usunąć kategorię \"" + nazwa + "\"?\nSpowoduje to usunięcie wszystkich miejsc w danej kategorii.")
                    .setPositiveButton("Tak", (dialog, which) -> {
                        usunKategorieZFirestore(nazwa, kontener, wiersz);
                        usunMiejscaZKategorii(nazwa);
                    })
                    .setNegativeButton("Nie", null)
                    .show();
        });

        wiersz.addView(cb);
        wiersz.addView(btnUsun);
        kontener.addView(wiersz);
    }

    private void usunKategorieZFirestore(String nazwaKategorii, LinearLayout kontener, LinearLayout wiersz) {
        bazaDanych.collection("miejsca")
                .whereEqualTo("kategorie", nazwaKategorii)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot dokument : querySnapshot) {
                        dokument.getReference().delete();
                    }

                    bazaDanych.collection("kategorie")
                            .whereEqualTo("nazwa", nazwaKategorii)
                            .get()
                            .addOnSuccessListener(query -> {
                                for (var doc : query) {
                                    bazaDanych.collection("kategorie").document(doc.getId()).delete();
                                }

                                kontener.removeView(wiersz);
                                odswiezMape();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Błąd usuwania kategorii", Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Błąd usuwania miejsc z tej kategorii", Toast.LENGTH_SHORT).show();
                });


    }


    private void usunMiejscaZKategorii(String nazwaKategorii) {
        bazaDanych.collection("miejsca")
                .whereEqualTo("kategorie", nazwaKategorii)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        bazaDanych.collection("miejsca").document(doc.getId())
                                .delete();
                    }
                    odswiezMape();
                });
    }

    private void odswiezMape() {
        if (mMapa == null) return;
        mMapa.clear();
        markeryZDokumentami.clear();

        bazaDanych.collection("miejsca")
                .get()
                .addOnSuccessListener(wynik -> {
                    for (var dokument : wynik) {
                        Double szerokoscGeo = dokument.getDouble("szerokoscGeo");
                        Double dlugoscGeo = dokument.getDouble("dlugoscGeo");
                        String nazwa = dokument.getString("nazwa");
                        String kategoria = dokument.getString("kategoria");

                        if (szerokoscGeo != null && dlugoscGeo != null) {
                            LatLng polozenie = new LatLng(szerokoscGeo, dlugoscGeo);

                            MarkerOptions opcjeMarkera = new MarkerOptions()
                                    .position(polozenie)
                                    .title(nazwa + " (" + kategoria + ")")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                            Marker marker = mMapa.addMarker(opcjeMarkera);

                            if (marker != null) {
                                markeryZDokumentami.put(marker, dokument.getId());
                            }
                        }
                    }
                });
    }
    private void zapiszMiejsceFirestore(String nazwa, String kategoria, LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String adres = "";
        try {
            List<Address> adresy = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (adresy != null && !adresy.isEmpty()) {
                adres = adresy.get(0).getAddressLine(0);
            } else {
                adres = "Adres nieznany";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> miejsce = new HashMap<>();
        miejsce.put("nazwa", nazwa);
        miejsce.put("kategoria", kategoria);
        miejsce.put("szerokoscGeo", latLng.latitude);
        miejsce.put("dlugoscGeo", latLng.longitude);
        miejsce.put("adres", adres);

        bazaDanych.collection("miejsca")
                .add(miejsce)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity.this, "Miejsce dodane!", Toast.LENGTH_SHORT).show();
                    odswiezMape();
                    MarkerOptions opcje = new MarkerOptions()
                            .position(latLng)
                            .title(nazwa + " (" + kategoria + ")")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    Marker marker = mMapa.addMarker(opcje);
                    if (marker != null) {
                        markeryZDokumentami.put(marker, documentReference.getId());
                    }
                })

                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Błąd podczas zapisu", Toast.LENGTH_SHORT).show();
                });
    }

    private void zapiszNowaKategorie(String kategoria) {
        if (kategoria == null || kategoria.isEmpty()) return;

        Map<String, Object> kat = new HashMap<>();
        kat.put("nazwa", kategoria);

        bazaDanych.collection("kategorie")
                .add(kat)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Kategoria zapisana: " + kategoria);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Błąd zapisu kategorii", e);
                });
    }

    // Obsługa zgody na lokalizację
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] uprawnienia, @NonNull int[] wyniki) {
        super.onRequestPermissionsResult(requestCode, uprawnienia, wyniki);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (wyniki.length > 0
                    && wyniki[0] == PackageManager.PERMISSION_GRANTED) {
                wlaczLokalizacjeUzytkowika();
            } else {
                Toast.makeText(this, "Brak pozwolenia na lokalizację", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pokazDialogUsuwaniaMiejsca(Marker marker, String dokumentId) {
        new AlertDialog.Builder(this)
                .setTitle("Usuń miejsce")
                .setMessage("Czy na pewno chcesz usunąć to miejsce?")
                .setPositiveButton("Tak", (dialog, which) -> {
                    bazaDanych.collection("miejsca").document(dokumentId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                markeryZDokumentami.remove(marker);
                                marker.remove();
                                odswiezMape();
                                Toast.makeText(MainActivity.this, "Miejsce usunięte", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MainActivity.this, "Błąd usuwania miejsca", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void pokazSzczegolyMiejsca(Marker marker, String dokumentId) {
        bazaDanych.collection("miejsca").document(dokumentId)
                .get()
                .addOnSuccessListener(dokument -> {
                    if (dokument.exists()) {
                        String nazwa = dokument.getString("nazwa");
                        String kategoria = dokument.getString("kategoria");
                        String adres = dokument.getString("adres");

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(nazwa);
                        builder.setMessage("Kategorie: " + kategoria + "\nAdres: " + adres);
                        builder.setPositiveButton("Usuń", (dialog, which) -> {
                            pokazDialogUsuwaniaMiejsca(marker, dokumentId);
                        });
                        builder.setNegativeButton("Zamknij", null);
                        builder.show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Błąd ładowania szczegółów", Toast.LENGTH_SHORT).show();
                });
    }
}