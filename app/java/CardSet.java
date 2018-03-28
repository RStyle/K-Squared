package de.savrasov.kaysquared;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by Stopp on 25.02.2018.
 */

public class CardSet implements Serializable{
    ArrayList<Card> cards;
    String name, description, id;
    long createdOn;
    int chapter;
    CardSet(String i, String n, int c, String d, long cr){
        id = i;
        name = n;
        chapter = c;
        description = d;
        createdOn = cr;
        cards = new ArrayList<>();
    }

    public CardSet(CardSet other){
        id = other.id;
        name = other.name;
        chapter = other.chapter;
        description = other.description;
        createdOn = other.createdOn;
        cards = new ArrayList<>();
        ListIterator<Card> li = other.getIterator();
        while (li.hasNext()){
            cards.add(new Card(li.next()));
        }
    }

    public void addCard(String fT, String bT, String fI, String bI){
        cards.add(new Card(fT, bT, fI, bI));
    }
    public void addCard(Card card){
        cards.add(card);
    }
    public void clear(){
        cards.clear();
    }

    public ListIterator<Card> getIterator(){
        return cards.listIterator(0);
    }

    public boolean saveSet(Context context , String fileName){
        try{
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException ex){
            return false;
        }
        return true;
    }

    public static CardSet loadSet(Context context , String fileName){
        CardSet loaded = null;
        try{
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            loaded = (CardSet) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ex){
            loaded = null;
        }
        return loaded;
    }
}
