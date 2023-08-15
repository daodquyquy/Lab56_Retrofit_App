package vn.fpt.ph26439.lab56_retrofit_app.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.fpt.ph26439.lab56_retrofit_app.MainActivity;
import vn.fpt.ph26439.lab56_retrofit_app.Models.User;
import vn.fpt.ph26439.lab56_retrofit_app.R;
import vn.fpt.ph26439.lab56_retrofit_app.UserInterface;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholer> {
    private Context context;
    private List<User> userList;
    MainActivity mainActivity;

    public UserAdapter(Context context, List<User> userList, MainActivity mainActivity) {
        this.context = context;
        this.userList = userList;
        this.mainActivity = mainActivity;
    }

    public void setList(List<User> userList){
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new Viewholer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholer holder, int position) {
        holder.tv_ten.setText("Tên : "+userList.get(position).getTen());
        holder.tv_diachi.setText("Địa chỉ : "+userList.get(position).getDiachi());
        holder.tv_email.setText("Email : "+userList.get(position).getEmail());
        holder.tv_sodt.setText("Số đt : "+userList.get(position).getSodt());
        Glide.with(context).load(userList.get(position).getImage()).into(holder.image);

        // xoá
        User user = userList.get(position);
        int i = position;
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle("Xoá user ?");
                builder.setMessage("Có chắc xoá user : "+user.getTen());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Tạo đối tượng retrofit
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://172.16.0.2:8000/user/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        // Lấy request
                        UserInterface userInterface = retrofit.create(UserInterface.class);
                         //xử lý interface
                        Call<User> call = userInterface.delById(user.get_id());
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                userList.remove(user);
                                setList(userList);
                                Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate(Gravity.CENTER,i);
            }
        });

    }
    private void dialogUpdate(int gravity, int position){
        EditText edt_ten,edt_diachi,edt_email,edt_sodt,edt_image;
        Button btnCancel, btnEdit;
        User user = userList.get(position);
        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams  windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }

        edt_ten = dialog.findViewById(R.id.edt_ten);
        edt_ten.setText(user.getTen());
        edt_diachi = dialog.findViewById(R.id.edt_diachi);
        edt_diachi.setText(user.getDiachi());
        edt_email = dialog.findViewById(R.id.edt_email);
        edt_email.setText(user.getEmail());
        edt_sodt = dialog.findViewById(R.id.edt_sodt);
        edt_sodt.setText(user.getSodt());
        edt_image = dialog.findViewById(R.id.edt_image);
        edt_image.setText(user.getImage());
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnEdit = dialog.findViewById(R.id.btnEdit);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://172.16.0.2:8000/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Lấy request
                UserInterface userInterface = retrofit.create(UserInterface.class);
                user.setTen(edt_ten.getText().toString());
                user.setDiachi(edt_diachi.getText().toString());
                user.setEmail(edt_email.getText().toString());
                user.setSodt(edt_sodt.getText().toString());
                user.setImage(edt_image.getText().toString());
                //xử lý interface
                Call<User> call = userInterface.editById(user.get_id(),user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        dialog.dismiss();
                        setList(userList);
                        Toast.makeText(context, "Update thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                        Log.e("errorupdate",t.getMessage());
                    }
                });
            }
        });


        dialog.show();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class Viewholer extends RecyclerView.ViewHolder{
        private TextView tv_ten,tv_diachi,tv_email,tv_sodt;
        private Button btnDelete,btnUpdate;
        private ImageView image;
        public Viewholer(@NonNull View itemView) {
            super(itemView);
            tv_ten = itemView.findViewById(R.id.tv_ten);
            tv_diachi = itemView.findViewById(R.id.tv_diachi);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_sodt = itemView.findViewById(R.id.tv_sodt);
            image = itemView.findViewById(R.id.img_image);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }

}
