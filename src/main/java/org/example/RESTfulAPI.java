package org.example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;


public class RESTfulAPI {


    public static String printCoinList() throws MalformedURLException, IOException {
        URL url = new URL("https://api.coingecko.com/api/v3/coins/list");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        scanner.close();
        JSONArray jsonObject = new JSONArray(inline);
        String responseList = "";
        for (int i = 0; i < jsonObject.length(); i++)
        {
            String id = jsonObject.getJSONObject(i).getString("id");
            String name = jsonObject.getJSONObject(i).getString("name");
            responseList += id + "\t" + name + "\n";
        }
        return responseList;
    }

    public static String printRequestedPriceList(String requestedCoins) throws MalformedURLException, IOException {

        String parseUrl = "https://api.coingecko.com/api/v3/simple/price?ids=" + requestedCoins + "&vs_currencies=try";

        URL url = new URL(parseUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }

        JSONObject JSONObject = new JSONObject(inline);

        scanner.close();
        if (inline.equals("{}")) {
            return "Coin Not Found.";
        } else {
            return Double.toString(JSONObject.getJSONObject(requestedCoins).getDouble("try"));
        }
    }
    
}
