package me.onlyfire.yukigram.android;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.ApplicationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class AccountAgeController {

    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();
    private static final JSONObject agesJson;

    private static final String[] ids;
    private static final int[] ages;

    private static final int minId;
    private static final int maxId;

    static {
        try {
            agesJson = loadAgesFromJson(ApplicationLoader.applicationContext);
            Iterator<String> keysIterator = agesJson.keys();
            List<String> idList = new ArrayList<>();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                idList.add(key);
            }
            ids = idList.toArray(new String[0]);

            ages = new int[ids.length];
            for (int i = 0; i < ids.length; i++) {
                try {
                    ages[i] = Integer.parseInt(ids[i]);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }

            minId = ages[0];
            maxId = ages[ages.length - 1];
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAge(long id) {
        Date dateResult = getDate(id);
        if (dateResult == null) {
            return "Unknown";
        }
        return "approx." + " " + DATE_FORMAT.format(dateResult);
    }

    private static JSONObject loadAgesFromJson(Context context) throws IOException, JSONException {
        InputStream inputStream = context.getAssets().open("account_ages.json");
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        String json = new String(buffer, StandardCharsets.UTF_8);
        return new JSONObject(json);
    }

    @Nullable
    private static Date getDate(long id) {
        try {
            if (id < minId) {
                return new Date((Long) agesJson.get(ids[0]));
            } else if (id > maxId) {
                return new Date((Long) agesJson.get(ids[ids.length - 1]));
            } else {
                int lowerId = 0;
                for (int i = 0; i < ids.length; i++) {
                    if (id <= ages[i]) {
                        long lowerAge = (Long) agesJson.get(ids[lowerId]);
                        long upperAge = (Long) agesJson.get(ids[i]);

                        double idRatio = (double) (id - ages[lowerId]) / (ages[i] - ages[lowerId]);
                        long midDate = (long) (idRatio * (upperAge - lowerAge) + lowerAge);

                        return new Date(midDate);
                    } else {
                        lowerId = i;
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
