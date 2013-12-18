import java.util.*;


public class Pao extends Piece // Soldier
{
  static Pao pao;
  private Pao() {}
  public static Vector<Destination> possibleDestinations(char board[][], SquareCoord origin, boolean checkCheck)
  {
    if (pao == null) pao = new Pao();
    return pao._possibleDestinations(board,origin, checkCheck);
  }

  private Vector<Destination> _possibleDestinations(char board[][], SquareCoord origin, boolean checkCheck)
  {
    Vector<Destination> moves = new Vector<Destination>();
    int x = origin.getRow();
    int y = origin.getFile();
    int i;
    int j;
    int k;
    char [][] tempBoard = fillTempBoard(board);
    tempBoard[x][y] = ' ';
    char piece = board[x][y];
    char color = ' ';
    if (Character.isUpperCase(piece)) color = 'r';
    else color = 'b';
    i=x+1;
    while(tempBoard[i-1][y]==' ')
    {
      
      if(i>9){
        break;
      }
      if(tempBoard[i][y]==' '){

         checkMove(moves, i, y, piece, color, tempBoard, board, checkCheck);
      }
      
      else
      { 
          
          for(k=i+1;k<=9;k++)
          {
            if(tempBoard[k][y]==' ')  
            {
               continue;
            } 
            else 
            {
              checkMove(moves, k, y, piece, color, tempBoard, board, checkCheck);
              break;
            }
        }
      }
     
      i++;
    }
    i=x-1;
    while(tempBoard[i+1][y]==' ')
    {
      if(i<0){
        break;
      }
      if(tempBoard[i][y]==' '){

         checkMove(moves, i, y, piece, color, tempBoard, board, checkCheck);
      }
      
      else
      { 
          
          for(k=i-1;k>=0;k--)
          {
            if(tempBoard[k][y]==' ')  
            {
               continue;
            } 
            else 
            {
              checkMove(moves, k, y, piece, color, tempBoard, board, checkCheck);
              break;
            }
        }
      }
     
      i--;
    }
    j=y+1;
    while(tempBoard[x][j-1]==' ')
    {

      if(j>8){
        break;
      }
      if(tempBoard[x][j]==' '){

         checkMove(moves, x, j, piece, color, tempBoard, board, checkCheck);
      }
      
      else
      { 
          
          for(k=j+1;k<=8;k++)
          {
            if(tempBoard[x][k]==' ')  
            {
               continue;
            } 
            else 
            {
              checkMove(moves, x, k, piece, color, tempBoard, board, checkCheck);
              break;
            }
        }
      }
     
      j++;
    }
    j=y-1;
    while(tempBoard[x][j+1]==' ')
    {
      
      if(j<0){
        break;
      }
      if(tempBoard[x][j]==' '){

         checkMove(moves, x, j, piece, color, tempBoard, board, checkCheck);
      }
      
      else
      { 
          
          for(k=j-1;k>=0;k--)
          {
            if(tempBoard[x][k]==' ')  
            {
               continue;
            } 
            else 
            {
              checkMove(moves, x, k, piece, color, tempBoard, board, checkCheck);
              break;
            }
        }
      }
     
      j--;
    }
    
   return moves;
 }
  
}

