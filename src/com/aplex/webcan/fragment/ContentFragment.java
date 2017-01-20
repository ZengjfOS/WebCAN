package com.aplex.webcan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aplex.webcan.R;
import com.aplex.webcan.bean.CmdBean;
import com.aplex.webcan.bean.Constant;
import com.aplex.webcan.util.FragmentFactory;
import com.aplex.webcan.viewpagerindicator.TabPageIndicator;

public class ContentFragment extends Fragment{
	private LinearLayout mRootView;
	private TabPageIndicator mTabPageindicator;
	private ViewPager mViewPager;
	private String[] mViewPagerTabsData;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initView(inflater);
		initLisetner();
		initData();
		
		return mRootView;
	}

	private void initLisetner() {
		mViewPagerTabsData = getResources().getStringArray(R.array.viewpager_tabs);
	}

	private void initData() {
		FragmentPagerAdapter adapter = new TestAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        
        mTabPageindicator.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
        mTabPageindicator.setViewPager(mViewPager);
	}

	private void initView(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_content, null);
		mTabPageindicator = (TabPageIndicator) mRootView.findViewById(R.id.Tabs);
		mViewPager = (ViewPager) mRootView.findViewById(R.id.main_vp);
	}

	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private CmdBean inflateCmdBean(int position) {
        CmdBean cmdBean = new CmdBean();
        switch (position) {
            case 0:
                cmdBean.CmdType = Constant.BUZZ_TYPE;
                cmdBean.isScoll = Constant.IS_SCROLL;
                break;

            case 1:
                cmdBean.CmdType = Constant.EEPROM_TYPE;
                cmdBean.isScoll = Constant.IS_SCROLL;
                break;
            default:
                break;
        }
        return cmdBean;
    }

    private class TestAdapter extends FragmentPagerAdapter {
        public TestAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return mViewPagerTabsData.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mViewPagerTabsData[position % mViewPagerTabsData.length];
        }
    }
}
