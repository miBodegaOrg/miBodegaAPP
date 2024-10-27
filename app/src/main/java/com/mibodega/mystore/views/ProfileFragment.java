package com.mibodega.mystore.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.SignInResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.DBfunctionsTableData;
import com.mibodega.mystore.views.user.EditProfileActivity;


public class ProfileFragment extends Fragment {

    private ImageButton btn_downloadCodes;
    private TextView tv_name,tv_address,tv_phone, tv_rucdni;
    private Button btn_editProfile;
    private MaterialCardView btn_signOut;

    private Config config = new Config();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        btn_downloadCodes = root.findViewById(R.id.IBtn_downloadCodeBars_profile);
        tv_name = root.findViewById(R.id.Tv_userName_profile);
        tv_address = root.findViewById(R.id.Tv_address_profile);
        tv_phone = root.findViewById(R.id.Tv_phone_profile);
        tv_rucdni = root.findViewById(R.id.Tv_ruc_profile);
        btn_editProfile = root.findViewById(R.id.Btn_editProfile_profile);
        btn_signOut = root.findViewById(R.id.Mc_signOut_profile);

        SignInResponse user = config.getUserData();
        if(user!=null){
            tv_name.setText(user.getName());
           // tv_address.setText();
            tv_phone.setText(user.getPhone());
            tv_rucdni.setText(user.getUsername());
        }


        btn_downloadCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

        return  root;
    }
}