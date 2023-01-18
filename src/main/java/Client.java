import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private static final int PORT = 8989;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()))
        ) {
            out.println("бизнес");
            System.out.println(jsonList(in.readLine()));
        }
    }

    public static List<PageEntry> jsonList(String json) {

        List<PageEntry> list = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(json);
            JSONArray jsonArray = (JSONArray) obj;

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            for (Object object : jsonArray) {

                JSONObject jsonObject = (JSONObject) object;
                PageEntry pageEntry = gson.fromJson(String.valueOf(jsonObject), PageEntry.class);
                list.add(pageEntry);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}