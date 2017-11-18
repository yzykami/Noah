package com.tzw.noah.models;

import com.google.gson.reflect.TypeToken;
import com.tzw.noah.logger.Log;
import com.tzw.noah.net.IMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class Advertising implements Serializable {

    public int seatId;
    public String advertUrl = "";
    public String advertImage = "";
    public String advertTitle = "";
    public String startTime = "";
    public String endTime = "";
    public String advertRemark = "";
    public int advertProbability;
    public int orderValue;
    public int adsNumber;
    public List<Advertising> list = new ArrayList<>();

    public static List<Advertising> loadList(IMsg iMsg) {

        Advertising advertising = new Advertising();
        try {
             List<Advertising> advertisings = iMsg.getModelList("advertisingRObj", new TypeToken<List<Advertising>>() {
            }.getType());
            if (advertisings.size() > 0)
                advertising = advertisings.get(0);

        } catch (Exception e) {
            Log.log("Advertising", e);
        }
        return advertising.list;
    }


    public final static int LIST_TYPE_PIC_RL = 22001;
    public final static int LIST_TYPE_PIC_UD = 22002;
    public final static int LIST_TYPE_PIC_UD_BIG = 22003;

    public boolean isRL() {
        return seatId == LIST_TYPE_PIC_RL;
    }

    public boolean isUD() {
        return seatId == LIST_TYPE_PIC_UD;
    }

    public boolean isUDBig() {
        return seatId == LIST_TYPE_PIC_UD_BIG;
    }
}