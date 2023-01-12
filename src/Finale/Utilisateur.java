package Finale;
//connected


import java.net.DatagramPacket;
import java.net.InetAddress;

public class Utilisateur extends Utilisateur_ {
    private DatagramPacket receivePacket;
    int portNumber;
    InetAddress receivePacketAddress;

    Utilisateur(InetAddress receivePacketAddress,int portNumber, String name,String password) {
        super(name,password);

        this.receivePacketAddress=receivePacketAddress;
        this.portNumber=portNumber;


    } // end constructor

    @Override
    public String toString() {
        return "ClientsConnected{" +

                ", portNumber=" + portNumber +

                ", receivePacketAddress=" + receivePacketAddress +
                '}';
    }

    public InetAddress getIPAddress() {

        return this.receivePacketAddress;
    } // end of getter returns IPAddress

    public int getPortNumber() {

        return this.portNumber;
    } // end of getter returns Port number


}
