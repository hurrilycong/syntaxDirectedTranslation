import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;

public class translation {
	
	private static ArrayDeque<String> varible = new ArrayDeque<>();
	
	private static int TEMP = 0;
	
	public static void main(String[] args) {
		fenxi f1 = new fenxi();
		if (!f1.doFenxi()) {
			System.out.println("文件分析失败。退出程序。");
			System.exit(0);
		}
		String inner = fenxi.fname.concat(".inner");
		String num = fenxi.fname.concat(".num");
		ArrayDeque<String> innerNumber = getStringInFile(inner);
		varible = getStringInFile(num);
		while(!innerNumber.isEmpty()) {
			String aLine = "";
			if (!innerNumber.getFirst().equals("25")) {
				aLine = aLine.concat(innerNumber.peek()+" ");
				//对一行赋值语句进行翻译
				
			}
		}
	}
	
	/*
	 * getNewTemp 调用一次获得一个新的临时变量
	 * 
	 */
	public static String getNewTemp() {
		return "T".concat(String.valueOf(++TEMP));
	}
	
	/*
	 * 获得innerNumber中程序的一行
	 * 
	 */
	public static ArrayDeque<FourYuanExp> analyALine(String aLine) {
		aLine = aLine.concat("25");
		ArrayDeque<FourYuanExp> fourYuanExps = new ArrayDeque<>();
		ArrayDeque<String> sym = new ArrayDeque<>();
		ArrayDeque<String> place = new ArrayDeque<>();
		
		String[] strings = aLine.split(" ");
		for(int i = 0; i < strings.length; i++) {
			if (!strings[i].equals("25")) {
				if (strings[i].equals("3")) {
					i++;
					sym.push(strings[i]);
				}
			}
		}
		
		return fourYuanExps;
	}

	/*
	 * 获得文件内容。
	 * 
	 * 将翻译生成的机器内部代码文件和变量及值的文件加载到队列中。
	 * 
	 */
	public static ArrayDeque<String> getStringInFile(String filename) {
		ArrayDeque<String> innerNumber = new ArrayDeque<>();
		File fileInner = new File(filename);
		BufferedReader readerInner = null;
		try {
			readerInner = new BufferedReader(new FileReader(fileInner));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String temp = null;
			while((temp  = readerInner.readLine()) != null) {
				innerNumber.offer(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			readerInner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return innerNumber;
	}
}
