package com.ckt.cc.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Cc on 2017/7/21.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
