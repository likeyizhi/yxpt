package com.bbld.yxpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.AddWithdrawa;
import com.bbld.yxpt.bean.WithdrawaAccountInfo;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 提现
 * Created by likey on 2017/7/11.
 */

public class WithdrawscashActivity extends BaseActivity{
    @BindView(R.id.llBankCard)
    LinearLayout llBankCard;
    @BindView(R.id.ivBankLogo)
    ImageView ivBankLogo;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvBankNo)
    TextView tvBankNo;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvCanMoney)
    TextView tvCanMoney;
    @BindView(R.id.tvAllMoney)
    TextView tvAllMoney;
    @BindView(R.id.btnOK)
    Button btnOK;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private String token;
    private String accountMoney;
    private WithdrawaAccountInfo.WithdrawaAccountInfocardinfo cardinfo;
    private int bankCardId;
    private Dialog mWeiboDialog;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void loadData() {
        try {
            Call<WithdrawaAccountInfo> call= RetrofitService.getInstance().getWithdrawaAccountInfo(token);
            call.enqueue(new Callback<WithdrawaAccountInfo>() {
                @Override
                public void onResponse(Response<WithdrawaAccountInfo> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            accountMoney=response.body().getAccountMoney();
                            cardinfo=response.body().getCardinfo();
                            Glide.with(getApplicationContext()).load(cardinfo.getBankLogo()).into(ivBankLogo);
                            bankCardId=cardinfo.getBankCardID();
                            if (bankCardId!=0){
                                tvBankName.setText(cardinfo.getBankName()+"");
                                tvBankNo.setText("尾号"+cardinfo.getCardNo().substring(cardinfo.getCardNo().length()-4,cardinfo.getCardNo().length()));
                            }
                            tvCanMoney.setText("可提现金额"+accountMoney+"元");
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }else{
                        showToast(response.body().getMes());
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setListeners() {
        llBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    readyGoForResult(BankCardActivity.class,7788);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvAllMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    etMoney.setText(accountMoney);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(WithdrawscashActivity.this, "处理中...");
                    toAddWithdrawa();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 7788:
                try {
                    String bankName = data.getExtras().getString("bankName");
                    String bankNo = data.getExtras().getString("bankNo");
                    String bankLogo = data.getExtras().getString("bankLogo");
                    bankCardId=data.getExtras().getInt("bankCardId");
                    tvBankName.setText(bankName);
                    tvBankNo.setText("尾号"+bankNo.substring(bankNo.length()-4,bankNo.length())+"("+bankName+")");
                    Glide.with(getApplicationContext()).load(bankLogo).into(ivBankLogo);
                }catch (Exception e){
                    showToast(someException());
                }
                break;
        }
    }

    private void toAddWithdrawa() {
        try {
            Call<AddWithdrawa> call=RetrofitService.getInstance().addWithdrawa(token,bankCardId,etMoney.getText()+"");
            call.enqueue(new Callback<AddWithdrawa>() {
                @Override
                public void onResponse(Response<AddWithdrawa> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            showToast("提现成功");
                            ActivityManagerUtil.getInstance().finishActivity(WithdrawscashActivity.this);
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }else{
                        showToast(response.body().getMes());
                    }
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }catch (Exception e){
            showToast(someException());
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_withdrawscash;
    }
}
