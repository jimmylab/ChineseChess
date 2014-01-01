import java.io.*;
public interface GameObserver extends Serializable
{
  public void newPosition(String fen);
  public void newMove(String move);
  public void mate(short which, char color);
  public void setColor(char color);
}