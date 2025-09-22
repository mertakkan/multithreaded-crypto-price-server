package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class ServerThread extends Thread
{
    protected BufferedReader is;
    protected PrintWriter os;
    protected Socket s;

    private String line;
    private String reqLine;
    char[] charrr;
    String msgType;
    char reqChar;
    char msgTy;
    String req;
    private RESTfulAPI apiClient = new RESTfulAPI();


    public ServerThread(Socket s)
    {
        this.s = s;
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

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public String writeToByteHex(String messageType, String cryptoType, String cryptoPrice, String requestCount) {
        String hexString = new String();
        try
        {

            ByteArrayOutputStream outputStreamByte = new ByteArrayOutputStream( );

            byte[] messageTypeBytes = messageType.getBytes();
            byte[] socketBytes = s.getLocalSocketAddress().toString().getBytes();
            byte[] cryptoBytes = cryptoType.getBytes();
            byte[] cryptoPriceBytes = cryptoPrice.getBytes();
            byte[] requestBytes = requestCount.getBytes();

            outputStreamByte.write(messageTypeBytes);
            outputStreamByte.write(cryptoBytes);
            outputStreamByte.write(cryptoPriceBytes);
            outputStreamByte.write(socketBytes);
            outputStreamByte.write(requestBytes);
            outputStreamByte.close();

            byte[] endArray = outputStreamByte.toByteArray();

            hexString = byteArrayToHex(endArray);
            return hexString;

        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return hexString;
    }


    public void run()
    {
        try
        {

            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());
            s.setSoTimeout(15000);


        }
        catch (IOException e)
        {
            System.err.println("Server Thread. Run. IO error in server thread");
        }

        try
        {

            reqLine = is.readLine();
            line = hexToString(reqLine);
            charrr = line.toCharArray();
            msgTy = charrr[0];
            reqChar = charrr[charrr.length-1];
            msgType = String.valueOf(msgTy);
            req = line.replaceAll("[^a-zA-Z]", "");

            while (Integer.parseInt(msgType) != 0)
            {


                if (Integer.parseInt(msgType) == 2) {
                    os.println(apiClient.printCoinList());
                } else {
                    os.println(writeToByteHex("3", req, apiClient.printRequestedPriceList(req), String.valueOf(reqChar)));

                }

                os.flush();
                System.out.println("Client " + s.getRemoteSocketAddress() + " requested :  " + line);

                reqLine = is.readLine();
                line = hexToString(reqLine);
                charrr = line.toCharArray();
                msgTy = charrr[0];
                reqChar = charrr[charrr.length-1];
                msgType = String.valueOf(msgTy);
                req = line.replaceAll("[^a-zA-Z]", "");


            }
        }
        catch (IOException e)
        {
            line = this.getName();
            System.err.println("Server Thread. Run. IO Error/ Client " + line + " terminated abruptly");
        }
        catch (NullPointerException e)
        {
            line = this.getName();
            System.err.println("Server Thread. Run.Client " + line + " Closed");
        } finally
        {
            try
            {
                System.out.println("Closing the connection");
                if (is != null)
                {
                    is.close();
                    System.err.println(" Socket Input Stream Closed");
                }

                if (os != null)
                {
                    os.close();
                    System.err.println("Socket Out Closed");
                }
                if (s != null)
                {
                    s.close();
                    System.err.println("Socket Closed");
                }

            }
            catch (IOException ie)
            {
                System.err.println("Socket Close Error");
            }
        }
    }
}
