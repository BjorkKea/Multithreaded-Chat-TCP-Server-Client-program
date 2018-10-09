package com.company.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient implements Runnable {

    private static Socket clientSocket = null;
    private static Scanner clientInput = null;
    private static PrintStream serverOutput = null;
    private static BufferedReader inputLine = null;
    private static Boolean closed = false;

    private static int portNumber = 5000;
    private static String host = "localhost";
    //final static String host = ""

    public static void main(String[] args) {
        // write your code here

        if (args.length < 2) {
            System.out.println("Host: " + host + "\nPortnumber: " + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

        try {
            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            serverOutput = new PrintStream(clientSocket.getOutputStream());
            clientInput = new Scanner(clientSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("J_ER <<err_code6>> : <<err_unknown host>>" + host);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (clientSocket != null && serverOutput != null && clientInput != null) {
            try {
                new Thread(new TCPClient()).start();

                while (!closed) {
                    serverOutput.println(inputLine.readLine().trim());
                }

                serverOutput.close();
                clientInput.close();
                clientSocket.close();

            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }


    }

    @Override
    public void run() {

        String responseLine;
        while ((responseLine = clientInput.nextLine()) != null) {
            System.out.println(responseLine);

            if (responseLine.indexOf("Bye") != -1)
                break;
        }
        closed = true;

    }
}
