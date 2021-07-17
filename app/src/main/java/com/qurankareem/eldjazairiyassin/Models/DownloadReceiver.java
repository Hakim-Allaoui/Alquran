package com.qurankareem.eldjazairiyassin.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.qurankareem.eldjazairiyassin.R;

public class DownloadReceiver extends ResultReceiver {

    private ProgressDialog dialog;
    private Context mContext;
    public DownloadReceiver(Context mContext,ProgressDialog dialog, Handler handler) {
        super(handler);
        this.dialog = dialog;
        this.mContext = mContext;

    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        super.onReceiveResult(resultCode, resultData);

        if (resultCode == DownloadService.UPDATE_PROGRESS) {

            int progress = resultData.getInt("progress"); //get the progress
            dialog.setProgress(progress);

            if (progress == 100) {
                dialog.dismiss();
                Toast.makeText(mContext, mContext.getString(R.string.seccus_downlaod), Toast.LENGTH_SHORT).show();
                showMessage(mContext.getString(R.string.derectry),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogd, int which) {
                        dialogd.dismiss();
                    }
                });
            }
        }
    }

    private void showMessage(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton(mContext.getString(R.string.yea), okListener)
                .create()
                .show();
    }

}
