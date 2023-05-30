package com.example.lab4_0252;


import com.example.lab4_0252.entities.DTOdepartments;
import com.example.lab4_0252.entities.DTOemployee;
import com.example.lab4_0252.entities.DTOjob;
import com.example.lab4_0252.entities.Employee;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;


public interface EmployeeRepository {

    @GET("/tutor/list")
    Call<List<Employee>> getEmployees();

    @GET("/tutor/infoEmployee")
    Call<DTOemployee> getEmployee(@Query("id") Integer id);

    @GET("/tutor/job")
    Call<DTOjob> getJob(@Query("id") String id);

    @GET("/tutor/depa")
    Call<DTOdepartments> obtenerDepa(@Query("id") Integer id);

    @FormUrlEncoded
    @POST("/asignar")
    Call<Integer> asignar(@Field("id") Integer id);
}
