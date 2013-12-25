import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import java.util.concurrent.*;

public class ReflectTest {
	public static void main(String args[]) {
		myMethods obj = new myMethods();
		AsyncCall caller = new AsyncCall( obj, "abc", "123", "456", 789 );
		//AsyncCall caller = new AsyncCall( obj, "FuckingNoExist", "123", "456", 789 );
		AsyncCall[] callers = new AsyncCall[10];
		for ( int i=0; i<5; i++ ) {
			callers[i] = new AsyncCall( obj, "abc", "123", "456", i+1 );
			callers[i].onError( obj, "abc", "Error of "+String.valueOf(i), "654", 0 );
		}
		for ( int i=0; i<5; i++ ) callers[i].call();
		//caller.onError( obj, "abc", "987", "654", 0 );
		//caller.call();
		for ( int i=0; i<15; i++ ) {
			System.out.println("Main running!");
			try{Thread.sleep(700);}catch(InterruptedException e){}
		}
		caller.call();
		AsyncCall.destory();
	}
}

class myMethods {
	public void abc(String arg1, String arg2, Integer arg3 ) {
		if ( arg3 != 0 ) try{Thread.sleep(2000);}catch(InterruptedException e){}
		System.out.println("Arguments are "+arg1+" and "+arg2+" and "+arg3+".");
		if ( arg3 > 5 ) throw new myException("Error"+arg3);
	}
}
class myException extends RuntimeException {
	String Message;
	public myException( String Msg ) { Message = Msg; }
	/*@Override public void printStackTrace() {
		System.out.println("Custom StackTrace with Message '"+Message+"'.");
	}*/
}

@SuppressWarnings (value={"unchecked"})
class AsyncCall implements Runnable
{
	static boolean Running = false;
	static Thread EventLooper = null;
	static LinkedBlockingDeque<AsyncTask[]> WorkLine = null;
	private AsyncTask[] task;
	private AsyncCall() {
		if ( WorkLine ==null )
			WorkLine = new LinkedBlockingDeque<AsyncTask[]>();
		Running = true;
	}
	public AsyncCall( Object object, String methodName, Object... arguments ) {
		if ( EventLooper == null ) {
			AsyncCall worker = new AsyncCall();
			EventLooper = new Thread(worker);
		}
		task = new AsyncTask[3];
		task[0] = new AsyncTask( object, methodName, arguments );
	}
	public void run() {
		AsyncTask[] current = null;
		while ( Running ) {
			while ( WorkLine.peek() != null ) {
				current = WorkLine.remove();
				try { current[0].Run(); }
				catch (Throwable e) {
					try { current[2].Run(); } catch(Throwable f){}    // 一旦发生错误，调用预先设定的onError方法
					continue;    // 然后跳过onFinish
				}
				try { current[1].Run(); } catch(Throwable e){}    // 一旦发生错误，调用预先设定的onError方法
			}
			try { Thread.sleep(1000); }
			catch (InterruptedException e) { System.out.println("Interrupted!"); }
		}
		System.out.println("Ended!");
	}
	static synchronized void destory() {
		if ( EventLooper != null ) {
			EventLooper.interrupt();
			while ( EventLooper.isAlive() ) {
				if ( WorkLine.peek() == null ) Running = false;
			}
			EventLooper = null;
		}
	}
	public void onFinish( Object object, String methodName, Object... arguments ) {
		task[1] = new AsyncTask( object, methodName, arguments );
	}
	public void onError( Object object, String methodName, Object... arguments ) {
		task[2] = new AsyncTask( object, methodName, arguments );
	}
	public void call() {
		WorkLine.offer(task);
		if ( !EventLooper.isAlive() )
			EventLooper.start();
		else
			EventLooper.interrupt();
	}
}
class AsyncTask {
	Object object; String methodName; Object[] arguments;
	public AsyncTask( Object object, String methodName, Object... arguments ) {
		this.object = object; this.methodName = methodName; this.arguments = arguments;
	}
	public void Run() throws Throwable {
		Class c = object.getClass();
		Class[] argTypes = new Class[arguments.length];
		try {
			for ( int i=0; i<arguments.length; i++ )
				argTypes[i] = arguments[i].getClass();
			Method m = c.getMethod(methodName, argTypes);
			try {
				m.invoke(object, arguments);
			}
			catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}
}
