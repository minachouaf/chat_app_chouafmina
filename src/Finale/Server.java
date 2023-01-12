package Finale;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class Server {
    public final static int PORT = 2001;
    private final static int BUFFER = 20048;

    boolean connected;


    public  DatagramSocket socket;
    public ArrayList<Utilisateur> client_connected;
    Connexion_db con;
    Connection connection;





    public Server() throws SocketException, SQLException {
        connected = false;
        socket=new DatagramSocket(PORT);
        System.out.println("Server is running and is listening on port " + PORT);
        client_connected = new ArrayList<>();
        con = Connexion_db.getInstance();
        con.connectToDatabase();
        connection = con.getConnection();

    }



    public void servir() throws IOException {
        byte[] buffer = new byte[BUFFER];
       while (true){
      //intialisation the buffer
     Arrays.fill(buffer, (byte) 0);
     DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
     socket.receive(packet);
     String op= new String(packet.getData()).trim();
     InetAddress clientAddress = packet.getAddress();
    int  client_port = packet.getPort();

         try {
             Threadclient client = new Threadclient(socket,this,op,clientAddress,client_port);
             Thread t = new Thread(client);
             System.out.println("start thread");
             t.start();

         } catch (SocketException e) {
             e.printStackTrace();
         } catch (SQLException e) {
             e.printStackTrace();
         }






 }

        }

    public static void main(String args[]) throws Exception {

        Server server_thread = new Server();
        server_thread.servir();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public ArrayList<Utilisateur> getClientconnected() {
        return this.client_connected;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
