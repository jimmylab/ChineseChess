import java.util.Vector;

public class Shi extends Piece // General
{
  static Shi shi;
  private Shi() {}
  public static Vector<Destination> possibleDestinations(char [][] board, SquareCoord origin, boolean checkCheck)
  {
    if (shi == null) shi = new Shi();
    return shi._possibleDestinations(board, origin, checkCheck);
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
    if(y==3)
		checkMove(moves, x+1,y+1,'A','r', tempBoard, board, checkCheck); 
    if (y==5) checkMove(moves, x+1,y-1,'A','r', tempBoard, board, checkCheck);
	}
	else if(x==1){
    if(y==4){
      checkMove(moves, x+1,y+1,'A','r', tempBoard, board, checkCheck); 
      checkMove(moves, x+1,y-1,'A','r', tempBoard, board, checkCheck); 
      checkMove(moves, x-1,y-1,'A','r', tempBoard, board, checkCheck); 
      checkMove(moves, x-1,y+1,'A','r', tempBoard, board, checkCheck); 
      }
    }
	else if(x==2){
			if (y==3) checkMove(moves, x-1,y+1,'A','r', tempBoard, board, checkCheck); 
      if (y==5) checkMove(moves, x-1,y-1,'A','r', tempBoard, board, checkCheck); 
      
  }

    int i = 0;
    for (i = x+1; i < 9 && board[i][y] == ' '; i++);
    if (board[i][y] == 'a') moves.add(new Destination(i, y, true));

    return moves;
  }
  private Vector<Destination> blue(char [][] board, SquareCoord origin, boolean checkCheck)
  {

    Vector<Destination> moves = new Vector<Destination>();
    int x = origin.getRow();
    int y = origin.getFile();
    char [][] tempBoard = fillTempBoard(board);
    tempBoard[x][y] = ' ';

    if(x==7){
      if (y==3) checkMove(moves, x+1,y+1,'A','b', tempBoard, board, checkCheck); 
      if (y==5) checkMove(moves, x+1,y-1,'A','b', tempBoard, board, checkCheck); 

  }
  else if(x==8){
    if(y==4){
      checkMove(moves, x+1,y+1,'A','b', tempBoard, board, checkCheck); 
      checkMove(moves, x+1,y-1,'A','b', tempBoard, board, checkCheck); 
      checkMove(moves, x-1,y-1,'A','b', tempBoard, board, checkCheck); 
      checkMove(moves, x-1,y+1,'A','b', tempBoard, board, checkCheck); 
    }
  }
  else if(x==9){
      if (y==3) checkMove(moves, x-1,y+1,'A','b', tempBoard, board, checkCheck); 
      if (y==5) checkMove(moves, x-1,y-1,'A','b', tempBoard, board, checkCheck); 
  }

    int i = 0;
    for (i = x-1; i > 0 && board[i][y] == ' '; i--);
    if (board[i][y] == 'A') moves.add(new Destination(i, y, true));

    return moves;
  }
}
