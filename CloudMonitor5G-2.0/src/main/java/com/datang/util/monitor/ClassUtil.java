package com.datang.util.monitor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.ReflectionException;

//off checkstyle
/**
 * Clazz类型处理工具
 * 
 * @author Bruce xu
 * @version 1.0.0
 */
public class ClassUtil {
	/**
	 * 原始类型对应的Class类
	 */
	private static Map<String, Object> primitiveClasses;

	static {
		primitiveClasses = new HashMap<String, Object>();
		primitiveClasses.put(Boolean.TYPE.toString(), Boolean.TYPE);
		primitiveClasses.put(Character.TYPE.toString(), Character.TYPE);
		primitiveClasses.put(Byte.TYPE.toString(), Byte.TYPE);
		primitiveClasses.put(Short.TYPE.toString(), Short.TYPE);
		primitiveClasses.put(Integer.TYPE.toString(), Integer.TYPE);
		primitiveClasses.put(Long.TYPE.toString(), Long.TYPE);
		primitiveClasses.put(Float.TYPE.toString(), Float.TYPE);
		primitiveClasses.put(Double.TYPE.toString(), Double.TYPE);
		primitiveClasses.put("boolean[]", boolean[].class);
		primitiveClasses.put("byte[]", byte[].class);
		primitiveClasses.put("char[]", char[].class);
		primitiveClasses.put("short[]", short[].class);
		primitiveClasses.put("int[]", int[].class);
		primitiveClasses.put("long[]", long[].class);
		primitiveClasses.put("float[]", float[].class);
		primitiveClasses.put("double[]", double[].class);
		primitiveClasses.put("Object", Object.class);
		primitiveClasses.put("Object[]", Object[].class);
		primitiveClasses.put("String", String.class);
		primitiveClasses.put("String[]", String[].class);
		primitiveClasses.put("void", Void.TYPE);
	}

	/**
	 * 根据完整类名返回包名 "com.datangmobile.util.ClassUtil" --> "com.datangmobile.util"
	 * 
	 * @param className
	 *            完整类名
	 * @return 返回包名
	 */
	public static String getPackageName(String className) {
		if (className == null) {
			throw new IllegalArgumentException("Null class name");
		}
		int index = className.lastIndexOf('.');
		if (index != -1) {
			return className.substring(0, index);
		}
		return "";
	}

	/**
	 * 取得类的实例
	 * 
	 * @param className
	 *            完整的类名
	 * @return 返回类实例.
	 * @throws ReflectionException
	 *             失败时抛异常.
	 */
	public static Object newInstance(String className)
			throws ReflectionException {
		Class clazz = internalLoadClass(className);
		return newInstance(clazz);
	}

	/**
	 * 内部加载类实例
	 * 
	 * @param className
	 *            完整类名
	 * @return 类定义clazz
	 * @throws ReflectionException
	 *             失败时抛异常.
	 */
	static Class internalLoadClass(String className) throws ReflectionException {
		try {
			return loadClass(className);
		} catch (Exception ex) {
			throw new ReflectionException(ex, "Loading class error:"
					+ className);
		}
	}

	/**
	 * 取得类的实例
	 * 
	 * @param clazz
	 *            类
	 * @return 类实例
	 * @throws ReflectionException
	 *             失败时抛异常.
	 */
	public static Object newInstance(Class clazz) throws ReflectionException {
		try {
			return clazz.newInstance();
		} catch (Exception x) {
			throw new ReflectionException(x, "Instantiation failed for "
					+ clazz.getName());
		}
	}

	/**
	 * 用给定的类装载器去装载类
	 * 
	 * @param loader
	 *            类加载器
	 * @param className
	 *            完整类名
	 * @return 返回对应的类
	 * @throws ClassNotFoundException
	 *             失败时抛异常.
	 */
	public static Class loadClass(ClassLoader loader, String className)
			throws ClassNotFoundException {
		return loader != null ? loader.loadClass(className)
				: loadClass(className);
	}

	/**
	 * 用系统默认的类装载器去装载类
	 * 
	 * @param className
	 *            完整类名
	 * @return 返回对应的类
	 * @throws ClassNotFoundException
	 *             如果该类不存在于系统的类装载器中
	 */
	public static Class loadClass(String className)
			throws ClassNotFoundException {
		ClassLoader loader = getClassLoader();
		return loader != null ? loadClass(loader, className) : Class
				.forName(className);
	}

	/**
	 * 返回ClassLoader
	 * 
	 * @param file
	 *            资源路径
	 * @throws ClassNotFoundException
	 *             如果该类不存在于系统的类装载器中
	 * @throws java.net.MalformedURLException
	 *             如果该类不存在于系统的类装载器中
	 * @return ClassLoader
	 */
	public static ClassLoader getClassLoader(File file)
			throws ClassNotFoundException, MalformedURLException {
		if (!file.canRead()) {
			return null;
		}
		if (file.isDirectory()) {
			return getClassLoaderFromJars(file);
		} else {
			return getClassLoaderFromJar(file);
		}
	}

	/**
	 * 当为目录时返回ClassLoader
	 * 
	 * @param file
	 *            jar目录路径
	 * @return ClassLoader
	 * @throws ClassNotFoundException
	 *             如果该类不存在于系统的类装载器中
	 * @throws java.net.MalformedURLException
	 *             如果该类不存在于系统的类装载器中
	 */
	public static ClassLoader getClassLoaderFromJars(File file)
			throws ClassNotFoundException, MalformedURLException {
		List<URL> list = new ArrayList<URL>();

		File jars[] = file.listFiles(new JarFileList());
		for (File jar : jars) {
			list.add(jar.toURL());
		}
		URL jarurl[] = list.toArray(new URL[jars.length]);
		return URLClassLoader.newInstance(jarurl);
	}

	/**
	 * 当为单个文件时返回ClassLoader
	 * 
	 * @param file
	 *            jar文件
	 * @throws ClassNotFoundException
	 *             如果该类不存在于系统的类装载器中
	 * @throws java.net.MalformedURLException
	 *             如果该类不存在于系统的类装载器中
	 * @return ClassLoader
	 */
	public static ClassLoader getClassLoaderFromJar(File file)
			throws ClassNotFoundException, MalformedURLException {
		URL jarurl[] = { file.toURL() };
		return URLClassLoader.newInstance(jarurl);
	}

	/**
	 * Return the default ClassLoader to use: typically the thread context
	 * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
	 * class will be used as fallback.
	 * <p>
	 * Call this method if you intend to use the thread context ClassLoader in a
	 * scenario where you absolutely need a non-null ClassLoader reference: for
	 * example, for class path resource loading (but not necessarily for
	 * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
	 * reference as well).
	 * 
	 * @return the default ClassLoader (never <code>null</code>)
	 * @see java.lang.Thread#getContextClassLoader()
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtil.class.getClassLoader();
		}
		return cl;
	}

	/**
	 * 返回类装载器
	 * 
	 * @return 类装载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}
		return loader;
	}

	/**
	 * 根据资源文件的路径，取得classload的输入流
	 * 
	 * @param resourceName
	 *            资源文件
	 * @return 输入流
	 */
	public static InputStream getResourceAsStream(String resourceName) {
		return getResourceAsStream(null, resourceName);
	}

	/**
	 * 根据资源文件的路径，取得classload的输入流
	 * 
	 * @param loader
	 *            类加载器
	 * @param resourceName
	 *            资源文件
	 * @return 输入流
	 */
	public static InputStream getResourceAsStream(ClassLoader loader,
			String resourceName) {
		try {
			return new BufferedInputStream(new FileInputStream(resourceName));
		} catch (Exception ioe) {
			if ((resourceName.charAt(0)) == '/') {
				resourceName = resourceName.substring(1);
			}
			if (loader == null) {
				loader = getClassLoader();
			}
			return loader.getResourceAsStream(resourceName);
		}

	}

	/**
	 * 根据资源文件的路径，取得classload中的枚举类型
	 * <p/>
	 * <p/>
	 * For example, to print all elements of a vector <i>v</i>: <blockquote>
	 * <p/>
	 * 
	 * <pre>
	 *     for (Enumeration e = v.elements() ; e.hasMoreElements() ;) {
	 *         System.out.println(e.nextElement());&lt;br&gt;
	 *     }
	 * </pre>
	 * <p/>
	 * </blockquote>
	 * <p/>
	 * 
	 * @param resourceName
	 *            资源文件
	 * @return 枚举类型
	 * @throws IOException
	 *             IO异常
	 * @see java.util.Enumeration
	 */
	public static Enumeration getResources(String resourceName)
			throws IOException {
		return getResources(null, resourceName);
	}

	/**
	 * 根据资源文件的路径，取得classload中的枚举类型
	 * <p/>
	 * <p/>
	 * For example, to print all elements of a vector <i>v</i>: <blockquote>
	 * <p/>
	 * 
	 * <pre>
	 *     for (Enumeration e = v.elements() ; e.hasMoreElements() ;) {
	 *         System.out.println(e.nextElement());&lt;br&gt;
	 *     }
	 * </pre>
	 * <p/>
	 * </blockquote>
	 * <p/>
	 * 
	 * @param resourceName
	 *            资源文件
	 * @param loader
	 *            类加载器
	 * @return 枚举类型
	 * @throws IOException
	 *             IO异常
	 * @see java.util.Enumeration
	 */
	public static Enumeration getResources(ClassLoader loader,
			String resourceName) throws IOException {
		if ((resourceName.charAt(0)) == '/') {
			resourceName = resourceName.substring(1);
		}
		if (loader == null) {
			loader = getClassLoader();
		}
		return loader.getResources(resourceName);
	}

	/**
	 * 根据资源文件，返回文件的URL路径
	 * 
	 * @param resourceName
	 *            资源文件
	 * @return URL 统一资源定位
	 * @see java.net.URL
	 */
	public static URL getResource(String resourceName) {
		return getResource(null, resourceName);
	}

	/**
	 * 根据资源文件，从calssload中返回文件的URL路径
	 * 
	 * @param loader
	 *            类加载器
	 * @param resourceName
	 *            资源文件
	 * @return URL 统一资源定位
	 * @see java.net.URL
	 */
	public static URL getResource(ClassLoader loader, String resourceName) {
		if ((resourceName.charAt(0)) == '/') {
			resourceName = resourceName.substring(1);
		}
		if (loader == null) {
			loader = getClassLoader();
		}
		return loader.getResource(resourceName);
	}

	/**
	 * 返回type对应的类.
	 * 
	 * @param type
	 *            primitiveClasses中的key
	 * @return the corresponding class, or null.
	 */
	public static Class getPrimitiveClass(String type) {
		return (Class) primitiveClasses.get(type);
	}

	/**
	 * 根据完整类名返回短类型 "com.datangmobile.util.ClassUtil" --> "ClassUtil"
	 * 
	 * @param className
	 *            完整类名
	 * @return 短Clss类名类型
	 */
	public static String getLocalName(String className) {
		if (className == null) {
			throw new IllegalArgumentException("Null class name");
		}
		int index = className.lastIndexOf('.');
		if (index != -1) {
			return className.substring(index + 1);
		}
		return className;
	}

	/**
	 * 将Method列表转换成Method数组
	 * 
	 * @param list
	 *            Method列表
	 * @return 方法数组
	 */
	public static Method[] toArray(List<Method> list) {
		Method[] methods;
		int sz = list.size();
		if (sz > 0) {
			methods = list.toArray(new Method[sz]);
		} else {
			methods = new Method[0];
		}
		return methods;
	}

	/**
	 * 文件过滤器
	 */
	private static class JarFileList implements FileFilter {
		/**
		 * 过滤方法，如果是jar文件返回true
		 * 
		 * @param pathname
		 *            文件名
		 * @return true/false
		 */
		public boolean accept(File file) {
			return file.getName().toLowerCase().endsWith(".jar");
		}
	}
}
// on checkstyle