package _11.cn.itcast.huffmanTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import _11.cn.itcast.huffmanTree.Huffman.Node;

public class HuffmanTest {
	@Test
	public void testBuildHuffmanTree() {
		Huffman huffman = new Huffman();
		String str = "hello world java hello world java hello world java ";
		byte[] strBytes = str.getBytes();
		System.out.println("源字节数组：" + Arrays.toString(strBytes));
		int[] frequencyTab = huffman.getFrequencyTabBySourceBytes(strBytes, null);
		
		Node huffmanTree = huffman.buildHuffmanTree(frequencyTab);
		
		String[] huffmanCodes = huffman.getHuffmanCodesByHuffmanTree(huffmanTree, "", null);
		System.out.println("哈夫曼编码表：" + Arrays.toString(huffmanCodes));
		for (int i = 0; i < huffmanCodes.length; i++) {
			if(huffmanCodes[i] != null) {
				System.out.println(huffmanCodes[i]);
			}
		}
		
		System.out.println("***********");
		
		StringBuilder sb = new StringBuilder();
		byte[] compressedBytes = new byte[strBytes.length];
		int len = huffman.compress(huffmanCodes, strBytes, strBytes.length, sb, compressedBytes);
		if(sb.length() > 0) {
			System.out.println("还剩下这些二进制字符串未处理：" + sb);
			compressedBytes = Arrays.copyOf(compressedBytes, compressedBytes.length + 1);
			// 剩下的二进制字符串肯定是不足 8 位，也就是说第8位肯定是补零，所以不可能超过 127
			// 不需要考虑负数情况
			compressedBytes[compressedBytes.length - 1] = (byte)Integer.parseInt(sb.toString(), 2);
		}
		System.out.println("压缩后的字节数组：" + Arrays.toString(compressedBytes));
	}
	
	@Test
	public void testCompressString() throws IOException, ClassNotFoundException {
		String str = "123";
		Huffman huffman = new Huffman();
		byte[] compressBytes = huffman.compressString(str, "utf-8");
		System.out.println("压缩前字节长度： " + str.getBytes().length);
		System.out.println("压缩后字节长度：" + compressBytes.length);
	}
	
	@Test
	public void testUncompressString() throws Exception {
		String str = "你好呀 java hello world java 早好了";
		System.out.println(str.length());
		Huffman huffman = new Huffman();
		byte[] compressBytes = huffman.compressString(str, "utf-8");
		
		String uncompressString = huffman.uncompressString(compressBytes);
		System.out.println(uncompressString);
		System.out.println(uncompressString.length());
	}
	
	@Test
	public void testCompressFile() throws Exception {
		Huffman huffman = new Huffman();
		huffman.compressFile("compressTest.bmp", null);
	}
	
	@Test
	public void testUncompressFile() throws Exception {
		Huffman huffman = new Huffman();
		huffman.uncompressFile("compressTest.bmp.mzip", "uncompress/");
	}
	
	// 使用一个视频文件进行压缩和解压
	// 确认代码没有什么问题
	@Test
	public void testCompressAndUnpressFile() throws Exception {
		Huffman huffman = new Huffman();
		huffman.compressFile("vedio.avi", null);
		huffman.uncompressFile("vedio.avi.mzip", "uncompress/");
	}
	
	@Test
	public void testStringBuilder() {
		StringBuilder sb = new StringBuilder("helloworld");
		System.out.println(sb.substring(0, 5));
		System.out.println(sb.substring(5, 10));
		System.out.println(sb.length());
		System.out.println(sb.delete(0, 5));
		
		System.out.println(Arrays.toString("".getBytes()));
	}
	
	@Test
	public void testStringFormat() {
		String str = "11123";
		System.out.println(String.format("%8s", str).replaceAll(" ", "0"));
		str = "12345678123456781234567802345678";
		System.out.println(str.substring(24));
	}
	
	// 测试一下，通过对象流添加一个对象数据到文件中
	//	对比学生对象的 id 为1 跟 为 2 时，文件的大小有没有发生变化。 如果没有变化的话，那么我们可以考虑在
	//  写入压缩数据之前先使用对象流写入 metaData 和 decodeMap
	//  当我们写入全部的压缩数据以后，这个时候我们已经知道最后一个字节对应的二进制字符串长度了
	//  更新完 metaData 以后，我们再重新创建一个对象流，去覆盖原先的  metaData 数据
	//   因为长度是固定的，所以只要我们保证开头的数据是 metaData ，那么就不会影响到其他的数据
	@Test
	public void testObjectLength() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("object.txt"));
		Student stu1 = new Student(2, "eric", 12);
		oos.writeObject(stu1);
		oos.flush();
		oos.close();
	}
	
	@Test
	public void testObjectOutputStream() throws FileNotFoundException, IOException, ClassNotFoundException {
		Student stu1 = new Student(2, "eric", 12);
		ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("object.txt"));
		oos1.writeObject(stu1);
		oos1.close();
		// 我们使用 fis1 读取 object.txt 里面的数据， 并把读取到的 全部数据使用 普通的输出流写入到 object2.txt
		FileInputStream fis1 = new FileInputStream("object.txt");
		byte[] bys = new byte[fis1.available()];
		fis1.read(bys);
		fis1.close();
		
		// 现在我们再 new 一个普通的 输出流
		FileOutputStream fos = new FileOutputStream("object2.txt");
		fos.write(bys);
		fos.close();
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object2.txt"));
		Student stu = (Student) ois.readObject();
		System.out.println(stu);
		ois.close();
	}
	
	@Test
	public void testFileName() {
		File file = new File("object.txt");
		System.out.println(file.getName());
	}
	
	// 这两个方法都是根据抽象的路径，也就是我们在构造函数输入的参数来返回特定的值的
	// 我们构造函数不是写绝对路径，所以这里全部返回 null
	@Test
	public void testParentFile() {
		File file = new File("object.txt");
		System.out.println(file.getParent());
		System.out.println(file.getParentFile());
	}
	
	@Test
	public void testGetFolder() {
		File file = new File("object.txt");
		String path = file.getAbsoluteFile().getParentFile().getPath();
		System.out.println(path);
	}
}
