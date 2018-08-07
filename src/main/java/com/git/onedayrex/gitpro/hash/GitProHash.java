package com.git.onedayrex.gitpro.hash;


import com.git.onedayrex.gitpro.utils.ZlibUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GitProHash {
    private static final Logger logger = LoggerFactory.getLogger(GitProHash.class);

    /**
     * 生成规则
     * 1、生成header = type+content.length+\0
     * 2、够造git对象 store = header + content
     * @param content
     * @param type
     * @return
     */
    public String gitProHash(String content,String type) {
        byte[] bytes = this.gitStore(content, type);
        String sha1Hex = DigestUtils.sha1Hex(bytes);
        logger.info("create hash for object ==>[{}]", sha1Hex);
        return sha1Hex;
    }

    /**
     * 压缩文件对象
     * @param store
     * @return
     */
    public byte[] gitZlibFile(byte[] store) {
        return ZlibUtils.compress(store);
    }

    /**
     * 生成文件对象
     * @param content
     * @param type
     * @return
     */
    public byte[] gitStore(String content,String type) {
        String header = type + content.getBytes().length + "\0";
        byte[] bytes = this.arrayMerge(header.getBytes(), content.getBytes());
        return bytes;
    }

    public boolean gitWriteObj(String content,String type) {
        String hash = this.gitProHash(content, type);
        byte[] store = this.gitStore(content, type);
        byte[] bytes = this.gitZlibFile(store);
        File file = new File(".git/objects/" + hash.substring(0, 2));
        if (!file.exists()) {
            file.mkdir();
        }
        logger.info("create file path {}", file.getAbsolutePath());
        File gitFile = new File(".git/objects/" + hash.substring(0, 2) + "/" + hash.substring(2, 40));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(gitFile);
            fileOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private byte[] arrayMerge(byte[] bytes1,byte[] bytes2) {
        byte[] bytes3 = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, bytes3, 0, bytes1.length);
        System.arraycopy(bytes2, 0, bytes3, bytes1.length, bytes2.length);
        return bytes3;
    }

}
