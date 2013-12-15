  public class Destination
  {
    int x, y;
    boolean capture;
    public boolean equals(Destination thisOne)
    {
      int [] sq = thisOne.getSquare();
      if (x == sq[0] && y == sq[1] && capture == thisOne.isCapture())
        return true;
      else return false;
    }
    public Destination(int _x, int _y, boolean _capture)
    {
      x = _x; y = _y; capture = _capture;
    }
    public int[] getSquare()
    {
      int [] returning = {x, y};
      return returning;
    }
    public boolean isCapture() { return capture; }
    public String algCoord()
    {
      return (char)('i'-y)+Integer.toString(x+1);
    }
    public String toString()
    {
      return algCoord()+(capture ? "CAPTURE":"NO CAPTURE");
    }
  }