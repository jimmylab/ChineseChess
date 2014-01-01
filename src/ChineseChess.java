import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChineseChess
{
  static ChineseChess app;
  JFrame mainFrame;
  ChineseChessCtrl control;
  public JMenuItem menuRevert;
  public JMenuItem menuRedo;

  private ChineseChess()
  {
    mainFrame = new JFrame("Chinese Chess");
    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    control = new ChineseChessCtrl(mainFrame);
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    KeyStroke ctrl_n = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK);
    JMenuItem item = new JMenuItem("New");
    item.setActionCommand("NEW");
    item.setAccelerator(ctrl_n);
    item.addActionListener(control);
    menu.add(item);
    
    KeyStroke shift_n = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.SHIFT_MASK);
    item = new JMenuItem("New From File");
    item.setActionCommand("NEW_FROM_FILE");
    item.setAccelerator(shift_n);
    item.addActionListener(control);
    menu.add(item);
    
    menu.addSeparator();
    KeyStroke ctrl_s = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK);
    item = new JMenuItem("Save");
    item.setActionCommand("SAVE");
    item.setAccelerator(ctrl_s);
    item.addActionListener(control);
    menu.add(item);
    
    
    menu.addSeparator();
    KeyStroke ctrl_q = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK);
    item = new JMenuItem("Exit");
    item.setActionCommand("EXIT");
    item.setAccelerator(ctrl_q);
    item.addActionListener(control);
    menu.add(item);
    
    menuBar.add(menu);
    
    menu = new JMenu("Operations");
    KeyStroke ctrl_z = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK);
    menuRevert = new JMenuItem("Revert");
    menuRevert.setActionCommand("REVERT");
    menuRevert.setEnabled(false);
    menuRevert.setAccelerator(ctrl_z);
    menuRevert.addActionListener(control);
    menu.add(menuRevert);
    menu.addSeparator();
    
    KeyStroke ctrl_y = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK);
    menuRedo = new JMenuItem("Redo");
    menuRedo.setActionCommand("REDO");
    menuRedo.setEnabled(false);
    menuRedo.setAccelerator(ctrl_y);
    menuRedo.addActionListener(control);
    menu.add(menuRedo);
    
    menuBar.add(menu);
    mainFrame.setJMenuBar(menuBar);

    Game game = new Game();
    mainFrame.getContentPane().setLayout(new GridBagLayout());
    BoardView.defaultBoardView().setGame(game);

    GridBagConstraints constrs = new GridBagConstraints();
    
    mainFrame.add(BoardView.defaultBoardView(), constrs);

    constrs.fill = GridBagConstraints.BOTH;
    constrs.gridwidth = constrs.gridheight = GridBagConstraints.REMAINDER;
    constrs.weighty = 1.0;
    mainFrame.setSize(320, 400);

    mainFrame.addWindowListener(control);
  }
  static public ChineseChess runningApplication()
  {
    if (app == null) app = new ChineseChess();
    return app;
  }

  private void start()
  {
      mainFrame.setVisible(true);
  }

  public static void main(String [] args)
  {
    ChineseChess.runningApplication().start();
  }
}

class ChineseChessCtrl extends WindowAdapter implements ActionListener
{
  JFrame mainFrame;
  public ChineseChessCtrl(JFrame frame) { mainFrame = frame; }
  @Override public void actionPerformed(ActionEvent event)
  {
    if (event.getActionCommand().compareTo("NEW") == 0)
      {
        Game game = new Game();
        BoardView.defaultBoardView().setGame(game);
        BoardView.defaultBoardView().setColor('r');
      }
    if (event.getActionCommand().compareTo("REVERT") == 0) {
        BoardView.defaultBoardView().game.revert();
    }
    if (event.getActionCommand().compareTo("REDO") == 0) {
        BoardView.defaultBoardView().game.redo();
    }
    if (event.getActionCommand().compareTo("SAVE") == 0) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter( new FileNameExtensionFilter("Chinese Chess File (*.chess)", "chess") );
        chooser.setDialogTitle("Save the Game to...");
        chooser.setDialogType(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.showDialog(null, null);
        File target = chooser.getSelectedFile();
        String path = target.getAbsolutePath();
        if ( !path.toLowerCase().endsWith(".chess") ) {
            path+=".chess";
            target = new File(path);
        }
        if (target.exists() &&
                JOptionPane.showConfirmDialog(mainFrame, 
                target.getName()+" already exists, overwrite?", "Overwrite Confirm", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) !=0 )
            return;
        try {
            FileOutputStream out = new FileOutputStream(path);
            ObjectOutputStream serialization = new ObjectOutputStream(out);
            serialization.writeObject(BoardView.defaultBoardView().game);
            out.close();
            serialization.close();
            JOptionPane.showMessageDialog(mainFrame, "Successfully saved \""+path+'"');
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(mainFrame, 
                    "Failure occurred while saving the game!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    if (event.getActionCommand().compareTo("EXIT") == 0) {
        WindowEvent e = new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING);
        windowClosing(e);
    }
  }
  @Override public void windowClosing(WindowEvent event) {
        if (event.getID() == WindowEvent.WINDOW_CLOSING) {
            //处理Jframe关闭事件
            int choice = JOptionPane.showConfirmDialog(mainFrame, 
              "Exit Confirm", "Really Exit?", 
              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if ( choice == 0 ) System.exit(0);
        }else{
            //忽略其他事件，交给JFrame处理
            super.windowClosing(event);
        }
    }
}
