import java.util.Vector;

public class Jiang extends Piece // General
{
  static Jiang jiang;
  private Jiang() {}
  public static Vector<Destination> possibleDestinations(char [][] board, SquareCoord origin, boolean checkCheck)
  {
    if (jiang == null) jiang = new Jiang();
    return jiang._possibleDestinations(board, origin, checkCheck);
  }
  private Vector<Destination> _possibleDestinations(char [][] board, SquareCoord origin, boolean checkCheck)
  {
    if (Character.isUpperCase(board[origin.getRow()][origin.getFile()])) return red(board, origin, checkCheck);
    else return blue(board, origin, checkCheck);
  }

  private Vector<Destination> red(char [][] board, SquareCoord origin, boolean checkCheck)
  {
    Vector<Destination> moves = new Vector<Destination>();
    int x = origin.getRow();
    int y = origin.getFile();
    char [][] tempBoard = fillTempBoard(board);
    tempBoard[x][y] = ' ';

    // N
    if (x+1 < 3) checkMove(moves, x+1,y,'K','r', tempBoard, board, checkCheck);
    // S
    if (x-1 > -1) checkMove(moves, x-1,y,'K','r', tempBoard, board, checkCheck);
    // E
    if (y+1 < 6) checkMove(moves, x, y+1, 'K', 'r', tempBoard, board, checkCheck);
    // W
    if (y-1 > 2) checkMove(moves, x, y-1, 'K', 'r', tempBoard, board, checkCheck);

    // This is for finding check only...will never happen in normal play.
    int i = 0;
    for (i = x+1; i < 9 && board[i][y] == ' '; i++);
    if (board[i][y] == 'k') moves.add(new Destination(i, y, true));

    return moves;
  }
  private Vector<Destination> blue(char [][] board, SquareCoord origin, boolean checkCheck)
  {
    Vector<Destination> moves = new Vector<Destination>();
    int x = origin.getRow();
    int y = origin.getFile();
    char [][] tempBoard = fillTempBoard(board);
    tempBoard[x][y] = ' ';

    if (x+1 < 10) checkMove(moves, x+1,y,'k','b', tempBoard, board, checkCheck);
    // S
    if (x-1 > 6) checkMove(moves, x-1,y,'k','b', tempBoard, board, checkCheck);
    // E
    if (y+1 < 6) checkMove(moves, x, y+1, 'k', 'b', tempBoard, board, checkCheck);
    // W
    if (y-1 > 2) checkMove(moves, x, y-1, 'k', 'b', tempBoard, board, checkCheck);

    int i = 0;
    for (i = x-1; i > 0 && board[i][y] == ' '; i--);
    if (board[i][y] == 'K') moves.add(new Destination(i, y, true));

    return moves;
  }
}
