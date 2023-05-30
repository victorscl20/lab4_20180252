package com.example.lab4_0252;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_0252.databinding.ActivityInformacionBinding;
import com.example.lab4_0252.entities.DTOemployee;
import com.example.lab4_0252.entities.DTOjob;
import com.example.lab4_0252.entities.Employee;
import com.example.lab4_0252.entities.Job;
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

public class InformacionActivity extends AppCompatActivity {

    ActivityInformacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

        EditText idemployee = binding.editTextTextPersonName;



        binding.buttonVer.setOnClickListener(new View.OnClickListener() {
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
                                binding.textView16.setText(employee.getFirstName());
                                binding.textView17.setText(employee.getLastName());
                                binding.textView18.setText(employee.getEmail());
                                binding.textView19.setText(employee.getPhoneNumber());
                                binding.textView21.setText(employee.getHireDate().toString());
                                binding.textView22.setText(employee.getSalary().toString());
                                if(employee.getManagerId() == null){
                                    binding.textView23.setText("NA");
                                }else{
                                    employeeRepository.getEmployee(employee.getManagerId()).enqueue(new Callback<DTOemployee>() {
                                        @Override
                                        public void onResponse(Call<DTOemployee> call, Response<DTOemployee> response) {
                                            if (response.isSuccessful()){
                                                Employee manag = response.body().getEmployee();
                                                binding.textView23.setText(manag.getFirstName()+" "+manag.getLastName());
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<DTOemployee> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });
                                }
                                employeeRepository.getJob(employee.getJobId()).enqueue(new Callback<DTOjob>() {
                                    @Override
                                    public void onResponse(Call<DTOjob> call, Response<DTOjob> response) {
                                        if (response.isSuccessful()){
                                            Job job = response.body().getJob();
                                            binding.textView20.setText(job.getJobTitle());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DTOjob> call, Throwable t) {
                                        t.printStackTrace();

                                    }
                                });

                                //gaa

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

        binding.buttonDownload.setOnClickListener(new View.OnClickListener() {
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
                            if(employee !=null){
                                saveJson(employee,idemployee.getText().toString());
                                System.out.println("todo ok");
                            }
                        }
                        else{
                            Log.d(TAG, "algo salio mal");
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

    private void saveJson(Employee employee, String id) {
        Gson gson = new Gson();
        String trabajador = gson.toJson(employee);

        Log.d(TAG, trabajador);
        String fileName = id+".txt";
        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write(trabajador);
            Log.d(TAG, "good");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
