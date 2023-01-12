package Finale;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;

public class Window  extends JFrame{
    JButton deconecte;
     JButton ajouter;

     JButton sendButton;

    JScrollPane pane2;
    File file;
    JTextPane room_field;
    private JPanel pan;
    private JTextPane message_field;

     JList list1;
    private JButton browsButton;
    JTextPane connected_field;

    String host_name;
    boolean message_is_ready = false;
    String message = "";
    private void browseButtonActionPerformed(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();

        int res = fc.showOpenDialog(null);

        try {
            if (res == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                message_field.setText(file.getPath());
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "You must select one file to be the reference.", "Attention...",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception iOException) {
        }

    }

    public Window(){

        list1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        host_name="Localhost";
        setSize(800, 600);

        setTitle("UDP Chat room");
        setLocationRelativeTo(null);
        room_field.setEditable(false);



        browsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseButtonActionPerformed(e);
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!message_is_ready) {
                    List<String> selectedTags = list1.getSelectedValuesList();
                    for (String tag : selectedTags) {
                        System.out.println(tag);
                        message = message + tag;
                    }


                    message = message + " " + message_field.getText().trim();


                    message_field.setText(null);
                }
                    if (!message.equals(null) && !message.equals("")) {
                        message_is_ready = true;
                    }
                }

        });

        setContentPane(pan);
        setVisible(true);
    }

    public void displayMessage(String receivedMessage) {
        StyledDocument doc = room_field.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), receivedMessage + "\n", null);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }
    public void displayFile(Icon icon,String name) {
        StyledDocument doc = room_field.getStyledDocument();
        try {



            doc.insertString(doc.getLength(),name +new ImageIcon()+"\n",null);




        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }




    public void setMessageReady(boolean messageReady) {
        this.message_is_ready = messageReady;
    }

    public String getMessage() {
        return message;
    }

    public String getHostName() {
        return host_name;
    }


    public void setMessage(String  o) {
        this.message=o;
    }
}
