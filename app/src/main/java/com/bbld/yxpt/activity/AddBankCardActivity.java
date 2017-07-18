package com.bbld.yxpt.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.AddBankCard;
import com.bbld.yxpt.bean.BankCardRecognition;
import com.bbld.yxpt.bean.BankList;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 添加银行卡
 * Created by dell on 2017/7/7.
 */

public class AddBankCardActivity extends BaseActivity {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.tvBank)
    TextView tvBank;
    @BindView(R.id.ivMoreBank)
    ImageView ivMoreBank;
    @BindView(R.id.llMoreBank)
    LinearLayout llMoreBank;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private int bankId;
    private List<BankList.BankListlist> banks;
    private String[] items;
    private String token;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        getBank();
        setListeners();
    }

    private void getBank() {
        Call<BankList> call=RetrofitService.getInstance().getBankList();
        call.enqueue(new Callback<BankList>() {
            @Override
            public void onResponse(Response<BankList> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    banks=response.body().getList();
                    items=new String[banks.size()];
                    for (int i=0;i<banks.size();i++){
                        items[i]=banks.get(i).getBankName();
                    }
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setListeners() {
        etNumber.addTextChangedListener(watcher);
        llMoreBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBankDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNumber.getText().toString().trim().equals("")){
                    showToast("请填写银行卡");
                }else if(etName.getText().toString().trim().equals("")){
                    showToast("请填写持卡人");
                }else if (tvBank.equals("请选择银行")){
                    showToast("请选择银行");
                }else{
                    toAddBankCard();
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(AddBankCardActivity.this);
            }
        });
    }

    private void toAddBankCard() {
        Call<AddBankCard> call=RetrofitService.getInstance().addBankCard(token,bankId,etName.getText()+"",etNumber.getText()+"","");
//        showToast(token+","+bankId+","+etName.getText()+","+etNumber.getText()+"");
        call.enqueue(new Callback<AddBankCard>() {
            @Override
            public void onResponse(Response<AddBankCard> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    showToast("添加成功");
                    ActivityManagerUtil.getInstance().finishActivity(AddBankCardActivity.this);
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Recognition(editable+"");
        }
    };

    private void Recognition(String cardno) {
        Call<BankCardRecognition> call= RetrofitService.getInstance().BankCardRecognition(cardno);
        call.enqueue(new Callback<BankCardRecognition>() {
            @Override
            public void onResponse(Response<BankCardRecognition> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    if (response.body().getList()==null || response.body().getList().size()==0){
                        tvBank.setText("请选择银行");
                    }else{
                        tvBank.setText(response.body().getList().get(0).getBankName());
                        bankId=response.body().getList().get(0).getBankID();
                    }
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void showBankDialog() {
        AlertDialog alertDialog=new AlertDialog.Builder(AddBankCardActivity.this)
                .setTitle("请选择银行")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvBank.setText(items[i]);
                        bankId=banks.get(i).getBankID();
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_addbankcard;
    }
}
