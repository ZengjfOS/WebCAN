package com.aplex.webcan.util;

import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;

import com.aplex.webcan.fragment.LEDFragment;
import com.aplex.webcan.fragment.can.CanFragment;

public class FragmentFactory {
	private static SparseArrayCompat<Fragment> mCaches = new SparseArrayCompat<Fragment>();

    public static Fragment getFragment(int position) {
        Fragment fragment = mCaches.get(position);

        //
        if (fragment != null) {
            return fragment;
        }

        switch (position)
        {
            case 0:
                fragment = new CanFragment();
                break;
            default:
                break;
        }

        // 存储
        mCaches.put(position, fragment);

        return fragment;
    }
}
