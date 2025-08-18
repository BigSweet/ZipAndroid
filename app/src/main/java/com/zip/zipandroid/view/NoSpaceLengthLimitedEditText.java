package com.zip.zipandroid.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class NoSpaceLengthLimitedEditText extends androidx.appcompat.widget.AppCompatEditText {
    private int maxLength;

    public NoSpaceLengthLimitedEditText(Context context) {
        super(context);
        init();
    }

    public NoSpaceLengthLimitedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoSpaceLengthLimitedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 默认最大长度
        this.maxLength = 10;

        // 监听文本变化
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 不需要实现
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 不需要实现
            }

            @Override
            public void afterTextChanged(Editable s) {
                String filteredText = s.toString().replace(" ", "");
                if (!s.toString().equals(filteredText)) {
                    setText(filteredText);
                    setSelection(filteredText.length());
                }

                if (filteredText.length() > maxLength) {
                    setText(filteredText.substring(0, maxLength));
                    setSelection(maxLength);
                }
            }
        });
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean consumed = super.onTextContextMenuItem(id);
        if (id == android.R.id.paste) {
            String text = getText().toString().replace(" ", "");
            if (text.length() > maxLength) {
                text = text.substring(0, maxLength);
            }
            setText(text);
            setSelection(text.length());
        }
        return consumed;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
