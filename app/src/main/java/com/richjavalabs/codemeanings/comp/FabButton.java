package com.richjavalabs.codemeanings.comp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;
import com.richjavalabs.codemeanings.R;

/**
 * Created by richard_lovell on 1/16/2015.
 */
public class FabButton extends ImageButton {


    public FabButton(Context context) {
        super(context);
        View.OnClickListener fabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
        this.setImageDrawable(new IconicsDrawable(context, FontAwesome.Icon.faw_upload).color(Color.WHITE).actionBarSize());
        this.setOnClickListener(fabClickListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    int fabSize = view.getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, fabSize, fabSize);
                }
            });
        } else {
            ((ImageButton) this).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }
}