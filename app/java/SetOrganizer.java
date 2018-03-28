package de.savrasov.kaysquared;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Stopp on 25.02.2018.
 */

public class SetOrganizer implements Serializable {
    ArrayList<CardSet> sets;
    SetOrganizer(){
        sets = new ArrayList<>();
    }

    public void addSet(CardSet newSet){
        if (newSet != null){
            sets.add(newSet);
        }
    }

    public boolean removeSet(Context context, String setString){
        CardSet set = findSet(context, setString);
        if(set != null){
            sets.remove(set);
            return true;
        }
        return false;
    }

    public CardSet findSet(Context context, String setString){
        ListIterator<CardSet> li = sets.listIterator();
        while (li.hasNext()){
            CardSet cs = li.next();
            if ((cs.name + context.getString(R.string.set_name) + cs.chapter).equals(setString)){
                return li.previous();
            }
        }
        return null;
    }

    public ListIterator<CardSet> listIterator(){
        return sets.listIterator();
    }

    public boolean saveSetOrganizer(Context context){
        try{
            FileOutputStream fos = context.openFileOutput(context.getString(R.string.so_filename), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException ex){
            return false;
        }
        return true;
    }
    public static SetOrganizer loadSetOrganizer(Context context){
        SetOrganizer loaded = null;
        try{
            FileInputStream fis = context.openFileInput(context.getString(R.string.so_filename));
            ObjectInputStream ois = new ObjectInputStream(fis);
            loaded = (SetOrganizer) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ex){
            loaded = null;
        }
        if (loaded == null) loaded = new SetOrganizer();
        return loaded;
    }
}
