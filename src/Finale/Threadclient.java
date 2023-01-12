package Finale;

import java.io.ByteArrayOutputStream;
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

public class Threadclient implements Runnable{
    private final static int BUFFER = 1024;
    InetAddress clientAddress;
    boolean connected;
    ResultSet rs;
    int client_port;
    DatagramSocket socket;
    private ArrayList<Utilisateur> client_connected;
    Connection connection;
    PreparedStatement ps;
    DatagramPacket packet;
    Utilisateur user;

    Server server;
    String op;


    public Threadclient(DatagramSocket udpServerSocket,Server server,String op,InetAddress clientAddress,int client_port) throws SocketException, SQLException {
        this.op=op;
        this.server=server;
        connected = false;
        this.client_port=client_port;
        this.clientAddress=clientAddress;
        socket= udpServerSocket;
        client_connected=server.getClientconnected();
        connection = server.getConnection();

    }
    //tous le monde
    public  void va_vient(String word){


        try {


            //String messagee = new String(buffer, 0, buffer.length).trim();
            String id = "";
            //get name of the source
            for (int i = 0; i < client_connected.size(); i++) {
                System.out.println("list port "+client_connected.get(i).portNumber);
                if (client_connected.get(i).portNumber == client_port) {

                    id = client_connected.get(i).getName();
                }

            }
            ArrayList<Utilisateur> listdestinationconnected=getamisconnected(id);
            System.out.println("id "+id);
            System.out.println(id + " : " + word);
            byte[] dataa = (id + " : " + word).getBytes();
            String ch="";

            for (int i = 0; i < listdestinationconnected.size(); i++) {
                if (listdestinationconnected.get(i).getPortNumber() != client_port) {
                    InetAddress cl_address = listdestinationconnected.get(i).receivePacketAddress;

                    int cl_port = listdestinationconnected.get(i).portNumber;


                    System.out.println(cl_port + " port ");
                    packet = new DatagramPacket(dataa, dataa.length, cl_address, cl_port);
                    ch=ch+" "+listdestinationconnected.get(i).getName();
                    socket.send(packet);
                    System.out.println("send message" + new String(packet.getData()));
                }

            }
            if(listdestinationconnected.size()>0) {
                byte[] dataaa = ("you(" + ch + ")  : " + word).getBytes();

                System.out.println(clientAddress + " adress ");

                System.out.println(client_port + " port ");
                packet = new DatagramPacket(dataaa, dataaa.length, clientAddress, client_port);
                socket.send(packet);
                System.out.println("send message" + new String(packet.getData()));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
//login
    public  void login(String name,String password) {

        byte[] buffer = new byte[BUFFER];

        try {

            Arrays.fill(buffer, (byte) 0);
            packet = new DatagramPacket(buffer, buffer.length);
            System.out.println(name + " |" + password);


            try {
                ps = connection.prepareStatement("SELECT UserName,Password FROM user WHERE UserName=? AND Password =?");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.setString(1, name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.setString(2, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();


            }
            boolean exist=false;
            for(int i=0;i<client_connected.size();i++)
            {
                if(client_connected.get(i).getName().equals(name))
                {
                    exist=true;
                }
            }


            if (rs.next() && !exist) {
                System.out.println("@"+clientAddress);
                user = new Utilisateur(clientAddress,client_port, name, password);

                System.out.println(user.toString());
                System.out.println(client_connected.contains(user));

                client_connected.add(user);



                System.out.println(client_connected.contains(user));
                String message = "reussir";
                byte[] data = message.getBytes();

                packet = new DatagramPacket(data, data.length, clientAddress, client_port);
                System.out.println(new String(packet.getData()));
                socket.send(packet);



            }




            else{

                System.out.println("send non Ok");
                String message="refuser";
                byte[] data = message.getBytes();

                packet = new DatagramPacket(data, data.length, clientAddress, client_port);
                System.out.println(new String(packet.getData()));
                socket.send(packet);


            }

        } catch (SQLException | IOException e) {
        }

    }
    //send message to special freinds
    public void special(String [] destination,String word){
        byte[] buffer = new byte[BUFFER];
        ArrayList<Utilisateur> des=new ArrayList<>();


        try {


            String messagee = new String(buffer, 0, buffer.length).trim();
            String id = "";
            System.out.println("me port "+client_port);
            System.out.println("sixe"+des.size());
            for (int i = 0; i < client_connected.size(); i++) {
                System.out.println("list port "+client_connected.get(i).portNumber);
                if (client_connected.get(i).portNumber == client_port) {
                    System.out.println("i" + i);
                    id = client_connected.get(i).getName();
                }

            }

            System.out.println("id "+id);
            System.out.println(id + " : " + word);
            byte[] dataa = (id + " : " + word).getBytes();

            ArrayList<Utilisateur> listdestinationconnected=getamisconnected(id);


            String ch="";
            for(int i=0;i<listdestinationconnected.size();i++)
            {
                for (String name:destination
                ) {

                    if(listdestinationconnected.get(i).getName().equals(name) && (listdestinationconnected.get(i).getPortNumber() != client_port))
                    {
                        ch=ch+" "+listdestinationconnected.get(i).getName();
                        des.add(listdestinationconnected.get(i));
                    }
                }

            }
            for (int i = 0; i < des.size(); i++) {
                if (des.get(i).getPortNumber() != client_port ) {

                    InetAddress cl_address = des.get(i).receivePacketAddress;

                    int cl_port = des.get(i).portNumber;
                    System.out.println(cl_address + " adress ");

                    System.out.println(cl_port + " port ");
                    packet = new DatagramPacket(dataa, dataa.length, cl_address, cl_port);
                    socket.send(packet);
                    System.out.println("send message" + new String(packet.getData()));
                }
            }

            if(des.size()>0){



                byte[] dataaa = ("you("+ch+")  : " + word).getBytes();

                System.out.println(clientAddress+ " adress ");

                System.out.println(client_port + " port ");
                packet = new DatagramPacket(dataaa, dataaa.length, clientAddress, client_port);
                socket.send(packet);
                System.out.println("send message" + new String(packet.getData()));


            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }
    //inscription====================

    public void register(String name,String password) {
        byte[] buffer = new byte[BUFFER];

        try {

            Arrays.fill(buffer, (byte) 0);
            packet = new DatagramPacket(buffer, buffer.length);
            System.out.println(name + " |" + password);


            try {
                ps = connection.prepareStatement("SELECT UserName,Password FROM user WHERE UserName=? AND Password =?");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.setString(1, name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.setString(2, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();


            }
            if (!rs.next() ){

                PreparedStatement r = connection.prepareStatement("insert into user(UserName,Password) values (?,?)");
                r.setString(1, name);
                r.setString(2, password);

                int row=r.executeUpdate();


                String message = "reussir";
                byte[] data = message.getBytes();

                packet = new DatagramPacket(data, data.length, clientAddress, client_port);
                System.out.println(new String(packet.getData()));
                socket.send(packet);


            } else {

                System.out.println("send non Ok");
                String message = "refuser";
                byte[] data = message.getBytes();

                packet = new DatagramPacket(data, data.length, clientAddress, client_port);
                System.out.println(new String(packet.getData()));
                socket.send(packet);


            }

        } catch (SQLException | IOException e) {
        }

    }
    //get list of freinds========================
    public ArrayList<Utilisateur> getamisconnected(String name) throws SQLException {

        ArrayList<Utilisateur> amisconnected=new ArrayList<>();
        PreparedStatement p = connection.prepareStatement("SELECT UserName FROM  user u ,amis a  WHERE  (UserName=user or UserName=ami ) and (user=?  or ami=?) and  UserName!=?");
        p.setString(1, name);
        p.setString(2, name);
        p.setString(3, name);


        ResultSet rs = p.executeQuery();

        while (rs.next()) {

            String nom = rs.getString(1);

            for(int i=0;i<client_connected.size();i++)
            {
                if(client_connected.get(i).getName().equals(nom)) {

                    amisconnected.add(client_connected.get(i));
                }
            }

        }

        return amisconnected;
    }
    //get list of non_freinds=========================
    public void getamis() throws SQLException {
        PreparedStatement p = null;
        try {
            p = connection.prepareStatement("SELECT UserName from user where UserName!=? and UserName not in (SELECT UserName FROM user u ,amis a WHERE (UserName=user or UserName=ami ) and (user=? or ami=?) );");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String name = "";
        for (int i = 0; i < client_connected.size(); i++) {
            if (client_connected.get(i).getPortNumber() == client_port) {
                name = client_connected.get(i).getName();
            }
        }

        p.setString(1, name);
        p.setString(2, name);
        p.setString(3, name);


        ResultSet rs = p.executeQuery();

        String amis="nonamis";
        while (rs.next()) {

            String nom = rs.getString(1);
            amis=amis+" "+nom;




            System.out.println(nom);
        }
        System.out.println(amis);

        byte[] amiss = amis.getBytes();




        packet = new DatagramPacket(amiss, amiss.length, clientAddress, client_port);
        try {
            socket.send(packet);
            System.out.println("send"+new String(packet.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //send list of connected freinds===================
    public void envoyerList(){
        for (int i=0;i<client_connected.size();i++){
            try {
                ArrayList<Utilisateur> amis=getamisconnected(client_connected.get(i).getName());
                String liste = "list";
                for (int j = 0; j < amis.size(); j++) {

                    liste = liste + " " + amis.get(j).getName();
                }
                byte[] list = liste.getBytes();
                InetAddress cl_address = client_connected.get(i).getIPAddress();

                int cl_port = client_connected.get(i).getPortNumber();



                packet = new DatagramPacket(list, list.length, cl_address, cl_port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        this.server.client_connected=client_connected;
    }

    @Override
    public void run() {
        System.out.println(op);
        String [] words=op.split("\\|",5);
        for (String word:words
        ) {

            System.out.println("word:::::"+word);
        }
        System.out.println(words[0]);


        switch (words[0]){
            case "login":
                login(words[1],words[2]);
                try {
                    getamisconnected(words[1]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                envoyerList();
                break;

            case "register" :
                String nom=words[1];
                String pass=words[2];
                register(nom,pass);
                break;
            case "quitter"  :
                for(int i=0;i<client_connected.size();i++)
                {
                    if(client_connected.get(i).portNumber==client_port)
                    {
                        client_connected.remove(i);
                    }
                }
                envoyerList();
                break;
            case "string"  :
                if(words[1].substring(0,1).equals("@")) {
                    String worde = "";
                    String[] destinations = words[1].split("\\@");
                    for (String word : destinations
                    ) {
                        System.out.println("ggg" + word);
                        worde = word;


                    }

                    if (worde.equals("All")) {
                        System.out.println("suis la");
                        va_vient(words[2]);
                    } else {
                        System.out.println("anaaaa");
                        special(destinations, words[2]);
                    }

                }
                break;
            case "invitation":
                try {
                    getamis();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "add"     :

                System.out.println("suis la ");
                try {
                    addamis(words[1]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case "file2"    :
                if(words[1].substring(0,1).equals("@")) {
                    String worde = "";
                    String[] destinations = words[1].split("\\@");

                    String pack="";
                    for(int j=2;j<words.length;j++)
                    {
                        pack=pack+words[j];
                    }

                    sendfile_(destinations,pack);


                }
                break;
            case "file"     :

                if(words[1].substring(0,1).equals("@")) {
                    //String worde = "";
                    String[] destinations = words[1].split("\\@");

                    sendfile(destinations, words[2]);
                }
                break;
            case "image"    :
                if(words[1].substring(0,1).equals("@")) {
                    String worde = "";
                    String[] destinations = words[1].split("\\@");

                    String pack="";
                    for(int j=2;j<words.length;j++)
                    {
                        pack=pack+words[j];
                    }

                    //sendfile_(destinations,pack);


                }
                break;

        }





    }

    private void sendfile_(String[] destination, String word) {
        System.out.println("test1  "+word );
        byte[] buffer = new byte[20048];
        ArrayList<Utilisateur> des=new ArrayList<>();


        try {


            String messagee = new String(buffer, 0, buffer.length).trim();

            String id = "";

            for (int i = 0; i < client_connected.size(); i++) {

                if (client_connected.get(i).portNumber == client_port) {

                    id = client_connected.get(i).getName();
                }

            }


            byte[] daaa = ("file2|"+id+":|").getBytes();
            byte[] da=word.getBytes();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(daaa);
            baos.write(da);
            baos.flush();
            byte[] dataa=baos.toByteArray();
            ArrayList<Utilisateur> listdestinationconnected=getamisconnected(id);


            String ch="";
            for(int i=0;i<listdestinationconnected.size();i++)
            {
                for (String name:destination
                ) {

                    if(listdestinationconnected.get(i).getName().equals(name) && (listdestinationconnected.get(i).getPortNumber() != client_port))
                    {
                        ch=ch+" "+listdestinationconnected.get(i).getName();
                        des.add(listdestinationconnected.get(i));
                    }
                }

            }
            for (int i = 0; i < des.size(); i++) {
                if (des.get(i).getPortNumber() != client_port ) {

                    InetAddress cl_address = des.get(i).receivePacketAddress;

                    int cl_port = des.get(i).portNumber;

                    packet = new DatagramPacket(dataa, dataa.length, cl_address, cl_port);
                    socket.send(packet);
                    System.out.println("send message" + new String(packet.getData()));
                }
            }


        } catch (Exception e) {
            System.err.println(e);
        }

    }


    public void sendfile(String[] destination,String word){

        byte[] buffer = new byte[BUFFER];
        ArrayList<Utilisateur> des=new ArrayList<>();


        try {


            String messagee = new String(buffer, 0, buffer.length).trim();
            String id = "";
            System.out.println("me port "+client_port);
            System.out.println("sixe"+des.size());
            for (int i = 0; i < client_connected.size(); i++) {
                System.out.println("list port "+client_connected.get(i).portNumber);
                if (client_connected.get(i).portNumber == client_port) {
                    System.out.println("i" + i);
                    id = client_connected.get(i).getName();
                }

            }

            System.out.println("id "+id);
            System.out.println(id + " : " + word);
            byte[] dataa = ("file|"+id+":|"+ word).getBytes();

            ArrayList<Utilisateur> listdestinationconnected=getamisconnected(id);
            //ArrayList<Utilisateur> destinationfinal=new ArrayList<>();


           /* for (int i = 0; i < des.size(); i++) {
                for (int j = 0; j < listdestinationconnected.size(); j++) {
                    if (listdestinationconnected.get(j).getName().equals(des.get(i).getName())) {
                        System.out.println("wlah");
                        destinationfinal.add(des.get(i));
                    }
                }
            }

            */

            String ch="";
            for(int i=0;i<listdestinationconnected.size();i++)
            {
                for (String name:destination
                ) {

                    if(listdestinationconnected.get(i).getName().equals(name) && (listdestinationconnected.get(i).getPortNumber() != client_port))
                    {
                        ch=ch+" "+listdestinationconnected.get(i).getName();
                        des.add(listdestinationconnected.get(i));
                    }
                }

            }
            for (int i = 0; i < des.size(); i++) {
                if (des.get(i).getPortNumber() != client_port ) {

                    InetAddress cl_address = des.get(i).receivePacketAddress;

                    int cl_port = des.get(i).portNumber;
                    System.out.println(cl_address + " adress ");

                    System.out.println(cl_port + " port ");
                    packet = new DatagramPacket(dataa, dataa.length, cl_address, cl_port);
                    socket.send(packet);
                    System.out.println("send message5" + new String(packet.getData()));
                }
            }

            if(des.size()>0){



                byte[] dataaa = ("file|"+"you("+ch+"):|"+ word).getBytes();

                System.out.println(clientAddress+ " adress ");

                System.out.println(client_port + " port ");
                packet = new DatagramPacket(dataaa, dataaa.length, clientAddress, client_port);
                socket.send(packet);
                System.out.println("send message" + new String(packet.getData()));


            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private void addamis(String word) throws SQLException {
        PreparedStatement r = connection.prepareStatement("insert into amis(user,ami) values (?,?)");
        System.out.println("la2");
        String name = "";
        for (int i = 0; i < client_connected.size(); i++) {
            if (client_connected.get(i).getPortNumber() == client_port) {
                name = client_connected.get(i).getName();
                System.out.println("res"+name);
            }
        }
        r.setString(1, name);
        r.setString(2, word);
        System.out.println("test add");
        int row=r.executeUpdate();
        envoyerList();

        System.out.println("fin add");


    }
}
