package com.qurankareem.alsudais.Fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.qurankareem.alsudais.R;

public class me_quran extends androidx.appcompat.widget.AppCompatTextView  {


    AttributeSet attr;

    public me_quran(Context context) {
        super(context);
        setCustomFont(context, attr);
    }

    public me_quran(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public me_quran(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        String customFont = null;
        TypedArray a = null;
        if (attrs != null) {
            a = ctx.obtainStyledAttributes(attrs, R.styleable.jazira1);
            customFont = a.getString(R.styleable.jazira1_jazira_customFont);
        }
        if (customFont == null)
            customFont = "fonts/me_quran.ttf";
        setCustomFont(ctx, customFont);
        if (a != null) {
            a.recycle();
        }
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e("textView", "Could not get typeface", e);
            return false;
        }
        setTypeface(tf);
        return true;
    }
}
