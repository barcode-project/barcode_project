package com.example.barcode;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static final String STREET_IDS_KEY = "StreetIDs";

    private static final String PREFERENCES_NAME = "مفضلات_التطبيق";
    private static final String SHOP_TYPE_IDS_KEY = "ShopTypeIDs";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public DataRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveShopTypeIDs(List<String> shopTypeIDs) {
        saveData(SHOP_TYPE_IDS_KEY, shopTypeIDs);
    }

    public List<String> loadShopTypeIDs() {
        return loadData(SHOP_TYPE_IDS_KEY);
    }

    private void saveData(String key, List<String> dataList) {
        String jsonList = gson.toJson(dataList);
        sharedPreferences.edit().putString(key, jsonList).apply();
    }

    private List<String> loadData(String key) {
        String jsonString = sharedPreferences.getString(key, null);

        if (jsonString != null && !jsonString.isEmpty()) {
            Type type = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(jsonString, type);
        } else {
            return new ArrayList<>();
        }
    }
    public void saveStreetIDs(List<String> streetIDs) {
        saveData(STREET_IDS_KEY, streetIDs);
    }

    public List<String> loadStreetIDs() {
        return loadData(STREET_IDS_KEY);
    }
}
