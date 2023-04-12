package com.rovaind.academy.Controllers;

import android.app.ProgressDialog;

import androidx.fragment.app.Fragment;



/**
 * Created by Mohamed El Sayed
 */

public abstract class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading....");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }




    // [END write_fan_out]
}
