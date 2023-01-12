package Finale;



import java.net.DatagramSocket;


public class ChatClient {

    public static void main(String args[]) throws Exception {



        String host = "Localhost";

    DatagramSocket socket = new DatagramSocket();
    Client window = new Client(socket,host);
    window.home();








    }
}