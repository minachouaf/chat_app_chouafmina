package Finale;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class MessageSender implements Runnable {
    public final static int PORT = 2001;
    private DatagramSocket socket;
    private String hostName;
    private Window window;

    //construct
    MessageSender(DatagramSocket sock, String host, Window win) {
        socket = sock;
        hostName = host;
        window = win;
    }
//send binary file
    public void envoiFile_(String host, int port, String chemin, String mess) {
        FileInputStream fis;
        try {
            File f=new File(chemin);
            fis = new FileInputStream(window.file);
            BufferedInputStream b = new BufferedInputStream(fis);

            InetAddress IPHost = InetAddress.getByName(host);
            byte[] buffer = new byte[1024];

            int nbr = 1;
            int bytesRead;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            String messge = "file2|" + mess + "|";

            byte a[] = messge.getBytes();
            baos.write(a);





            while ((bytesRead = b.read(buffer)) != -1) {


                baos.write(buffer);
               // System.out.println("buffer" + bytesRead);
                byte c[]  = baos.toByteArray();
                //System.out.println("boas" + c.length);
                DatagramPacket packet = new DatagramPacket(c, c.length, IPHost, port);
                socket.send(packet);

                System.out.println("mini-packet envoy�: " + nbr++);
            }


            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bao.write(a);
            bao.write(new byte[0]);
            byte c[] = bao.toByteArray();
            DatagramPacket packet = new DatagramPacket(c, c.length, IPHost, port);
            socket.send(packet);
            System.out.println("Tout est Envoy�");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String s) throws Exception {
        byte buffer[] = s.getBytes();
        System.out.println("send");
        InetAddress address = InetAddress.getByName(hostName);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
        socket.send(packet);
        System.out.println(new String(packet.getData()));
    }

    //verify message if its a path or string message
    public static boolean notPathValid(String path) {

        try {

            Paths.get(path);


        } catch (InvalidPathException ex) {
            return true;
        }

        return false;
    }


//send text file
    public void envoiFile(String host, int port, String chemin, String mess) {
        //chemin = chemin.replace("\\", "\\\\");

        FileInputStream fis;
        System.out.println(window.file);
        try {
            fis = new FileInputStream(window.file);
            byte[] clientBuffer = new byte[65024];
            byte[] a = mess.getBytes();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            fis.read(clientBuffer);
            baos.write(a);
            baos.write(clientBuffer);
           // baos.flush();
            byte c[] = baos.toByteArray();


            InetAddress IPHost = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(c,
                    c.length, IPHost, port);
            fis.close();
            socket.send(packet);
            System.out.println(new String(packet.getData()).trim());
            System.out.println("Fichier a �t� envoy� !");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void envoiImg(String host, int port, String chemin, String ext, String destinations) {
        BufferedImage img;
        try {

            img = ImageIO.read(new File(chemin));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, ext, baos);
            baos.flush();
            byte[] buffer = baos.toByteArray();

            ByteArrayOutputStream bo=new ByteArrayOutputStream();
            byte[] a = destinations.getBytes();
            bo.write(a);
            InetAddress IPHost = InetAddress.getByName("127.0.0.1");
            byte[] buff = new byte[1];
            int c = 0;
            int nbr = 1;
            for (int i = 0; i < buffer.length; i++) {
                buff[c] = buffer[i];
                c++;
                if (c == 1) {
                    bo.write(buff);
                   byte[] t= bo.toByteArray();
                    bo.flush();
                    DatagramPacket packet = new DatagramPacket(t, t.length, IPHost, port);
                    buff = new byte[1];

                    socket.send(packet);
                    System.out.println("Mini-packet Envoyer : " + nbr++ + "  " + c);
                    //System.out.println("eee "+new String(packet.getData()).trim()+ "tt=== "+packet.getLength());
                    c = 0;
                }
            }
            if (c > 0) {
                bo.write(buff);
                byte[] t= bo.toByteArray();
                bo.flush();
                DatagramPacket packet = new DatagramPacket(t, t.length, IPHost, port);
                socket.send(packet);
                System.out.println("Dernier mini-packet Envoy� : " + c);
            }
            DatagramPacket packet = new DatagramPacket(new byte[0], 0, IPHost, port);
            socket.send(packet);
            System.out.println("Tout est Envoy�");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void run() {


        while (true) {
            try {
                while (!window.message_is_ready) {
                    Thread.sleep(100);
                }

                String message = window.getMessage();

                window.setMessage("");
                System.out.println(message);
                String[] words = message.split(" ");
                String destination = words[0];

                String text = message.substring(destination.length());

               if (notPathValid(text)) {
                  String  chemin = text.replace("\\", "\\\\");
                   //String cheminencode = URLEncoder.encode(chemin,"UTF-8");
                   File file = new File(text);
                   String fileName = file.getName();
                   int index = fileName.lastIndexOf(".");
                   String fileExtension="";
                   if (index > 0) {
                      fileExtension = fileName.substring(index + 1);
                   }
                   if(fileExtension.equals("txt")) {
                       System.out.println("tt"+chemin);
                       envoiFile("localhost", PORT, chemin, "file|" + destination + "|");
                   }
                   else if(fileExtension.equals("png")){
                       System.out.println("tt"+chemin);
                       System.out.println("image");
                       //envoiImg("localhost", PORT, "C:\\Users\\HUAWEI D14\\IdeaProjects\\chat_app\\src\\img.png","png","image|" + destination + "|");
                   }
                   else{
                       System.out.println("binaire");
                       System.out.println("tt"+chemin);
                       envoiFile_("localhost", PORT, chemin,destination);
                   }

                } else {


                   System.out.println(text);

                   sendMessage("string|" + destination + "|" + text);
                    }
                   window.setMessageReady(false);





            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }}