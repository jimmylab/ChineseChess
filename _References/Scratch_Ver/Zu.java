import java.util.*;

public class Zu extends Piece // Soldier
{
  static Zu zu;
  private Zu() {}
  public static Vector<Destination> possibleDestinations(char board[][], SquareCoord origin, boolean checkCheck)
  {
    if (zu == null) zu = new Zu();
    return zu._possibleDestinations(board,origin, checkCheck);
  }

  private Vector<Destination> _possibleDestinations(char board[][], SquareCoord origin, boolean checkCheck)
  {
    Vector<Destination> moves = new Vector<Destination>();
    int x = origin.getRow();
    int y = origin.getFile();
    char [][] tempBoard = fillTempBoard(board);
    tempBoard[x][y] = ' ';
    char piece = board[x][y];
    char color = ' ';
    if (Character.isUpperCase(piece)) color = 'r';
    else color = 'b';
    
    if ((color == 'r' && x > 4) || (color == 'b' && x < 5))
      {
        // sides
        if (y > 0) checkMove(moves, x, y-1, piece, color, tempBoard, board, checkCheck);
        if (y < 8) checkMove(moves, x, y+1, piece, color, tempBoard, board, checkCheck);
      }
    if (color == 'r' && x < 9)
      {
        checkMove(moves, x+1, y, piece, color, tempBoard, board, checkCheck);
      }
    if (color == 'b' && x > 0)
      {
        checkMove(moves, x-1, y, piece, color, tempBoard, board, checkCheck);
      }
    return moves;
  }
}
