package se.rcdotnet.udacity.baking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public  class JsonParser {
    public  JsonParser(){};
    public Recipe[] Parse (String toParse){
        Recipe[] retVal;
        Gson gson;
        gson = new GsonBuilder().create();
        retVal = gson.fromJson(toParse,Recipe[].class);

        return retVal;
    }
}
