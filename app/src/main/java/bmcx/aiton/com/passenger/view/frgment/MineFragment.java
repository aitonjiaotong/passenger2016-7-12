package bmcx.aiton.com.passenger.view.frgment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.FileUtils;
import com.aiton.administrator.shane_library.shane.utils.UILUtils;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IWxCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import bmcx.aiton.com.passenger.R;
import bmcx.aiton.com.passenger.model.UserLoginInfo;
import bmcx.aiton.com.passenger.utils.LoginState;
import bmcx.aiton.com.passenger.view.activity.LawProvisionsActivity;
import bmcx.aiton.com.passenger.view.activity.MineInfoActivity;
import bmcx.aiton.com.passenger.view.activity.SmsLoginActivity;
import bmcx.aiton.com.passenger.view.activity.UseGuideActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener
{

    private String mPhoneNum;
    private boolean isLogined;
    private TextView mTextView_unLogin;
    private RelativeLayout mRela_login;
    private TextView mTextView_login;
    private RelativeLayout mLoginED;
    private TextView mTextView_feedBackCount;
    private boolean isOpenFeedBack = false;
    private RelativeLayout mRl_mine_evething_clear_diskcache;
    private TextView mTv_diskcache_num;
    private View mInflate;
    private RelativeLayout mRl_mine_evething_call_phone;
    private IWxCallback callback = new IWxCallback()
    {
        @Override
        public void onSuccess(final Object... objects)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    int count = (int) objects[0];
                    if (isOpenFeedBack)
                    {
                        count = 0;
                    }
                    if (count == 0)
                    {
                        mTextView_feedBackCount.setText("");
                    } else
                    {
                        mTextView_feedBackCount.setText("+" + count);
                    }
                    isOpenFeedBack = false;
                }
            });
        }

        @Override
        public void onError(int i, String s)
        {

        }

        @Override
        public void onProgress(int i)
        {
        }
    };
    private ImageView mIv_ueser_avatar;
    private LoginState mLoginState;
    private String mImageUrl;
    private TextView mTv_version_info;

    public MineFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        if (mInflate == null)
        {
            mInflate = inflater.inflate(R.layout.fragment_more2, null);
            findID();
            initUI();
            setListener();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mInflate.getParent();
        if (parent != null)
        {
            parent.removeView(mInflate);
        }
        return mInflate;
    }

    private void setListener()
    {
        mLoginED.setOnClickListener(this);
        mInflate.findViewById(R.id.rl_mine_evething_soft_info).setOnClickListener(this);
        mInflate.findViewById(R.id.rl_mine_evething_law_provisions).setOnClickListener(this);
        mInflate.findViewById(R.id.rela_feedback).setOnClickListener(this);
        mRl_mine_evething_clear_diskcache.setOnClickListener(this);
        mRl_mine_evething_call_phone.setOnClickListener(this);
    }

    private void findID()
    {
        mTextView_unLogin = (TextView) mInflate.findViewById(R.id.textView_unLogin);
        mRela_login = (RelativeLayout) mInflate.findViewById(R.id.rela_login);
        mTextView_login = (TextView) mInflate.findViewById(R.id.textView_login);
        mLoginED = (RelativeLayout) mInflate.findViewById(R.id.loginED);
        mTextView_feedBackCount = (TextView) mInflate.findViewById(R.id.textView_feedBackCount);
        mRl_mine_evething_clear_diskcache = (RelativeLayout) mInflate.findViewById(R.id.rl_mine_evething_clear_diskcache);
        mTv_diskcache_num = (TextView) mInflate.findViewById(R.id.tv_diskcache_num);
        mRl_mine_evething_call_phone = (RelativeLayout) mInflate.findViewById(R.id.rl_mine_evething_call_phone);
        mIv_ueser_avatar = (ImageView) mInflate.findViewById(R.id.iv_ueser_avatar);
        mTv_version_info = (TextView) mInflate.findViewById(R.id.tv_version_info);
    }

    private void initUI()
    {
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try
        {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        mTv_version_info.setText("V" + version);
        mTv_diskcache_num.setText(getSize());
        if (isLogined && mImageUrl != null && !"".equals(mImageUrl))
        {
            UILUtils.displayImageNoAnim(mImageUrl, mIv_ueser_avatar, false);
        }
    }


    /**
     * -------------获取缓存大小-----------------
     */
    private String getSize()
    {
        File cacheDir = getActivity().getExternalCacheDir();
        long fileLen = FileUtils.getFileLen(cacheDir);
        String size = FileUtils.size(fileLen);

        return size;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        initLogin();
        if (isLogined && mImageUrl != null && !"".equals(mImageUrl))
        {
            UILUtils.displayImageNoAnim(mImageUrl, mIv_ueser_avatar, false);
        } else
        {
            mIv_ueser_avatar.setImageResource(R.mipmap.a02);
        }
    }

    private void initLogin()
    {
        mLoginState = LoginState.getInstance(getActivity());
        isLogined = mLoginState.isLogin();
        UserLoginInfo loginInfo = mLoginState.getLoginInfo();
        mPhoneNum = loginInfo.getPhoneNum();
        mImageUrl = loginInfo.getImage();

        if (isLogined)
        {
            mTextView_unLogin.setVisibility(View.INVISIBLE);//未登录状态下显示(登录/注册)
            mRela_login.setVisibility(View.VISIBLE);
            mTextView_login.setVisibility(View.VISIBLE);
            String phoneNum = mPhoneNum.substring(0, mPhoneNum.length() - (mPhoneNum.substring(3)).length()) + "****" + mPhoneNum.substring(7);
            mTextView_login.setText(phoneNum);

        } else
        {
            mTextView_unLogin.setVisibility(View.VISIBLE);
            mRela_login.setVisibility(View.INVISIBLE);
            mTextView_login.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent();
        switch (v.getId())
        {
            case R.id.loginED:
                if (isLogined)
                {
                    intent.setClass(getActivity(), MineInfoActivity.class);
                    startActivity(intent);
                } else
                {
                    intent.setClass(getActivity(), SmsLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_mine_evething_soft_info:
                intent.setClass(getActivity(), UseGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_evething_law_provisions:
                intent.setClass(getActivity(), LawProvisionsActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_feedback:
                //第二个参数是appkey，就是百川应用创建时候的appkey
                FeedbackAPI.initAnnoy(getActivity().getApplication(), "23348731");
                Map<String, String> map = new HashMap<>();
                map.put("bgColor", "#ff7d27");
                map.put("themeColor", "#ff7d27");
                map.put("enableAudio", "1");
                FeedbackAPI.setUICustomInfo(map);
                //可选功能，第二个参数是当前登录的openim账号，如果是匿名账号方式使用，则可以传空的
                FeedbackAPI.getFeedbackUnreadCount(getActivity().getApplication(), null, callback);
                //6.0以上系统需要判断权限
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 2);
                } else
                {
                    //如果发生错误，请查看logcat日志
                    FeedbackAPI.openFeedbackActivity(getActivity());
                    isOpenFeedBack = true;
                }
                break;
            case R.id.rl_mine_evething_clear_diskcache:
                ImageLoader.getInstance().clearDiskCache();
                mTv_diskcache_num.setText(getSize());
                Toast.makeText(getActivity(), "缓存清除完毕!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_mine_evething_call_phone:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 0);
                }
                Intent intent_call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4000593330"));
                startActivity(intent_call);
                break;
        }
    }
}
