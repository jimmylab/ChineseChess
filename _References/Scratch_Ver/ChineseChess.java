import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChineseChess
{
  static ChineseChess app;
  JFrame mainFrame;
  ChineseChessCtrl control;

  private ChineseChess()
  {
    mainFrame = new JFrame("Chinese Chess");
    control = new ChineseChessCtrl(mainFrame);
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem item = new JMenuItem("New");
    item.setActionCommand("NEW");
    item.addActionListener(control);
    menu.add(item);

    menu.addSeparator();
    item = new JMenuItem("Exit");
    item.setActionCommand("EXIT");
    item.addActionListener(control);
    menu.add(item);

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
  public void actionPerformed(ActionEvent event)
  {
    if (event.getActionCommand().compareTo("NEW") == 0)
      {
        Game game = new Game();
        BoardView.defaultBoardView().setGame(game);
        BoardView.defaultBoardView().setColor('r');
      }
    if (event.getActionCommand().compareTo("EXIT") == 0)
      System.exit(0);
  }
}
