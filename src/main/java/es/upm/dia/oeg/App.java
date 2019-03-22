package es.upm.dia.oeg;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            String content = FileUtils.readFileToString(new File("./gtfs.yml"), Charsets.toCharset("UTF-8"));
            String rmlcMapping=Yarrrml2rmlc.translateYarrrml2RMLC(content);
            PrintWriter pw = new PrintWriter("./gts.rmlc.ttl","UTF-8");
            pw.println(rmlcMapping);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
