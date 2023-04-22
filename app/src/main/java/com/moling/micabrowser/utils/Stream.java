package com.moling.micabrowser.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Stream {
    public static void write(String content, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {}
    }
    public static String Read(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int result=0;
            StringBuilder sb = new StringBuilder();
            while((result=reader.read())!=-1) {
                sb.append((char)result);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {}
        return "";
    }
}
