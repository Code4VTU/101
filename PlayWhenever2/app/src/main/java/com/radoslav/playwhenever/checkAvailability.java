package com.radoslav.playwhenever;

import java.util.ArrayList;


public class checkAvailability {

    boolean CheckTime (ArrayList<Float> ArrList, float TimeStart, float TimeEnd){
        if(TimeStart < ArrList.get(0) && TimeEnd <= ArrList.get(0)){
            return true;
        }
        for(int i = 1; i<ArrList.size()/2; i+=2){
            if(TimeStart >= ArrList.get(i) && TimeEnd <= ArrList.get(i+1)){
                return true;
            }
        }
        if(TimeStart >= ArrList.get(ArrList.size())){
            return true;
        }
        return false;
    }

}