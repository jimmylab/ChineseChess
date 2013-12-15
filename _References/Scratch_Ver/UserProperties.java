import java.awt.Color;
import java.util.*;
import java.io.*;

public class UserProperties extends Properties
{
  static String filename = ".xiangqirc";
  static final String defaultUserName = "XiangQiPlayer";

  static UserProperties properties;
  Vector<PropertyObserver> observers;

  private UserProperties()
  {
    observers = new Vector<PropertyObserver>();
    filename = System.getProperty("user.home")+
               System.getProperty("file.separator")+
               ".xiangqirc";
    try
      {
        load(new FileInputStream(filename));
      }
    catch (Exception e)
      {
        setProperty("UserName", defaultUserName);
        setProperty("BoardColor", "#bfaa65");
        setProperty("CaptureColor", "#ff0000");
        setProperty("HighlightColor", "#00ff00");
        setProperty("DestinationColor", "#0000ff");
        setProperty("WinMate", "0");
        setProperty("WinStale", "0");
        setProperty("LossMate", "0");
        setProperty("LossStale", "0");
      }
  }
  public Object setProperty(String prop, String val)
  {
    Object ob = super.setProperty(prop,val);
    for (Enumeration e = observers.elements(); e.hasMoreElements();)
      {
        ((PropertyObserver)e.nextElement()).userPropertyChanged();
      }
    return ob;
  }

  public static String getUserProperty(String prop)
  {
    if (properties == null) properties = new UserProperties();
    return properties.getProperty(prop);
  }
  public static Object setUserProperty(String prop, String value)
  {
    if (properties == null) properties = new UserProperties();
    return properties.setProperty(prop, value);
  }
  public static UserProperties defaultProperties()
  {
    if (properties == null) properties = new UserProperties();
    return properties;
  }
  public static Color getColorProperty(String property)
  {
    try
      {
        return Color.decode(defaultProperties().getProperty(property));
      }
    catch (Exception e)
      {
        return null;
      }
  }
  public static void setColorProperty(String property, Color color)
  {
    String red = "";
    String green = "";
    String blue = "";

    if (color.getRed() < 16) red += "0";
    red += Integer.toHexString(color.getRed());

    if (color.getGreen() < 16) green += "0";
    green += Integer.toHexString(color.getGreen());
    
    if (color.getBlue() < 16) blue += "0";
    blue += Integer.toHexString(color.getBlue());

    defaultProperties().setProperty(property, "#"+red+green+blue);
  }
  public static boolean storeUserProperties()
  {
    if (properties == null) return false; // don't bother.

    try
      {
        properties.store(new FileOutputStream(filename), "Java XiangQi User Preferences");
      }
    catch (Exception e)
      {
        return false;
      }
    return true;
  }
  public void addObserver(PropertyObserver obs)
  {
    observers.add(obs);
  }
    
}
