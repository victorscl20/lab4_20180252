package com.example.lab4_0252;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lab4_0252.databinding.ActivityTrabajadorBinding;
import com.example.lab4_0252.entities.DTOemployee;
import com.example.lab4_0252.entities.Employee;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorActivity extends AppCompatActivity {

    ActivityTrabajadorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());


        binding.buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrabajadorActivity.this, InformacionActivity.class);
                startActivity(intent);
            }
        });

        EditText idemployee = binding.editTextTextPersonName2;

        binding.buttonHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeRepository employeeRepository = new Retrofit.Builder()
                        .baseUrl("http://192.168.50.177:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(EmployeeRepository.class);
                employeeRepository.getEmployee(Integer.parseInt(idemployee.getText().toString())).enqueue(new Callback<DTOemployee>() {
                    @Override
                    public void onResponse(Call<DTOemployee> call, Response<DTOemployee> response) {
                        if (response.isSuccessful()) {
                            Employee employee = response.body().getEmployee();

                            if(employee != null){
                                if((int)employee.getMeeting() == 1){
                                    descargarConDownloadManager();
                                    Toast.makeText(getApplicationContext(),"Descargando", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"No tiene tutoias", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),"No existe ese ID", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d(TAG, "error en la respuesta del webservice");
                        }
                    }

                    @Override
                    public void onFailure(Call<DTOemployee> call, Throwable t) {
                        t.printStackTrace();

                    }
                });
            }
        });

    }

    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {

                if (isGranted) { // permiso concedido
                    descargarConDownloadManager();
                } else {
                    Log.e(TAG, "Permiso denegado");
                }
            });
    public void descargarConDownloadManager() {

        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        // >29
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            //si tengo permisos
            String fileName = "pucp.jpg";
            String endPoint = "http://maternet.edu.pe/sites/default/files/images/LOGO-PUCP.jpg";

            Uri downloadUri = Uri.parse(endPoint);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle(fileName);
            request.setMimeType("image/jpeg");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + fileName);

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(request);
        } else {
            //si no tiene permisos
            launcher.launch(permission);
        }

    }

}
