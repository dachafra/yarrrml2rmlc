package es.upm.dia.oeg;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            String content = FileUtils.readFileToString(new File("./mapping.yml"), Charsets.toCharset("UTF-8"));
            String rmlcMapping=Yarrrml2rmlc.translateYarrrml2RMLC(content);
            System.out.println(rmlcMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
