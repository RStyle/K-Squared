package de.savrasov.kaysquared;

import java.io.Serializable;

/**
 * Created by Stopp on 25.02.2018.
 */

public class Card implements Serializable{
    String frontText, backText, frontImage, backImage;
    long timeToLearn;
    Card(String fT, String bT, String fI, String bI){
        this(fT, bT, fI, bI, 0);
    }
    Card(String fT, String bT, String fI, String bI, long ttl){
        frontText = fT;
        backText = bT;
        frontImage = fI;
        backImage = bI;
        timeToLearn = ttl;
    }
    Card(Card other){
        frontText = other.frontText;
        backText = other.backText;
        frontImage = other.frontImage;
        backImage = other.backImage;
        timeToLearn = other.timeToLearn;
    }
}
