package de.savrasov.kaysquared;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Stopp on 02.03.2018.
 */

public class CardAdapter extends ArrayAdapter<Card> {

    public CardAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CardAdapter(Context context, int resource, List<Card> items) {
        super(context, resource, items);
    }

    @Override
    public void add(@Nullable Card object) {
        super.add(object);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.card_lv_item, null);
        }

        Card currentCard = getItem(position);

        if (currentCard != null) {
            TextView fText = (TextView) view.findViewById(R.id.frontText);
            TextView bText = (TextView) view.findViewById(R.id.backText);
            TextView fImage = (TextView) view.findViewById(R.id.frontImage);
            TextView bImage = (TextView) view.findViewById(R.id.backImage);

            fText.setText(currentCard.frontText);
            bText.setText(currentCard.backText);
            fImage.setText(currentCard.frontImage);
            bImage.setText(currentCard.backImage);
        }

        return view;
    }
}
