package vn.fpt.ph26439.lab56_retrofit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.fpt.ph26439.lab56_retrofit_app.Adapter.UserAdapter;
import vn.fpt.ph26439.lab56_retrofit_app.Models.ListUser;
import vn.fpt.ph26439.lab56_retrofit_app.Models.User;

public class MainActivity extends AppCompatActivity {
    private static Retrofit retrofit;
    private RecyclerView rcv_user;
    private FloatingActionButton dialog_add;
    private EditText edt_ten,edt_diachi,edt_email,edt_sodt,edt_image;
    private Button btnCancel, btnSend;
    private static final String BASE_URL = "http://172.16.0.2:8000/user/";

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        getRetrofitInstance();

        getData();

        dialog_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddUser(Gravity.CENTER);
            }
        });

    }
    private void anhxa(){
        dialog_add = findViewById(R.id.fltBtn_add);
        rcv_user = findViewById(R.id.rcv_user);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        rcv_user.setLayoutManager(manager);
    }
    private void getData(){
        UserInterface userService = MainActivity.getRetrofitInstance().create(UserInterface.class);
        Call<ListUser> call = userService.getAllUser();
        call.enqueue(new Callback<ListUser>() {
            @Override
            public void onResponse(Call<ListUser> call, Response<ListUser> response) {
                Log.e("data",response.toString());
                List<User> list = new ArrayList<>(Arrays.asList(response.body().getUsers()));
                Log.e("size",""+list.size());
                for (User u :list){
                    Log.d("list",u.toString());
                }
                userAdapter = new UserAdapter(getApplicationContext(),list,MainActivity.this);
                rcv_user.setAdapter(userAdapter);

            }

            @Override
            public void onFailure(Call<ListUser> call, Throwable t) {
                Log.e("zzz",t.getMessage());
            }
        });
    }
    private void openDialogAddUser(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add);

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
            edt_diachi = dialog.findViewById(R.id.edt_diachi);
            edt_email = dialog.findViewById(R.id.edt_email);
            edt_sodt = dialog.findViewById(R.id.edt_sodt);
            edt_image = dialog.findViewById(R.id.edt_image);
            btnCancel = dialog.findViewById(R.id.btnCancel);
            btnSend = dialog.findViewById(R.id.btnSend);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserInterface userService = MainActivity.getRetrofitInstance().create(UserInterface.class);

                    String ten = edt_ten.getText().toString();
                    String diachi = edt_diachi.getText().toString();
                    String email = edt_email.getText().toString();
                    String sodt = edt_sodt.getText().toString();
                    String image =edt_image.getText().toString();
                    User user = new User(ten,diachi,email,sodt,image);
                    Gson gson = new Gson();
                    String strJson = gson.toJson(user);
                    Log.e("quynd",strJson);
                    Call<User> call = userService.postData(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Log.d("abc",response.toString());
                            if(response.isSuccessful()){
                                // xử lý dữ liệu;
                                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                getData();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


            dialog.show();

    }
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}