package com.brian.speechtherapistapp.view.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.util.ColorUtils;
import com.brian.speechtherapistapp.util.Const;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private static final String LOG_TAG = WordAdapter.class.getSimpleName();
    private WordAdapterClickListener mOnClickListener;
    private static int viewHolderCount;
    private int mNumberItems;


    public interface WordAdapterClickListener {
        void onListItemClicked(String itemClicked);
    }


    public WordAdapter(int numberOfItems, WordAdapterClickListener listener) {
        mOnClickListener = listener;
        mNumberItems = numberOfItems;
        viewHolderCount = 0;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.word_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        WordViewHolder viewHolder = new WordViewHolder(view);

        int backgroundColorForViewHolder = ColorUtils
                .getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);

        viewHolderCount++;
        Log.d(LOG_TAG, "onCreateViewHolder: number of ViewHolders created: " + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        Log.d(LOG_TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_item_number)
        TextView listItemNumberView;

        @BindView(R.id.tv_view_holder_instance)
        TextView viewHolderIndex;


        public WordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            String wordClicked = Const.CORRECT_WORDS_LIST.get(listIndex);
            listItemNumberView.setText(wordClicked);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String itemClicked = Const.CORRECT_WORDS_LIST.get(clickedPosition);
            mOnClickListener.onListItemClicked(itemClicked);
        }
    }
}
