import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * AsyncCall��
 * �첽ִ����ص�
 * @author JimmyLiu
 * @version 1.1.2
 * 
 * �÷���
 *     ���������ĵ��÷�ʽΪ
 *     try {
 *	        obj.doSomeThing("param1", "param2");
 *	    } catch ( Throwable e ) {
 *	        obj.errorHandler(e);
 *	    }
 *	    obj.Success("param1", "param2");
 *	    
 *     ��ô�첽��Ӧ����������
 *     AsyncCall caller = new AsyncCall( obj, "doSomeThing", "param1", "param2" );    // ˳��Ϊ�����󣬷������������б�
 *     caller.onError( obj, "errorHandler" );
 *     caller.onSuccess( obj, "obj.Success", "param1", "param2" );
 *     caller.call();
 * 
 * ע������౾��Ҳ��Ϊ��ѯ�߳�ʹ�ã�����Ҫ�ּ�ȥ����run()����������˼��
 * �������̣߳��˳�ǰ�����@see AsyncCall#destroy ������ƨ�ɣ���������ͻᷢ�ֳ�����Զ�˲����Ǻǣ�
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
	private static boolean Running = false;    // ����״̬
	private static Thread EventLooper = null;    // �¼���ѯ�߳�
	private static LinkedBlockingDeque<AsyncTask[]> WorkLine = null;    // ������ˮ�߶���
	private AsyncTask[] task;    // �������飬��������+onSuccess+onError
	/**
	 * AsyncCall
	 * Ĭ�Ϲ��캯����˽�еģ�ֻ�������ڲ������߳��ã�
	 */
	private AsyncCall() {
		if ( WorkLine ==null )
			WorkLine = new LinkedBlockingDeque<AsyncTask[]>();    // ��ʼ����ˮ��
		Running = true;
	}
	/**
	 * AsyncCall
	 * ���캯�������У����趨Ҫ�첽ִ�еķ���
	 * @param Object object Ҫִ�еĶ���
	 * @param String methodName Ҫִ�еķ�����
	 * @param Object... arguments Ҫ���ݵĲ���
	 */
	public AsyncCall( Object object, String methodName, Object... arguments ) {
		task = new AsyncTask[3];
		task[0] = new AsyncTask( object, methodName, arguments );
	}
	/**
	 * run
	 * ��������Ϊ��ѯ�߳�ʱ�����ô�run����������ֱ�ӵ��ã�
	 * @return void
	 */
	public void run() {    // ��������Ϊ��ѯ�߳�ʱ���д�run����
		AsyncTask[] current = null;
		while ( Running ) {
			while ( WorkLine!=null && WorkLine.peek() != null ) {
				current = WorkLine.remove();
				try { current[0].Run(); }
				catch (Throwable e) {
					try { current[2].Run(e); } catch(Throwable f){}    // һ���������󣬵���Ԥ���趨��onError�����������쳣������һ��������onError����������Ĵ��󽫱����ԣ�����onError����Ϊ�յ����
					continue;    // Ȼ������onSuccess������onSuccess����Ϊ�յ����
				}
				try { current[1].Run(); } catch(Throwable e){}    // onSuccess����������Ĵ��󽫱�����
			}
			try { Thread.sleep(30000); }    // ��Ϣ30�룬�ó�CPU����Ȩ
			catch (InterruptedException e) { continue; }    // ��֮�򱻳��ѣ�������ѯ
		}
	}
	/**
	 * onSuccess
	 * ָ��ִ�гɹ�ʱ�Ļص�����
	 * @param Object object Ҫִ�еĶ���
	 * @param String methodName Ҫִ�еķ�����
	 * @param Object... arguments Ҫ���ݵĲ���
	 * @return void
	 */
	public void onSuccess( Object object, String methodName, Object... arguments ) {
		task[1] = new AsyncTask( object, methodName, arguments );
	}
	/**
	 * onError
	 * ָ��ִ��ʱ�����쳣�Ļص�����
	 * @param Object object Ҫִ�еĶ���
	 * @param String methodName Ҫִ�еķ�����
	 * @return void
	 * ע�⣺�������ķ�����ֻ��ӵ��һ������ΪThrowable�Ĳ��������ڽ����쳣�ľ��
	 */
	public void onError( Object object, String methodName ) {
		Throwable err = new Throwable();
		task[2] = new AsyncTask( object, methodName, err );
	}
	/**
	 * call
	 * ��ʼִ������
	 * @return void
	 */
	public void call() {
		if ( EventLooper == null ) {    // �����ѯ�߳�����̬��Ϊnull������ƽ�ʬ̬^_^��
			AsyncCall worker = new AsyncCall();
			EventLooper = new Thread(worker);
			EventLooper.start();    // �������߳�
			call();    // �ٴγ��Իص�
		} else {
			WorkLine.offer(task);    // ��֮�����������У�
			EventLooper.interrupt();    // ����ͼ������ѯ�߳�
		}
	}
	/**
	 * (static) destory
	 * ֹͣ��ѯ�̣߳�����һ���й�AsyncCall����Դ
	 * @return void
	 */
	static void destory() {
		if ( EventLooper != null && EventLooper.isAlive() ) {    // ���̲߳���ʬ����
			EventLooper.interrupt();    // �����߳�
			while ( WorkLine.peek() != null ) {    // ĬĬ�ȴ���ˮ����ɡ���
				try { Thread.sleep(10); } catch (InterruptedException e){ }    // ÿ�εȴ�10ms
			}
			while ( EventLooper.isAlive() ) {    // ֻҪ�̻߳�ûͣ
				Running = false;    // ������״̬Ϊֹͣ
				EventLooper.interrupt();    // �ֳ���(>_<)
			}
			EventLooper = null;
			WorkLine = null;    // ��ƨ�ɲ���
		}
	}
}

/**
 * AsyncTask�ṹ��
 * �첽���������
 * @author JimmyLiu
 * @version 1.1
 */
class AsyncTask {
	Object object; String methodName; Object[] arguments;
	/**
	 * AsyncTask
	 * ���캯��
	 * @param Object object Ҫִ�еĶ�������
	 * @param String methodName Ҫִ�еķ�����
	 * @param Object... arguments Ҫ���ݵĲ���
	 */
	public AsyncTask( Object object, String methodName, Object... arguments ) {
		this.object = object; this.methodName = methodName; this.arguments = arguments;
	}
	/**
	 * Run
	 * ִ�г��ص���������Java�ķ������
	 * @param Throwable err = null Ҫ�����쳣��ı�������ѡ
	 * @return void
	 * @throws Throwable �׳��ص������з������쳣���ߣ�������������
	 *  - �����޷����ã�NoSuchMethodException��NullPointerException��SecurityException
	 *  - ���Ե���ʱ�Ĵ���NullPointerException��IllegalAccessException��IllegalArgumentException��ExceptionInInitializerError
	 */
	public void Run(Throwable err) throws Throwable {
		Class c = object.getClass();
		Class[] argTypes = new Class[arguments.length];
		try {
			for ( int i=0; i<arguments.length; i++ ) {
				argTypes[i] = arguments[i].getClass();    // ��һ��ȡÿ������������
			}
			Method m = c.getMethod(methodName, argTypes);    // Ѱ��ƥ��ķ���
			try {
				m.invoke(object, arguments);    // ����ִ��
			}
			catch (InvocationTargetException e) {    // ����ص����������׳��쳣
				throw e.getTargetException();    // ���ȡ���׳�Դ�쳣
			}
		} catch (Throwable e) {
			//e.printStackTrace();    // ��ʱ��ʵ�����׷�ٶ�ջ
			if ( err != null) err = e; 
			throw e;    // �ٴ��׳�
		}
	}
	public void Run() throws Throwable {
		Run(null);
	}
}
