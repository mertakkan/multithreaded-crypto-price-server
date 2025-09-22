package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ConnectionToServer
{
    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    private Socket s;
    protected BufferedReader is;
    protected PrintWriter os;

    protected String serverAddress;
    protected int serverPort;


    public ConnectionToServer(String address, int port)
    {
        serverAddress = address;
        serverPort    = port;
    }

    public void Connect()
    {
        try
        {
            s=new Socket(serverAddress, serverPort);
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

    public String SendForAnswer(String messageType, String cryptoType, int requestCount)
    {
        String response = new String();
        try
        {

            ByteArrayOutputStream outputStreamByte = new ByteArrayOutputStream( );

            byte[] messageTypeBytes = messageType.getBytes();
            byte[] socketBytes = s.getLocalSocketAddress().toString().getBytes();
            byte[] cryptoBytes = cryptoType.getBytes();
            byte[] requestBytes = Integer.toString(requestCount).getBytes();

            outputStreamByte.write(messageTypeBytes);
            outputStreamByte.write(cryptoBytes);
            outputStreamByte.write(socketBytes);
            outputStreamByte.write(requestBytes);
            outputStreamByte.close();

            byte[] endArray = outputStreamByte.toByteArray();

            String hexString = byteArrayToHex(endArray);


            os.println(hexString);
            os.flush();

            response = is.readLine();

            if (messageType.equals("2")) {
                for (int i = 0 ; i < 100 ; i++) {
                    response += is.readLine();
                }
            }




            if (!messageType.equals("2"))
            response = hexToString(response);


        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return response;
    }

    public void Disconnect()
    {
        try
        {
            is.close();
            os.close();
            s.close();
            System.out.println("ConnectionToServer. SendForAnswer. Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
    public String hexToString(String hex) {
        String result = new String();
        char[] charArray = hex.toCharArray();
        for(int i = 0; i < charArray.length; i=i+2) {
            String st = ""+charArray[i]+""+charArray[i+1];
            char ch = (char)Integer.parseInt(st, 16);
            result = result + ch;
        }
        return result;
    }

}
