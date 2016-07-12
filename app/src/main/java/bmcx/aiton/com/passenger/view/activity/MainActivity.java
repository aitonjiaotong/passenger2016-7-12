package bmcx.aiton.com.passenger.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.upgrade.UpgradeUtils;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.VolleyListener;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import bmcx.aiton.com.passenger.R;
import bmcx.aiton.com.passenger.constant.Constant;
import bmcx.aiton.com.passenger.model.UserLoginInfo;
import bmcx.aiton.com.passenger.utils.ApiClient;
import bmcx.aiton.com.passenger.utils.LoginState;
import bmcx.aiton.com.passenger.utils.VersionAndServerIsCanUse;
import bmcx.aiton.com.passenger.view.frgment.MainFragment;
import bmcx.aiton.com.passenger.view.frgment.MineFragment;
import bmcx.aiton.com.passenger.view.frgment.OrderFragment;

public class MainActivity extends AppCompatActivity
{

    private String[] tabsItem;
    private Class[] fragment = new Class[]{MainFragment.class, OrderFragment.class, MineFragment.class};
    private int[] imgRes = new int[]{R.drawable.shouye_selector, R.drawable.dingdan_selector, R.drawable.ic_home_me_selector};
    private FragmentTabHost mTabHost;
    private int mBackKey;
    private String mId;
    private String mDeviceId;
    private LoginState mLoginState;
    private long currentTime = 0;
    private String isConnection;
    private String mConfirm;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isConnection = getResources().getString(R.string.no_connection);
        tabsItem = getResources().getStringArray(R.array.tab);
        mConfirm = getResources().getString(R.string.confirm);
        initUI();
        initLoginState();
        checkVersionAndServerIsCanUse();
        getIntentForSetCurrentTab();
    }

    private void initUI()
    {
        mTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtab);
        for (int i = 0; i < imgRes.length; i++)
        {
            View inflate = getLayoutInflater().inflate(R.layout.tabs_item, null);
            TextView tabs_text = (TextView) inflate.findViewById(R.id.tabs_text);
            ImageView tabs_img = (ImageView) inflate.findViewById(R.id.tabs_img);
            tabs_text.setText(tabsItem[i]);
            tabs_img.setImageResource(imgRes[i]);
            mTabHost.addTab(mTabHost.newTabSpec("" + i).setIndicator(inflate), fragment[i], null);
        }
    }

    private void initLoginState()
    {
        mLoginState = LoginState.getInstance(MainActivity.this);
        UserLoginInfo loginInfo = mLoginState.getLoginInfo();
        mId = loginInfo.getUserId();
        mDeviceId = loginInfo.getDeviceId();
    }

    private void checkVersionAndServerIsCanUse()
    {
        ApiClient.getServerIsLive(MainActivity.this, new VolleyListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                if (isConnection.equals(volleyError.getMessage()))
                {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.not_connect_net), Toast.LENGTH_SHORT).show();
                } else
                {
                    setDialogCheck(getResources().getString(R.string.server_is_upgrading), mConfirm);
                }
            }

            @Override
            public void onResponse(String s)
            {
                VersionAndServerIsCanUse versionAndHouTaiIsCanUse = GsonUtils.parseJSON(s, VersionAndServerIsCanUse.class);
                int ableVersion = versionAndHouTaiIsCanUse.getAbleVersion();
                if (versionAndHouTaiIsCanUse.isLive())
                {
                    if (Constant.ABLEVERSION < ableVersion)
                    {
                        setDialogCheck(getResources().getString(R.string.able_version_unused), mConfirm);
                    } else
                    {
                        checkUpGrade();
                        //检查是否在其他设备上登录
                        checkIsLoginOnOtherDevice();
                    }
                } else
                {
                    setDialogCheck(versionAndHouTaiIsCanUse.getMessage(), mConfirm);
                }
            }
        });
    }


    private void getIntentForSetCurrentTab()
    {
        Intent intent = getIntent();
        mBackKey = intent.getIntExtra(Constant.IntentKey.BACK_TO_ORDER_LIST_KEY, -1);
        if (-1 != mBackKey)
        {
            switch (mBackKey)
            {
                case Constant.IntentKey.ORG_BACK_INT_VALUE:
                    //机构用车返回的当前【订单列表默认为机构用车，无需设置二级Tab】
                    break;
            }
            mTabHost.setCurrentTab(1);
        }
    }

    private void checkIsLoginOnOtherDevice()
    {
        if (mLoginState.isLogin())
        {
            Map<String, String> map = new HashMap<>();
            map.put("account_id", mId);
            ApiClient.checkIsLoginOnOtherDevice(MainActivity.this, map, new VolleyListener()
            {
                @Override
                public void onErrorResponse(VolleyError volleyError)
                {
                    if (isConnection.equals(volleyError.getMessage()))
                    {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.not_connect_net), Toast.LENGTH_SHORT).show();
                    } else
                    {
                        setDialogCheck(getResources().getString(R.string.server_is_upgrading), mConfirm);
                    }
                }

                @Override
                public void onResponse(String s)
                {
                    if (!s.equals(mDeviceId))
                    {
                        setUnLoginDialog(getResources().getString(R.string.account_abnormal), mConfirm);
                    }
                }
            });
        }
    }

    private void setUnLoginDialog(String messageTxt, String iSeeTxt)
    {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        TextView message = (TextView) commit_dialog.findViewById(R.id.message);
        Button ISee = (Button) commit_dialog.findViewById(R.id.ISee);
        message.setText(messageTxt);
        ISee.setText(iSeeTxt);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        mDialog = builder.setView(commit_dialog)
                .create();
        mDialog.setCancelable(false);
        mDialog.show();
        commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                //清除用户登录信息
                mLoginState.logout(MainActivity.this);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SmsLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkUpGrade()
    {
        UpgradeUtils.checkUpgrade(MainActivity.this, Constant.URL.UP_GRADE);
    }

    //dialog提示
    private void setDialogCheck(String messageTxt, String iSeeTxt)
    {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        TextView message = (TextView) commit_dialog.findViewById(R.id.message);
        Button ISee = (Button) commit_dialog.findViewById(R.id.ISee);
        message.setText(messageTxt);
        ISee.setText(iSeeTxt);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.setView(commit_dialog).create();
        dialog.setCancelable(false);
        dialog.show();
        commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                animFromBigToSmallOUT();
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        if ((System.currentTimeMillis() - currentTime) > 1000)
        {
            Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.double_click_to_exit), Toast.LENGTH_SHORT);
            toast.show();
            currentTime = System.currentTimeMillis();
        } else
        {
            finish();
            animFromBigToSmallOUT();
        }
    }

    private void animFromBigToSmallOUT()
    {
        overridePendingTransition(R.anim.fade_in, R.anim.big_to_small_fade_out);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing())
        {
            mDialog.dismiss();
        }
    }
}
