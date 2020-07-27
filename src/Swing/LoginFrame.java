package Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import entity.User;

/**
 * A frame with a menu whose File->Connect action shows a password dialog.
 */
public class LoginFrame extends JFrame
{
   public static final int TEXT_ROWS = 20;
   public static final int TEXT_COLUMNS = 40;
   private PasswordChooser dialog = null;
   private JTextArea textArea;

   public LoginFrame()
   {
      // construct a File menu

      var mbar = new JMenuBar();
      setJMenuBar(mbar);
      var fileMenu = new JMenu("File");
      mbar.add(fileMenu);

      // add Connect and Exit menu items

      var connectItem = new JMenuItem("Login");
      connectItem.addActionListener(new ConnectAction());
      fileMenu.add(connectItem);

      // the Exit item exits the program

      var exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(event -> System.exit(0));
      fileMenu.add(exitItem);

      textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
      add(new JScrollPane(textArea), BorderLayout.CENTER);
      pack();
   }

   /**
    * The Connect action pops up the password dialog.
    */
   private class ConnectAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         // if first time, construct dialog

         if (dialog == null) dialog = new PasswordChooser();

         // set default values
         dialog.setUser(new User("name", "stu/tea"));

         // pop up dialog
         if (dialog.showDialog(LoginFrame.this, "Login"))
         {
            // if accepted, retrieve user input
            User u = dialog.getUser();
            textArea.append("user name = " + u.getUserName() + ", type= "
               + u.getType()+ "\n");
         }
      }
   }
}
