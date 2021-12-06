package com.changgou;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/12/01 11:37
 **/
public class FileTest {

    @Test
    public void read() throws IOException {
        FileReader fileReader = new FileReader("C:\\Users\\HASEE\\Desktop\\CF006.txt");
        int ch = 0;
        while ((ch = fileReader.read()) != -1) {
            System.out.print(((char)ch));
        }
    }

    @Test
    public void read1() {
        
    }

}
