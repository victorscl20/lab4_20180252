package com.example.lab4_0252;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_0252.databinding.ActivityMainBinding;
import com.example.lab4_0252.databinding.ActivityTutorBinding;
import com.example.lab4_0252.entities.Employee;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutorActivity extends AppCompatActivity {

    ActivityTutorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeRepository employeeRepository = new Retrofit.Builder()
                        .baseUrl("http://192.168.50.177:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(EmployeeRepository.class);

                employeeRepository.getEmployees().enqueue(new Callback<List<Employee>>() {
                    @Override
                    public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {

                        if(response.isSuccessful()){
                            List<Employee> employeeList = response.body();
                            saveJson(employeeList);
                            System.out.println("todo ok");
                        }else{
                            Log.d(TAG, "algo salio mal");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Employee>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


                }
        });


        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, BuscarActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, BuscarActivity.class);
                startActivity(intent);
            }
        });



    }

    private void saveJson(List<Employee> employeeList) {
        Gson gson = new Gson();
        String listaEmployeesAsJson = gson.toJson(employeeList);

        Log.d(TAG, listaEmployeesAsJson);
        String fileName = "listEmployees.txt";
        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write(listaEmployeesAsJson);
            Log.d(TAG, "good");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
