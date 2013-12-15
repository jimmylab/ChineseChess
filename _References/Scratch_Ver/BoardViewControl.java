import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BoardViewControl extends MouseAdapter
{
  BoardView.PiecePanel originPanel;
  static BoardViewControl defaultControl;
  Vector possibleMoves;
  protected BoardViewControl()
  {
    originPanel = null;
  }
  public void mouseClicked(MouseEvent event)
  {
    BoardView.PiecePanel thisPiece = (BoardView.PiecePanel)event.getSource();
    if (originPanel == null)
      {
        BoardView.defaultBoardView().clearHighlights();
        originPanel = thisPiece;
        if (originPanel.occupied())
          {
            originPanel.highlight(BoardView.H_SEL);
            possibleMoves = BoardView.defaultBoardView().getGame().possibleMoves(originPanel.getCoord(), true);
            for (Enumeration e = possibleMoves.elements();
                 e.hasMoreElements();)
              {
                Destination dest = (Destination)e.nextElement();
                short highlight = 0;
                if (dest.isCapture()) highlight = BoardView.H_CAPTURE;
                else highlight = BoardView.H_DEST;
                BoardView.defaultBoardView().getPiecePanel(dest.algCoord()).highlight(highlight);
              }
          }
        else originPanel = null;
      }
    else if (originPanel == thisPiece)
      {
        BoardView.defaultBoardView().clearHighlights();
        originPanel = null;
      }
    else if ((Character.isUpperCase(thisPiece.getPiece()) && Character.isUpperCase(originPanel.getPiece())) ||
             (Character.isLowerCase(thisPiece.getPiece()) && Character.isLowerCase(originPanel.getPiece())))
      {
        for (Enumeration e = possibleMoves.elements(); e.hasMoreElements();)
          BoardView.defaultBoardView().getPiecePanel(((Destination)e.nextElement()).algCoord()).highlight(BoardView.H_NONE);
        originPanel.highlight(BoardView.H_NONE);
        originPanel = thisPiece;
        originPanel.highlight(BoardView.H_SEL);
        possibleMoves = BoardView.defaultBoardView().getGame().possibleMoves(originPanel.getCoord(), true);
        for (Enumeration e = possibleMoves.elements();
             e.hasMoreElements();)
          {
            Destination dest = (Destination)e.nextElement();
            short highlight = 0;
            if (dest.isCapture()) highlight = BoardView.H_CAPTURE;
            else highlight = BoardView.H_DEST;
            BoardView.defaultBoardView().getPiecePanel(dest.algCoord()).highlight(highlight);
          }
      }
    else
      {
        if (!BoardView.defaultBoardView().getGame().move(originPanel.getCoord()+"-"+thisPiece.getCoord()))
          {
            originPanel = null;
            mouseClicked(event);
            return;
          }
        originPanel = null;
      }
  }
  public static BoardViewControl defaultControl()
  {
    if (defaultControl == null) defaultControl = new BoardViewControl();
    return defaultControl;
  }
}
