package com.datang.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author yinzhipeng
 * @date:2016年2月22日 上午9:46:50
 * @version
 */
@Component
@Scope("singleton")
public class BeanLoader implements BeanFactoryAware {

	/**
	 * Bean工厂
	 */
	private static BeanFactory beanFactory = null;

	/**
	 * Bean装载器
	 */
	private static BeanLoader locator = null;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		BeanLoader.beanFactory = beanFactory;
	}

	/**
	 * 获取Bean工厂
	 * 
	 * @return
	 */
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * 从Bean工厂中获取共用的serviceLocator
	 * 
	 * @return
	 */
	public static BeanLoader getInstance() {
		if (locator == null)
			locator = (BeanLoader) beanFactory.getBean("beanLoader");
		return locator;
	}

	/**
	 * 根据提供的bean名称得到相应的服务类
	 * 
	 * @param beanName
	 *            bean名称
	 */
	public static Object getBean(String beanName) {
		return getInstance().getBeanFactory().getBean(beanName);
	}

	/**
	 * 根据提供的bean名称得到对应于指定类型的服务类
	 * 
	 * @param beanName
	 *            bean名称
	 * @param clazz
	 *            返回的bean类型,若类型不匹配,将抛出异常
	 */
	public static Object getBean(String beanName, Class<?> clazz) {
		return getInstance().getBeanFactory().getBean(beanName, clazz);
	}
}