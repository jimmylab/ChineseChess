import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class BoardView extends JPanel implements GameObserver
{
  static BoardView defaultBoard;
  // Identifies what type of highlight is being made, colors may be different.
  public static final short H_NONE    = 0;
  public static final short H_SEL     = 1;
  public static final short H_CAPTURE = 2;
  public static final short H_DEST    = 3;
  static final short BOARD_WIDTH = 9;
  static final short BOARD_HEIGHT = 10;
  boolean reverse;

  PiecePanel board[][];
  Game	 game;
  Hashtable<String,Image> pieceImages;

  public static BoardView defaultBoardView()
  {
    if (defaultBoard == null) defaultBoard = new BoardView();
    return defaultBoard;
  }

  private BoardView()
  {
    super();
    reverse = true;
    setLayout(new GridLayout(10,9));
    board = new PiecePanel[BOARD_HEIGHT][BOARD_WIDTH];
    for (int i = 0; i < BOARD_HEIGHT; i++)
      for (int j = 0; j < BOARD_WIDTH; j++)
        {
          add((board[i][j] = new PiecePanel()));
          board[i][j].addMouseListener(BoardViewControl.defaultControl());
          board[i][j].setBackground(UserProperties.getColorProperty("BoardColor"));
        }
    setGrid();
    setPanelCoords(reverse);

    pieceImages = new Hashtable<String,Image>();
    pieceImages.put("P", (new ImageIcon(getClass().getResource("pieces/rsoldier.png")).getImage()));
    pieceImages.put("p", (new ImageIcon(getClass().getResource("pieces/bsoldier.png")).getImage()));
    pieceImages.put("R", (new ImageIcon(getClass().getResource("pieces/rchariot.png")).getImage()));
    pieceImages.put("r", (new ImageIcon(getClass().getResource("pieces/bchariot.png")).getImage()));
    pieceImages.put("H", (new ImageIcon(getClass().getResource("pieces/rhorse.png")).getImage()));
    pieceImages.put("h", (new ImageIcon(getClass().getResource("pieces/bhorse.png")).getImage()));
    pieceImages.put("E", (new ImageIcon(getClass().getResource("pieces/relephant.png")).getImage()));
    pieceImages.put("e", (new ImageIcon(getClass().getResource("pieces/belephant.png")).getImage()));
    pieceImages.put("A", (new ImageIcon(getClass().getResource("pieces/rmandarin.png")).getImage()));
    pieceImages.put("a", (new ImageIcon(getClass().getResource("pieces/bmandarin.png")).getImage()));
    pieceImages.put("K", (new ImageIcon(getClass().getResource("pieces/rgeneral.png")).getImage()));
    pieceImages.put("k", (new ImageIcon(getClass().getResource("pieces/bgeneral.png")).getImage()));
    pieceImages.put("C", (new ImageIcon(getClass().getResource("pieces/rcannon.png")).getImage()));
    pieceImages.put("c", (new ImageIcon(getClass().getResource("pieces/bcannon.png")).getImage()));

  }
  public void setBoardColor(Color color)
  {
    for (int i = 0; i < BOARD_HEIGHT; i++)
      for (int j = 0; j < BOARD_WIDTH; j++)
        board[i][j].setBackground(color);
  }
  public Image getPieceImage(char piece)
  {
    return (Image)pieceImages.get(new String("")+piece);
  }
  private void setGrid()
  {
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image northWestCorner = tk.createImage(getClass().getResource("board_images/top_left.png"));
    Image northEastCorner = tk.createImage(getClass().getResource("board_images/top_right.png"));
    Image southEastCorner = tk.createImage(getClass().getResource("board_images/bottom_right.png"));
    Image southWestCorner = tk.createImage(getClass().getResource("board_images/bottom_left.png"));
    Image north = tk.createImage(getClass().getResource("board_images/top_side.png"));
    Image east  = tk.createImage(getClass().getResource("board_images/right_side.png"));
    Image eastStart = tk.createImage(getClass().getResource("board_images/right_start.png"));
    Image south = tk.createImage(getClass().getResource("board_images/bottom_side.png"));
    Image west = tk.createImage(getClass().getResource("board_images/left_side.png"));
    Image westStart = tk.createImage(getClass().getResource("board_images/left_start.png"));
    Image northBank = tk.createImage(getClass().getResource("board_images/river_north.png"));
    Image southBank = tk.createImage(getClass().getResource("board_images/river_south.png"));

    Image center = tk.createImage(getClass().getResource("board_images/cross.png"));
    Image centSt = tk.createImage(getClass().getResource("board_images/cross_start.png"));

    // North Palace
    Image northPalaceNW = tk.createImage(getClass().getResource("board_images/north_palace_nw.png"));
    Image northPalaceNE = tk.createImage(getClass().getResource("board_images/north_palace_ne.png"));
    Image northPalaceSE = tk.createImage(getClass().getResource("board_images/north_palace_se.png"));
    Image northPalaceSW = tk.createImage(getClass().getResource("board_images/north_palace_sw.png"));

    Image palaceCenter = tk.createImage(getClass().getResource("board_images/palace_center.png")); // change!!

    // SouthPalace
    Image southPalaceNW = tk.createImage(getClass().getResource("board_images/south_palace_nw.png"));
    Image southPalaceNE = tk.createImage(getClass().getResource("board_images/south_palace_ne.png"));
    Image southPalaceSE = tk.createImage(getClass().getResource("board_images/south_palace_se.png"));
    Image southPalaceSW = tk.createImage(getClass().getResource("board_images/south_palace_sw.png"));

    for (int i = 0; i < BOARD_HEIGHT; i++)
      {
        for (int j = 0; j < BOARD_WIDTH; j++)
          {
            if (i == 0)
              {
                if (j == 0) board[i][j].setGrid(northWestCorner);
                else if (j == BOARD_WIDTH-1) board[i][j].setGrid(northEastCorner);
                else if (j == 3) board[i][j].setGrid(northPalaceNW);
                else if (j == 5) board[i][j].setGrid(northPalaceNE);
                else
                  board[i][j].setGrid(north);
              }
            else if (i == BOARD_HEIGHT-1)
              {
                if (j == 0) board[i][j].setGrid(southWestCorner);
                else if (j == BOARD_WIDTH-1) board[i][j].setGrid(southEastCorner);
                else if (j == 3) board[i][j].setGrid(southPalaceSW);
                else if (j == 5) board[i][j].setGrid(southPalaceSE);
                else
                  board[i][j].setGrid(south);
              }
            else if (j == 0)
              {
                if (i == 3 || i == 6)
                  board[i][j].setGrid(westStart);
                else
                  board[i][j].setGrid(west);
              }
                
            else if (j == BOARD_WIDTH-1)
              {
                if (i == 3 || i == 6)
                  board[i][j].setGrid(eastStart);
                else
                  board[i][j].setGrid(east);
              }
            else if (i == 4) board[i][j].setGrid(northBank);
            else if (i == 5) board[i][j].setGrid(southBank);
            else if (i == 2)
              {
                if (j == 3) board[i][j].setGrid(northPalaceSW);
                else if (j == 5) board[i][j].setGrid(northPalaceSE);
                else if (j == 1 || j == BOARD_WIDTH - 2) board[i][j].setGrid(centSt);
                else
                  board[i][j].setGrid(center);
              }
            else if (i == BOARD_HEIGHT-3)
              {
                if (j == 3) board[i][j].setGrid(southPalaceNW);
                else if (j == 5) board[i][j].setGrid(southPalaceNE);
                else if (j == 1 || j == BOARD_WIDTH - 2) board[i][j].setGrid(centSt);
                else
                  board[i][j].setGrid(center);
              }
            else if (i == 3 || i == 6)
              {
                switch (j)
                  {
                  case 2:case 4:case 6:
                    board[i][j].setGrid(centSt);
                    break;
                  default:
                    board[i][j].setGrid(center);
                  }
              }
            else if ((i == 1 && j == 4) || (i == 8 && j == 4))
              board[i][j].setGrid(palaceCenter);
            else
              board[i][j].setGrid(center);
          }
      }
  }
  public void mate(short which, char color)
  {
    String message = null;
    String col = null;
    if (color == 'r') col = "Red";
    else col = "Blue";
    if (which == Game.MATE_CHECK)
      {
        message = col+" has been checkmated.";
      }
                
    else
      {
        message = col+" has been stalemated.";
      }
    JOptionPane.showMessageDialog(this, message, "Game Over - "+col+" loses.", JOptionPane.INFORMATION_MESSAGE);
  }
  public void newMove(String move)
  {
    SquareCoord origin = new SquareCoord(move.substring(0, move.indexOf('-')));
    SquareCoord destin = new SquareCoord(move.substring(move.indexOf('-')+1));

    clearHighlights();
    if (reverse)
      {
        Image pieceImage =  board[9-origin.getRow()][8-origin.getFile()].getPieceImage();
        char  piece = board[9-origin.getRow()][8-origin.getFile()].getPiece();
        board[9-origin.getRow()][8-origin.getFile()].setPieceImage(null);
        board[9-origin.getRow()][8-origin.getFile()].setPiece((char)0);
        board[9-origin.getRow()][8-origin.getFile()].highlight(H_SEL);
        if (board[9-destin.getRow()][8-destin.getFile()].occupied())
          board[9-destin.getRow()][8-destin.getFile()].highlight(H_CAPTURE);
        else
          board[9-destin.getRow()][8-destin.getFile()].highlight(H_DEST);
        board[9-destin.getRow()][8-destin.getFile()].setPieceImage(pieceImage);
        board[9-destin.getRow()][8-destin.getFile()].setPiece(piece);
      }
    else
      {
        Image pieceImage =  board[origin.getRow()][origin.getFile()].getPieceImage();
        char  piece = board[origin.getRow()][origin.getFile()].getPiece();
        board[origin.getRow()][origin.getFile()].setPieceImage(null);
        board[origin.getRow()][origin.getFile()].setPiece((char)0);
        board[origin.getRow()][origin.getFile()].highlight(H_SEL);
        if (board[destin.getRow()][destin.getFile()].occupied())
          board[destin.getRow()][destin.getFile()].highlight(H_CAPTURE);
        else
          board[destin.getRow()][destin.getFile()].highlight(H_DEST);
        board[destin.getRow()][destin.getFile()].setPieceImage(pieceImage);
        board[destin.getRow()][destin.getFile()].setPiece(piece);
      }
  }

  public void highlightSquare(int row, int col, short type)
  {
  }
  private void setPanelCoords(boolean reverse)
  {
    String coord;
    // reverse means red on bottom...
    for (int i = 0; i < BOARD_HEIGHT; i++)
      {
        for (int j = 0; j < BOARD_WIDTH; j++)
          {
            if (reverse)
              {
                coord = (char)('a'+j) + Integer.toString(10-i);
              }
            else
              {
                coord = (char)('i'-j) + Integer.toString(i+1);
              }
            board[i][j].setCoord(coord);
          }
      }
  }
  public PiecePanel getPiecePanel(String which)
  {
    SquareCoord sq = new SquareCoord(which);
    if (reverse)
      {
        return board[9-sq.getRow()][8-sq.getFile()];
      }
    else return board[sq.getRow()][sq.getFile()];
  }
  private String reverse(String in)
  {
    String out = new String("");
    for (int i = in.length() - 1; i >= 0; i--) out += in.charAt(i);
    return out;
  }

  public void setColor(char color)
  {
    if (color == 'b') reverse = false;
    else reverse = true;
    setPanelCoords(reverse);
    newPosition(game.getPosition());
  }
  public void newPosition(String fen)
  {
    clearHighlights();
    String pieces = fen.substring(0, fen.indexOf(' ')-1);
    if (reverse) pieces = reverse(pieces);
    int boardIndex = 0;
    for (int i = 0; i < pieces.length(); i++)
      {
        char c = pieces.charAt(i);
        if (c == '/') continue;
        if (c >= '0' && c <= '9')
          {
            for (int x = 0; x < (c - '0'); x++, boardIndex++)
              {
                board[boardIndex/9][boardIndex%9].setPieceImage(null);
                board[boardIndex/9][boardIndex%9].setPiece((char)0);
              }
          }
        else
          {
            Image im = (Image)pieceImages.get((Object)((new String(""))+c));
            if (im != null)
              {
                board[boardIndex/9][boardIndex%9].setPieceImage(im);
                board[boardIndex/9][boardIndex%9].setPiece(c);
                board[boardIndex/9][boardIndex%9].repaint();
              }
            boardIndex++;
          }
      }
  }
  public void clearHighlights()
  {
    for (int i = 0; i < BOARD_HEIGHT; i++)
      for (int j = 0; j < BOARD_WIDTH; j++)
        board[i][j].highlight(H_NONE);
  }
  public void setGame(Game gm)
  {
    if (game != null)
      {
        game.removeObserver(this);
        game = null;
      }
    game = gm;
    game.addGameObserver(this);
  }
  public Game getGame() { return game; }
  public class PiecePanel extends JPanel
  {
    Image pieceImage;
    char  piece;
    String coord;
    Image grid;
    int highlight;
    public PiecePanel()
    {
      highlight = BoardView.H_NONE;
      pieceImage = null;
      grid = null;
      piece = 0;
      setMinimumSize(new Dimension(33,33));
      setPreferredSize(new Dimension(33,33));
    }
    public void setPieceImage(Image i)
    {
      pieceImage = i;
    }
    public Image getPieceImage() { return pieceImage; }
    public void setGrid(Image i)
    {
      grid = i;
    }
    public boolean occupied() { return piece != 0; }
    public void setPiece(char p)
    {
      piece = p;
    }
    public char getPiece() { return piece; }
    public void setCoord(String c)
    {
      coord = c;
    }
    public String getCoord() { return coord; }
    public void paint(Graphics gc)
    {
      super.paint(gc);
      if (grid != null) gc.drawImage(grid,0,0,getWidth(),getHeight(),this);
      if (pieceImage != null)
        {
          gc.drawImage(pieceImage, 0, 0, getWidth(), getHeight(), this);
        }
      if (highlight != BoardView.H_NONE)
        {
          switch (highlight)
            {
            case BoardView.H_SEL:
              gc.setColor(Color.green);
              break;
            case BoardView.H_DEST:
              gc.setColor(Color.blue);
              break;
            case BoardView.H_CAPTURE:
              gc.setColor(Color.red);
              break;
            }
            gc.drawRect(0,0,getWidth()-1,getHeight()-1);
        }
    }
    public void highlight(short which) { highlight = which; repaint(); }
  }
}
