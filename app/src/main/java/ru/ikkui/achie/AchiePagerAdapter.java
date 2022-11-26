package ru.ikkui.achie;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AchiePagerAdapter extends FragmentStateAdapter {
    final int pageCount = 2;
    Context context;
    public AchiePagerAdapter(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity);
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return pageCount;
    }
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AchiesFragment();
            case 1:
                return new PlansFragment();
            default:
                return null;
        }
    }
}
