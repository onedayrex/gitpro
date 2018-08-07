package com.git.onedayrex.gitpro;

import com.git.onedayrex.gitpro.hash.GitProHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("create git");
        GitProHash gitProHash = new GitProHash();
        gitProHash.gitWriteObj("hello", "blob");
    }
}
