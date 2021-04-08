/**
 * 
 */
package com.datang.util;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * ZMQ的req的socket池
 * 
 * @author yinzhipeng
 * @date:2016年7月19日 上午11:10:07
 * @version
 */
public class ZMQUtils {
	private static final Context context = ZMQ.context(1); // 创建一个I/O线程的上下文
	private static final int DEFAULT_POOL_SIZE = 5;// 默认池大小
	private static final Queue<Socket> pool;// 池
	static {
		pool = new ArrayBlockingQueue<Socket>(DEFAULT_POOL_SIZE);
	}

	/**
	 * 获取zmq的socket(req)
	 * 
	 * @return
	 */
	public static Socket getZMQSocket() {
		Socket socket = pool.poll();
		if (socket == null) {
			socket = context.socket(ZMQ.REQ);// 创建一个request类型的socket，这里可以将其简单的理解为客户端，用于向response端发送数据
		}
		return socket;
	}

	/**
	 * 将zmq的socket(req)归还池()
	 * 
	 * @param socket
	 */
	public static void releaseZMQSocket(Socket socket) {
		if (socket != null) {
			if (!pool.offer(socket)) {
				socket.close();
			}
		}
	}
	
	/**
	 * 将zmq的异常socket(req)处理并创建新的socket归还池()
	 * 
	 * @param socket
	 */
	public static void releaseSocketException(Socket socket) {
		if (socket != null) {
			socket = context.socket(ZMQ.REQ);
			if (!pool.offer(socket)) {
				socket.close();
			}
		}
	}

}
