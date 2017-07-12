/*
 * Created by intern.htnguyen on 3/22/2017.
 */
package com.htnguyen.healthy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.htnguyen.healthy.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectImageDialog extends Dialog implements View.OnClickListener {

    public static final int CAMERA_REQUEST = 1;
    public static final int GALLERY_REQUEST = 2;
    @BindView(R.id.llGallery)
    LinearLayout llGallery;
    @BindView(R.id.llCamera)
    LinearLayout llCamera;
    //Owner Activity
    private Activity mActivity;

    public SelectImageDialog(@NonNull Context context, Activity mActivity) {
        super(context);
        //Request icon
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_image);
        //Set transparent
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        this.getWindow().setAttributes(lp);
        setTitle(R.string.tittleChooseImage);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Butter Knife to find view by ID
        ButterKnife.bind(this);
        setCancelable(true);
        this.mActivity = mActivity;
        //Set on click listener
        llGallery.setOnClickListener(this);
        llCamera.setOnClickListener(this);
    }

    //OnClickListener
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llCamera:
                Intent mIntentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                // mIntentCamera.setType("image/*");
                mIntentCamera.putExtra("aspectX", 1);
                mIntentCamera.putExtra("aspectY", 1);
                mIntentCamera.putExtra("scale", true);
                mIntentCamera.putExtra("return-data", true);
                mActivity.startActivityForResult(mIntentCamera, CAMERA_REQUEST);
                this.dismiss();
                break;

            case R.id.llGallery:
                Intent mIntentGallery = new Intent(Intent.ACTION_GET_CONTENT);
                mIntentGallery.setType("image/*");
                mActivity.startActivityForResult(mIntentGallery, GALLERY_REQUEST);
                this.dismiss();
                break;
            default:
                break;
        }//End switch
    }

}
