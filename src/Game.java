import	java.util.*;

public class Game
{
  static final String legalChars = "RHEAKCPrheakcp";
  public static final int BOARD_WIDTH  =  9;
  public static final int BOARD_HEIGHT = 10;
  public static final char RED  = 'r';
  public static final char BLUE = 'b';
  static final char TURNSWITCH = RED^BLUE;
  public static final int MATE_NONE = 0;
  public static final int MATE_STALE = 1;
  public static final int MATE_CHECK = 2;
  char	board[][];
  protected char	toMove;
  protected boolean reverse;

  protected Vector<GameObserver> observers;
  String captures = "";

  /**
   * Creates a game with the default position. */
  public Game()
  {
    reverse = true;
    observers = new Vector<GameObserver>();
    try
      {
        setPosition("RHEAKAEHR/9/1C5C1/P1P1P1P1P/9/9/p1p1p1p1p/1c5c1/9/rheakaehr r");
      }
    catch (InvalidPositionException e)
      {
        System.out.println(e.getMessage());
      }
  }

  /**
   * Sets the board up for the given position.
   * @para position - FEN like string of characters representing the state of a game.
   * @throws InvalidPositionException when the position string makes no sence.
   */
  public void setPosition(String position)
    throws InvalidPositionException
  {
    char    [][] tempBoard = new char[BOARD_HEIGHT][BOARD_WIDTH];
    int 	spaceIndex = position.indexOf(' ');
    String 	    pieces = position.substring(0, spaceIndex);
    if (position.length() < spaceIndex+2)
      throw new InvalidPositionException("No color supplied");
    char color = position.charAt(spaceIndex+1);
    if (!(color == RED || color == BLUE))
      throw new InvalidPositionException("Color supplied was not legal: "+color);
    setTurn(color);

    // Insert pieces into array...
    // Checks not performed: is the K or A outside of the Palace?
    // Are there too many peices on the board?
    int boardIndex = 0;
    for (int i = 0; i < pieces.length(); i++)
      {
        char c = pieces.charAt(i);
        if (c == '/')
          {
            if ((boardIndex%9) != 0)
              throw new InvalidPositionException("Invalid length of row at: "+boardIndex/9);
          }
        else if (c >= '0' && c <= '9')
          {
            for (int k = 0; k < (c-'0'); k++, boardIndex++)
              tempBoard[boardIndex/9][boardIndex%9] = ' ';
          }
        else if (legalChars.indexOf(c) > -1)
          {
            tempBoard[boardIndex/9][boardIndex%9] = c;
            boardIndex++;
          }
        else
          {
            throw new InvalidPositionException("Unknown character: "+c);
          }
      }
    board = tempBoard;
    notifyObservers();
  }

  private void setTurn(char color) throws InvalidPositionException
  {
    if (color == BLUE || color == RED) toMove = color;
    else throw new InvalidPositionException("Unknown color: "+color);
  }

  public void addGameObserver(GameObserver observer)
  {
    observers.add(observer);
    observer.newPosition(getPosition());
  }
  public void removeObserver(GameObserver observer)
  {
    observers.remove(observer);
  }
  public void notifyObservers(String move)
  {
    for (Enumeration e = observers.elements(); e.hasMoreElements();)
      {
        ((GameObserver)e.nextElement()).newMove(move);
      }
  }
  public void notifyObservers()
  {
    String fen = getPosition();
    for (Enumeration e = observers.elements(); e.hasMoreElements();)
      {
        ((GameObserver)e.nextElement()).newPosition(fen);
      }
  }
  public String getPosition()
  {
    if (board == null) return null;
    String fen = new String("");
    int spaces = 0;
    for (int i = 0; i < BOARD_HEIGHT; i++)
      {
        for (int j = 0; j < BOARD_WIDTH; j++)
          {
            if (board[i][j] == ' ') spaces++;
            else
              {
                if (spaces > 0)
                  {
                    fen += (new Integer(spaces)).toString();
                    spaces = 0;
                  }
                fen += board[i][j];
              }
          }
        if (spaces > 0) fen += (new Integer(spaces)).toString();
        spaces = 0;
        fen += '/';
      }
    fen += " "+toMove;
    return fen;
  }

  /**
   * Used for debugging only.
   */
  public void printBoard()
  {
    for (int i = 0; i < BOARD_HEIGHT; i++)
      {
        for (int j = 0; j < BOARD_WIDTH; j++)
          {
            System.out.print(board[i][j]);
          }
        System.out.println();
      }
  }

  public Vector possibleMoves(String origin, boolean checkCheck)
  {
    SquareCoord coord = new SquareCoord(origin);
    return possibleMoves(coord, checkCheck);
  }
  public Vector possibleMoves(SquareCoord coord, boolean checkCheck)
  {
    int [] arr = {coord.getRow(),coord.getFile()};
    switch (pieceAtCoord(coord))
      {
      case 'K':case 'k': return Jiang.possibleDestinations(board, coord, checkCheck);
      case 'A':case 'a': return Shi.possibleDestinations(board,coord,checkCheck);
      case 'H':case 'h': return Ma.possibleDestinations(board,coord,checkCheck);
      case 'C':case 'c': return Pao.possibleDestinations(board,coord,checkCheck);
      case 'R':case 'r': return Che.possibleDestinations(board,coord,checkCheck);
      case 'E':case 'e': return Xiang.possibleDestinations(board,coord,checkCheck);
      case 'P':case 'p': return Zu.possibleDestinations(board, coord, checkCheck);
      default:
        return new Vector();
      }
  }
  /**
   * Attempts to make a move in the game
   *
   * @param moveText text of move - format is d3-d4 where d3 is the origin
   *		     and d4 is the destination
   */
  public boolean move(String moveText)
  {
    SquareCoord origin = new SquareCoord(moveText.substring(0, moveText.indexOf('-')));
    SquareCoord destin = new SquareCoord(moveText.substring(moveText.indexOf('-')+1));
    if (origin.equals(destin)) {return false;} // don't mess with non-moves.
    boolean legalMove = false; // assume it is not...
    char piece = pieceAtCoord(origin);
    if ((toMove == RED && !Character.isUpperCase(piece))) return false;
    if ((toMove == BLUE && Character.isUpperCase(piece))) return false;

    Vector legalDestinations = possibleMoves(origin, true);
    for (Enumeration e = legalDestinations.elements(); e.hasMoreElements();)
      {
        Destination possible = (Destination)e.nextElement();
        if (possible.algCoord().compareTo(destin.toString()) == 0)
          {
            if (board[destin.getRow()][destin.getFile()] != ' ')
              captures += board[destin.getRow()][destin.getFile()];
            board[origin.getRow()][origin.getFile()] = ' ';
            board[destin.getRow()][destin.getFile()] = piece;
            toMove ^= TURNSWITCH;
            notifyObservers(moveText);
            checkForMate();
            return true;
          }
      }
    return false;
  }
  public String getCaptures() { return captures; }
  private void checkForMate()
  {
    short mate = MATE_STALE;
    for (int i = 0; i < BOARD_HEIGHT && mate == MATE_STALE; i++)
      for (int j = 0; j < BOARD_WIDTH && mate == MATE_STALE; j++)
        {
          Vector v = null;
          if ((toMove == RED && Character.isUpperCase(board[i][j])) ||
              (toMove == BLUE && Character.isLowerCase(board[i][j])))
            {
              SquareCoord coord = new SquareCoord(i,j);
              boolean checkCheck = true;
              switch (board[i][j])
                {
                case 'K':case 'k': 
                  v =  Jiang.possibleDestinations(board, coord, checkCheck);
                  break;
                case 'A':case 'a':
                 v =  Shi.possibleDestinations(board, coord, checkCheck);
                  break;
                case 'H':case 'h': 
                  v =  Ma.possibleDestinations(board, coord, checkCheck);
                  break;
                case 'C':case 'c': 
                  v =  Pao.possibleDestinations(board, coord, checkCheck);
                  break;
                  
                case 'R':case 'r': 
                 v =  Che.possibleDestinations(board, coord, checkCheck);
                 break;
                case 'E':case 'e': 
                  v =  Xiang.possibleDestinations(board, coord, checkCheck);
                  break;
                case 'P':case 'p': 
                  v =  Zu.possibleDestinations(board, coord, checkCheck);
                  break;
                }
              if (v.size() != 0)
                {
                  mate = MATE_NONE;
                }
            }
        }
    if (mate == MATE_STALE)
      {
        if (checkForCheck(board, toMove)) mate = MATE_CHECK;
        for (Enumeration e = observers.elements(); e.hasMoreElements();)
          ((GameObserver)e.nextElement()).mate(mate, toMove);
      }
  }
  public static boolean checkForCheck(char board[][], char color)
  {
    HashSet opponentsMoves = new HashSet();
    // Find King...
    Destination kingCapture = null;
    if (color == 'r')
      {
        for (int i = 0; i < 3; i++)
          for (int j = 3; j < 6; j++)
            if (board[i][j] == 'K')
              kingCapture = new Destination(i, j, true);
      }
    else 
      {
        for (int i = 7; i < 10; i++)
          for (int j = 3; j < 6; j++)
            if (board[i][j] == 'k')
              kingCapture = new Destination(i, j, true);
      }
    if (kingCapture == null) return false; // should never happen.
    
    for (int i = 0; i < 10; i++)
      for (int j = 0; j < 9; j++)
        if (board[i][j] != ' ')
          {
            if ((color == 'r' && Character.isLowerCase(board[i][j])) ||
                (color == 'b' && Character.isUpperCase(board[i][j])))
              {
                Vector possible = null;
                switch (board[i][j])
                  {
                  case 'K':case 'k':
                    possible = Jiang.possibleDestinations(board, new SquareCoord(i,j), false);
                    break;
                  case 'R':case 'r':
                    possible = Che.possibleDestinations(board, new SquareCoord(i,j), false);
                    break;
                  case 'H':case 'h':
                     possible = Ma.possibleDestinations(board, new SquareCoord(i,j), false);
                     break;
                  case 'E':case 'e':
                     possible = Xiang.possibleDestinations(board, new SquareCoord(i,j), false);
                     break;

                  case 'A':case 'a':
                     possible = Shi.possibleDestinations(board, new SquareCoord(i,j), false);
                     break;
                  case 'C':case 'c':
                     possible = Pao.possibleDestinations(board, new SquareCoord(i,j), false);
                     break;
                  case 'P':case 'p':
                    possible = Zu.possibleDestinations(board, new SquareCoord(i,j), false);
                    break;
                  }
                for (Enumeration e = possible.elements(); e.hasMoreElements();)
                  if (((Destination)e.nextElement()).equals(kingCapture))
                    return true;
              }
          }
    return false;
  }
  private char pieceAtCoord(SquareCoord sqc)
  {
    return board[sqc.getRow()][sqc.getFile()];
  }

}
