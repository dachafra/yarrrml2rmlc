package es.upm.dia.oeg;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            String content = FileUtils.readFileToString(new File("./mapping2.yml"), Charsets.toCharset("UTF-8"));
            Yarrrml2rmlc.translateYarrrml2RMLC(content);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
