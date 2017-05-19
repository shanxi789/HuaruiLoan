package com.mzth.huaruiloan.ui.activity.sub;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.common.MainApplication;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.ui.adapter.sub.FragmentAdapter;
import com.mzth.huaruiloan.ui.fragment.base.BaseFragment;
import com.mzth.huaruiloan.ui.fragment.sub.HomeFragment;
import com.mzth.huaruiloan.ui.fragment.sub.UserFragment;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;
import com.mzth.huaruiloan.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 * 主页
 */

public class HomeActivity extends BaseBussActivity implements BaseFragment.FragmentCallBack{
    private TextView tv_home,tv_user;
    private Fragment homeFrag, userFrag;
    private List<Fragment> fragments;
    private ViewPager vp_main;
    private FragmentAdapter adapter;
    private long mExitTime;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = HomeActivity.this;
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void initView() {
        super.initView();
        //主页
        tv_home = (TextView) findViewById(R.id.tv_home);
        //我的
        tv_user = (TextView) findViewById(R.id.tv_user);
        //主页viewpager
        vp_main = (ViewPager) findViewById(R.id.vp_main);
    }
    //重置
    private void resetView() {
        //让主页显示
        tv_home.setTextColor(getResources().getColor(R.color.black));
        tv_user.setTextColor(getResources().getColor(R.color.black));
        Drawable drawable = getResources().getDrawable(R.mipmap.footer01);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_home.setCompoundDrawables(null,drawable,null,null);
        //让我的显示
        Drawable drawable2 = getResources().getDrawable(R.mipmap.footer02);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
        tv_user.setCompoundDrawables(null,drawable2,null,null);
    }
    @Override
    protected void initData() {
        super.initData();
        tv_home.setTextColor(getResources().getColor(R.color.red));//更改字体颜色
        //默认首页图标变色
        Drawable drawable = getResources().getDrawable(R.mipmap.footer01_on);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_home.setCompoundDrawables(null,drawable,null,null);

        FragmentManager fm = this.getSupportFragmentManager();
        fragments = new ArrayList<Fragment>();
        homeFrag = new HomeFragment(_context,R.layout.fragment_home);
        userFrag = new UserFragment(_context,R.layout.fragment_user);
        fragments.add(homeFrag);
        fragments.add(userFrag);
        adapter=new FragmentAdapter(fm,_context,fragments);
        vp_main.setAdapter(adapter);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        vp_main.addOnPageChangeListener(mOnPageChangeListener);
        tv_home.setOnClickListener(myonclick);
        tv_user.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_home://首页
                    resetView();
                    //让主页显示
                    tv_home.setTextColor(getResources().getColor(R.color.red));
                    Drawable drawable = getResources().getDrawable(R.mipmap.footer01_on);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_home.setCompoundDrawables(null,drawable,null,null);
                    vp_main.setCurrentItem(0);
                    break;
                case R.id.tv_user://我的
                    resetView();
                    //让主页显示
                    tv_user.setTextColor(getResources().getColor(R.color.red));
                    Drawable drawable2 = getResources().getDrawable(R.mipmap.footer02_on);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    tv_user.setCompoundDrawables(null,drawable2,null,null);
                    vp_main.setCurrentItem(1);
                    break;
            }
        }
    };
    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    resetView();
                    tv_home.setTextColor(getResources().getColor(R.color.red));

                    //让主页显示
                    Drawable drawable = getResources().getDrawable(R.mipmap.footer01_on);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_home.setCompoundDrawables(null,drawable,null,null);
                    vp_main.setCurrentItem(0);
                    break;
                case 1:
                    resetView();
                    tv_user.setTextColor(getResources().getColor(R.color.red));
                    //让主页显示
                    Drawable drawable2 = getResources().getDrawable(R.mipmap.footer02_on);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    tv_user.setCompoundDrawables(null,drawable2,null,null);
                    vp_main.setCurrentItem(1);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    public void setResult(Object... param) {

    }
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.showShort(this, "再按一次退出登录");
            mExitTime = System.currentTimeMillis();
        } else {
            MainApplication.closeActivity();
        }
    }
}
