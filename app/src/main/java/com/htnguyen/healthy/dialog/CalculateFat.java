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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CalculateFat extends Dialog{

    @BindView(R.id.edtWeight)
    EditText weightView;
    @BindView(R.id.edtHeight)
    EditText heightView;
    @BindView(R.id.tilWeight)
    TextInputLayout txtWeight;
    @BindView(R.id.tilHeight)
    TextInputLayout txtHeight;

    @BindView(R.id.txt_title)
    TextInputLayout txtTitle;
    @BindView(R.id.edt_title)
    EditText titleView;
    @BindView(R.id.gender)
    RadioGroup genderView;
    @BindView(R.id.male)
    RadioButton maleView;


    public CalculateFat(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calculate_fat);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(true);
        maleView.setChecked(true);
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

        int checkGender = genderView.getCheckedRadioButtonId();
        int gender;
        switch (checkGender) {
            case R.id.male:
                gender = 0;
                break;
            case R.id.female:
                gender = 1;
                break;
            default:
                gender = 0;
                break;
        }
        int age = 0;
        try {
            age = Integer.parseInt(titleView.getText().toString().trim());
            if(age<0 || age >130){
                txtTitle.setError(getContext().getString(R.string.errage));
                return;
            }
        }catch (Exception e){
            txtTitle.setError(getContext().getString(R.string.errage));
            return;
        }

        double fatPercent= Tools.fatInBody(bmi,age, gender);
        String fatNew = String.format("%.2f", fatPercent);
        fatNew = fatNew.concat(" "+ getContext().getString(R.string.percent));

        new DialogResult(getContext(), getContext().getString(R.string.fat) +
                ": " + fatNew).show();

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
        if(titleView.getText().toString().trim().length() == 0){
            txtTitle.setError(getContext().getString(R.string.errrequired));
            valid = false;
        }else {
            txtTitle.setError(null);
        }
        return valid;
    }
}
