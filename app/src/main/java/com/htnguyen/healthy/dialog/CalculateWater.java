package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculateWater extends Dialog {
    @BindView(R.id.txt_title)
    TextInputLayout txtTitle;
    @BindView(R.id.edt_title)
    EditText titleView;

    public CalculateWater(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calculate_water);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnOk)
    public void OnOk() {
        if (!checkRequired()) return;
        double water = -1;
        try {
            double tepm = Double.parseDouble(titleView.getText().toString().trim());
            water = Tools.getWaterNeeded(tepm);
            if (water < 0) {
                txtTitle.setError(getContext().getString(R.string.errvalue));
                return;
            }
        } catch (Exception e) {
            txtTitle.setError(getContext().getString(R.string.errvalue));
            return;
        }

        String maxWater = String.format("%.2f", water);
        new DialogResult(getContext(), getContext().
                getString(R.string.your_water) + " " + maxWater + " " + getContext().getString(R.string.litre)).
                show();
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void OnCancel() {
        dismiss();
    }

    private boolean checkRequired() {
        if (titleView.getText().toString().trim().length() == 0) {
            txtTitle.setError(getContext().getString(R.string.errrequired));
            return false;
        } else {
            txtTitle.setError(null);
        }

        return true;
    }
}
