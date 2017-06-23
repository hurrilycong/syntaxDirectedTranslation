

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

public class fenxi {
	static String returnNumber = "";
	
	public static String fname = "";

	public boolean doFenxi()  {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);

		HashMap<String, Integer> keyvalue = new HashMap<String, Integer>();
		keyvalue = readFileByLines("keyword.txt");

		System.out.println("请输入源文件名");
		String filename = input.nextLine();
		File file = new File(filename);
		String[] name = filename.split("\\.");//小数点.有特殊意义，需转义
		String innerName = name[0].concat(".inner");
		fname = name[0];
		String numName = name[0].concat(".num");
		BufferedReader reader = null;
		BufferedWriter outputStream = null;
		BufferedWriter outputStream2 = null;
		returnNumber = "";
		try
		{
			reader = new BufferedReader(new FileReader(file));
			outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(innerName)));

			String tempString = null;
			while((tempString = reader.readLine()) != null)
			{
				String aString = fenxiByLine(tempString, keyvalue);
				if (aString != null) {
					outputStream.write(aString);
				}
				else
				{
					System.out.println("出现错误");
				}
			}
		}
		catch(IOException ioException)
		{
			//ioException.printStackTrace();
			System.out.println("文件不存在。请确认后再输入。");
			return false;
		}
		finally {
			if (reader != null) {
				try
				{
					reader.close();
				}
				catch(Exception exception)
				{		
					exception.printStackTrace();
					return false;
				}
			}
			try {
				outputStream.close();
				System.out.println("转换结束");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		if (!returnNumber.equals("")) {
			try {
				outputStream2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(numName)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return false;
			}
			try {
				outputStream2.write(returnNumber);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outputStream2.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/*
	 * 以行为单位读取文件
	 * 读取类号的文件操作
	 * 
	 */
	public static HashMap<String, Integer> readFileByLines(String filename)
	{
		File file = new File(filename);
		BufferedReader reader = null;
		HashMap<String, Integer> keyvalue = new HashMap<String, Integer>();
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while((tempString = reader.readLine()) != null)
			{
				String[] line = tempString.split(" ");
				try
				{
					int a = Integer.parseInt(line[0]);
					keyvalue.put(line[1], a);
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return keyvalue;
	}

	/*
	 * 词法分析过程
	 * 
	 *
	public static String fenxiByLine(String string, HashMap<String, Integer> keyvalue)
	{
		String[] str = string.split(" ");
		String returnString = new String();
		Object object = null;
		for(int i = 0;i < str.length;i++)
		{
			if ((object = keyvalue.get(str[i])) != null) {
				returnString = returnString.concat(String.valueOf((int)object)+" "+str[i]+"\r\n ");
			}
			else if (isVariable(str[i])) {
				returnString = returnString.concat("9  变量"+str[i]+"\r\n ");
			}
			else {
				try {
					Integer.parseInt(str[i]);
					returnString = returnString.concat("10 数字"+str[i]+"\r\n ");
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("不能识别:"+str[i]);
				}
			}
		}
		return returnString;
	}*/

	/*
	 * 另一种方法来实现分析每一行
	 * 
	 */
	public static String fenxiByLine(String string, HashMap<String, Integer> keyvalue)
	{
		String returnString = new String();
		int a = 0;
		boolean judge = false;
		String str = new String();
		for(int i = 0;i < string.length();i++)
		{
			if (isLetter(string.charAt(i))) {
				str = str.concat(String.valueOf(string.charAt(i)));
			}
			else if (isNumber(string.charAt(i))) {
				int n = i+1;
				for(;n < string.length();i++)
				{
					if (!isNumber(string.charAt(n))) {
						break;
					}
				}
				if (n == i+1) {
					returnString = returnString.concat("10"+"\r\n");
					returnNumber = returnNumber.concat(string.charAt(i)+"\r\n");
				}
				else {
					String number = string.substring(i, n);
					returnString = returnString.concat("10"+"\r\n");
					returnNumber = returnNumber.concat(number+"\r\n");
				}	
			}
			else if (string.charAt(i) == ' ') {
				if (!str.equals("") && (a = searchHashMap(str, keyvalue)) != 0) {
					returnString = returnString.concat(String.valueOf(a)+"\r\n");	
				}
				else if (isVariable(str)) {
					returnString = returnString.concat("9"+"\r\n");
					returnNumber = returnNumber.concat(str+"\r\n");
				}
				else {
					for(int i1 = 0;i1 < str.length();i1++)
					{
						if (isNumber(str.charAt(i1))) {
							judge = true;
						}
						else							
						{	
							judge = false;
							System.out.println("不能识别:"+str);
						}
					}
					if (judge == true) {
						judge = false;
						returnString = returnString.concat("10"+"\r\n");
						returnNumber = returnNumber.concat(str+"\r\n");
					}
				}
				str = "";
			}
			else if (isOperator1(string.charAt(i))) {
				if (!str.equals("") && (a = searchHashMap(str, keyvalue)) != 0) {
					returnString = returnString.concat(String.valueOf(a)+"\r\n");
				}
				else if (isVariable(str)) {
					returnString = returnString.concat("9"+"\r\n");
					returnNumber = returnNumber.concat(str+"\r\n");
				}
				else {
					for(int i3 = 0;i3 < str.length();i3++)
					{
						if (isNumber(str.charAt(i3))) {
							judge = true;
						}
						else							
						{	
							judge = false;
							System.out.println("不能识别:"+str);
						}
					}
					if (judge == true) {
						judge = false;
						returnString = returnString.concat("10"+"\r\n");
						returnNumber = returnNumber.concat(str+"\r\n");
					}
				}
				if ((i != string.length()-1) && isOperator2(string.charAt(i+1))) {
					String string2 = string.substring(i, i+2);		
					if ((a = searchHashMap(string2, keyvalue)) != 0) {
						returnString = returnString.concat(String.valueOf(a)+"\r\n");
						i++;
					}
					else {
						System.out.println("发生错误");
					}
				}
				returnString = returnString.concat(String.valueOf(searchHashMap(String.valueOf(string.charAt(i)), keyvalue))+"\r\n");
				str = "";
			}
		}
		//System.out.println(returnNumber);
		return returnString;
	}

	/*
	 * searchHashMap
	 * 搜索HashMap中的value
	 * 
	 */
	public static int searchHashMap(String string, HashMap<String, Integer> keyvalue)
	{
		Object object = null;
		int a = 0;
		if((object = keyvalue.get(string)) != null)
		{
			a = (int)object;
		}
		return a;
	}

	/*
	 * 判断是否是变量名
	 * 
	 */
	public static boolean isVariable(String string)
	{
		if (string.equals("")) {
			return false;
		}
		if (isLetter(string.charAt(0)) || string.charAt(0) == '_') {
			for(int i = 1;i < string.length();i++)
			{
				if (!(isLetter(string.charAt(i)) || isNumber(string.charAt(i)) || string.charAt(i) == '_')) {
					return false;
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * isLetter
	 * 判断是否为字母
	 * 
	 */
	public static boolean isLetter(char ch)
	{
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
			return true;
		}
		else {
			return false;
		}
	}

	/*
	 * isNumber
	 * 判断是否是数字
	 * 
	 */
	public static boolean isNumber(char ch)
	{
		if (ch >= '0' && ch <= '9') {
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * isOperator1
	 * 判断是否为运算符
	 * 
	 */
	public static boolean isOperator1(char ch)
	{
		if (ch == '=' || ch == '<' || ch == '>') {
			return true;
		}
		else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
			return true;
		}
		else if (ch == '!' || ch == '|' || ch == '%' || ch == '&') {
			return true;
		}
		else if (ch == '(' || ch == ')' || ch == '{' || ch == '}') {
			return true;
		}
		else if (ch == ';') {
			return true;
		}
		return false;
	}

	/*
	 * isOperator2
	 * 判断第二个字符是否是运算符
	 * 
	 */
	public static boolean isOperator2(char ch)
	{
		if (ch == '=' || ch == '<' || ch == '>') {
			return true;
		}
		else if (ch == '+' || ch == '-') {
			return true;
		}
		else if (ch == '|' || ch == '&') {
			return true;
		}
		else if (ch == ';') {
			return true;
		}
		return false;
	}
}
