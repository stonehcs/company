package com.lichi.increaselimit.common.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 随机数utils
 * 
 * @author majie
 *
 */
public class RandomUtils {

	/**
	 * 生成一个随机数
	 * 
	 * @param rangeL
	 * @param rangeR
	 * @return
	 */
	public static int generateRandomArray(int rangeL, int rangeR) {

		assert rangeL <= rangeR;

		return (int) (Math.random() * (rangeR - rangeL + 1) + rangeL);
	}

	/**
	 * 生成有n个元素的随机数组,每个元素的随机范围为[rangeL, rangeR]
	 * 
	 * @param n
	 * @param rangeL
	 * @param rangeR
	 * @return
	 */
	public static Set<Integer> generateRandomArray(int n, int rangeL, int rangeR) {

		assert rangeL <= rangeR;

		Set<Integer> set = new HashSet<>();
		while (set.size() < n) {

			int a = (int) (Math.random() * (rangeR - rangeL + 1) + rangeL);

//			a = a - (a % 10) + 10;  //进位

			set.add(a);

		}
		return set;
	}
	
	/**
	 * 百分比的产生随机数
	 * @param n
	 * @param rangeL
	 * @param rangeR
	 * @return
	 */
	public static Set<Integer> generateRandomArray(int n, double rangeL, double rangeR,int min,int max,double money) {

		assert rangeL <= rangeR;
		
		min = (int)(money*rangeL) + min;

		max = (int)(money*rangeR) + max;
		

		Set<Integer> set = new HashSet<>();
		while (set.size() < n) {

			int a = (int) (Math.random() * (min - max + 1) + max);

//			a = a - (a % 10) + 10;  //进位

			set.add(a);

		}
		return set;
	}

	public static void main(String[] args) {
		
		Integer integer = generateRandomArray(2, 10);
		System.out.println(integer);
		System.out.println("------------------------");
		Set<Integer> set = generateRandomArray(integer, 0.05, 0.2, 10, 100, 1000);
		set.forEach(e -> System.out.println(e));
	}
}
