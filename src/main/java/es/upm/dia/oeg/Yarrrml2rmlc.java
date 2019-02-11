package es.upm.dia.oeg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Yarrrml2rmlc {


    public static String translateYarrrml2RMLC(String yarrrmlContent){
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        StringBuilder rmlcContent= new StringBuilder();
        try {
            LinkedHashMap<String,Object> obj = (LinkedHashMap<String,Object>)yamlReader.readValue(yarrrmlContent, Object.class);
            translatePrefix((LinkedHashMap<String,String>)obj.get("prefixes"),rmlcContent);
            translateMappings((LinkedHashMap<String,Object>)obj.get("mappings"),rmlcContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rmlcContent.toString();
    }

    private static void translatePrefix (LinkedHashMap<String,String> content, StringBuilder rmlcContent){
        if(content!=null) {
            for (Map.Entry<String, String> entry : content.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                rmlcContent.append("@prefix " + key + ": <" + value + ">.\n");
            }
        }

    }

    private static void translateMappings(LinkedHashMap<String,Object> content, StringBuilder rmlcContent){

        if(content!=null) {
            for (Map.Entry<String, Object> entry : content.entrySet()) {
                rmlcContent.append("<"+entry.getKey()+"TriplesMap>\n");
                LinkedHashMap<String,LinkedHashMap<String,Object>> objects = (LinkedHashMap<String,LinkedHashMap<String,Object>>) entry.getValue();
                translateSource(objects.get("source"),rmlcContent);
                LinkedHashMap<String,Object> subject = objects.get("subject");
                if(subject==null){
                    subject = objects.get("s");
                }
                transalteSubject(subject,rmlcContent);
                LinkedHashMap<String,Object> predicateObjects = objects.get("predicateobjects");
                if(predicateObjects==null){
                    predicateObjects = objects.get("po");
                }
                translatePredicateObjectMaps(predicateObjects,rmlcContent);
            }
        }
    }


    private static void translateSource(LinkedHashMap<String,Object> content, StringBuilder rmlcContent){

    }

    private static void transalteSubject(LinkedHashMap<String,Object> content, StringBuilder rmlcContent){

    }

    private static void translatePredicateObjectMaps(LinkedHashMap<String,Object> content, StringBuilder rmlcContent){

    }



}
