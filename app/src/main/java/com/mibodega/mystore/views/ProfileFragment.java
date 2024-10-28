package com.mibodega.mystore.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestUpdateProfile;
import com.mibodega.mystore.models.Responses.SignInResponse;
import com.mibodega.mystore.models.Responses.UpdateProfileResponse;
import com.mibodega.mystore.services.ISettingService;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.DBfunctionsTableData;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.views.employers.ManageEmployerActivity;
import com.mibodega.mystore.views.user.EditProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment {

    private ImageButton btn_deleteAllDataShop;
    private TextView tv_name,tv_address,tv_phone, tv_rucdni;
    private Button btn_editProfile;
    private MaterialCardView btn_signOut;

    private Dialog dialog;
    private Config config = new Config();
    private Utils utils = new Utils();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        btn_deleteAllDataShop = root.findViewById(R.id.IBtn_deleteAllDataApp_profile);
        tv_name = root.findViewById(R.id.Tv_userName_profile);
        tv_address = root.findViewById(R.id.Tv_address_profile);
        tv_phone = root.findViewById(R.id.Tv_phone_profile);
        tv_rucdni = root.findViewById(R.id.Tv_ruc_profile);
        btn_editProfile = root.findViewById(R.id.Btn_editProfile_profile);
        btn_signOut = root.findViewById(R.id.Mc_signOut_profile);

        SignInResponse user = config.getUserData();
        if(user!=null){
            tv_name.setText(user.getName());
            tv_address.setText(user.getEmail());
            tv_phone.setText(user.getPhone());
            tv_rucdni.setText(user.getUsername());
        }

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBfunctionsTableData dBfunctionsTableData = new DBfunctionsTableData();
                dBfunctionsTableData.cleanTokensSignIn(getContext());
                Activity activity = getActivity();
                activity.finish();
            }
        });

        loadDialog(getContext());
        btn_deleteAllDataShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        return  root;
    }


    public void loadDialog(Context context){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete_employee);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroun_custom_rectangle);
        }
        Display display;
        Point size = new Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = context.getDisplay();
            display.getSize(size);
        } else {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                display = windowManager.getDefaultDisplay();
                display.getSize(size);
            }
        }
        int screenWidth = size.x;
        int dialogWidth = (int) (screenWidth * 0.9);
        int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextInputEditText editText = dialog.findViewById(R.id.Edt_confirWordDelete_dialog);
        ImageButton btn_close = dialog.findViewById(R.id.Imgb_custom_closeDialog);

        Button btn_accept = dialog.findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("ELIMINAR")){
                    deleteAllData();
                    dialog.dismiss();
                }else{
                    Dialog dialog = utils.getAlertCustom(getContext(),"danger","Error","Ingresa correctamente el mensaje de confirmación",false);
                    dialog.show();
                }

            }
        });
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }

    private void deleteAllData(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISettingService service = retrofit.create(ISettingService.class);
        Call<ResponseBody> call = service.deleteAllDataShop("Bearer " + config.getJwt());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                System.out.println(response.toString());
                if (response.isSuccessful() && response.body() != null) {
                    String data = null;
                    try {
                        data = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Response: " + data);
                    if(Objects.equals(data, "All data deleted")){
                        Utils utils = new Utils();
                        Dialog dialog = utils.getAlertCustom(getContext(),"success","Eliminado","Todos los datos han sido eliminados de la cuenta",false);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        });
                        dialog.show();
                    }else{
                        Utils utils = new Utils();
                        Dialog dialog = utils.getAlertCustom(getContext(),"danger","Error","Error al eliminar datos",false);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        });
                        dialog.show();
                    }



                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        //Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    Utils utils = new Utils();
                    Dialog dialog = utils.getAlertCustom(getContext(),"danger","Error","Error al eliminar datos",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils utils = new Utils();
                Dialog dialog = utils.getAlertCustom(getContext(),"danger","Error","Error al eliminar datos",false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        SignInResponse user = config.getUserData();
        if(user!=null){
            tv_name.setText(user.getName());
            tv_address.setText(user.getEmail());
            tv_phone.setText(user.getPhone());
            tv_rucdni.setText(user.getUsername());
        }
    }
}