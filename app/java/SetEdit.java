package de.savrasov.kaysquared;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ListIterator;

public class SetEdit extends AppCompatActivity {
    SetOrganizer so;
    String name; int chapter;
    EditText nameT, chapterT, descriptionT;
    ListView cardlv;
    CardSet cardSet;
    Card editCard;
    Context context;
    CardAdapter cardAdapter;
    boolean newset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_edit);
        context = getApplicationContext();
        name = getIntent().getExtras().getString("SetName");
        chapter = getIntent().getIntExtra("SetChapter", 0);
        nameT = (EditText) findViewById(R.id.itName);
        chapterT = (EditText) findViewById(R.id.itChapter);
        descriptionT = (EditText) findViewById(R.id.itDescription);
        cardlv = (ListView) findViewById(R.id.cardlv);
        newset = true;
        initializeFields();

    }
    @Override
    public void onPause(){
        super.onPause();
        save();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyPreferences();
    }

    public void applyPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("pref_Colorsetting", "wi");
        if (theme.equals("fa")) setTheme(R.style.AppTheme_NoActionBarFall);
        else if (theme.equals("sp")) setTheme(R.style.AppTheme_NoActionBarSpring);
        else if (theme.equals("su")) setTheme(R.style.AppTheme_NoActionBarSummer);
    }

    public void save(){
        //check if input is valid
        try {
            int newchp = Integer.parseInt(chapterT.getText().toString());
            String newname = nameT.getText().toString();
            String newdescription = descriptionT.getText().toString();
            if (!newset && (!cardSet.name.equals(newname) || !(cardSet.chapter == newchp))
                    && so.findSet(context,newname + context.getString(R.string.set_name) + newchp) != null){
                Toast toast = Toast.makeText(context, newname + context.getString(R.string.set_name) + newchp + " already exists.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (newset && so.findSet(context, newname + context.getString(R.string.set_name) + newchp) != null){
                Toast toast = Toast.makeText(context, newname + context.getString(R.string.set_name) + newchp + " already exists.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (newname.length() > 100 || newdescription.length() > 2000){
                Toast toast = Toast.makeText(context, "Name or description is too long(Name: 100, Description: 2000).", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            cardSet.clear();
            cardSet.name = newname; cardSet.chapter = newchp; cardSet.description = newdescription;
            for (int n=1; n< cardAdapter.getCount(); n++){
                cardSet.addCard(cardAdapter.getItem(n));
            }
            if (newset) so.addSet(cardSet);
            so.saveSetOrganizer(context);
            Toast toast = Toast.makeText(context,newname + context.getString(R.string.set_name) + newchp + " was saved.", Toast.LENGTH_LONG);
            toast.show();
        }catch(Exception ex){
            Toast toast = Toast.makeText(context, chapterT.getText().toString() + " is not a valid number.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

    }
    public void onResetButtonPressed(View v){
        initializeFields();
    }

    public void initializeFields(){
        so = SetOrganizer.loadSetOrganizer(context);
        if (so == null) so = new SetOrganizer();
        if (name != null) {
            if (chapter != 0) {
                cardSet = so.findSet(context, name + context.getString(R.string.set_name) + chapter);
                newset = false;
            } else {
                int i = 1;
                while (chapter == 0) {
                    if (so.findSet(context,name + context.getString(R.string.set_name) + i) == null) chapter = i;
                    i++;
                }
            }
            long currentTime = System.currentTimeMillis() / 1000;
            if (cardSet == null) cardSet = new CardSet(null, name, chapter, descriptionT.getText().toString(), currentTime);
            nameT.setText(name, TextView.BufferType.EDITABLE);
            chapterT.setText(Integer.toString(chapter), TextView.BufferType.EDITABLE);
            descriptionT.setText(cardSet.description, TextView.BufferType.EDITABLE);
            // change "user" to current login user
            initializeLV();
        }
    }
    public void initializeLV(){
        ArrayList<Card> cardArrayList = new ArrayList<>();
        cardArrayList.add(new Card("Add a new Card!",null, null, null, System.currentTimeMillis()/1000));
        ListIterator<Card> li = cardSet.getIterator();
        while (li.hasNext()) {
            cardArrayList.add(li.next());
        }
        cardAdapter = new CardAdapter(getApplicationContext(), R.layout.string_lv_item, cardArrayList);
        cardlv.setAdapter(cardAdapter);
        cardlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0){
                    createNewCard();
                } else {
                    editCard = (Card)adapterView.getItemAtPosition(position);
                    editCard();
                }
            }
        });
        cardlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    createNewCard();
                } else {
                    editCard =  (Card)adapterView.getItemAtPosition(position);
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Delete");
                    alert.setMessage("Delete this card?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cardAdapter.remove(editCard);
                            editCard = null;
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.show();
                }
                return true;
            }
        });
    }

    public void createNewCard(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Card creation");
        alert.setMessage("Please enter values for your new card. Leave fields you want to be empty unchanged or empty");
        final MyInputText ft = new MyInputText(this, "Fronttext");
        final MyInputText bt = new MyInputText(this, "Backtext");
        final MyInputText fi = new MyInputText(this, "Frontimage URL");
        final MyInputText bi = new MyInputText(this, "Backimage URL");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(ft); layout.addView(bt); layout.addView(fi); layout.addView(bi);
        alert.setView(layout);
        alert.setPositiveButton("Create", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String frontT = ft.getText().toString(); String backT = bt.getText().toString();
                String frontI = fi.getText().toString(); String backI = bi.getText().toString();
                if (inputIsEmpty(frontT, 0) && inputIsEmpty(frontI, 2)){
                    Toast toast = Toast.makeText(getApplicationContext(), "A card needs a Frontside", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (inputIsEmpty(backT, 1) && inputIsEmpty(backI, 3)){
                    Toast toast = Toast.makeText(getApplicationContext(), "A card needs a Backside", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Card card = new Card(frontT, backT, frontI, backI, System.currentTimeMillis()/ 1000);
                    cardAdapter.add(card);
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast toast = Toast.makeText(getApplicationContext(), "Card creation cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                Toast toast = Toast.makeText(getApplicationContext(), "Card creation cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        alert.show();
    }

    public void editCard(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Card editing");
        alert.setMessage("Please enter values for your card.");
        final MyInputText ft = new MyInputText(this, editCard.frontText);
        final MyInputText bt = new MyInputText(this, editCard.backText);
        final MyInputText fi = new MyInputText(this, editCard.frontImage);
        final MyInputText bi = new MyInputText(this, editCard.backImage);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(ft); layout.addView(bt); layout.addView(fi); layout.addView(bi);
        alert.setView(layout);
        alert.setPositiveButton("Change", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String frontT = ft.getText().toString(); String backT = bt.getText().toString();
                String frontI = fi.getText().toString(); String backI = bi.getText().toString();
                if(frontT.equals("")) frontT = ft.getHint().toString(); if(backT.equals("")) backT = bt.getHint().toString();
                if(frontI.equals("")) frontI = fi.getHint().toString(); if(backI.equals("")) backI = bi.getHint().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);

                if (inputIsEmpty(frontT, 0) && inputIsEmpty(frontI, 2)){
                    Toast toast = Toast.makeText(getApplicationContext(), "A card needs a Frontside", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (inputIsEmpty(backT, 1) && inputIsEmpty(backI, 3)){
                    Toast toast = Toast.makeText(getApplicationContext(), "A card needs a Backside", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    editCard.frontText = frontT; editCard.backText = backT;
                    editCard.frontImage = frontI; editCard.backImage = backI;
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast toast = Toast.makeText(getApplicationContext(), "Card editing cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                Toast toast = Toast.makeText(getApplicationContext(), "Card editing cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        alert.show();
    }
    public boolean inputIsEmpty(String input, int tfID){
        if (!input.isEmpty()){
            if (tfID == 0 && input.equals("Fronttext"))return true;
            if (tfID == 1 && input.equals("Backtext"))return true;
            if (tfID == 2 && input.equals("Frontimage URL"))return true;
            if (tfID == 3 && input.equals("Backimage URL"))return true;
            return false;
        }
        return true;
    }
    public Activity getActivity(){
        return this;
    }
}
