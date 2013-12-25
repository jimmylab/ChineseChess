import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * AsyncCall类
 * 异步执行与回调
 * @author JimmyLiu
 * @version 1.1.2
 * 
 * 用法：
 *     比如正常的调用方式为
 *     try {
 *	        obj.doSomeThing("param1", "param2");
 *	    } catch ( Throwable e ) {
 *	        obj.errorHandler(e);
 *	    }
 *	    obj.Success("param1", "param2");
 *	    
 *     那么异步就应该是这样：
 *     AsyncCall caller = new AsyncCall( obj, "doSomeThing", "param1", "param2" );    // 顺序为：对象，方法名，参数列表
 *     caller.onError( obj, "errorHandler" );
 *     caller.onSuccess( obj, "obj.Success", "param1", "param2" );
 *     caller.call();
 * 
 * 注意事项：类本身也作为轮询线程使用，万万不要手贱去调用run()方法【有意思吗】
 * 程序（主线程）退出前务必用@see AsyncCall#destroy 方法擦屁股！（否则你就会发现程序永远退不出呵呵）
 */
@SuppressWarnings (value={"unchecked"})
public class AsyncCall implements Runnable
{
	public static void main(String args[]) {
		myMethods obj = new myMethods();
		AsyncCall caller = new AsyncCall( obj, "abc", "123", "456", 789 );
		//AsyncCall caller = new AsyncCall( obj, "FuckingNoExist", "123", "456", 789 );
		AsyncCall[] callers = new AsyncCall[10];
		for ( int i=0; i<5; i++ ) {
			callers[i] = new AsyncCall( obj, "abc", "123", "456", i+1 );
			callers[i].onError( obj, "Er" );
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
	private static boolean Running = false;    // 运行状态
	private static Thread EventLooper = null;    // 事件轮询线程
	private static LinkedBlockingDeque<AsyncTask[]> WorkLine = null;    // 任务流水线队列
	private AsyncTask[] task;    // 任务数组，方法本身+onSuccess+onError
	/**
	 * AsyncCall
	 * 默认构造函数（私有的！只能用于内部建立线程用）
	 */
	private AsyncCall() {
		if ( WorkLine ==null )
			WorkLine = new LinkedBlockingDeque<AsyncTask[]>();    // 初始化流水线
		Running = true;
	}
	/**
	 * AsyncCall
	 * 构造函数（公有），设定要异步执行的方法
	 * @param Object object 要执行的对象
	 * @param String methodName 要执行的方法名
	 * @param Object... arguments 要传递的参数
	 */
	public AsyncCall( Object object, String methodName, Object... arguments ) {
		task = new AsyncTask[3];
		task[0] = new AsyncTask( object, methodName, arguments );
	}
	/**
	 * run
	 * 当把类作为轮询线程时，才用此run方法，请勿直接调用！
	 * @return void
	 */
	public void run() {    // 当把类作为轮询线程时，有此run方法
		AsyncTask[] current = null;
		while ( Running ) {
			while ( WorkLine!=null && WorkLine.peek() != null ) {
				current = WorkLine.remove();
				try { current[0].Run(); }
				catch (Throwable e) {
					try { current[2].Run(e); } catch(Throwable f){}    // 一旦发生错误，调用预先设定的onError方法，并将异常交给第一参数，但onError方法中自身的错误将被忽略，包括onError方法为空的情况
					continue;    // 然后跳过onSuccess，包括onSuccess方法为空的情况
				}
				try { current[1].Run(); } catch(Throwable e){}    // onSuccess方法中自身的错误将被忽略
			}
			try { Thread.sleep(30000); }    // 休息30秒，让出CPU控制权
			catch (InterruptedException e) { continue; }    // 反之则被吵醒，继续轮询
		}
	}
	/**
	 * onSuccess
	 * 指定执行成功时的回调函数
	 * @param Object object 要执行的对象
	 * @param String methodName 要执行的方法名
	 * @param Object... arguments 要传递的参数
	 * @return void
	 */
	public void onSuccess( Object object, String methodName, Object... arguments ) {
		task[1] = new AsyncTask( object, methodName, arguments );
	}
	/**
	 * onError
	 * 指定执行时发生异常的回调函数
	 * @param Object object 要执行的对象
	 * @param String methodName 要执行的方法名
	 * @return void
	 * 注意：处理错误的方法，只能拥有一个类型为Throwable的参数，用于接收异常的句柄
	 */
	public void onError( Object object, String methodName ) {
		Throwable err = new Throwable();
		task[2] = new AsyncTask( object, methodName, err );
	}
	/**
	 * call
	 * 开始执行任务
	 * @return void
	 */
	public void call() {
		if ( EventLooper == null ) {    // 如果轮询线程死亡态或为null（下面称僵尸态^_^）
			AsyncCall worker = new AsyncCall();
			EventLooper = new Thread(worker);
			EventLooper.start();    // 开启新线程
			call();    // 再次尝试回调
		} else {
			WorkLine.offer(task);    // 反之将任务加入队列，
			EventLooper.interrupt();    // 并试图吵醒轮询线程
		}
	}
	/**
	 * (static) destory
	 * 停止轮询线程，回收一切有关AsyncCall的资源
	 * @return void
	 */
	static void destory() {
		if ( EventLooper != null && EventLooper.isAlive() ) {    // 若线程不僵尸……
			EventLooper.interrupt();    // 吵醒线程
			while ( WorkLine.peek() != null ) {    // 默默等待流水线完成……
				try { Thread.sleep(10); } catch (InterruptedException e){ }    // 每次等待10ms
			}
			while ( EventLooper.isAlive() ) {    // 只要线程还没停
				Running = false;    // 置运行状态为停止
				EventLooper.interrupt();    // 又吵醒(>_<)
			}
			EventLooper = null;
			WorkLine = null;    // 擦屁股操作
		}
	}
}

/**
 * AsyncTask结构体
 * 异步任务的载体
 * @author JimmyLiu
 * @version 1.1
 */
class AsyncTask {
	Object object; String methodName; Object[] arguments;
	/**
	 * AsyncTask
	 * 构造函数
	 * @param Object object 要执行的对象引用
	 * @param String methodName 要执行的方法名
	 * @param Object... arguments 要传递的参数
	 */
	public AsyncTask( Object object, String methodName, Object... arguments ) {
		this.object = object; this.methodName = methodName; this.arguments = arguments;
	}
	/**
	 * Run
	 * 执行承载的任务，利用Java的反射机制
	 * @param Throwable err = null 要接收异常类的变量，可选
	 * @return void
	 * @throws Throwable 抛出回调函数中发生的异常或者，包括但不限于
	 *  - 方法无法调用：NoSuchMethodException、NullPointerException、SecurityException
	 *  - 尝试调用时的错误：NullPointerException，IllegalAccessException，IllegalArgumentException，ExceptionInInitializerError
	 */
	public void Run(Throwable err) throws Throwable {
		Class c = object.getClass();
		Class[] argTypes = new Class[arguments.length];
		try {
			for ( int i=0; i<arguments.length; i++ ) {
				argTypes[i] = arguments[i].getClass();    // 逐一获取每个参数的类型
			}
			Method m = c.getMethod(methodName, argTypes);    // 寻找匹配的方法
			try {
				m.invoke(object, arguments);    // 尝试执行
			}
			catch (InvocationTargetException e) {    // 如果回调函数本身抛出异常
				throw e.getTargetException();    // 则获取并抛出源异常
			}
		} catch (Throwable e) {
			//e.printStackTrace();    // 这时其实你可以追踪堆栈
			if ( err != null) err = e; 
			throw e;    // 再次抛出
		}
	}
	public void Run() throws Throwable {
		Run(null);
	}
}
