package com.album.jayson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.album.jayson.libraryfragments.AllPhotosFragment;
import com.album.jayson.libraryfragments.DaysFragment;
import com.album.jayson.libraryfragments.MonthFragment;
import com.album.jayson.libraryfragments.YearFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LibraryFragment extends Fragment {

    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        BottomNavigationView bottomNavigationViewLibrary = view.findViewById(R.id.library_bottom_navigation);
        bottomNavigationViewLibrary.setBackgroundResource(R.drawable.bottom_nav_background);

        bottomNavigationViewLibrary.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.library_nav_year) {
                fragment = new YearFragment();
            } else if (itemId == R.id.library_nav_month) {
                fragment = new MonthFragment();
            } else if (itemId == R.id.library_nav_days) {
                fragment = new DaysFragment();
            } else if (itemId == R.id.library_nav_all_photos) {
                fragment = new AllPhotosFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }

            return false;
        });

        bottomNavigationViewLibrary.setSelectedItemId(R.id.library_nav_all_photos);

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.library_fragment_container, fragment);
        transaction.commit();
    }
}

