package Finale;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
//global intialisation
    String name;
    String password;
    DatagramSocket socket;
    private String hostName;
    public final static int PORT = 2001;
    byte buffer[];



    //send obj user
    private void sendMessage(Utilisateur_ obj) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        byte buffer[] = baos.toByteArray();
        InetAddress address = InetAddress.getByName(hostName);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
        System.out.println("sending message");
        socket.send(packet);

    }
    //send String
    private void sendMessage2(String s) throws Exception {
        byte buffer[] = s.getBytes();
        InetAddress address = InetAddress.getByName(hostName);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
        socket.send(packet);
        System.out.println(new String(packet.getData()));
    }

//construct
    public Client(DatagramSocket sock, String host) throws Exception {

        socket = sock;
        hostName = host;
        buffer = new byte[1024];


    }



    public void form_login() throws Exception {
        JLabel auth;
        JLabel pseudo;
        JLabel resultat;
        JLabel password1;
        JTextField tpseudo;
        JPasswordField tpass;
        JButton button;


        JFrame frame=new JFrame();
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        Font fb = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        Font fauth = new Font(Font.SANS_SERIF, Font.BOLD, 28);
        resultat = new JLabel(" ");
        auth = new JLabel("Login");
        auth.setBounds(50, 10, 400, 70);
        auth.setFont(fauth);
        JPanel panel = new JPanel(null);
        JPanel panel2 = new JPanel(null);
        panel2.add(auth);

        panel.add(resultat);
        panel.add(auth);
        pseudo = new JLabel("Name");
        pseudo.setFont(font);
        password1 = new JLabel("Password");
        password1.setFont(font);

        tpseudo = new JTextField();
        tpass = new JPasswordField();
        button = new JButton("Login");
        button.setFont(fb);
        pseudo.setBounds(50, 100, 90, 40);
        password1.setBounds(50, 200, 90, 40);
        tpseudo.setBounds(150, 100, 170, 40);
        tpass.setBounds(150, 200, 170, 40);
        button.setBounds(190, 250, 100, 40);

        resultat.setBounds(150, 300, 300, 70);
        resultat.setFont(fb);
        resultat.setForeground(Color.red);
        button.setBackground(Color.white);
        button.setForeground(Color.blue);
        //panel.add(resultat);
        panel.add(pseudo);

        panel.add(password1);
        panel.add(tpseudo);
        panel.add(tpass);

        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // while (reponce == "refuser") {


                name = tpseudo.getText().trim();
                password = tpass.getText().trim();


                try {

                    Utilisateur_ user=new Utilisateur_(name,password);
                    System.out.println("login|"+user.getName()+"|"+ user.getPassword());
                   String t="login|"+user.getName()+"|"+ user.getPassword();

                    sendMessage2(t);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    System.out.println("recieve message");
                    String received = new String(packet.getData()).trim();
                    System.out.println(new String(packet.getData()).trim());
                    if(received.equals("reussir")){

                        frame.dispose();
                        //create frame chat
                        Window window=new Window();
                        window.deconecte.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                try {
                                    sendMessage2("quitter");
                                    window.dispose();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }});

                        window.ajouter.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                try {
                                    form_addami();
                                    //window.dispose();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }});
                        String host = window.getHostName();
                        window.setTitle("Client: " + name);


                        MessageReceiver receiver = new MessageReceiver(socket, window);
                        MessageSender sender = new MessageSender(socket, host, window);
                        Thread receiverThread = new Thread(receiver);
                        Thread senderThread = new Thread(sender);
                        receiverThread.start();
                        senderThread.start();


                    }
                    else {
                        resultat.setText("votre nom ou mot de passe incorrect ");
                        System.out.println("refuser");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }





            }
        });
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 400);
        frame.setContentPane(panel);
    }

    //form to add friend
    public void form_addami() throws Exception {

        sendMessage2("invitation");}


//inscription form

    public void form_register() throws Exception {

        JLabel auth;
        JLabel pseudo;
        JLabel resultat;
        JLabel password1;
        JLabel password2;
        JTextField tpseudo;
        JPasswordField tpass;
        JPasswordField cpass;
        JButton button;
        JFrame frame=new JFrame();

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        Font fb = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        Font fauth = new Font(Font.SANS_SERIF, Font.BOLD, 28);
        resultat = new JLabel("");


        auth = new JLabel("Register");
        auth.setBounds(50, 10, 400, 70);
        auth.setFont(fauth);
        JPanel panel = new JPanel(null);
        JPanel panel2 = new JPanel(null);
        panel2.add(auth);
        panel.add(resultat);
        panel.add(auth);
        pseudo = new JLabel("name");
        pseudo.setFont(font);
        password1 = new JLabel("Password");
        password1.setFont(font);
        password2 = new JLabel("Confirmed password");
        password2.setFont(font);

        tpseudo = new JTextField();
        tpass = new JPasswordField();
        cpass = new JPasswordField();
        button = new JButton("register");
        button.setFont(fb);
        pseudo.setBounds(50, 100, 90, 40);
        password1.setBounds(50, 150, 90, 40);
        password2.setBounds(50, 200, 210, 40);
        tpseudo.setBounds(250, 100, 170, 40);
        tpass.setBounds(250, 150, 170, 40);
        cpass.setBounds(250, 200, 170, 40);
        button.setBounds(250, 250, 100, 40);
        resultat.setBounds(200, 300, 300, 70);
        resultat.setFont(fb);
        resultat.setForeground(Color.red);
        button.setBackground(Color.white);
        button.setForeground(Color.blue);

        panel.add(pseudo);
        panel.add(tpseudo);


        panel.add(password1);
        panel.add(tpass);
        panel.add(password2);
        panel.add(cpass);

        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String nom=tpseudo.getText().trim();
                  String pass = tpass.getText().trim();
                  String confrmed=cpass.getText().trim();
                  if(pass.equals(confrmed)){
                      System.out.println("register|"+nom+"|"+pass);
                      String t="register|"+nom+"|"+pass;

                      try {
                          sendMessage2(t);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                      try {
                          socket.receive(packet);
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                      System.out.println("recieve message");
                      String received = new String(packet.getData()).trim();
                      System.out.println(new String(packet.getData()).trim());
                      if(received.equals("reussir")) {

                          frame.dispose();
                          try {
                              form_login();
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                      else {
                          resultat.setText("  vous avez deja un compte");
                          System.out.println("refuser");
                      }



                  }
                  else {
                      resultat.setText(" password non identique");
                      System.out.println("refuser");
                  }
            }

        });

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 400);
        frame.setContentPane(panel);
    }

    //the first form
 public void home(){
     JFrame home;
     JPanel panel;
     JButton register;
     JButton login;
     home=new JFrame();
     Font fb = new Font(Font.SANS_SERIF, Font.BOLD, 12);
     register = new JButton("Register");
     register.setFont(fb);
     register.setBounds(50, 100, 100, 40);
     login = new JButton("Login");
     login.setFont(fb);
     login.setBounds(50, 200, 100, 40);
     panel=new JPanel(null);
     panel.add(register);
     panel.add(login);
     register.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent actionEvent) {
             home.dispose();
             try {
                form_register();

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     });
     login.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent actionEvent) {
             home.dispose();
             try {
                 form_login();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     });

     home.setVisible(true);
     home.setLocationRelativeTo(null);
     home.setSize(600, 400);
     home.setContentPane(panel);

 }





    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


}
