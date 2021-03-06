package com.example.btl_music4b.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.btl_music4b.Model.NguoiDungModel;
import com.example.btl_music4b.Model.ResponseModel;
import com.example.btl_music4b.R;
import com.example.btl_music4b.Service_API.APIService;
import com.example.btl_music4b.Service_API.Dataservice;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dialog_Dangky_Free extends AppCompatDialogFragment implements NguoiDungModel.checkSendMail{

    TextInputLayout tk, mk, emaildangky;
    TextInputEditText maxndky;
    Button btnDangKy, btngetmadangky;
    ImageView imgclose;
    boolean accept = false, aceptmail = false;
    private ExampleDialogListener listener;
    String taikhoan="", matkhau="", email="", maxacnhan="";
    static int interval;
    static Timer timer;
    int delay = 1000, period = 1000, timeValue, code;
    LoadingDialog loadingDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_dang_ky_free, null);
        tk = view.findViewById(R.id.edttaikhoan);
        mk = view.findViewById(R.id.edtmatkhau);
        loadingDialog = new LoadingDialog(getActivity());
        emaildangky = view.findViewById(R.id.emaildangky);
        maxndky = view.findViewById(R.id.edtmaxacnhandangky);
        btnDangKy = view.findViewById(R.id.btndangKy);
        btngetmadangky = view.findViewById(R.id.btngetmadangky);
        imgclose = view.findViewById(R.id.imageClose);
        builder.setView(view);

        btngetmadangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setInterval() < 1){
                    aceptmail = false;
                    loadingDialog.show();

                    email = emaildangky.getEditText().getText().toString().trim();
                    EmailValidator validator = new EmailValidator();

                    if (email.trim().length() < 6 || email.trim().length() > 36){
                        Toast.makeText(getActivity(), "????? d??i email t??? 6 -> 36 k?? t???", Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                    }else {
                        if (validator.validate(email)) {
                            checkEmail(emaildangky.getEditText().getText().toString().trim());
                        } else {
                            Toast.makeText(getActivity(), "Email kh??ng ????ng ?????nh d???ng", Toast.LENGTH_LONG).show();
                            loadingDialog.dismiss();
                        }
                    }
                }
            }
        });


        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept = false;
                try {
                    loadingDialog.show();
                    taikhoan = tk.getEditText().getText().toString().trim();
                    matkhau = mk.getEditText().getText().toString().trim();
                    maxacnhan = maxndky.getText().toString().trim();
                }catch (Exception e){

                }
                if(taikhoan.trim().length() < 6 || taikhoan.trim().length() > 25){
                    Toast.makeText(getActivity(), "????? d??i t??i kho???n t??? 6 -> 25 k?? t???", Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                }else {
                    checkUser(taikhoan);
                }
            }
        });
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }
    private static final int setInterval() {
        if (interval == 1){
            timer.cancel();
        }
        return --interval;
    }
    private void senMail(String emaildangky){
        HashMap<String, String> data = new HashMap<>();
        Random random = new Random();
        code = 10000 + random.nextInt(89999);
        String messenger = "[Music4B]M?? x??c nh???n c???a b???n l?? : "+ code+". Kh??ng chia s??? m?? n??y cho b???t k?? ai.";
        data.put("email", emaildangky);
        data.put("subject", "????ng k?? t??i kho???n");
        data.put("message", messenger);
        NguoiDungModel nguoiDungModel = new NguoiDungModel();
        nguoiDungModel.sendMail(data, (NguoiDungModel.checkSendMail) this);
    }
    public void checkUser(String us) {
        Dataservice dataservice = APIService.getService();
        Call<ResponseModel> callback = dataservice.checkusername(us);
        callback.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        Toast.makeText(getActivity(), "T??i kho???n ???? ???????c s??? d???ng", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    } else {
                        if (matkhau.trim().length() < 6 || matkhau.trim().length() > 36){
                            Toast.makeText(getActivity(), "????? d??i m???t kh???u t??? 6 -> 36 k?? t???", Toast.LENGTH_LONG).show();
                            loadingDialog.dismiss();
                        }else if (email.equals(emaildangky.getEditText().getText().toString().trim())){
                            if (maxacnhan.equals(String.valueOf(code))){
                                listener.apply(taikhoan, matkhau, email);
                            }else {
                                Toast.makeText(getActivity(), "M?? x??c nh???n kh??ng ????ng", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Email n??y ch??a nh???n m??", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }

        });
    }
    public void checkEmail(String em) {
        Dataservice dataservice = APIService.getService();
        Call<ResponseModel> callback = dataservice.checkemail(em);
        callback.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        Toast.makeText(getActivity(), "email ???? ???????c s??? d???ng", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    } else {
                        senMail(email);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "G???i m?? th???t b???i", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ExampleDialogListener) context;
    }

    @Override
    public void send(boolean check) {
        if (check){
            Toast.makeText(getActivity(), "???? g???i", Toast.LENGTH_SHORT).show();
            String secs = "60";
            interval = Integer.parseInt(secs);
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    timeValue = setInterval();
                    if (timeValue == 0){
                        btngetmadangky.setText("L???y m??");
                    }else {
                        btngetmadangky.setText(""+timeValue);
                    }
                }
            }, delay, period);
            loadingDialog.dismiss();
        }else {
            loadingDialog.dismiss();
            Toast.makeText(getActivity(), "G???i email th???t b???i", Toast.LENGTH_SHORT).show();
        }
    }

    public interface ExampleDialogListener{
        void apply(String taikhoan, String matkhau, String email);
    }
}
