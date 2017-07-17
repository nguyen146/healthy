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

public class CalculateBMI extends Dialog {

    @BindView(R.id.edtWeight)
    EditText weightView;
    @BindView(R.id.edtHeight)
    EditText heightView;
    @BindView(R.id.tilWeight)
    TextInputLayout txtWeight;
    @BindView(R.id.tilHeight)
    TextInputLayout txtHeight;


    public CalculateBMI(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calculate_bmi);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnOk)
    public void OnOk() {


        if (!checkRequired()) return;
        double bmi = -1;
        try {
            double weight = Double.parseDouble(weightView.getText().toString().trim());
            double height = Double.parseDouble(heightView.getText().toString().trim());
            bmi = Tools.getBMI(weight, height);
            if (bmi < 0) {
                txtWeight.setError(getContext().getString(R.string.errValue));
                txtHeight.setError(getContext().getString(R.string.errValue));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            txtWeight.setError(getContext().getString(R.string.errValue));
            txtHeight.setError(getContext().getString(R.string.errValue));
            return;
        }
        String status = Tools.getStatusBMI(getContext(), bmi);
        if (status == null) {
            txtWeight.setError(getContext().getString(R.string.errValue));
            txtHeight.setError(getContext().getString(R.string.errValue));
            return;
        }
        String bmiNew = String.format("%.2f", bmi);
        new DialogResult(getContext(), getContext().getString(R.string.your_bmi) +
                bmiNew + "\r\n(" + getContext().getString(R.string.you) + status + ")").show();
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void OnCancel() {
        dismiss();
    }


    public boolean checkRequired() {
        boolean valid = true;
        if (weightView.getText().toString().trim().length() == 0) {
            txtWeight.setError(getContext().getString(R.string.errValue));
            valid = false;
        } else {
            txtWeight.setError(null);
        }

        if (heightView.getText().toString().trim().length() == 0) {
            txtHeight.setError(getContext().getString(R.string.errValue));
            valid = false;
        } else {
            txtHeight.setError(null);
        }

        return valid;
    }
}
