package org.example;

import java.util.Scanner;

public class MultithreadClient {



    public static void main(String[] args) {
        // TODO code application logic here
        ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
        connectionToServer.Connect();
        int requestCount = 1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message type: ");
        String messageType = scanner.nextLine();
        System.out.println("Enter crypto type: ");
        String cryptoType = scanner.nextLine();
        while (!messageType.equals("0"))
        {
            System.out.println("Response from CoinNet Server: " + connectionToServer.SendForAnswer(messageType, cryptoType, requestCount));
            requestCount++;
            System.out.println("Enter message type: ");
            messageType = scanner.nextLine();
            System.out.println("Enter crypto type: ");
            cryptoType = scanner.nextLine();

        }
        connectionToServer.Disconnect();
    }
    
}
