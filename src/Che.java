import java.util.*;


public class Che extends Piece // Soldier
{
  static Che che;
  private Che() {}
  public static Vector<Destination> possibleDestinations(char board[][], SquareCoord origin, boolean checkCheck)
  {
    if (che == null) che = new Che();
    return che._possibleDestinations(board,origin, checkCheck);
  }

  private Vector<Destination> _possibleDestinations(char board[][], SquareCoord origin, boolean checkCheck)
  {
    Vector<Destination> moves = new Vector<Destination>();
    int x = origin.getRow();
    int y = origin.getFile();
    int i;
    int j;
    char [][] tempBoard = fillTempBoard(board);
    tempBoard[x][y] = ' ';
    char piece = board[x][y];
    char color = ' ';
    if (Character.isUpperCase(piece)) color = 'r';
    else color = 'b';
    for(i=x+1;i<=9;i++)
    {
      if(i>9){
        break;
      }
     if(tempBoard[i][y]==' '){
          checkMove(moves, i, y, piece, color, tempBoard, board, checkCheck);
        }
      else 
      {
      checkMove(moves, i, y, piece, color, tempBoard, board, checkCheck);
            break;
      }
      
    }

    for(i=x-1;i>=0;i--){
      if(i<0){
        break;
      }
      if(tempBoard[i][y]==' '){
        checkMove(moves, i, y, piece, color, tempBoard, board, checkCheck);
      }
      else 
      {
      checkMove(moves, i, y, piece, color, tempBoard, board, checkCheck);
            break;
      }
    }

    for(j=y+1;j<=8;j++){
      if(j>8){
        break;
      }
     if(tempBoard[x][j]==' '){
        checkMove(moves, x, j, piece, color, tempBoard, board, checkCheck);
      }
      else 
      {
          checkMove(moves, x, j, piece, color, tempBoard, board, checkCheck);
          break;
      }
    }
    for(j=y-1;j>=0;j--){
      if(j<0){
        break;
      }
      if(tempBoard[x][j]==' '){
        checkMove(moves, x,j ,piece, color, tempBoard, board, checkCheck);
      }
      else 
      {
      checkMove(moves, x, j, piece, color, tempBoard, board, checkCheck);
            break;
      }
    }
    

     
   return moves;
 }
  
}

