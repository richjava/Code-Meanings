package com.richjavalabs.codemeanings.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.richjavalabs.backend.codeItemApi.model.CodeItem;
import com.richjavalabs.codemeanings.MainActivity;
import com.richjavalabs.codemeanings.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard_lovell on 1/16/2015.
 */
public class CodeItemAdapter extends RecyclerView.Adapter<CodeItemAdapter.ViewHolder>{

    private List<CodeItem> codeItems;
    private int rowLayout;
    private MainActivity activity;

    public CodeItemAdapter(List<CodeItem> codeItems, int rowLayout, MainActivity activity) {
        this.codeItems = codeItems;
        this.rowLayout = rowLayout;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final CodeItem codeItem = codeItems.get(i);
        viewHolder.codeText.setText(Html.fromHtml(codeItem.getCode()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.meaningText.setText(activity.getResources().getString(R.string.gt)+" "+codeItem.getMeaning());
                //activity.animateActivity(codeItem, viewHolder.image);
            }
        });
    }

    public void clearCodeItems() {
        int size = this.codeItems.size();
        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                codeItems.remove(0);
//            }
            this.codeItems = new ArrayList<CodeItem>();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addCodeItems(List<CodeItem> codeItems) {
        this.codeItems.addAll(codeItems);
        this.notifyItemRangeInserted(0, codeItems.size() - 1);
    }

    @Override
    public int getItemCount() {
        return codeItems == null ? 0 : codeItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView codeText;
        public TextView meaningText;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            codeText = (TextView) itemView.findViewById(R.id.text_code);
            meaningText = (TextView)itemView.findViewById(R.id.text_meaning);
            //image = (ImageView) itemView.findViewById(R.id.userImage);
        }

    }
}
