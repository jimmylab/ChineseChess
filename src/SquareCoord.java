  public class SquareCoord
  {
    int _x;
    int _y;
    String text;
    public SquareCoord(int x, int y)
    {
      _x = x;
      _y = y;
    }
    public SquareCoord(String coordText)
    {
      text = coordText;
      _y = 8 - (coordText.charAt(0) - 'a');
      try
        {
          _x = Integer.parseInt(coordText.substring(1)) -1;
        }
      catch (Exception e)
        {
        }
    }
    public int getRow()  { return _x; }
    public int getFile() { return _y; }
    public boolean equals(SquareCoord sc)
    {
      return (_x == sc.getRow() && _y == sc.getFile());
    }
    public String toString()
    {
      return text;
    }
  }
