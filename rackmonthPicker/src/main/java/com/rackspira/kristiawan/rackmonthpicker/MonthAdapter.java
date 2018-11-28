package com.rackspira.kristiawan.rackmonthpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kristiawan on 9/8/2017.
 */

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder> {

    private String[] months;
    private OnSelectedListener listener;
    private int selectedItem = -1;
    private Context context;
    private int color;

    MonthAdapter(Context context, OnSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        months = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();

        if (selectedItem == -1) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            selectedItem = cal.get(Calendar.MONTH);
        }
    }

    @NonNull
    @Override
    public MonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MonthHolder(LayoutInflater.from(context).inflate(R.layout.item_view_month, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MonthHolder holder, int position) {
        holder.textViewMonth.setText(months[position]);
        holder.textViewMonth.setTextColor(selectedItem == position ? Color.WHITE : Color.BLACK);
        holder.itemView.setSelected(selectedItem == position);
    }

    @Override
    public int getItemCount() {
        return months.length;
    }

    void setLocale(Locale locale) {
        months = new DateFormatSymbols(locale).getShortMonths();
        notifyDataSetChanged();
    }

    void setSelectedItem(int index) {
        selectedItem = index;
        notifyItemChanged(selectedItem);
    }

    void setBackgroundMonth(int color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    int getMonth() {
        return selectedItem + 1;
    }

    int getStartDate() {
        return 1;
    }

    int getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, selectedItem + 1);
        cal.set(Calendar.DAY_OF_MONTH, selectedItem + 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    String getShortMonth() {
        return months[selectedItem];
    }

    class MonthHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layoutMain;
        TextView textViewMonth;

        MonthHolder(View itemView) {
            super(itemView);
            layoutMain = itemView.findViewById(R.id.main_layout);
            textViewMonth = itemView.findViewById(R.id.text_month);
            if (color != 0)
                setMonthBackgroundSelected(color);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedItem);
            selectedItem = getAdapterPosition();
            notifyItemChanged(selectedItem);
            listener.onContentSelected();
        }

        private void setMonthBackgroundSelected(int color) {
            LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.month_selected);
            GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.getDrawable(1);
            gradientDrawable.setColor(ContextCompat.getColor(context, color));
            layerDrawable.setDrawableByLayerId(1, gradientDrawable);

            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_selected}, gradientDrawable);
            states.addState(new int[]{}, ContextCompat.getDrawable(context, R.drawable.month_default));
            layoutMain.setBackground(states);
        }
    }

    interface OnSelectedListener {
        void onContentSelected();
    }
}
