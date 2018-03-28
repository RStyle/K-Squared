package de.savrasov.kaysquared;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by Yannick on 25.01.2018.
 */

public class MyInputText extends android.support.v7.widget.AppCompatEditText{

    public MyInputText(Context context) {
        super(context);
        createTF("Input");
    }

    public MyInputText(Context context, CharSequence sequence){
        super(context);
        createTF(sequence);
    }

    public MyInputText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyInputText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void createTF(CharSequence sequence){
        setTypeface(null, Typeface.ITALIC);
        setHint(sequence);
        setPadding(24, 8, 8 , 16);
    }

}