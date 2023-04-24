package com.moling.micabrowser.browser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Bookmark {
    private FileOutputStream fos;
    private FileInputStream fis;
    public Bookmark(FileOutputStream fos, FileInputStream fis) {
        this.fos = fos;
        this.fis = fis;
    }
}
