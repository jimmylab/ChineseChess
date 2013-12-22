import java.lang.reflect.*;

public class ReflectTest {
	@SuppressWarnings (value={"unchecked"})
	public static void callback( Object object, String methodName, Object... arguments ) {
		try {
			Class c = object.getClass();
			Class[] argTypes = new Class[arguments.length];
			for ( int i=0; i<arguments.length; i++ )// {
				argTypes[i] = arguments[i].getClass();
				//System.out.println( argTypes[i].toString() ); }
			Method m = c.getMethod("abc", argTypes);
			m.invoke(object, arguments);
		} catch (Throwable e) { System.out.println("Exception Occured: "); e.printStackTrace(); }
	}
	public static void main(String args[]) {
		Object obj = new myMethods();
		callback(obj, "abc", "123", "456", 789);
	}
}
/* abstract class AsyncCall
{
} */

class myMethods
{
	public void abc(String arg1, String arg2, Integer arg3 ) {
		System.out.println("Arguments are "+arg1+" and "+arg2+" and "+arg3+".");
	}
}