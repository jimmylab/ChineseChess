import java.io.*;

/**
 * A struct records each step in the Game
 * @author JimmyLiu
 */
public class StepRecord implements Serializable
{
    String origin;
    String destin;
    boolean hadCaptured = false;
    char CaptureChar;
    public StepRecord( String moveText ) {
        destin = moveText.substring(0, moveText.indexOf('-'));
        origin = moveText.substring(moveText.indexOf('-')+1);
    }
    public StepRecord( String moveText, char CaptureChar ) {
        this(moveText);
        hadCaptured = true;
        this.CaptureChar = CaptureChar;
    }
    public String getOrigin() { return origin; }
    public String getDestin() { return destin; }
    public char getCapture() { return hadCaptured ? CaptureChar : ' '; }
    
    public void Invert() {
        String temp = destin;
        destin = origin;
        origin = temp;
    }
    public String moveText() {
        return origin+'-'+destin;
    }
    @Override public String toString() {
        String s = origin+'-'+destin;
        return hadCaptured ? s+'&'+CaptureChar : s;
    }
}