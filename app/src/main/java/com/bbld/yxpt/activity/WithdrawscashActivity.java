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
        Call<WithdrawaAccountInfo> call= RetrofitService.getInstance().getWithdrawaAccountInfo(token);
        call.enqueue(new Callback<WithdrawaAccountInfo>() {
            @Override
            public void onResponse(Response<WithdrawaAccountInfo> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    accountMoney=response.body().getAccountMoney();
                    cardinfo=response.body().getCardinfo();
                    Glide.with(getApplicationContext()).load(cardinfo.getBankLogo()).into(ivBankLogo);
                    bankCardId=cardinfo.getBankCardID();
                    if (bankCardId!=0){
                        tvBankName.setText(cardinfo.getBankName()+"");
                        tvBankNo.setText("尾号"+cardinfo.getCardNo().substring(cardinfo.getCardNo().length()-4,cardinfo.getCardNo().length())+"("+cardinfo.getBankName()+")");
                    }
                    tvCanMoney.setText("可提现金额"+accountMoney+"元");
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
        llBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(BankCardActivity.class,7788);
            }
        });
        tvAllMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMoney.setText(accountMoney);
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(WithdrawscashActivity.this, "处理中...");
                toAddWithdrawa();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 7788:
                String bankName = data.getExtras().getString("bankName");
                String bankNo = data.getExtras().getString("bankNo");
                String bankLogo = data.getExtras().getString("bankLogo");
                bankCardId=data.getExtras().getInt("bankCardId");
                tvBankName.setText(bankName);
                tvBankNo.setText("尾号"+bankNo.substring(bankNo.length()-4,bankNo.length())+"("+bankName+")");
                Glide.with(getApplicationContext()).load(bankLogo).into(ivBankLogo);
                break;
        }
    }

    private void toAddWithdrawa() {
        Call<AddWithdrawa> call=RetrofitService.getInstance().addWithdrawa(token,bankCardId,etMoney.getText()+"");
        call.enqueue(new Callback<AddWithdrawa>() {
            @Override
            public void onResponse(Response<AddWithdrawa> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    showToast("提现成功");
                    ActivityManagerUtil.getInstance().finishActivity(WithdrawscashActivity.this);
                }else{
                    showToast(response.body().getMes());
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_withdrawscash;
    }
}
