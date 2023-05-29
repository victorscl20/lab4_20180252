package com.example.lab4_0252;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.lab4_0252.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    String canal1 = "canaltutor";

    String canal2 = "canaltrabajador";


    public void Permiso(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101){
            Log.d(TAG,"Condedido");
        }else{
            Log.d(TAG,"Denegado");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        crearNotificacion();

        binding.buttonTutor.setOnClickListener(view -> {
            Intent intent= new Intent(this, TutorActivity.class);
            startActivity(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                    .setContentTitle("IMPORTANTE")
                    .setContentText("Entrando en modo TUTOR")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = builder.build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

            if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManagerCompat.notify(1, notification);
            }

        });

        binding.buttonTrabajador.setOnClickListener(view -> {
            Intent intent= new Intent(this, TrabajadorActivity.class);
            startActivity(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal2)
                    .setContentTitle("IMPORTANTE")
                    .setContentText("Entrando en modo EMPLEADO")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = builder.build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

            if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManagerCompat.notify(2, notification);
            }

        });
    }

    private void crearNotificacion() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(canal1,
                    "Canal 1 notificaciones",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationChannel channel2 = new NotificationChannel(canal2,
                    "Canal 2 notificaciones",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel2);

            Permiso();

        }
    }
}