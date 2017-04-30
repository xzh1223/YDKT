package com.ma.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 
 * @author MZ
 * @2016年10月31日
 */
public class HandleFileUtil {

	/**
	 * 获取当前时间
	 * @param fileName
	 */
	public static String getModifiedTime(String fileName) {
		File file = new File(fileName);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedTime = sdf.format(date);
		// System.out.println(modifiedTime);
		// System.out.println(date);
		return modifiedTime;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtensionName(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length() - 1))) {
				return fileName.substring(dot + 1);
			}
		}
		return fileName;
	}

	/**
	 * 获取文件夹名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getLastFileName(String filePath) {
		String fileName = "";
		fileName = filePath.trim();

		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

		return fileName;
	}

	/**
	 * 修改文件名
	 * @return String
	 * @param fileName
	 * @return
	 */
	public static String renameFile(String fileName) {
		String rename = "";
		rename = getRanNum() + "_" + fileName;
		return rename;
	}

	public static String getRanNum() {
		String num = "";
		Random random = new Random();
		num += random.nextInt(9) + 1;
		for (int i = 0; i < 15 - 1; i++) {
			num += random.nextInt(10);
		}

		return num;
	}

	public static void main(String[] args) {
		// String num = getRanNum();
		// System.out.println(num);
		// 542314561585046718
		String fileName = "作业.doc";
		System.out.println(renameFile(fileName));

	}
}
