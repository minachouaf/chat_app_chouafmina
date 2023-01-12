package Finale;



import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MessageReceiver implements Runnable {
    DatagramSocket socket;
    byte buffer[];
    Window window;



    MessageReceiver(DatagramSocket sock, Window win) {
        socket = sock;
        buffer = new byte[20048];
        window = win;

    }


    public void receptFile(String received) {
        String chemin = "Copie_Monfichier_.txt";
        try {


            String[] words = received.split("\\|",5);

            System.out.println("Fichier recu..");
            FileOutputStream fos = new FileOutputStream(chemin);
            byte[] data = words[2].getBytes(StandardCharsets.UTF_8);
            fos.write(data);



             FileSystemView view = FileSystemView.getFileSystemView();

            window.displayMessage(words[1]+chemin);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        String chemin="copier.docx";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(chemin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        System.out.println("start");
        while (true) {
            try {
                System.out.println("recive paket");
                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                socket.receive(packet);

                System.out.println("recu");
                String received = new String(packet.getData(), 0, packet.getLength()).trim();

                //list of connected freinds
                if (received.startsWith("list")) {

                    String[] words = received.split(" ");
                    ArrayList<String> liste1 = new ArrayList<>();
                    for (int i = 1; i < words.length; i++) {
                        System.out.println(words[i]);
                        liste1.add("@" + words[i]);

                    }
                    window.list1.setListData(liste1.toArray());
                }
                //list of non_freinds====================
                else if (received.startsWith("nonamis")) {
                    String[] words = received.split(" ");
                    String s1[] = new String[words.length - 1];

                    for (int i = 1; i < words.length; i++) {
                        System.out.println(words[i]);
                    }
                    for (int i = 0; i < words.length - 1; i++) {
                        s1[i] = words[i + 1];
                    }
                    form_add_freind(s1);



                } else if (received.startsWith("file2")) {
                    System.out.println("aggs");
                    String[] mesmots = received.split("\\|",5);
                    String pack="";
                    System.out.println("0 "+mesmots[0]);
                    System.out.println("1 "+mesmots[1]);

                    for(int j=2;j<mesmots.length;j++)
                    {
                        System.out.println("notre "+mesmots[j]);
                        pack=pack+mesmots[j];
                    }
                    byte[] t=pack.getBytes();

                    System.out.println("receive Mini-packet :   ");



                    try {

                    if(t.length>0) {


                        bos.write(t, 0, t.length);


                    }
                    else{
                        System.out.println("fin");
                        bos.flush();
                        bos.close();
                    }

                   // receptFile_(received);
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } }else if (received.startsWith("file")) {
                    receptFile(received);
                }
                else if (received.startsWith("image")) {
                    {

                    }
                }

 else {
     System.out.println(received);
     window.displayMessage(received);
 }

            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    private void form_add_freind(String[] s1) {
        JComboBox combobox;
        JLabel l1, l2;
        JFrame f = new JFrame("frame");
        f.setLayout(new FlowLayout());
        combobox = new JComboBox(s1);
        l1 = new JLabel("select user");
        l2 = new JLabel("");
        l2.setForeground(Color.blue);
        JPanel p = new JPanel();
        p.add(l1);
        p.add(combobox);
        p.add(l2);
        f.add(p);
        f.setSize(600, 400);
        f.setLocationRelativeTo(null);
        int resultat = JOptionPane.showConfirmDialog(
                null, p, "Add friend", JOptionPane.OK_CANCEL_OPTION);
        combobox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == combobox) {

                    l2.setText(" " + combobox.getSelectedItem());
                    System.out.println(combobox.getSelectedItem());
                }
            }
        });

        if (resultat == JOptionPane.OK_OPTION) {
            String nomuser = "add|" + combobox.getSelectedItem();

            byte buffer[] = nomuser.getBytes();
            InetAddress address = null;
            try {
                address = InetAddress.getByName("localhost");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length, address, 2001);
            try {
                socket.send(packet2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(new String(packet2.getData()));
        }

    }
}

