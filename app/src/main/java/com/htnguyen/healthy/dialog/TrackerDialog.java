package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Category;
import com.htnguyen.healthy.model.Items;
import com.htnguyen.healthy.util.Tools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrackerDialog extends Dialog{

    @BindView(R.id.edtValue)
    EditText valueView;
    @BindView(R.id.titValue)
    TextInputLayout txtValue;
    private Category category;
    private DatabaseReference mCategory;
    public TrackerDialog(@NonNull Context context, Category category, DatabaseReference mCategory) {
        super(context);
        this.category = category;
        this.mCategory = mCategory;
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = this.getWindow();
//        lp.copyFrom(window.getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        this.getWindow().setAttributes(lp);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_tracker_confim);
        ButterKnife.bind(this, getWindow().getDecorView());
        if(category.getNameValue() != null){
            CharSequence cs = category.getNameValue();
            valueView.setHint(cs);
        }
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnOk)
    public void onOk(){
        int value = checkValue();
        if (value ==-1) {
            txtValue.setError(getContext().getString(R.string.errValue));
            return;
        }else {
            txtValue.setError(null);
        }

        String description;
        List<Items> itemCate = new ArrayList<>();
        if(category.getItemsList()!=null)
            itemCate = category.getItemsList();

        if(itemCate.size()>=7){
            itemCate.remove(0);
        }
        if (itemCate.size()>0){
            description = String.valueOf(value - itemCate.get(itemCate.size()-1).getValue());
        }else {
            description = "";
        }
        Items items = new Items(Tools.getCurrentDate(),description,value);
        itemCate.add(items);
        final LoadingDialog loadingDialog = new LoadingDialog(getContext(),getContext().getString(R.string.progressing));
        loadingDialog.show();
        mCategory.child(category.getCateId()).child("itemsList").setValue(itemCate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!= null){
                    Toast.makeText(getContext(), getContext().getString(R.string.errsaved),
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), getContext().getString(R.string.saved),
                        Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        dismiss();
    }

    private void editTracker(Category category, int value){
        int tracker = 0;
        List<Items> itemCate = category.getItemsList();
        if(itemCate.size()>=5){
            itemCate.remove(0);
        }
        if (itemCate.size()>0){
            tracker = value - itemCate.get(itemCate.size()-1).getValue();
        }
        Items items = new Items(Tools.getCurrentDate(),String.valueOf(tracker), value);
        itemCate.add(items);
        final Category category1 = new Category(Tools.getCurrentDate(),"Weight","Can nang", itemCate);
        mCategory.push().setValue(category1, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!= null){
                    Toast.makeText(getContext(), getContext().getString(R.string.errsaved),
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), getContext().getString(R.string.saved),
                        Toast.LENGTH_SHORT).show();
//                loadingDialog.dismiss();
            }
        });
        dismiss();
    }

    private int checkValue(){
        String value = valueView.getText().toString().trim();
        if (value.length()==0) return -1;
        int trackerValue = -1;
        try {
            trackerValue = Integer.parseInt(value);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return trackerValue;
    }
}
