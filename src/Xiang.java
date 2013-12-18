import java.util.Vector;

public class Xiang extends Piece // General
{
  static Xiang xiang;
  private Xiang() {}
  public static Vector<Destination> possibleDestinations(char [][] board, SquareCoord origin, boolean checkCheck)
  {
    if (xiang == null) xiang = new Xiang();
    return xiang._possibleDestinations(board, origin, checkCheck);
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
    
  if(x==0){
    if (tempBoard[x+1][y+1]==' ') checkMove(moves, x+2,y+2,'E','r', tempBoard, board, checkCheck); 
        if (tempBoard[x+1][y-1]==' ') checkMove(moves, x+2,y-2,'E','r', tempBoard, board, checkCheck);
  }
  else if(x==2){
    if(y==0){
      if (tempBoard[x+1][y+1]==' ') checkMove(moves, x+2,y+2,'E','r', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y+1]==' ') checkMove(moves, x-2,y+2,'E','r', tempBoard, board, checkCheck); 
        }
    if(y==4){
      if (tempBoard[x+1][y+1]==' ') checkMove(moves, x+2,y+2,'E','r', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y+1]==' ') checkMove(moves, x-2,y+2,'E','r', tempBoard, board, checkCheck); 
            if (tempBoard[x+1][y-1]==' ') checkMove(moves, x+2,y-2,'E','r', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y-1]==' ') checkMove(moves, x-2,y-2,'E','r', tempBoard, board, checkCheck); 
        }
    if(y==8){
      if (tempBoard[x+1][y-1]==' ') checkMove(moves, x+2,y-2,'E','r', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y-1]==' ') checkMove(moves, x-2,y-2,'E','r', tempBoard, board, checkCheck); 
        }
  }
  if(x==4){
    if (tempBoard[x-1][y+1]==' ') checkMove(moves, x-2,y+2,'E','r', tempBoard, board, checkCheck); 
        if (tempBoard[x-1][y-1]==' ') checkMove(moves, x-2,y-2,'E','r', tempBoard, board, checkCheck);
  }

    int i = 0;
    for (i = x+1; i < 9 && board[i][y] == ' '; i++);
    if (board[i][y] == 'e') moves.add(new Destination(i, y, true));

    return moves;
  }
  private Vector<Destination> blue(char [][] board, SquareCoord origin, boolean checkCheck)
  {
    Vector<Destination> moves = new Vector<Destination>();
    int x = origin.getRow();
    int y = origin.getFile();
    char [][] tempBoard = fillTempBoard(board);
    tempBoard[x][y] = ' ';

    if(x==5){
    if (tempBoard[x+1][y+1]==' ') checkMove(moves, x+2,y+2,'E','b', tempBoard, board, checkCheck); 
        if (tempBoard[x+1][y-1]==' ') checkMove(moves, x+2,y-2,'E','b', tempBoard, board, checkCheck);
  }
  else if(x==7){
    if(y==0){
      if (tempBoard[x+1][y+1]==' ') checkMove(moves, x+2,y+2,'E','b', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y+1]==' ') checkMove(moves, x-2,y+2,'E','b', tempBoard, board, checkCheck); 
        }
    if(y==4){
      if (tempBoard[x+1][y+1]==' ') checkMove(moves, x+2,y+2,'E','b', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y+1]==' ') checkMove(moves, x-2,y+2,'E','b', tempBoard, board, checkCheck); 
            if (tempBoard[x+1][y-1]==' ') checkMove(moves, x+2,y-2,'E','b', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y-1]==' ') checkMove(moves, x-2,y-2,'E','b', tempBoard, board, checkCheck); 
        }
    if(y==8){
      if (tempBoard[x+1][y-1]==' ') checkMove(moves, x+2,y-2,'E','b', tempBoard, board, checkCheck); 
            if (tempBoard[x-1][y-1]==' ') checkMove(moves, x-2,y-2,'E','b', tempBoard, board, checkCheck); 
        }
  }
  else if(x==9){
    if (tempBoard[x-1][y+1]==' ') checkMove(moves, x-2,y+2,'E','b', tempBoard, board, checkCheck); 
        if (tempBoard[x-1][y-1]==' ') checkMove(moves, x-2,y-2,'E','b', tempBoard, board, checkCheck);
  }

    int i = 0;
    for (i = x-1; i > 0 && board[i][y] == ' '; i--);
    if (board[i][y] == 'E') moves.add(new Destination(i, y, true));

    return moves;
  }
}
