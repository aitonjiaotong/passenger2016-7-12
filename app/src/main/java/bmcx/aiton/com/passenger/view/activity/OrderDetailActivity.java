package bmcx.aiton.com.passenger.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.UILUtils;
import com.aiton.administrator.shane_library.shane.utils.VolleyListener;
import com.android.volley.VolleyError;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import bmcx.aiton.com.passenger.R;
import bmcx.aiton.com.passenger.model.OrderDetailsInfo;
import bmcx.aiton.com.passenger.utils.ApiClient;
import bmcx.aiton.com.passenger.utils.DateCommonUtils;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener
{
    private String TAG = "OrderDetailActivity";
    private int mOrder_id;
    private ImageView mIv_org_detailback;
    private TextView mTv_org_order_stage;
    private ImageView mIv_car_img;
    private TextView mTv_car_name;
    private TextView mTv_carriage_count;
    private TextView mTv_displacement;
    private TextView mTv_car_seat_count;
    private TextView mTv_dache_get_time_date;
    private TextView mTv_dache_get_time_time;
    private TextView mTv_dache_how_long;
    private TextView mTv_dache_return_time_date;
    private TextView mTv_dache_return_time_time;
    private TextView mTv_dache_jg_store_name_get;
    private TextView mTv_dache_jg_store_name_return;

    private OrderDetailsInfo mOrderDetailsInfo;
    private int mOrderStage;
    private LinearLayout mLl_for_driver;
    private TextView mTv_driver_name;
    private TextView mTv_driver_phone;
    private double mHowLongDay;
    private TextView mTv_base_rent_explain;
    private TextView mTv_base_rent_price;
    private TextView mTv_base_insurance_price;
    private TextView mTv_poundage_price;
    private TextView mTv_driver_cost_explain;
    private TextView mTv_driver_cost_price;
    private TextView mTv_beyond_mileage_explain;
    private TextView mTv_beyond_mileage_price;
    private TextView mTv_send_car_to_addr_price;
    private TextView mTv_get_car_to_addr_price;
    private TextView mTv_order_cost_price;
    private TextView mTv_rate_percentage_price;
    private TextView mTv_rate_percentage_explain;
    private TextView mTv_discountPercentage_price;
    private TextView mTv_discount_percentage_explain;
    private TextView mTv_base_total_price;
    private TextView mTv_total_price;
    private TextView mTv_org_get_car_stores_title;
    private TextView mTv_org_retrun_car_stores_title;
    private String mOrderStageStr;
    private long mZuchuDate;
    private long mReturnDate;
    private String mZucheDataStr;
    private String mZucheTimeStr;
    private String mReturnDataStr;
    private String mReturnTimeStr;
    private String mHowLongDayStr;
    private double mCarPrice;
    private String mCarPriceDetailStr;
    private double mAllCarPrice;
    private double mInsurancePrice;
    private double mPoundagePrice;
    private double mDriverPrice;
    private String mDriverPriceDetailStr;
    private double mAllDriverPrice;
    private double mAllMileagePrice;
    private String mOutMileagePriceDetailStr;
    private double mPickUpCarPrice;
    private double mSendCarPrice;
    private double mAllBasePrice;
    private double mDiscountPercentage;
    private double mAllDiscountPercentagePrice;
    private String mAllDiscountPercentagePriceStr;
    private double mRatePercentage;
    private double mAllRatePercentagePrice;
    private String mAllRatePercentagePriceStr;
    private double mOtherPrice;
    private String mOtherPriceNoteStr;
    private double mTotalPrice;
    private TextView mTv_ohter_cost_explain;
    private ScrollView mScrollView_per_order_detail;
    private LinearLayout mLl_loading_remind_progress_bar;
    private TextView mTv_btn_cancel_order;
    private TextView mTv_what_discount;
    private LinearLayout mLl_lease4other_phone;
    private TextView mTv_lease4other_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_order_detail);
        initGetIntent();
        findViewID();
        setListener();
        initData();
    }

    private void initGetIntent()
    {
        Intent intent = getIntent();
        mOrder_id = intent.getIntExtra("order_id", -1);
    }

    private void findViewID()
    {
        mScrollView_per_order_detail = (ScrollView) findViewById(R.id.scrollView_per_order_detail);
        mLl_loading_remind_progress_bar = (LinearLayout) findViewById(R.id.ll_loading_remind_progress_bar);

        mLl_lease4other_phone = (LinearLayout) findViewById(R.id.ll_lease4other_phone);
        mTv_lease4other_phone = (TextView) findViewById(R.id.tv_lease4other_phone);

        mIv_org_detailback = (ImageView) findViewById(R.id.iv_org_detailback);
        mTv_org_order_stage = (TextView) findViewById(R.id.tv_org_order_stage);
        mIv_car_img = (ImageView) findViewById(R.id.iv_car_img);
        mTv_car_name = (TextView) findViewById(R.id.tv_car_name);
        mTv_carriage_count = (TextView) findViewById(R.id.tv_carriage_count);
        mTv_displacement = (TextView) findViewById(R.id.tv_displacement);
        mTv_car_seat_count = (TextView) findViewById(R.id.tv_car_seat_count);
        mTv_dache_get_time_date = (TextView) findViewById(R.id.tv_dache_get_time_date);
        mTv_dache_get_time_time = (TextView) findViewById(R.id.tv_dache_get_time_time);
        mTv_dache_how_long = (TextView) findViewById(R.id.tv_dache_how_long);
        mTv_dache_return_time_date = (TextView) findViewById(R.id.tv_dache_return_time_date);
        mTv_dache_return_time_time = (TextView) findViewById(R.id.tv_dache_return_time_time);
        mTv_dache_jg_store_name_get = (TextView) findViewById(R.id.tv_dache_jg_store_name_get);
        mTv_dache_jg_store_name_return = (TextView) findViewById(R.id.tv_dache_jg_store_name_return);
        mTv_org_get_car_stores_title = (TextView) findViewById(R.id.tv_org_get_car_stores_title);
        mTv_org_retrun_car_stores_title = (TextView) findViewById(R.id.tv_org_retrun_car_stores_title);
        mTv_what_discount = (TextView) findViewById(R.id.tv_what_discount);

        mLl_for_driver = (LinearLayout) findViewById(R.id.ll_for_driver);
        mTv_driver_name = (TextView) findViewById(R.id.tv_driver_name);
        mTv_driver_phone = (TextView) findViewById(R.id.tv_driver_phone);

        //取消订单按钮
        mTv_btn_cancel_order = (TextView) findViewById(R.id.tv_btn_cancel_order);


        //基本租赁费
        mTv_base_rent_explain = (TextView) findViewById(R.id.tv_base_rent_explain);
        mTv_base_rent_price = (TextView) findViewById(R.id.tv_base_rent_price);
        //基本保险费
        mTv_base_insurance_price = (TextView) findViewById(R.id.tv_base_insurance_price);
        //手续费
        mTv_poundage_price = (TextView) findViewById(R.id.tv_poundage_price);
        //司机费用
        mTv_driver_cost_explain = (TextView) findViewById(R.id.tv_driver_cost_explain);
        mTv_driver_cost_price = (TextView) findViewById(R.id.tv_driver_cost_price);
        //里程费用
        mTv_beyond_mileage_explain = (TextView) findViewById(R.id.tv_beyond_mileage_explain);
        mTv_beyond_mileage_price = (TextView) findViewById(R.id.tv_beyond_mileage_price);
        //送车上门费用
        mTv_send_car_to_addr_price = (TextView) findViewById(R.id.tv_send_car_to_addr_price);
        //上门取车费用
        mTv_get_car_to_addr_price = (TextView) findViewById(R.id.tv_get_car_to_addr_price);
        //其它费用
        mTv_order_cost_price = (TextView) findViewById(R.id.tv_ohter_cost_price);
        mTv_ohter_cost_explain = (TextView) findViewById(R.id.tv_ohter_cost_explain);
        //税率价
        mTv_rate_percentage_price = (TextView) findViewById(R.id.tv_rate_percentage_price);
        mTv_rate_percentage_explain = (TextView) findViewById(R.id.tv_rate_percentage_explain);

        //优惠折扣价
        mTv_discountPercentage_price = (TextView) findViewById(R.id.tv_discountPercentage_price);
        mTv_discount_percentage_explain = (TextView) findViewById(R.id.tv_discount_percentage_explain);
        //小计
        mTv_base_total_price = (TextView) findViewById(R.id.tv_base_total_price);
        //合计
        mTv_total_price = (TextView) findViewById(R.id.tv_total_price);

    }


    private void setListener()
    {
        mIv_org_detailback.setOnClickListener(this);
        mTv_btn_cancel_order.setOnClickListener(this);
    }

    private void initData()
    {
        Map<String, String> params = new HashMap<>();
        params.put("order_id", mOrder_id + "");
        ApiClient.loadOrderDetail(OrderDetailActivity.this, params, new VolleyListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {

            }

            @Override
            public void onResponse(String s)
            {
                Log.e("OrderDetailActivity", "订单详情" + s);
                mOrderDetailsInfo = GsonUtils.parseJSON(s, OrderDetailsInfo.class);
                if (mOrderDetailsInfo != null)
                {
                    mScrollView_per_order_detail.setVisibility(View.VISIBLE);
                    mLl_loading_remind_progress_bar.setVisibility(View.GONE);
                    calculatePriceInfo();
                    initUI();
                } else
                {
                    Toast.makeText(OrderDetailActivity.this, "数据请求失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void calculatePriceInfo()
    {
        //flag:订单状态 0:进行中 1：完成 2:取消 3：等待结算(已还车) 4:预租订单 5:已结算未退压金
        mOrderStage = mOrderDetailsInfo.getOrder().getFlag();

        //判断是否有为他们叫车订单
        if (mOrderDetailsInfo.getOrder().getLeaseForOther() == 1)
        {
            mLl_lease4other_phone.setVisibility(View.VISIBLE);
            mTv_lease4other_phone.setText(mOrderDetailsInfo.getOrder().getPhoneOfUser());
        } else
        {
            mLl_lease4other_phone.setVisibility(View.GONE);
        }
        //判断订单是否符合相关优惠折扣活动
        if (mOrderDetailsInfo.getPrivilege() != null && mOrderDetailsInfo.getDiscount() != null)
        {
            mTv_what_discount.setText(mOrderDetailsInfo.getPrivilege().getName() + "--" + mOrderDetailsInfo.getDiscount().getName());
        }
        DecimalFormat df = new DecimalFormat("0.0");
        //总价
        mTotalPrice = mOrderDetailsInfo.getOrder().getPrice();
        switch (mOrderStage)
        {
            case 0:
                mOrderStageStr = "订单进行中";
                mReturnDate = mOrderDetailsInfo.getOrder().getPlanReturnDate();
                //里程费
                mAllMileagePrice = 0.0;
                mOutMileagePriceDetailStr = "行驶里程数  X " + mOrderDetailsInfo.getPlan().getFloatPrice() + "/公里";
                break;
            case 1:
                mOrderStageStr = "订单已完成";
                mReturnDate = mOrderDetailsInfo.getOrder().getHuancheDate();
                //里程费
                mAllMileagePrice = mOrderDetailsInfo.getOrder().getFloatCost();
                mOutMileagePriceDetailStr = "行驶里程数" + (mOrderDetailsInfo.getOrder().getAfterMileage() - mOrderDetailsInfo.getOrder().getBeforeMileage()) + "  X " + mOrderDetailsInfo.getPlan().getFloatPrice() + "/公里";
                mTotalPrice = mOrderDetailsInfo.getOrder().getRealPrice();
                break;
            case 2:
                mOrderStageStr = "订单已取消";
                mReturnDate = mOrderDetailsInfo.getOrder().getPlanReturnDate();
                //里程费
                mAllMileagePrice = 0.0;
                mOutMileagePriceDetailStr = "行驶里程数  X " + mOrderDetailsInfo.getPlan().getFloatPrice() + "/公里";
                break;
            case 3:
                mOrderStageStr = "订单等待结算(已还车)";
                mReturnDate = mOrderDetailsInfo.getOrder().getHuancheDate();
                //里程费
                mAllMileagePrice = mOrderDetailsInfo.getOrder().getFloatCost();
                mOutMileagePriceDetailStr = "行驶里程数" + (mOrderDetailsInfo.getOrder().getAfterMileage() - mOrderDetailsInfo.getOrder().getBeforeMileage()) + "  X " + mOrderDetailsInfo.getPlan().getFloatPrice() + "/公里";
                break;
            case 4:
                mTv_btn_cancel_order.setVisibility(View.VISIBLE);
                mOrderStageStr = "预约成功";
                mReturnDate = mOrderDetailsInfo.getOrder().getPlanReturnDate();
                //里程费
                mAllMileagePrice = 0.0;
                mOutMileagePriceDetailStr = "行驶里程数  X " + mOrderDetailsInfo.getPlan().getFloatPrice() + "/公里";
                break;
            case 5:
                mOrderStageStr = "已结算未退压金";
                mReturnDate = mOrderDetailsInfo.getOrder().getHuancheDate();
                //里程费
                mAllMileagePrice = mOrderDetailsInfo.getOrder().getFloatCost();
                mOutMileagePriceDetailStr = "行驶里程数" + (mOrderDetailsInfo.getOrder().getAfterMileage() - mOrderDetailsInfo.getOrder().getBeforeMileage()) + "  X " + mOrderDetailsInfo.getPlan().getFloatPrice() + "/公里";
                break;
            case 6:
                mOrderStageStr = "等待付款";
                mReturnDate = mOrderDetailsInfo.getOrder().getHuancheDate();
                //里程费
                mAllMileagePrice = mOrderDetailsInfo.getOrder().getFloatCost();
                mOutMileagePriceDetailStr = "行驶里程数" + (mOrderDetailsInfo.getOrder().getAfterMileage() - mOrderDetailsInfo.getOrder().getBeforeMileage()) + "  X " + mOrderDetailsInfo.getPlan().getFloatPrice() + "/公里";
                mTotalPrice = mOrderDetailsInfo.getOrder().getRealPrice();
                break;

        }
        //取车日期时间
        mZuchuDate = mOrderDetailsInfo.getOrder().getZuchuDate();
        mZucheDataStr = DateCommonUtils.getTheDateToString(mZuchuDate, DateCommonUtils.MM_DD);
        mZucheTimeStr = DateCommonUtils.getTheDateToString(mZuchuDate, DateCommonUtils.EE_HH_MM);
        //还车日期时间
        mReturnDataStr = DateCommonUtils.getTheDateToString(mReturnDate, DateCommonUtils.MM_DD);
        mReturnTimeStr = DateCommonUtils.getTheDateToString(mReturnDate, DateCommonUtils.EE_HH_MM);

        //取车时间与还车时间所计算的天数
        mHowLongDay = DateCommonUtils.getHowLongDay(mZuchuDate, mReturnDate);
        mHowLongDayStr = mHowLongDay + "天";
        //基本车辆租赁费
        mCarPrice = mOrderDetailsInfo.getPlan().getPrice();
        mCarPriceDetailStr = "￥" + mCarPrice + "/天  X " + mHowLongDay + "天";
        mAllCarPrice = mOrderDetailsInfo.getOrder().getTimePrice();
        //基本保险费
        mInsurancePrice = mOrderDetailsInfo.getPlan().getInsurance();
        //基本手续费
        mPoundagePrice = mOrderDetailsInfo.getPlan().getPoundage();
        //司机费用
        mDriverPrice = mOrderDetailsInfo.getPlan().getHasDriver();
        if (mOrderDetailsInfo.getOrder().getHasDriver() == 0)
        {
            mDriverPriceDetailStr = "￥" + mDriverPrice + "/天  X " + mHowLongDay + "天";
        } else
        {
            mDriverPriceDetailStr = "￥" + mDriverPrice + "/天";
        }
        mAllDriverPrice = mOrderDetailsInfo.getOrder().getDriverCost();
        //上门取车费用
        if (mOrderDetailsInfo.getOrder().getIsGetCarFromHome() == 0)
        {
            mPickUpCarPrice = 0.0;
        } else
        {
            mPickUpCarPrice = mOrderDetailsInfo.getPlan().getGetCarFromHome();
        }
        //送车上门费用
        if (mOrderDetailsInfo.getOrder().getIsSendCarToHome() == 0)
        {
            mSendCarPrice = 0.0;
        } else
        {
            mSendCarPrice = mOrderDetailsInfo.getPlan().getSendCarToHome();
        }
        //（车身价 X 天数）+ （司机费用 X 天数）+ （里程费用 X 浮动差价) + 送车上门费用 + 上门取车费用 = 基本总价
        if (mOrderDetailsInfo.getOrder().getHasLuo() == 1)
        {
            mAllMileagePrice = 0.0;
        }
        mAllBasePrice = Double.parseDouble(df.format(mAllCarPrice + mAllDriverPrice + mAllMileagePrice + mPickUpCarPrice + mSendCarPrice));

        //优惠折扣率
        mDiscountPercentage = mOrderDetailsInfo.getOrder().getDiscount();
        //基本总价 X 优惠折扣率 = 优惠折扣总价
        mAllDiscountPercentagePrice = mOrderDetailsInfo.getOrder().getTempPriceForDiscount();
        mAllDiscountPercentagePriceStr = "小计总价￥" + mAllBasePrice + " X 优惠折扣率" + mDiscountPercentage;
        //税率
        int hasTax = mOrderDetailsInfo.getOrder().getHasTax();
        if (hasTax == 1)
        {
            mRatePercentage = mOrderDetailsInfo.getPlan().getTax();
        } else
        {
            mRatePercentage = 1;
        }
        //优惠折扣总价 X 税率 = 税率折扣总价
        mAllRatePercentagePrice = mOrderDetailsInfo.getOrder().getTempPriceForTax();
        mAllRatePercentagePriceStr = "优惠后总价￥" + mAllDiscountPercentagePrice + " X 税率" + mRatePercentage;
        //其它费用
        mOtherPrice = mOrderDetailsInfo.getOrder().getOtherPrice();
        mOtherPriceNoteStr = mOrderDetailsInfo.getOrder().getNote();
    }

    private void initUI()
    {
        initCarInfo();
        initOrderStageAndRentTimeInfo();
        initRentCarStoreInfo();
        initDriverInfo(mOrderStage);
        initOrderPriceInfo();
    }


    private void initCarInfo()
    {
        if (mOrderDetailsInfo.getCar().getImage() != null && !"".equals(mOrderDetailsInfo.getCar().getImage()))
        {
            UILUtils.displayImageNoAnim(mOrderDetailsInfo.getCar().getImage(), mIv_car_img);
        }
        mTv_car_name.setText(mOrderDetailsInfo.getCar().getModel() + "|" + mOrderDetailsInfo.getCar().getType());
        mTv_carriage_count.setText(mOrderDetailsInfo.getCar().getBox());
        if (mOrderDetailsInfo.getCar().getZidong() == 0)
        {
            mTv_displacement.setText(mOrderDetailsInfo.getCar().getPailiang() + "自动");
        } else
        {
            mTv_displacement.setText(mOrderDetailsInfo.getCar().getPailiang() + "手动");
        }
        mTv_car_seat_count.setText("可乘坐" + mOrderDetailsInfo.getCar().getSeat() + "人");
    }

    private void initOrderStageAndRentTimeInfo()
    {
        mTv_org_order_stage.setText(mOrderStageStr);
        mTv_dache_get_time_date.setText(mZucheDataStr);
        mTv_dache_get_time_time.setText(mZucheTimeStr);
        mTv_dache_return_time_date.setText(mReturnDataStr);
        mTv_dache_return_time_time.setText(mReturnTimeStr);
        mTv_dache_how_long.setText(mHowLongDayStr);
        if (mOrderDetailsInfo.getDiscount() != null && mOrderDetailsInfo.getPrivilege() != null)
        {
            mTv_what_discount.setText(mOrderDetailsInfo.getDiscount().getName() + "--" + mOrderDetailsInfo.getPrivilege().getName());
        }
    }


    private void initRentCarStoreInfo()
    {
        if (mOrderDetailsInfo.getOrder().getIsSendCarToHome() == 0)
        {
            mTv_org_get_car_stores_title.setText("取车门店");
            mTv_dache_jg_store_name_get.setText(mOrderDetailsInfo.getGetCarStore().getName());
        } else
        {
            mTv_org_get_car_stores_title.setText("送车上门");
            mTv_dache_jg_store_name_get.setText(mOrderDetailsInfo.getOrder().getSendCarAddress());
        }
        if (mOrderDetailsInfo.getOrder().getIsGetCarFromHome() == 0)
        {
            mTv_org_retrun_car_stores_title.setText("还车门店");
            mTv_dache_jg_store_name_return.setText(mOrderDetailsInfo.getReturnStore().getName());
        } else
        {
            mTv_org_retrun_car_stores_title.setText("上门取车");
            mTv_dache_jg_store_name_return.setText(mOrderDetailsInfo.getOrder().getGetCarAddress());
        }
    }

    private void initDriverInfo(int orderStage)
    {
        if (mOrderDetailsInfo.getOrder().getHasDriver() == 0)
        {//有带司机
            mLl_for_driver.setVisibility(View.VISIBLE);
            //flag:订单状态 0:进行中 1：完成 2:取消 3：等待结算(已还车) 4:预租订单 5:已结算未退压金
            switch (orderStage)
            {
                case 0:
                    if (mOrderDetailsInfo.getDriver() != null)
                    {
                        mTv_driver_name.setText(mOrderDetailsInfo.getDriver().getName());
                        mTv_driver_phone.setText(mOrderDetailsInfo.getDriver().getPhone());
                    }
                    break;
                case 1:
                    if (mOrderDetailsInfo.getDriver() != null)
                    {
                        mTv_driver_name.setText(mOrderDetailsInfo.getDriver().getName());
                        mTv_driver_phone.setText(mOrderDetailsInfo.getDriver().getPhone());
                    }
                    break;
                case 2:
                    mTv_driver_name.setText("您已取消订单,司机分配取消中…");
                    mTv_driver_name.setTextColor(getResources().getColor(R.color.title_bar));
                    break;
                case 3:
                    if (mOrderDetailsInfo.getDriver() != null)
                    {
                        mTv_driver_name.setText(mOrderDetailsInfo.getDriver().getName());
                        mTv_driver_phone.setText(mOrderDetailsInfo.getDriver().getPhone());
                    }
                    break;
                case 4:
                    mTv_driver_name.setText("司机分配中…");
                    mTv_driver_name.setTextColor(getResources().getColor(R.color.title_bar));
                    break;
                case 5:
                    if (mOrderDetailsInfo.getDriver() != null)
                    {
                        mTv_driver_name.setText(mOrderDetailsInfo.getDriver().getName());
                        mTv_driver_phone.setText(mOrderDetailsInfo.getDriver().getPhone());
                    }
                    break;
            }
        } else
        {
            mLl_for_driver.setVisibility(View.GONE);
        }
    }

    private void initOrderPriceInfo()
    {
        //基本租赁费
        mTv_base_rent_explain.setText(mCarPriceDetailStr);
        mTv_base_rent_price.setText("￥" + mAllCarPrice);
        //基本保险费
        mTv_base_insurance_price.setText("￥" + mInsurancePrice);
        //手续费
        mTv_poundage_price.setText("￥" + mPoundagePrice);
        //司机费用
        mTv_driver_cost_explain.setText(mDriverPriceDetailStr);
        mTv_driver_cost_price.setText("￥" + mAllDriverPrice);
        //里程费用
        mTv_beyond_mileage_explain.setText(mOutMileagePriceDetailStr);
        mTv_beyond_mileage_price.setText("￥" + mAllMileagePrice);
        //送车上门费用
        mTv_send_car_to_addr_price.setText("￥" + mSendCarPrice);
        //上门取车费用
        mTv_get_car_to_addr_price.setText("￥" + mPickUpCarPrice);
        //总计
        mTv_base_total_price.setText("￥" + mAllBasePrice);
        //优惠折扣价
        mTv_discountPercentage_price.setText("￥" + mAllDiscountPercentagePrice);
        mTv_discount_percentage_explain.setText(mAllDiscountPercentagePriceStr);
        //税率价
        mTv_rate_percentage_price.setText("￥" + mAllRatePercentagePrice);
        mTv_rate_percentage_explain.setText(mAllRatePercentagePriceStr);
        //其它费用
        mTv_order_cost_price.setText("￥" + mOtherPrice);
        mTv_ohter_cost_explain.setText(mOtherPriceNoteStr);
        //合计总价
        mTv_total_price.setText("￥" + mTotalPrice);

    }

    public static double multiply(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_org_detailback:
                finish();
                break;
            case R.id.tv_btn_cancel_order:
                //向服务端请求取消订单
                new AlertDialog.Builder(OrderDetailActivity.this).setMessage("您确定要取消订单吗？").setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        cancelOrder();
                    }
                }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.ll_for_driver:
                //TODO 给司机评价
//                Intent intent = new Intent(OrderDetailActivity.this, AppraiseDriverActivity.class);
//                intent.putExtra("driver", mOrderDetailsInfo.getDriver());
//                startActivity(intent);
                break;
        }
    }

    /**
     * 向服务端取消订单
     */
    private void cancelOrder()
    {
        Map<String, String> params = new HashMap<>();
        params.put("order_id", mOrder_id + "");
        ApiClient.cancelOrder(OrderDetailActivity.this, params, new VolleyListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
            }

            @Override
            public void onResponse(String s)
            {
                if ("true".equals(s))
                {
                    Toast.makeText(OrderDetailActivity.this, "订单取消成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                {
                    Toast.makeText(OrderDetailActivity.this, "订单取消失败，请检查网络信号后重试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
