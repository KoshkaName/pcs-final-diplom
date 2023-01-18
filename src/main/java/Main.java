import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws Exception {
        //BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    String word = in.readLine();
                    String json = gson.toJson(new BooleanSearchEngine(new File("pdfs")).search(word));
                    out.println(json);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }

}