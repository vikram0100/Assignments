package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class client {

    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1"; // Default host, localhost
        int portNumber = 5151; // Default port to use
        if (args.length > 0)
        {
            hostName = args[0];
            if (args.length > 1)
            {
                portNumber = Integer.parseInt(args[1]);
                if (args.length > 2)
                {
                    System.err.println("Usage: java EchoClientTCP [<host name>] [<port number>]");
                    System.exit(1);
                }
            }
        }

        try {

            Socket clientSocket=new Socket(hostName,portNumber);
            BufferedReader sin=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream sout=new PrintStream(clientSocket.getOutputStream());
            BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
            String s;
            while (  true )
            {
                System.out.print("Please enter valid string for conversion : "+"\n");
                s=reader.readLine();
                sout.println(s);
                if ( s.equalsIgnoreCase("BYE") )
                {
                    System.out.println("Connection ended by client");
                    break;
                }
                s=sin.readLine();
                System.out.print("Server : " + s + "\n");
                //}
            }
            clientSocket.close();
            sin.close();
            sout.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
