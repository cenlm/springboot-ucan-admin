package com.ucan.shiro.util;

import org.apache.shiro.codec.CodecException;
import org.apache.shiro.crypto.UnknownAlgorithmException;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 加密工具（MD5和SHA-256）
 * 
 * @author liming.cen
 * @date 2022年12月28日 上午10:27:19
 */
public class EncryptionUtil extends SimpleHash {

    private static final long serialVersionUID = -8225617813817995671L;
    private static final String ALGORITHM_NAME_MD5 = "MD5";
    private static final String ALGORITHM_NAME_SHA_256 = "SHA-256";
    private static final int ITERATIONS = 3;// 加密算法迭代次数

    public EncryptionUtil(String algorithmName, Object originalSource)
	    throws CodecException, UnknownAlgorithmException {
	super(algorithmName, originalSource, null, ITERATIONS);
    }

    public EncryptionUtil(String algorithmName, Object originalSource, Object saltSource)
	    throws CodecException, UnknownAlgorithmException {
	super(algorithmName, originalSource, saltSource, ITERATIONS);
    }

    /**
     * MD5加密（不加盐，生成长度为32的字符串）
     * 
     * @param source
     * @return
     */
    public static String md5Encode(String source) {
	EncryptionUtil util = new EncryptionUtil(ALGORITHM_NAME_MD5, source);
	return util.toHex();
    }

    /**
     * SHA-256加密（不加盐，生成长度为64的字符串）
     * 
     * @param source
     * @return
     */
    public static String sha256Encode(String source) {
	EncryptionUtil util = new EncryptionUtil(ALGORITHM_NAME_SHA_256, source);
	return util.toHex();
    }

    /**
     * MD5加密（加盐，生成长度为32的字符串）
     * 
     * @param source
     * @return
     */
    public static String md5Encode(String source, String salt) {
	EncryptionUtil util = new EncryptionUtil(ALGORITHM_NAME_MD5, source, salt);
	return util.toHex();
    }

    /**
     * SHA-256加密（加盐，生成长度为64的字符串）
     * 
     * @param source
     * @return
     */
    public static String sha256Encode(String source, String salt) {
	EncryptionUtil util = new EncryptionUtil(ALGORITHM_NAME_SHA_256, source, salt);
	return util.toHex();
    }

    public static void main(String[] args) {
	String md5Str = EncryptionUtil.md5Encode("123456");
	String sha256Str = EncryptionUtil.sha256Encode("123456");
	System.out.println(md5Str + " 长度：" + md5Str.length());
	System.out.println(sha256Str + " 长度：" + sha256Str.length());
	System.out.println(EncryptionUtil.md5Encode("123456", "cenliming"));
	System.out.println(EncryptionUtil.sha256Encode("123456", "cenliming"));
    }
}
