/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * IO工具类
 * 
 * @author wangshuzhen
 * @version 1.0.0
 */
public abstract class IOUtils {
	/**
	 * 关闭输入流
	 * 
	 * @param in 输入流
	 */
	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @param out 输出流
	 */
	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 关闭Reader
	 * 
	 * @param reader Reader
	 */
	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 关闭Writer
	 * 
	 * @param writer Writer
	 */
	public static void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param connection Connection
	 * @param statement Statement
	 * @param resultSet ResultSet
	 */
	public static void close(Connection connection, Statement statement, ResultSet resultSet) {
		close(resultSet);
		close(statement);
		close(connection);
	}

	/**
	 * 关闭ResultSet
	 * 
	 * @param resultSet ResultSet
	 */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * 关闭Statement
	 * 
	 * @param statement　Statement
	 */
	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();

			} catch (SQLException ioe) {
			}
		}
	}

	/**
	 * 关闭Connection
	 * 
	 * @param connection Connection
	 */
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ioe) {
			}
		}
	}
}
