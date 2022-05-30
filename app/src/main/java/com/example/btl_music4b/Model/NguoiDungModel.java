package com.example.btl_music4b.Model;

import com.example.btl_music4b.Service_API.APIService;
import com.example.btl_music4b.Service_API.Dataservice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NguoiDungModel implements Serializable {
    @SerializedName("UserName")
    @Expose
    private String userName;

    @SerializedName("Password")
    @Expose
    private String password;

    @SerializedName("Name")
    @Expose
    private String nameUser;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Image")
    @Expose
    private String image;
    checkSendMail checkSendMail;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameuser() {
        return nameUser;
    }

    public void setNameuser(String nameuser) {
        this.nameUser = nameuser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void sendMail(HashMap<String, String> param, checkSendMail checkSendMail){
        this.checkSendMail = checkSendMail;
        Dataservice apiService = APIService.getService();
        Call<ResponseModel> call = apiService.sendmail(param);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().getSuccess().equals("1")){
                    checkSendMail.send(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                checkSendMail.send(false);
            }
        });
    }
    public interface checkSendMail{
        void send(boolean check);
    }
}
