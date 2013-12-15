import java.util.*;

public class Piece
{
  public static Vector<Destination> possibleDestinations(char board[][], SquareCoord coord, boolean checkCheck){ return new Vector<Destination>(); }

  protected char[][] fillTempBoard(char board[][])
  {
    char [][] tempBoard = new char[10][9];
    for (int i = 0; i < 10; i ++)
      for (int j = 0; j < 9; j++)
        tempBoard[i][j] = board[i][j];
    return tempBoard;
  }

  protected void checkMove(Vector<Destination> moves, int x, int y, char piece, char color, char [][] tempBoard, char [][] board, boolean checkCheck)
  {
    char oldPiece = tempBoard[x][y];
    tempBoard[x][y] = piece;
    if ((!checkCheck) || (!Game.checkForCheck(tempBoard, color)))
      {
        if (board[x][y] == ' ') moves.add(new Destination(x,y,false));
        else
          {
            switch (color)
              {
              case 'r':
                if (Character.isLowerCase(board[x][y])) moves.add(new Destination(x,y,true));
                break;
              case 'b':
                if (Character.isUpperCase(board[x][y])) moves.add(new Destination(x,y,true));
                break;
              }
          }
      }
    tempBoard[x][y] = oldPiece;
  }

}
