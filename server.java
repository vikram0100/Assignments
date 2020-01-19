package com;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class server {
    static float rate;
    public static void main(String[] args) throws InterruptedException {
        int port=5151;
        try {
            ServerSocket serverSocket= new ServerSocket(port);
            while(true) {
                System.out.println("Ready to accept new connection");
                Socket clientSocket = serverSocket.accept();
                System.out.println("accepted connection from"+clientSocket);
                Thread t =new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            handleCilents(clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
               t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleCilents(Socket clientSocket) throws IOException, InterruptedException {
        InputStream inputStream=clientSocket.getInputStream();
        OutputStream outputStream=clientSocket.getOutputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        Float total;
        String data;
        while((data = reader.readLine())!=null)
        {
            System.out.println(data);
            if ("exit".equalsIgnoreCase(data)) {
                break;
            }
            if (data.matches("^\\d[0-9.]*[A-Z]{3}2[A-Z]{3}$") == true) {
                System.out.println("MATCHED !!!");
                String[] exchangecode=data.split("[^A-Z0-9.]+|(?<=[A-Z])(?=[0-9.])|(?<=[0-9.])(?=[A-Z])");
                String torate=exchangecode[3];
                String conversionAmount=exchangecode[0];
                String fromrate=exchangecode[1];

                total = getConversion(conversionAmount, fromrate, torate);
                outputStream.write(("Exchange Rate:" + fromrate + "-" + torate + "=" + rate +" ,Total amount is " + total + "\n").getBytes());
                //outputStream.write(("Total amount is " + total + "\n").getBytes());

                // outputStream.write(("current time is"+new Date()+"\n").getBytes());
            }
            else {
               // System.out.println("here");
                outputStream.write(("UNRECONIZED FORMAT !!!").getBytes());
            }
        }
        clientSocket.close();

    }
    private static float getConversion(String amount,String fromrate,String torate){
        Float total,currentRate;
        total=Float.parseFloat("0");
        int i,j=0;
        String csvFile="/home/hadoop/Downloads/DATS2410/Oblig-1/TA Works/Code for Oblig1/socketprogramming/exchange.csv";
        String line="";
        String[] exchangeArr=null;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                exchangeArr= line.split(",");
                if (exchangeArr[0].equalsIgnoreCase(fromrate)  && exchangeArr[1].equalsIgnoreCase(torate)){
                    rate=Float.parseFloat(exchangeArr[2]);
                    System.out.println(rate);
                    total=rate*Float.parseFloat(amount);
                    System.out.println(total);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }
}




