package com.novatoresols.staysafe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 11/25/15.
 */
public class RouteModelParder {

    public static List<RouteModel> parseRouteRecords(JSONObject content) {

        List<RouteModel> routeList=routeList = new ArrayList<>();;
        try {
            JSONArray ar = content.getJSONArray("routes");


            for (int i = 0; i < ar.length(); i++) {
                JSONObject jobj = ar.getJSONObject(i);
                RouteModel rm = new RouteModel();
                //JSONObject ovpl=jobj.getJSONObject("overview_polyline");
                rm.setViaRoute(jobj.getString("summary"));

                JSONArray disarr=jobj.getJSONArray("legs");
                JSONObject mainLegObject=disarr.getJSONObject(0);
                JSONObject distobj=mainLegObject.getJSONObject("distance");
                JSONObject timeobj=mainLegObject.getJSONObject("duration");
                rm.setDistance(distobj.getString("text"));
                rm.setTime(timeobj.getString("text"));
                routeList.add(rm);


            }



        } catch (Exception e) {
            e.printStackTrace();

        }
        return routeList;
    }
}

