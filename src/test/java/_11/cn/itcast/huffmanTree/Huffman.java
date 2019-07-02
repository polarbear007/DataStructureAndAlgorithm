package _11.cn.itcast.huffmanTree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import _04.cn.itcast.linkedList.SinglyLinkedList;

/**
 * 哈夫曼压缩的基本流程 
 * 1、 先遍历源字节数组，得到一个字节出现频率统计表（我们是使用数组来处理的） 
 * 2、然后我们遍历频率统计表，根据所有频率大于0的字节生成 结点对象，从而生成一颗 哈夫曼树。 
 * 3、我们根据哈夫树得到哈夫曼编码表，这个编码属于前缀编码。（固定操作，为什么这样就可以生成前缀编码，尽量去理解就好了，不理解也没关系） 4、
 * 		有了哈夫曼编码表，我们就可以遍历源字节数组，然后把每个字节替换成对应的哈夫曼编码。因为编码我们是使用字符串处理，所以替换以后
 * 		得到的也是一个二进制字符串编码。 现在我们需要再把这个二进制字符串编码转成字节数组，最终的这个字节数组才是压缩以后的数据。
 * 
 * 解压的基本流程： 
 * 	1、 先根据前面的哈夫曼编码表，生成一个解码表。（根据哈夫曼编号找到对应的字节值）
 * 		前面的哈夫曼表，因为字节值都是整数，所以我们使用数组进行存储，把字节值转成数组索引，编码转成元素值。
 * 		现在需要反过来，因为编码是字符串，字符串没办法做为索引，所以我们只好使用一个map 集合来保存这个解码表。
 * 
 * 2、 然后我们需要遍历压缩以后的字节数组，先转成二进制字符串 
 * 	【注意】转成二进制字符串的时候，应该是每8位二进制字符串对应一个字节数据，我们使用Integer.toBinaryString() 
 * 		转成二进制字符串时，要注意以下的问题： 
 * 				1、 一个字节实际上是对应8位，我们使用Integer的api去转，负数会转成 32 位，需要截取成后8位。 
 * 				2、 正数转换后，可能会不足8位，我们需要自己补零，补到8位。
 * 
 * 【注意】 在压缩的过程中，二进制字符串最后长度不够8位，我们是按 8 位处理。
 * 		也就是说最后一个字节对应的二进制字符串长度是不确定的。如果我们前面没有处理的话，那么这里我们就无法再把 最后一个字节数据 转成 
 * 		原来的 二进制字符串。
 * 		假设现在 最后一个字节的值是 12， 对应二进制字符串可能是： 1100 / 01100 / 001100/ 0001100/ 00001100
 * 		假如最后一个字节的值是负数，那么说明当时肯定刚好8位，但是没有那么理想。
 * 			 ===>所以其实，当时压缩的时候，如果我们没有对最后一位进行特殊的处理，那么这个时候，再转成二进行字符串时就会出问题。 
 * 	【最后方案】
 * 		我们压缩的时候不特别处理，就正常转成一个 byte ，但是需要使用一个变量另外保存最后一个字节对应多少位二进制字符串。
 * 		我们使用一个 MetaData 对象来保存一些相关的信息。
 * 
 * @author Administrator
 *
 */
public class Huffman implements Serializable{
	private static final long serialVersionUID = -8913866455835641487L;
	
	public void uncompressFile(String targetZipFilePath, String destFolderPath) throws Exception {
		if(targetZipFilePath == null) {
			throw new RuntimeException("目标文件路径不能为空");
		}
		File targetZipFile = new File(targetZipFilePath);
		if(!targetZipFile.exists()) {
			throw new RuntimeException("目标文件不存在！");
		}
		if(!targetZipFile.getName().endsWith(".mzip")) {
			throw new RuntimeException("只支持解压 .mzip 文件！");
		}
		
		// 再然后，我们尝试通过对象流获取 metaData 和 解码表，如果获取不了，那么同样抛异常
		FileInputStream fis = new FileInputStream(targetZipFile);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		MetaData metaData = null;
		HashMap<String, Byte> decodeMap = null;
		try {
			metaData = (MetaData) ois.readObject();
			decodeMap = (HashMap<String, Byte>) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("无法解压！！");
		}
		
		
		// 现在我们已经拿到了 元信息 和 解码表了，就可以开始遍历后面的数据，并进行解码操作了
		// 首先，我们先根据解码表计算一下 最长的编码  和 最短编码 的长度， 优化一下遍历 sb 的过程
		int minLen = Integer.MAX_VALUE;
		int maxLen = 0;
		for (String code : decodeMap.keySet()) {
			System.out.println(code);
			if(code.length() < minLen) {
				minLen = code.length();
			}
			if(code.length() > maxLen) {
				maxLen = code.length();
			}
		}
		
		// 创建输出流，准备把解压后的数据写入到特定的文件中去
		// 如果我们指定了特定的文件夹路径，那么就解压到此路径中去； 如果我们没有指定路径
		// 那么默认解压到压缩文件同级目录
		File destFolder = null;
		if(destFolderPath != null) {
			destFolder = new File(destFolderPath);
			destFolder.mkdirs();
		}else {
			destFolder = targetZipFile.getAbsoluteFile().getParentFile();
		}
		
		// 现在我们可以安心地创建解压文件的输出流了
		FileOutputStream fos = new FileOutputStream(new File(destFolder, metaData.fileName));
		
		// 创建我们要把根据压缩后的字节数组根据解码表 生成的二进制字符串保存到一个 StringBuilder 对象中
		StringBuilder sb = new StringBuilder();
		// 创建一个 sourceBytes 字节数组，用来保存 二进制字符串再次转码后的字节数据，这些数据就是源文件的数据了
		// 我们在压缩文件的时候，说过哈夫曼编码是不定长编码，所以 1024 长度的压缩数据，最多能解压出多大的数据呢？
		// 我们先假设这些数据都是使用最短的哈夫曼编码组成的，也就是说    compressedBytes.lenth * 8 / minLen + 1
		// 因为这是解码，如果前面代码没错的话，每次解码 sb 里面的二进制字符串都会被消耗光，不会有剩余，所以我们就按上面的
		// 那个最大值来处理就好了
		byte[] compressedBytes = new byte[1024];
		byte[] sourceBytes = new byte[compressedBytes.length * 8 / minLen + 1];
		int len = 0;
		// 这个变量表示表示每次解码以后，  sourceBytes 数组里面实际有效的数据个数
		// 前面我们已经保证了 sourceBytes 的长度是够用的
		int sourceLen = 0;
		while((len = fis.read(compressedBytes)) != -1) {
			// 读取到 压缩后的字节数据，我们就先转成 二进制字符串
			// 这里我们得判断一下是不是最后一次解码了,如果 fis.available() == 0 ,说明这是最后一次解码了
			getBinaryStringByCompressedBytes(metaData, compressedBytes, len, fis.available() == 0, sb);
			
			// 得到了 sb 以后，我们就可以调用 decode() 方法对这个二进制字符串进行解码
			sourceLen = decode(decodeMap, minLen, maxLen, sb, sourceBytes);
			
			fos.write(sourceBytes, 0, sourceLen);
			fos.flush();
		}
		
		// 全部写出完毕以后，我们需要关闭流
		fos.close();
		fis.close();
	}
	
	/**
	 *	压缩文件的方法
	 * @param targetFilePath	传入目标文件的路径， 如果找不到指定的目标文件，则抛异常
	 * @param destFilePath		要生成的压缩文件的路径（如果为 null，则默认在目标文件同目录下，名字为  目标文件名.mzip）
	 * 							比如说，目标文件名     hello.jpg ===>   hello.jpg.mzip
	 * @throws Exception 
	 */
	public void compressFile(String targetFilePath, String destFilePath) throws Exception {
		if(targetFilePath == null) {
			throw new RuntimeException("目标文件路径不能为空");
		}
		File targetFile = new File(targetFilePath);
		if(!targetFile.exists()) {
			throw new RuntimeException("目标文件不存在！");
		}
		
		// 如果前面没有抛异常，那么说明目标文件存在，我们再看一下压缩文件的路径是否有效
		if(destFilePath == null) {
			destFilePath = targetFilePath + ".mzip";
		}
		// 最后我们创建压缩文件对象
		File destFile = new File(destFilePath);
		
		// 再然后，我们就开始使用 io 流读取目标文件，然后创建频率统计表
		int[] frequencyTab = null;
		FileInputStream fis = new FileInputStream(targetFile);
		byte[] bys = new byte[1024];
		int len = 0;
		while((len = fis.read(bys)) != -1) {
			frequencyTab = getFrequencyTabBySourceBytes(bys, frequencyTab);
		}
		
		// 拿到了频率统计表以后，我们先关闭一下流
		fis.close();
		
		// 再然后，我们就可以去创建哈夫曼树了
		Node huffmanTree = buildHuffmanTree(frequencyTab);
		
		// 再然后，我们就可以去创建哈夫曼编码表了
		String[] huffmanCodes = getHuffmanCodesByHuffmanTree(huffmanTree, "", null);
		
		// 再然后，我们就可以根据编码表开始压缩了
		// 我们重新再创建一个输入流
		fis = new FileInputStream(targetFile);
		// 创建一个多次共享的 sb 对象
		StringBuilder sb = new StringBuilder();
		// 创建一个多次共享的 compressedBytes 
		// 注意，为了多次使用 compressedBytes ，我们需要每次确保 compressedBytes 长度够用
		// 我想先找到最长的哈夫曼编码，然后假定源字节数组里面的每个字节都对应最长的哈夫曼编码
		// 于是，compressedBytes 最大长度应该是     maxCodeLen * bys.length / 8 + 1
		// 又因为 sb 里面可能会有上次未处理完的二进制字符串，但是最大长度不会超过 8
		// 所以最终  compressedBytes 最大长度应该是     maxCodeLen * bys.length / 8 + 2
		int maxCodeLen = 0;
		for (String code : huffmanCodes) {
			if(code != null && code.length() > maxCodeLen) {
				maxCodeLen = code.length();
			}
		}
		byte[] compressedBytes = new byte[maxCodeLen * bys.length / 8 + 2];
		
		// 创建一个输出流，把转码以后的压缩数据直接保存到压缩文件中
		FileOutputStream fos = new FileOutputStream(destFile);
		// 在输出流基础上再创建一个对象流
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		// 不过在开始写压缩数据之前，我们应该先写一些固定的信息
		// 最开始一定要写入 MetaData 数据，虽然现在我们还不知道最后一个字节的二进制字符长度，但是可以先占位置
		MetaData metaData = new MetaData();
		metaData.fileName = targetFile.getName();
		metaData.length = targetFile.length();
		// 这里我们默认值设置成 8 ，如果刚好是8，我们就不需要再覆盖
		metaData.lastByteLen = 8;
		oos.writeObject(metaData);
		
		// 接着，我们还需要写入转码表
		Map<String, Byte> decodeMap = getDecodeMapByHuffmanCodes(huffmanCodes);
		for (String code : decodeMap.keySet()) {
			System.out.println(code);
		}
		oos.writeObject(decodeMap);
		// 刷新一下
		oos.flush();
		
		// 再然后，我们就可以开始写压缩的数据了
		int compressedBytesLen = 0;
		while((len = fis.read(bys)) != -1) {
			// 现在 compressedBytes 里面就是保存压缩以后的字节数据， compressedBytesLen 就是这个有效数据的长度
			compressedBytesLen = compress(huffmanCodes, bys, len, sb, compressedBytes);
			fos.write(compressedBytes, 0, compressedBytesLen);
			fos.flush();
		}
		
		// 读取结束，关闭输入流
		fis.close();
		
		// compress 方法是不处理最后不满 8 位的数据的，所以我们这里还得再看一下 sb 里面是否还有数据
		if(sb.length() > 0) {
			metaData.lastByteLen = (byte)sb.length();
			// 转换最后一个字节数据，并写到压缩文件中
			byte lastByte = (byte)Integer.parseInt(sb.toString(), 2);
			fos.write(lastByte);
			fos.flush();
		}
		
		// 关闭输出流
		oos.close();
		fos.close();
		
		// 现在压缩数据已经全部写入，我们再来看一下， metaData.lastByteLen 不等于8
		// 如果大于零，那么我们需要覆盖 metaData 对象的数据
		// 【注意】 覆盖开头的 metaData 对象时，还有点小麻烦，因为如果你直接使用对象流去覆盖的话，有两种情况
		//        1、 默认情况下，会用 metaData 这个对象的序列化字节数组覆盖压缩文件的全部内容
		//        2、 如果你开启追加模式，那么 metaData 虽然不会覆盖全部内容，但是只是追加内容，覆盖不了开头的 metaData 数据
		//    上面两种情况，都不能完成我们的需求。
		//     ===> 只有 RandomAccessFile 可以实现我们的需求。 
		//          但是 RandomAccessFile 又没办法实现序列化！！！！
		//     ===> 暂时我们是使用对象流把新的 metaData 数据写到一个临时文件中，然后再使用一个普通的流读取临时文件
		//           里面的全部数据到一个字节数组中。
		//           最后，我们再 把这个字节数组数据 通过 RandomAccessFile 去覆盖压缩文件的文件头信息。
		// 		【注意】 写入的临时文件应该确保是不存在的文件，我们完成操作以后，也需要马上删除。
		if(metaData.lastByteLen != 8) {
			File tempFile = new File(UUID.randomUUID().toString()+ ".txt");
			// 确定这个临时文件是不存在的文件
			while(tempFile.exists()) {
				tempFile = new File(UUID.randomUUID().toString()+ ".txt");
			}
			oos = new ObjectOutputStream(new FileOutputStream(tempFile));
			oos.writeObject(metaData);
			oos.close();
			fis = new FileInputStream(tempFile);
			byte[] tempBytes = new byte[fis.available()];
			fis.read(tempBytes);
			fis.close();
			// 读取完毕以后，我们马上删除临时文件
			tempFile.delete();
			// 使用  RandomAccessFile 去覆盖特定压缩文件的头部数据
			RandomAccessFile raf = new RandomAccessFile(destFile, "rw");
			raf.write(tempBytes);
			raf.close();
		}
	}

	/**
	 * 	传入 sb 对象，然后把解析二进制字符串的结果写入 sourceBytes 数组里面，并返回
	 * 	为了避免频繁创建 byte[] 数组，我们这里添加一个  sourceBytes 参数，你可以多次传入同一个数组
	 * 
	 * 	因为哈夫曼编码是不定长的，所以我们无法像以前那样每次遍历 8 位 sb 里面的字符，得到一个固定的编码
	 * 	我们得一位一位地遍历 sb ，然后依次去对照  decodeMap ，看看有没有对应的编码
	 * 		有的话，就转成一个 byte 值
	 * 		没有的话，就再多加一位，再看看有没有。
	 * 	【说明】 遍历的时候，我们不需要担心会找不到对应的编码，只要我们前面的代码没写错，我们现在遍历的二进制字符串就是
	 * 			之前我们根据哈夫曼编码表生成的。现在反过来操作，肯定能在解码表里面找到对应的编码的。
	 * 
	 * 	【说明】当我们是在解压一个文件的时候，虽然最终一定会刚好都找到对应的编码，但是在解码的过程中，因为我们是多次调用
	 * 			此方法进行解码，多次传入二进制字符串，也就是说二进制字符串是破碎的。那么就存在这种可能性：
	 * 			即在一次解码的过程中，因为二进制字符串可能被分割，最后一个字符串可能不是完整的编码，这个时候我们直接
	 * 			跳过不处理，让下次调用此方法时，拼接上后面的编码，就可以处理了。
	 * 
	 * 	为了提高效率，我们在开始遍历之前，需要先确定哈夫曼编码的最大长度和最短长度。
	 * 	如果哈夫曼编码最短长度为3 ，那么我们遍历 sb 的时候，直接从第三位开始遍历，不需要从第一位去尝试。
	 * 	同样的道理，当我们尝试到最大长度的时候，我们就不用再去尝试了。如果超过了最大长度，那么我们可以直接抛异常。
	 * 
	 * 	sourceBytes 的长度问题：
	 * 	1、 如果我们是解压字符串，一般就一次性解压，sourceBytes 的长度就使用  metaData 里面的 length 就可以了
	 * 	2、 如果是文件的话，一般是分多次解压，每次拿到的 sb 可能会长度不一样，所以理论上讲 souceBytes 的长度也应该不一样
	 * 		我们应该每次开始 decode 的时候预估一下 sourceBytes 是否够长，如果不够的话，就自动扩容。
	 * 		但是这个扩容的工作，必须是在 decode() 方法外部去处理，不能在内部，不然会出现严重的问题。 因为我们是把 sourceBytes
	 * 		作为一个参数传入 decode() 方法里，如果我们把 sourceBytes 指向另一个数组，外部的 souceBytes并不会相应变化。
	 * 		也就是说， decode 方法里面解码的所有数据全部跟外部没有对接上。
	 * 
	 * 	3、 如果我们想要多次使用  sourceBytes 的话，那么必须再注意另一个问题： 即可能出现无用的数据，也就是 sourceBytes 长度
	 * 		太长，本次转码没有全部用上。
	 * 		===> 这个问题，我们最后决定使用返回值 len 来处理。
	 * @param decodeMap		传入解码表
	 * @param minLen		传入哈夫曼编码的最短长度
	 * @param maxLen		传入哈夫曼编码的最大长度
	 * @param sb			传入二进制字符串
	 * @param sourceBytes	传入一个容器，保存转码以后的字节数据
	 * @return				返回值表示这次转码后， sourceBytes 里面的有效数据长度
	 */
	public int decode(HashMap<String, Byte> decodeMap, int minLen, int maxLen, StringBuilder sb, byte[] sourceBytes) {
		int sbIndex = 0;
		Byte b = null;
		int sourceIndex = 0;
		while(sbIndex < sb.length() - 1) {
			for (int i = minLen; i <= maxLen; i++) {
				if(i + sbIndex <= sb.length()) {
					b = decodeMap.get(sb.substring(sbIndex, sbIndex + i));
				}else {
					// 如果 sbIndex + i 大于 sb.length() , 说明最后的这个二进制字符串不完整，我们直接删除
					// 已经处理的二进制字符串，然后直接返回
					// 未删除的二进制字符串会在跟下次转码的二进制字符串进行拼接
					sb.delete(0, sbIndex);
					return sourceIndex;
				}
				if(b != null) {
					if(sourceIndex == sourceBytes.length) {
						System.out.println("hehe");
					}
					// 保存 b 的值，并移动 sourceIndex 指针 
					sourceBytes[sourceIndex++] = b;
					// 移动 sbIndex 指针
					sbIndex += i;
					break;
				}
			}
		}
		// 返回前要注意先删除已经处理过的二进制字符串
		sb.delete(0, sbIndex);
		// 返回的 sourceIndex 就是本次转码产生的有效字节数据长度
		return sourceIndex;
	}
	
	/**
	 * 	这个方法可以多次调用，然后可以多次传入并共享使用  sb 和 compressedBytes 
	 * 	这个方法的主要作用是根据，压缩后的字节数组，生成对应的二进制字符串
	 * @param metaData			源信息
	 * @param compressedBytes	指被压缩以后的字节数组
	 * @param len			解压文件的时候，我们一般都是分多次取解压， compressedBytes 里面不一定全部是有效数据
	 * @param isLast		指定是否是最后一个 compressedBytes 数组，如果是的话，最后一个字节我们需要根据 MetaData 处理
	 * @param sb
	 * @return
	 */
	public StringBuilder getBinaryStringByCompressedBytes(MetaData metaData, byte[] compressedBytes, int len, boolean isLast, StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder();
		}
		String tempStr = null;
		// 这个for 循环只处理  [0, len - 2] 区间的元素
		for (int i = 0; i < len - 1; i++) {
			tempStr = Integer.toBinaryString(compressedBytes[i]);
			// 只要不是最后一个字节，我们都是一个字节对应 8 位二进制字符串
			// 不管转成二进制字符串后，是否满 8 位，都被满 8 位
			// 因为我们使用 Integer 的api 去转二进制字符串，所以负数最终可能显示 32 位，我们只需要截取前 8 位即可。
			if(tempStr.length() > 8) {
				// 如果超过 8 位的，肯定负数，长度肯定是 32 位， 我们截取后8位，就是从 24 位开始取
				sb.append(tempStr.substring(24));
			}else {
				// 如果不足 8 位，则补足 8 位
				sb.append(String.format("%8s", tempStr).replaceAll(" ", "0"));
			}
		}
		
		// 如果  compressedBytes 是最后一个字节数组，那么我们需要对  len - 1 索引处的字节处理需要根据 MetaData
		// 如果  compressedBytes 不是最后一个字节数组，那么对于 len - 1 索引处的字节就正常处理，只取8位，不足8位，补0
		tempStr = Integer.toBinaryString(compressedBytes[len - 1]);
		if(isLast) {
			sb.append(String.format("%"+ metaData.lastByteLen +"s", tempStr).replaceAll(" ", "0"));
		}else {
			// 只要不是最后一个字节，我们都是一个字节对应 8 位二进制字符串
			// 不管转成二进制字符串后，是否满 8 位，都被满 8 位
			// 因为我们使用 Integer 的api 去转二进制字符串，所以负数最终可能显示 32 位，我们只需要截取前 8 位即可。
			if(tempStr.length() > 8) {
				// 如果超过 8 位的，肯定负数，长度肯定是 32 位， 我们截取后8位，就是从 24 位开始取
				sb.append(tempStr.substring(24));
			}else {
				// 如果不足 8 位，则补足 8 位
				sb.append(String.format("%8s", tempStr).replaceAll(" ", "0"));
			}
		}
		return sb;
	}
	
	/**
	 * 	根据压缩以后的字节数组，直接解压成字符串（比较简单）
	 * 	【说明】 一般字符串压缩，都是一次到位。不像压缩文件那样，需要多次调用 compress() 方法。
	 * 			解压的时候，我们直接根据 metaData.length 创建一个等长的 字节数组，然后把转码后的字节数据保存进去
	 * 			最后，我们直接把字节数组按照指定字符串编码格式转成字符串
	 * @param compressBytes
	 * @return
	 * @throws Exception
	 */
	public String uncompressString(byte[] compressBytes) throws Exception {
		if(compressBytes == null || compressBytes.length == 0) {
			return null;
		}
		
		// 如果前面都没有什么问题的话，那么我们就要创建字节数组流和对象流来读取对应的内容了
		// 尝试使用 字节数组流 和 对象流拿到原始的信息
		ByteArrayInputStream bis = new ByteArrayInputStream(compressBytes);
		ObjectInputStream ois = new ObjectInputStream(bis);
		// 先读取元信息
		MetaData metaData = (MetaData)ois.readObject();
		if(metaData == null) {
			throw new RuntimeException("压缩信息为空");
		}
		
		// 再读取解码表
		HashMap<String, Byte> decodeMap = (HashMap<String, Byte>)ois.readObject();
		if(decodeMap == null || decodeMap.isEmpty()) {
			throw new RuntimeException("解码表为空");
		}
		
		// 最后再获取压缩后的字节数组
		// 我们先看一下剩下多少个字节，然后创建对应长度的字节数组
		byte[] compressedBytes = new byte[bis.available()];
		bis.read(compressedBytes);
		
		// 关闭流对象
		ois.close();
		bis.close();
		
		// 现在我们已经拿到了压缩后的字节数组了，我们就可以遍历 compressedBytes
		// 配合 解码表，生成二进制字符串
		// 不管什么情况，我们对  [ 0, compressedBytes.length - 2] 这些字节数据的处理都是一致的
		// 不管转成二进制字符串后，是否满 8 位，都被满 8 位
		// 因为我们使用 Integer 的api 去转二进制字符串，所以负数最终可能显示 32 位，我们只需要截取前 8 位即可。
		StringBuilder sb = new StringBuilder();
		getBinaryStringByCompressedBytes(metaData, compressedBytes, compressedBytes.length, true, sb);
		
		// 现在，sb 里面就保存着全部压缩以后的二进制字符串，我们需要根据解码表再把二进制字符串，转成原先的字节数组
		// 因为解码表的编码是不定长的，所以我们遍历 sb 的时候，不能像以前那样，每 8 位遍历一次
		// 为了不一位一位地遍历，我们最好先遍历一下解码表，看一下最长的编码长度和最短的编码长度
		int minLen = Integer.MAX_VALUE;
		int maxLen = 0;
		for (String key : decodeMap.keySet()) {
			if(key.length() < minLen) {
				minLen = key.length();
			}
			if(key.length() > maxLen){
				maxLen = key.length();
			}
		}
		
		// 开始遍历 sb 之前，我们还得事先创建一个字节数组来存放解析出来的 byte 值
		// 这个字节数组的长度就是  metaData 里面的 length 长度
		// 那个length 值考虑到可能是压缩文件，所以设置成 long 类型，而字符串，我们都是一次到位，所以我们直接转成 int 就好了
		byte[] sourceBytes = new byte[(int)metaData.length];
		
		int len = decode(decodeMap, minLen, maxLen, sb, sourceBytes);
		return new String(sourceBytes, 0, len, metaData.encoding);
	}
	/**
	 * 	根据 哈夫曼编码表， 拿到指定的解码表
	 * 	因为编码是字符串，虽然也是二进制字符串，但是我们无法再把这个字符串转成 byte 或者 int ，然后作为数组的索引
	 * 	因为  "011" 跟  "0011" 是两个不同的编码，但是转成数字以后，索引值就相同了。
	 * @param huffmanCodes
	 * @return
	 */
	public HashMap<String, Byte> getDecodeMapByHuffmanCodes(String[] huffmanCodes){
		HashMap<String, Byte> decodeMap = new HashMap<>();
		for (int i = 0; i < huffmanCodes.length; i++) {
			if(huffmanCodes[i] != null) {
				if(i > 127) {
					decodeMap.put(huffmanCodes[i], (byte)(i - 256));
				}else {
					decodeMap.put(huffmanCodes[i], (byte)i);
				}
			}
		}
		return decodeMap;
	}
	
	/**
	 * 	最后返回的字节数组里面包含着 元信息、 哈夫曼编码表 、 压缩以后的字节信息
	 * 
	 * @param sourceStr 需要压缩的字符串
	 * @param encoding  指定字符串的编码
	 * @return
	 * @throws IOException
	 */
	public byte[] compressString(String sourceStr, String encoding) throws IOException {
		if (sourceStr == null) {
			throw new RuntimeException("空字符串，无需压缩");
		}
		if (sourceStr.length() == 0) {
			return new byte[0];
		}
		if (encoding == null) {
			encoding = "utf-8";
		}

		// 如果前面的校验都没有什么问题，我们这里就根据源文件生成频率统计表
		byte[] sourceBytes = sourceStr.getBytes(encoding);
		int[] frequencyTab = getFrequencyTabBySourceBytes(sourceBytes, null);

		// 再然后，我们根据频率统计表生成哈夫曼树
		Node huffmanTree = buildHuffmanTree(frequencyTab);

		// 再然后，我们根据哈夫曼树，生成哈夫曼编码表
		String[] huffmanCodes = getHuffmanCodesByHuffmanTree(huffmanTree, "", null);

		// 再然后，我们就可以开始压缩了
		StringBuilder sb = new StringBuilder();
		// 创建一个多次共享的 compressedBytes 
		// 注意，为了多次使用 compressedBytes ，我们需要每次确保 compressedBytes 长度够用
		// 我想先找到最长的哈夫曼编码，然后假定源字节数组里面的每个字节都对应最长的哈夫曼编码
		// 于是，compressedBytes 最大长度应该是     maxCodeLen * sourceBytes.length / 8 + 1
		// 又因为 sb 里面可能会有上次未处理完的二进制字符串，但是最大长度不会超过 8
		// 所以最终  compressedBytes 最大长度应该是     maxCodeLen * sourceBytes.length / 8 + 2
		int maxCodeLen = 0;
		for (String code : huffmanCodes) {
			if(code != null && code.length() > maxCodeLen) {
				maxCodeLen = code.length();
			}
		}
		byte[] compressedBytes = new byte[maxCodeLen * sourceBytes.length / 8 + 2];
		int len = compress(huffmanCodes, sourceBytes, sourceBytes.length, sb, compressedBytes);
		// 虽然最后要返回的是一个字节数组，但是我们还需要添加元信息对象，所以我们最好使用 对象流去处理
		// 对象流需要一个基础的流对象，因为我们这里没有文件，只有字节数组，所以我们使用字节数组流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		// 指定压缩后的元信息
		MetaData metaData = new MetaData();
		metaData.length = sourceBytes.length;
		metaData.encoding = encoding;
		byte lastByte = 0;
		// 然后，我们要看一下 sb 是否是空的，如果不为空，那么我们需要处理最后一个字节对应的字符串
		if (sb.length() > 0) {
			metaData.lastByteLen = (byte) sb.length();
			// 我们还要把最后一个字节的字符串转成一个 byte 值
			lastByte = (byte) Integer.parseInt(sb.toString(), 2);
		}

		// 首先，我们需要通过 oos 添加 元信息对象到流
		oos.writeObject(metaData);
		// 然后，因为我们的编码解码规则是动态生成的，所以我们还得再把解码表也保存到流中
		// 保存解码表比保存编码表要好，因为编码表我们为了提高效率使用的是数组，里面有很多无效元素
		// 而解码表，我们使用的是 map 集合，里面没有无效元素
		oos.writeObject(getDecodeMapByHuffmanCodes(huffmanCodes));

		// 然后，我们要把前面的压缩字节流添加到基础的 字节数组流中
		bos.write(compressedBytes, 0, len);
		// 最后，我们还需要再看一下最后一个字节是否需要处理，如果 sb.length() > 0 说明需要处理, 前面我们已经处理过了
		// 直接把lastByte 保存到流中就可以了
		if (sb.length() > 0) {
			bos.write(lastByte);
		}

		// 因为我们最后要返回的是一个字节数组，所以我们还得再麻烦一下，把 字节数组流 转成 字节数组
		compressedBytes = bos.toByteArray();

		// 最后关闭流
		if (oos != null) {
			oos.close();
		}

		if (bos != null) {
			bos.close();
		}

		return compressedBytes;
	}

	/**
	 *	 根据哈夫曼编码表，把源字节数组转成压缩后的字节数组
	 * 	主要的流程其实就是遍历源字节数组，根据每个字节数据找到对应的编码，然后先拼接成字符串，最终拿到一个转码以后的二进制字符串 我们再重新解析二进制字符串，按照
	 * 	8 位一个字节去重新解析这些二进制字符串，转成一个新的字节数组 【注意】因为哈夫曼编码表里面的编号是不定长的 ，所以最终拿到的二进制字符串 一般不会刚好是
	 * 	8 的倍数，所以我们在解析的时候 在处理最后那些不足 8 位的 二进制字符串时，得注意长度
	 * 
	 * 	【注意】因为 Byte 包装类虽然提供了 Byte.parseByte(String s, Radix r)
	 * 	方法来解析二进制字符串，但是你如果去看内部的实现 其实是调用了 Integer.parseInt(String s, Radix r) 方法去解析。 因为
	 * int 类型占用 4 个字节，而 byte 类型占用 1 个字节，所以其实本来应该解析成负数的 二进制字符串，实际解析出来会 变成正数。 比如说：
	 * "1000 0000" 这个二进制字符串，如果解析成 byte 的话，应该是 -128 , 但是因为我们使用的是 Integer 的 api
	 *	 去解析，所以最终会在原有的字符串前面补 0， 补足 32 位。 最终被解析成正数 128。 而 128 是不在 byte 范围内的，所以
	 * Byte.parseByte(String s, Radix r) 最终会报错。
	 * 
	 * ===> 解决思路： 我们自己去调用 Integer.parseInt() 解析 最终如果大于 127 的话 ，我们就让这个值减去模长， 128 -
	 * 256 = -128 如果小于 127 , 那么我们就直接使用这个值即可
	 * 
	 * 	【说明】因为本身我们是从 byte 数组里面解析出来的，所以使用 Integer.parseInt() 解析出来的值只会在 [0, 255]
	 * 
	 * 	【说明】 如果我们是对一个比较大的文件进行流读取的话，每次只是使用一个定长的字节数组进读取字节数据，而这个字节数组里面的数据
	 *	 并不一定全部有效，可能字节数组的长度是 1024 ， 但是本次实际读取的长度只有 100， 所以我们需要指定一个 len ，说明 这个
	 * sourceBytes 里面的哪些数据是有效数据，哪些不需要进行转码
	 * 
	 * 	【说明】 因为我们每调用这个方法 ，生成的二进制字符串最后都可能有不足 8 位的二进制字符串被当成 8 位进行解析， 如果我们的 sourceBytes
	 *	 是全部需要源数据，那么这种处理只会有 一次。 但是如果我们是对一个大文件进行压缩的话，那么这种不足8 位当成 8 位解析的操作可能就会比较多,
	 * 	最终多少会影响压缩率。
	 * 
	 * 	【处理思路】
	 * 	 	1、 sourceBytes 长度直接等于 文件的长度，也就是一次性处理，上面的操作最多一次。===>但是占用的内存会很大。 
	 * 		2、这个方法不把二进制字符串转成字节数组，而是直接返回一个sb 对象。 多次执行以后，我们把所有的 sb 都拼接在一起， 
	 * 			最终再一起把 sb 转成byte[] 数组，或者转成流写进某个文件。 ==> sb是把一个比特转成一个字节（8比特），
	 * 			所以在完成压缩之前，你可能需要占用源文件大小的几倍内存。
	 * 		【3】、 我们每次对一个源字节数组进行压缩时，都不把最后不足8位的二进制字符串进行解析，就留在 sb 对象中。 而那些已经解析的二进制字符串，需要从 sb
	 * 			对象中删除。 下一次再调用 源字节数组时，我们还是传入这个 sb 对象，后面解析源字符数组得到的二进制字符串，直接拼接在sb 后面。 ==>
	 * 			使用这种方法来处理的话， sb 对象必须在外部创建，并保存。因为最后我们需要额外处理不足8位的二进制字符串。
	 * 
	 * @param huffmanCodes
	 * @param sourceBytes
	 * @param sourceLen          这个 sourceLen 参数主要是在压缩文件时使用，因为用流读取文件，会多次调用此方法，sourceBytes 不一定全是有效的数据
	 * @param sb				跟前面的 sourceBytes 一样，如果是压缩文件的时候，我们会共享使用 sb
	 * 							实际上，因为哈夫曼编码是非定长的，所以每次 sb 都可能没处理完，这个方法并不处理不足8位的二进制字符串
	 * @param compressedBytes	跟前面的 sourceBytes 一样，我们不希望频繁创建 compressedBytes 数组，所以添加此参数
	 * 							因为哈夫曼编码是非定长的，所以每次压缩产生的实际压缩数据长度是不固定的，我们在每次传入参数前需要确认
	 * 							compressedBytes 长度是否够用。
	 * 							另外，因为多次使用， compressedBytes 数组可能过长，所以我们每次都返回一个 int值，表示 compressedBytes 有多少有效数据
	 *
	 * @return				返回值表示  compressedBytes 有多少有效数据
	 */
	public int compress(String[] huffmanCodes, byte[] sourceBytes, int sourceLen, StringBuilder sb, byte[] compressedBytes) {
		// 把源字节数组使用哈夫曼编码表重新编码以后，我们暂时把二进制的字符串编码保存在 sb 中
		// 这里我们需要注意一下源字节数组里面的值可能是负数
		// 如果是负数，我们需要加上模长 256，转成正数
		int tempIndex = 0;
		for (int i = 0; i < sourceLen; i++) {
			tempIndex = sourceBytes[i] < 0 ? (256 + sourceBytes[i]) : sourceBytes[i];
			sb.append(huffmanCodes[tempIndex]);
		}

		int index = 0;
		int tempValue = 0;
		for (int i = 0; i < sb.length(); i += 8) {
			// 因为 substring(start, end) ，包左不包右，所以 i + 8 = sb.length() 是可以的，不会越界
			// 但是我们最好是不要加等于号
			// 因为如果加了等于号，这里一并处理了，后面再想根据 sb.length() 获取最后一个字节的长度就容易出错
			if (i + 8 < sb.length()) {
				tempValue = Integer.parseInt(sb.substring(i, i + 8), 2);
				// 因为我们是按 int 去解析的，所以解析出来的值不可能有负数，范围只能是 [0, 255]
				// 而 byte 的范围是 [-128, 127] ，所以如果解析出来的数值大于 127 的话，我们需要减去模长，变为负数
				compressedBytes[index++] = tempValue > 127 ? (byte) (tempValue - 256) : (byte) tempValue;
			}
		}

		// 最后我们需要删除已经处理完的二进制字符串
		// 如果sb 本身的长度不足8位，那么我们这个方法直接不处理，留到外部去处理就可以了
		if (index > 0) {
			sb.delete(0, index * 8);
		}
		return index;
	}

	/**
	 * 	根据哈夫曼树递归生成哈夫曼编码表 
	 * 	【说明】因为我们是根据字节来统计频率、生成哈夫曼树的，哈夫曼树有效的叶子结点最多也就 256 个 所以我们创建一个长度为
	 * 			256 的String[] 数组应该就很够用了 索引值就是叶子结点的 data， 具体值就是路径编码
	 * 
	 * 	【说明】所谓的路径编码就是从根结点到目标叶子结点走过的路径，如果是向左走，就是0，向右走就是1 使用这种路径编码可以有效避免 较短编码 成为
	 * 	其他编码的前缀问题，这种编码也被称为 前缀编码 至于，为什么你用个哈夫曼树能生成 前缀编码，我们就不去考虑了
	 * 
	 * @param huffmanTree	因为需要递归生成，所以我们需要传入一个数组，让多个方法共享
	 * @return
	 */
	public String[] getHuffmanCodesByHuffmanTree(Node node, String currCode, String[] huffmanCodes) {
		if (node != null) {
			if (huffmanCodes == null) {
				huffmanCodes = new String[256];
			}
			// 不管你是什么结点，我们都对结点的路径编码进行拼接
			currCode += node.pathCode;

			if (node.left != null) {
				getHuffmanCodesByHuffmanTree(node.left, currCode, huffmanCodes);
			}
			if (node.right != null) {
				getHuffmanCodesByHuffmanTree(node.right, currCode, huffmanCodes);
			}

			// 左右子结点都是空，说明这个结点是叶子结点，我们不需要再递归了，直接读取 data ，哈夫曼编码表数组的索引
			// 然后保存 currCode 就可以了
			if (node.right == null && node.left == null) {
				huffmanCodes[node.data] = currCode;
			}
		}

		return huffmanCodes;
	}

	/**
	 * 根据统计的频率数组创建 哈夫曼树
	 * 
	 * @param fArr
	 * @return
	 */
	public Node buildHuffmanTree(int[] frequencyTab) {
		// list 集合保存的是所有的 node 对象
		// 因为我们需要对 node 对象进行排序，所以我们使用自己实现的链表来存储
		SinglyLinkedList<Node> nodeList = new SinglyLinkedList<>();
		if (frequencyTab != null && frequencyTab.length > 0) {
			Node newNode = null;
			// 首先，遍历统计的频率数组，先创建对应多个结点对象，然后全部放进 nodeSet 里面
			for (int i = 0; i < frequencyTab.length; i++) {
				// 如果某个字节值出现的频率为 0 ，那么我们直接就不创建节点了
				// 减少结点数量可以降低整体哈夫曼树的高度和整体编码的长度
				// 某个字节值的出现频率为 0 ，反过来说，我们也不可能使用到这个字节值的编码
				if (frequencyTab[i] > 0) {
					newNode = new Node(i, frequencyTab[i]);
					nodeList.addByOrder(newNode);
				}
			}

			// 如果遍历统计表后，只生成一个结点，我们需要单独处理一下
			// 虽然这种情况在现实的情况下出现的概率并不是很高
			// 比如说，一个源字符串全部是 "iiiiiiiiiiiiiiiii" 这种情况。
			// 如果我们不处理的话，这个字节对应的结点的 PathCode 就是 "" ，而最终的huffmanCode 也会变成 ""
			// 再怎么编码也不可能用 空字符串去编码吧
			if (nodeList.size() == 1) {
				nodeList.getFirst().setPathCode("0");
				return nodeList.getFirst();
			}

			// 然后，我们需要根据 nodeList 来构建哈夫曼树
			// 建树的过程其实就是不断地从 nodeList 中取两个节点（removeFirst），然后为其构建一个父结点
			// 再把父结点放进 nodeList 中(还是需要按大小顺序排好)
			Node leftNode = null;
			Node rightNode = null;
			Node parentNode = null;
			while (nodeList.size() > 1) {
				leftNode = nodeList.removeFirst();
				leftNode.setPathCode("0");
				rightNode = nodeList.removeFirst();
				rightNode.setPathCode("1");
				parentNode = new Node(null, leftNode.weight + rightNode.weight, leftNode, rightNode);
				nodeList.addByOrder(parentNode);
			}
		}

		// 根结点的路径参数设置成 ""
		nodeList.getFirst().setPathCode("");

		return nodeList.removeFirst();
	}

	/**
	 * 根据源文件或者源字符串的字节数组，统计各个字节出现的频率表 因为我们是按字节去统计的，字节的范围是 [-128, 127]， 总共有 256
	 * 种可能性，所以我们直接创建一个 256 长度的数组即可 【注意】因为我们是创建 int[] 数组，次数最大值为 Integer.MAX_VALUE =
	 * ‭2147483647‬ ， 某个字节出现的次数超过这个值 那么源文件至少要达到 ‭2147483647‬ 个字节（2G）。
	 * 如果数据量确实很大的话，那么可以使用 long[] 数组来统计。
	 *
	 * 【注意】我们这里是直接使用一个字节数组来进行统计的，而现实的情况下，我们可能需要使用流读取文件，分成多个字节数组去读取 也就是说，我们可能无法调用一次
	 * getFrequencyTabBySourceBytes 方法就完成统计，所以我们把 frequencyTab
	 * 做成一个参数，我们多次调用的时候，可以传入同一个统计数组，这样就可以在多个方法间共享此数组
	 * 
	 * @param sourceBytes
	 * @return
	 */
	public int[] getFrequencyTabBySourceBytes(byte[] sourceBytes, int[] frequencyTab) {
		if (frequencyTab == null) {
			frequencyTab = new int[256];
		}
		for (int i = 0; i < sourceBytes.length; i++) {
			frequencyTab[Byte.toUnsignedInt(sourceBytes[i])]++;
		}
		return frequencyTab;
	}

	/**
	 * 按理说，这个结点对象应该对外部隐藏的
	 * 
	 * @author Administrator
	 *
	 */
	public class Node implements Comparable<Node> {
		Integer data;
		Integer weight;
		String pathCode;
		Node left;
		Node right;

		public Node(Integer data, Integer weight, Node left, Node right) {
			super();
			this.data = data;
			this.weight = weight;
			this.left = left;
			this.right = right;
		}

		public Node(Integer data, int weight) {
			super();
			this.data = data;
			this.weight = weight;
		}

		public String getPathCode() {
			return pathCode;
		}

		public void setPathCode(String pathCode) {
			this.pathCode = pathCode;
		}

		@Override
		public int compareTo(Node other) {
			return this.weight - other.weight;
		}

		@Override
		public String toString() {
			return "Node [data=" + data + ", weight=" + weight + "]";
		}
	}

	/**
	 * 压缩文件或者字符串的时候，添加的头文件信息，按理说应该对外部隐藏 主要保存一起压缩文件的基本信息，还有最后一个字节的二进制字符串长度
	 * 【说明】一般压缩文件的时候需要用到 lastByteLen / fileName / length 。我们都是操作字节数据，所以不用担心编码。
	 * 压缩字符串的时候，我们需要用到 lastByteLen / encoding / length
	 * 
	 * 【说明】因为我们需要把元信息对象添加到对象流里，所以我们这里需要实现序列化接口
	 * 
	 * @author Administrator
	 *
	 */
	public class MetaData implements Serializable {
		private static final long serialVersionUID = 4120829198331535234L;
		private byte lastByteLen;
		private String fileName;
		private String encoding;
		// 这个长度是指字节长度
		private long length;

		public byte getLastByteLen() {
			return lastByteLen;
		}

		public void setLastByteLen(byte lastByteLen) {
			this.lastByteLen = lastByteLen;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getEncoding() {
			return encoding;
		}

		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}

		public long getLength() {
			return length;
		}

		public void setLength(long length) {
			this.length = length;
		}

		@Override
		public String toString() {
			return "MetaData [lastByteLen=" + lastByteLen + ", fileName=" + fileName + ", encoding=" + encoding
					+ ", length=" + length + "]";
		}
	}
}
