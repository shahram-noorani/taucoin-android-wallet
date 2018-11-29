package com.mofei.tau.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.entity.res_post.StatusMessage;
import com.mofei.tau.entity.res_put.Register;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.info.key_address.taucoin.Key;
import com.mofei.tau.info.key_address.taucoin.KeyGenerator;
import com.mofei.tau.net.ApiService;
import com.mofei.tau.net.NetWorkManager;
import com.mofei.tau.transaction.KeyValue;
import com.mofei.tau.util.L;
import com.mofei.tau.util.UserInfoUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private TextView backTV;
    private ImageView clearImEmail,clearImPass;
    private EditText edEmail,edVerficode,edPassword;
    private TextView verifyCodeTV;
    private int count;
    private Handler handler=new Handler();
    private Button btnRegister;
   // private String email;
    private static KeyGenerator instance;
    private int status;
    private  String email;
    private  String password;

    private int verfiCodeStatus;

    // Cache Key
    final Key key = new Key();

    @SuppressLint("HandlerLeak")
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x11:
                    if(status==0){
                        showToast("register successful");
                        SharedPreferencesHelper.getInstance(RegisterActivity.this).putString("password",password);
                        UserInfoUtils.setCurrentEmail(RegisterActivity.this, email);
                        UserInfoUtils.setPublicKey(RegisterActivity.this, key.getPubkey());
                        UserInfoUtils.setPrivateKey(RegisterActivity.this, key.getPrivkey());
                        UserInfoUtils.setAddress(RegisterActivity.this, key.getAddress());

                        finish();
                    }else if(status==401){
                        showToast("Fail to get real_code");
                    }else if(status==402){
                        showToast("Code has expired");
                    }else if(status==403){
                        showToast("Code is wrong");
                    }else if(status==405){
                        showToast("Email is already exists");
                    }

                    break;

                case 0x14:
                    showToast("send successful");
                    startCountdown();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        initData();

        initEvent();
    }

    private void initView() {
        backTV=findViewById(R.id.imgBack);
        clearImEmail=findViewById(R.id.img_email2);
        clearImPass=findViewById(R.id.img_password2);
        edEmail=findViewById(R.id.edEmail);
        edVerficode=findViewById(R.id.edVerficode);
        edPassword=findViewById(R.id.edPassword);
        verifyCodeTV=findViewById(R.id.sendVerifyCodeTV);
        btnRegister=findViewById(R.id.btnRegister);
    }

    private void initData() {
        backTV.setOnClickListener(this);
        clearImEmail.setOnClickListener(this);
        clearImPass.setOnClickListener(this);
        verifyCodeTV.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initEvent() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                finish();
                break;
            case R.id.img_email2:
                cleanWord();
                break;
            case R.id.img_password2:
                cleanWord();
                break;
            case R.id.sendVerifyCodeTV:
                String email_=edEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email_)) {
                    showToast("Please enter your email");
                    return;
                }
                if(!isEmailValid(email_)){
                    showToast("invalid email");
                    L.e("isEmailValid");
                    return;
                }

                showWaitDialog("obtaining validation code...");
                getVerfiCode(email_);

                //login1_okhttp();
                break;
            case R.id.btnRegister:
                email=edEmail.getText().toString().trim();
                String verifcode=edVerficode.getText().toString().trim();
                password=edPassword.getText().toString().trim();

                if (email == null || email.length() == 0) {
                     showToast("Please enter your email");
                    return;
                }
                if(!isEmailValid(email)){
                    showToast("Incorrect password format");
                    L.e("isEmailValid");
                    return;
                }
                if(verifcode==null||verifcode.length()==0){
                    showToast("Please enter your verification code");
                    return;
                }
                if (password == null || password.length() == 0) {
                    showToast("Please enter your password ");
                    return;
                }
                showWaitDialog("Register...");
                register(email,verifcode,password);
                break;
        }
    }


    private void getVerfiCode(String str) {

        Map<String,String> email=new HashMap<>();
        email.put("email",str);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<StatusMessage> observable=apiService.verifyCode(email);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {

                        verfiCodeStatus=statusMessage.getStatus();

                        L.i("Status: "+statusMessage.getStatus());
                        L.i("Message: "+statusMessage.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.i("onError");
                        e.printStackTrace();


                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        handle.sendEmptyMessage(0x14);
                        L.i("onComplete");
                    }
                });
    }

    private void register(String email,String verifcode,String password) {
        generatekeyAddress();
        Register register=new Register();
        register.setEmail(email);
        register.setEmail_code(verifcode);
        register.setPassword(password);
        register.setAddress(key.getAddress());
        register.setPubkey(key.getPubkey());

        ApiService apiService= NetWorkManager.getApiService();
        Observable<StatusMessage> observable=apiService.register(register);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {

                        L.i("Status: "+statusMessage.getStatus());
                        L.i("Message: "+statusMessage.getMessage());
                        status=statusMessage.getStatus();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.i("onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        handle.sendEmptyMessage(0x11);
                        L.i("onComplete");
                    }
                });


    }

    private void generatekeyAddress() {
        key.Reset();
        if (getInstance().GenerateKey(key)) {
            L.e("Privkey :" + key.getPrivkey());
            L.e("Public: " + key.getPubkey());
            L.e("Address: " + key.getAddress());
        } else {
            L.i("Generate key error...");
        }
    }


    public static KeyGenerator getInstance(){
        if (instance == null) {
            instance = new KeyGenerator();
        }
        return instance;
    }

    private TextWatcherAdapter emailWatcher  = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            String emailInput = edEmail.getText().toString();
            if (TextUtils.isEmpty(emailInput)) {
                clearImEmail.setVisibility(View.GONE);
            } else {
                clearImEmail.setVisibility(View.VISIBLE);
            }
        }
    };
    private TextWatcherAdapter passwordWatcher  = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            String passwordInput = edPassword.getText().toString();
            if (TextUtils.isEmpty(passwordInput)) {
                clearImPass.setVisibility(View.GONE);
            } else {
                clearImPass.setVisibility(View.VISIBLE);
            }
        }
    };

    private void cleanWord() {
        edEmail.addTextChangedListener(emailWatcher);
        clearImEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edEmail.setText(null);
            }
        });
        edPassword.addTextChangedListener(passwordWatcher);
        clearImPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edPassword.setText(null);
            }
        });
    }

    private void startCountdown() {
        count = 90;
        verifyCodeTV.setText(String.valueOf(count));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count > 1) {
                    verifyCodeTV.setEnabled(false);
                    count--;
                    verifyCodeTV.setText(String.valueOf(count));
                    handler.postDelayed(this,1000L);
                } else {
                    verifyCodeTV.setText("Resend");
                    verifyCodeTV.setEnabled(true);
                }
            }
        }, 1000L);
    }

}
