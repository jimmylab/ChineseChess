import java.util.*;

public class Ma extends Piece // Soldier
{
  static Ma ma;
  private Ma() {}
  public static Vector<Destination> possibleDestinations(char board[][], SquareCoord origin, boolean checkCheck)
  {
    if (ma == null) ma = new Ma();
    return ma._possibleDestinations(board,origin, checkCheck);
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
    
   /* if((x<8&&x>1)&&(y<7&&y>1))
    {
    if(tempBoard[x][y-1]==' ') 
      {
      checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
      }
        if(tempBoard[x+1][y]==' ')
    {
      checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
      checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
    }
      if(tempBoard[x][y+1]==' ')
    {
        checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
        checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
    }
    if(tempBoard[x-1][y]==' ')
    {
      checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
      checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
    }
    }
    if(x==0){
      if(y==0) {
        if(tempBoard[x][y+1]==' ') checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
        if(tempBoard[x+1][y]==' ') checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
      }
      if(y==1){
              if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
      }
      if(y>1&&y<7){
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==7)
      { 
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==8)
      {
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
        }
      }
    }
    else if(x==1){
      if(y==0) {
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y]==' ') checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
      }
      if(y==1){
              if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y>1&&y<7){
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
                  checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==7)
      { 
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
                  checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==8)
      {
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck); 
        }
        if(tempBoard[x+1][y]==' ') {
          checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
        }
      }
    }
    else if(x==8){
      if(y==0) {
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x-1][y]==' ') checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
      }
      if(y==1){
              if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y>1&&y<7){
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
                  checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==7)
      { 
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
                  checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==8)
      {
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
            checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck); 
        }
        if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
        }
      }
    }
    else if(x==9){
      if(y==0) {
        if(tempBoard[x][y+1]==' ') checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        if(tempBoard[x-1][y]==' ') checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
      }
      if(y==1){
              if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
      }
      if(y>1&&y<7){
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x][y+1]==' ') {
          checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==7)
      { 
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
          checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
        }
      }
      if(y==8)
      {
        if(tempBoard[x][y-1]==' ') {
          checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x-1][y]==' ') {
          checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
        }
      }
    }
    if(x>1&&x<8)
    {
      if(y==0)
      {
        if(tempBoard[x][y+1]==' ')
        {
         checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
         checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck); 
        }
        if(tempBoard[x-1][y] == ' ')
              {
         checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y] == ' ')
              {
         checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
        }
      }
      else if(y==1)
      {
        if(tempBoard[x][y+1]==' ')
        {
         checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);
         checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck); 
        }
        if(tempBoard[x-1][y] == ' ')
              {
        checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
                checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y] == ' ')
              {
         checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
           checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
        }
      }
      else if(y==7)
      {
        if(tempBoard[x][y-1]==' ')
        {
         checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
         checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck); 
        }
        if(tempBoard[x-1][y] == ' ')
              {
        checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);
                checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y] == ' ')
              {
         checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);
           checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
        }
      }
      else if(y==8)
      {
        if(tempBoard[x][y-1]==' ')
        {
         checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
         checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck); 
        }
        if(tempBoard[x-1][y] == ' ')
              {
                checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
        }
        if(tempBoard[x+1][y] == ' ')
              {
           checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
        }
      }
    }*/
    if((x+2<10)&&(y-1>=0)&&tempBoard[x+1][y]==' ')
    checkMove(moves, x+2, y-1, piece, color, tempBoard, board, checkCheck);
    if((x+2<10)&&(y+1<9)&&tempBoard[x+1][y]==' ')
    checkMove(moves, x+2, y+1, piece, color, tempBoard, board, checkCheck);

    if((x+1<10)&&(y-2>=0)&&tempBoard[x][y-1]==' ')
    checkMove(moves, x+1, y-2, piece, color, tempBoard, board, checkCheck);
    if((x+1<10)&&(y+2<9)&&tempBoard[x][y+1]==' ')
    checkMove(moves, x+1, y+2, piece, color, tempBoard, board, checkCheck);

    if((x-1>=0)&&(y-2>=0)&&tempBoard[x][y-1]==' ')
    checkMove(moves, x-1, y-2, piece, color, tempBoard, board, checkCheck);
    if((x-1>=0)&&(y+2<9)&&tempBoard[x][y+1]==' ')
    checkMove(moves, x-1, y+2, piece, color, tempBoard, board, checkCheck);

    if((x-2>=0)&&(y-1>=0)&&tempBoard[x-1][y]==' ')
    checkMove(moves, x-2, y-1, piece, color, tempBoard, board, checkCheck);
    if((x-2>=0)&&(y+1<9)&&tempBoard[x-1][y]==' ')
    checkMove(moves, x-2, y+1, piece, color, tempBoard, board, checkCheck);

      return moves;
  }
}
