package com.qiyuan.gamesdk.core.ui.dialog.biz.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiyuan.gamesdk.BuildConfig;
import com.qiyuan.gamesdk.R;
import com.qiyuan.gamesdk.core.CoreManager;

public class TextInputBox extends RelativeLayout {

    private TextView tv_left_text;
    private EditText edt_input;

    public TextInputBox(Context context) {
        this(context, null);
    }

    public TextInputBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInputBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (BuildConfig.isApp) {
            LayoutInflater.from(CoreManager.getContext()).inflate(R.layout.text_input_box, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.text_input_box, this);
        }
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        tv_left_text = (TextView) findViewById(R.id.tv_left_text);
        edt_input = (EditText) findViewById(R.id.edt_input);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextInputBox, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.TextInputBox_leftText:
                    tv_left_text.setText(array.getString(attr));
                    break;
                case R.styleable.TextInputBox_leftTextColor:
                    tv_left_text.setTextColor(array.getColor(attr, Color.BLACK));
                    break;
                case R.styleable.TextInputBox_leftTextSize:
                    tv_left_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimensionPixelSize(attr, 0));
                    break;

                case R.styleable.TextInputBox_rightText:
                    edt_input.setText(array.getString(attr));
                    break;
                case R.styleable.TextInputBox_rightTextColor:
                    edt_input.setTextColor(array.getColor(attr, Color.BLACK));
                    break;
                case R.styleable.TextInputBox_rightTextSize:
                    edt_input.setTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimensionPixelSize(attr, 0));
                    break;

                case R.styleable.TextInputBox_rightHint:
                    edt_input.setHint(array.getString(attr));
                    break;
                case R.styleable.TextInputBox_rightHintColor:
                    edt_input.setHintTextColor(array.getColor(attr, Color.BLACK));
                    break;
            }
        }
        array.recycle();
    }

    /**
     * Returns the length, in characters, of the text managed by this TextView
     */
    public int length() {
        return edt_input.length();
    }

    public Editable getText() {
        return edt_input.getText();
    }

    public EditText getEdt_input() {
        return edt_input;
    }
}
