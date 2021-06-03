package com.datang.util;

/**
 * 
 *  求实数 虚数
 *
 */
public class Complex {
	private double x;// 实部
	private double y;// 虚部

	public Complex() {
	}

	/**
	 * 构造函数
	 * 
	 * @param x
	 *            实数部分
	 * @param y
	 *            虚数部分
	 */
	public Complex(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * 求模
	 * 
	 * @return 该复数的模
	 */
	public double mod() {
		return x * x + y * y;
	}

	/**
	 * 复数间加法
	 * 
	 * @param complex
	 *            加数
	 * @return 计算结果
	 */
	public Complex add(Complex complex) {
		double x = this.x + complex.x;
		double y = this.y + complex.y;
		return new Complex(x, y);
	}

	/**
	 * 复数与实数的加法
	 * 
	 * @param a
	 *            加数
	 * @return 计算结果
	 */
	public Complex add(double a) {
		return this.add(new Complex(a, 0));
	}

	/**
	 * 复数间减法
	 * 
	 * @param complex
	 *            减数
	 * @return 计算结果
	 */
	public Complex subtract(Complex complex) {
		double x = this.x - complex.x;
		double y = this.y - complex.y;
		return new Complex(x, y);
	}

	/**
	 * 复数与实数的减法
	 * 
	 * @param a
	 *            减数
	 * @return 计算结果
	 */
	public Complex subtract(double a) {
		return subtract(new Complex(a, 0));
	}

	/**
	 * 复数间乘法
	 * 
	 * @param complex
	 *            乘数
	 * @return 计算结果
	 */
	public Complex multiply(Complex complex) {
		double x = this.x * complex.x - this.y * complex.y;
		double y = this.y * complex.x + this.x * complex.y;
		return new Complex(x, y);
	}

	/**
	 * 复数间除法
	 * 
	 * @param complex
	 *            除数
	 * @return 计算结果
	 */
	public Complex divide(Complex complex) {
		double x = (this.x * complex.x + this.y * complex.y) / (complex.mod());
		double y = (this.y * complex.x - this.x * complex.y) / (complex.mod());
		return new Complex(x, y);
	}

	/**
	 * 复数的平均值
	 * @param complex
	 * @return
	 */
	public static double abs(Complex complex) {
		double x = complex.x;
		double y =complex.y;
		double abs=Math.sqrt(x*x+y*y);
		return abs;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (x != 0) {
			sb.append(x);
			if (y > 0) {
				sb.append("+" + y + "i");
			} else if (y < 0) {
				sb.append(y + "i");
			}
		} else {
			if (y != 0) {
				sb.append(y + "i");
			}
		}
		if (x == 0 && y == 0) {
			return "0";
		}
		return sb.toString();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
