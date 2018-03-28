package de.savrasov.kaysquared;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ListIterator;

public class KKActivity extends AppCompatActivity {

    TextView tvTop;
    TextView tvBot;
    ImageView ivTop;
    ImageView ivBot;
    Button btVisible;
    boolean botVisible, botNewImage;
    SetOrganizer so;
    boolean loadImagesAllowed;
    ListIterator<Card> li;
    Context context;
    Card currentCard;
    CardSet cs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kk);
        botVisible=false;
        tvTop = findViewById(R.id.tvTop);
        tvBot = findViewById(R.id.tvBot);
        ivTop = findViewById(R.id.imageViewTop);
        ivBot = findViewById(R.id.imageViewBot);
        btVisible = findViewById(R.id.btVisible);
        context = getApplicationContext();
        so = SetOrganizer.loadSetOrganizer(context);
        if(so == null) so = new SetOrganizer();
        String name; int chapter;
        name = getIntent().getExtras().getString("SetName");
        chapter = getIntent().getIntExtra("SetChapter", 0);
        CardSet baseSet = so.findSet(context,name + context.getString(R.string.set_name)+ chapter);
        cs = new CardSet("temporary", "temporary", 0, "temporary", System.currentTimeMillis()/1000);
        if (baseSet != null) {
            li = baseSet.getIterator();
            while (li.hasNext()){
                Card card = li.next();
                if (card.timeToLearn < System.currentTimeMillis()/1000) cs.addCard(card);
            }
        }
        li = cs.getIterator();
        if (!li.hasNext()){
            tvTop.setText("There are no cards you need to learn today!");
        } else {
            workWithCard();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyPreferences();
    }
    protected void onPause(){
        super.onPause();
        so.saveSetOrganizer(context);
    }

    public void applyPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        loadImagesAllowed = sp.getBoolean("pref_urlLoadSetting", true);
        String theme = sp.getString("pref_Colorsetting", "wi");
        if (theme.equals("fa")) setTheme(R.style.AppTheme_NoActionBarFall);
        else if (theme.equals("sp")) setTheme(R.style.AppTheme_NoActionBarSpring);
        else if (theme.equals("su")) setTheme(R.style.AppTheme_NoActionBarSummer);
    }

    public void makeVisible(View v){
        if (!botVisible){
            if (!cs.cards.isEmpty()){
                tvBot.setVisibility(View.VISIBLE);
                if (botNewImage) ivBot.setVisibility(View.VISIBLE);
                botVisible = true;
                currentCard.timeToLearn = (System.currentTimeMillis()/1000)+129600;
            }
        } else {
            if (li.hasNext()){
                botVisible = false;
                tvBot.setVisibility(View.INVISIBLE);
                ivBot.setVisibility(View.INVISIBLE);
                workWithCard();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Good Job!");
                alert.setMessage("You are done with this set for today.");
                alert.setPositiveButton("Great!", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int whichButton) {
                        save(); finish();
                     }
                });
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        save(); finish();
                    }
                });
                alert.show();
            }
        }

    }

    public void save(){
        so.saveSetOrganizer(context);
        return;
    }

    public void workWithCard(){
            botNewImage = false;
            currentCard = li.next();
            tvTop.setText(currentCard.frontText);
            tvBot.setText(currentCard.backText);
            String fi = currentCard.frontImage;
            String bi = currentCard.backImage;
            try{
                if (!fi.equals("") && fi != null){
                    if(loadImagesAllowed){
                        Picasso.with(context).load(fi).fit().centerInside().into(ivTop);
                    }
                    ivTop.setVisibility(View.VISIBLE);
                } else ivTop.setVisibility(View.INVISIBLE);
            } catch (RuntimeException ex){

            }
            try{
                if(!bi.equals("") && bi != null){
                    if (loadImagesAllowed){
                        Picasso.with(context).load(bi).fit().centerInside().into(ivBot);
                    }
                    botNewImage = true;
                }
            } catch (RuntimeException ex){

            }

    }
}
