package com.example.btl_music4b.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_music4b.Activity.DanhsachbaihatActivity;
import com.example.btl_music4b.Fragment.LoadingDialog;
import com.example.btl_music4b.Model.ResponseModel;
import com.example.btl_music4b.Model.ThuVienPlayListModel;
import com.example.btl_music4b.R;
import com.example.btl_music4b.Service_API.APIService;
import com.example.btl_music4b.Service_API.Dataservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThuVienPlayListAdapter extends RecyclerView.Adapter<ThuVienPlayListAdapter.ViewHolder> {

    Context context;
    ArrayList<ThuVienPlayListModel> mangthuvienplaylist;
    View view;
    private String tennguoidung;
    deleteThuVien deleteThuVien;
    LoadingDialog loadingDialog;

    public ThuVienPlayListAdapter(Context context, ArrayList<ThuVienPlayListModel> mangthuvienplaylist, String tennguoidung, deleteThuVien deleteThuVien) {
        this.context = context;
        this.mangthuvienplaylist = mangthuvienplaylist;
        this.tennguoidung = tennguoidung;
        this.deleteThuVien = deleteThuVien;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dong_thuvien_playlist,parent, false);
        loadingDialog = new LoadingDialog(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThuVienPlayListModel thuVienPlayList = mangthuvienplaylist.get(position);
        holder.txttenthuvienplaylist.setText(thuVienPlayList.getTenThuVienPlayList());
        holder.txttennguoidung.setText("Danh sách phát của "+tennguoidung);
        Picasso.get().load(thuVienPlayList.getHinhThuVienPlaylist()).into(holder.imgthuvienplaylist);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DanhsachbaihatActivity.class);
                intent.putExtra("idthuvienplaylist", mangthuvienplaylist.get(position));
                context.startActivity(intent);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Xóa thư viện")
                        .setMessage("Bạn có muốn xóa thư viện "+thuVienPlayList.getTenThuVienPlayList()+" ?")
                        .setPositiveButton("Xóa", null)
                        .setNegativeButton("Hủy", null)
                        .show();

                Button pos = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button neg = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                pos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        deletethuvien(thuVienPlayList.getIDThuVienPlayList());
                        deletenhieubaihatthuvien(thuVienPlayList.getIDThuVienPlayList());
                        alertDialog.dismiss();
                    }
                });
                neg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return false;
            }
        });

    }
    private void deletethuvien(int idthuvien) {

        Dataservice networkService = APIService.getService();
        Call<ResponseModel> login = networkService.deletethuvien(idthuvien);
        login.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                ResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        loadingDialog.dismiss();
                    } else {
                        Toast.makeText(context, "Xóa thât bại", Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void deletenhieubaihatthuvien(int idthuvien) {
        Dataservice networkService = APIService.getService();
        Call<ResponseModel> login = networkService.deletenhieubaihatthuvien(idthuvien);
        login.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                ResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        deleteThuVien.status(true);
                    } else {
                        Toast.makeText(context, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "Lỗi kết nối !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangthuvienplaylist != null ? mangthuvienplaylist.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgthuvienplaylist;
        TextView txttenthuvienplaylist, txttennguoidung;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgthuvienplaylist = view.findViewById(R.id.imageviewthuvienplaylist);
            txttenthuvienplaylist = view.findViewById(R.id.textviewthuvienplaylist);
            txttennguoidung = view.findViewById(R.id.txtusetname);
        }

    }

    public interface deleteThuVien{
        void status(Boolean status);
    }
}
