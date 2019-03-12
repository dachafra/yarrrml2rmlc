package es.upm.dia.oeg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.*;

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
                rmlcContent.append("<"+entry.getKey()+">\n");
                LinkedHashMap<String,Object> objects = (LinkedHashMap<String,Object>) entry.getValue();
                translateSource((ArrayList)objects.get("source"),rmlcContent);
                translateSubject(objects,rmlcContent);
                translatePredicateObjectMaps(objects,rmlcContent);
            }

        }
    }


    private static void translateSource(ArrayList content, StringBuilder rmlcContent){
        rmlcContent.append("\trml:logicalSource [\n");
        for(int i = 0; i< content.size(); i++){
            rmlcContent.append("\t\trml:source \""+((String)((ArrayList)content.get(i)).get(0)).replaceAll("\\[","").replaceAll("]","").replaceAll("~csv","")+"\";\n");
        }

        rmlcContent.append("\t\trml:referenceFormulation ql:CSV \n\t];\n\n");

    }

    //only one subject is currently allowed
    private static void translateSubject(LinkedHashMap<String,Object> content, StringBuilder rmlcContent){
        Object subject = content.get("subjects");
        List<String> subArray;

        if(subject==null){
            subject = content.get("subject");
            if(subject==null){
                subject = content.get("s");
            }
        }

        try {
            subArray = (ArrayList<String>)subject;
        }catch (Exception e){
            subArray = Arrays.asList((String)subject);
        }

        for (int i=0; i<subArray.size();i++){
            String template = subArray.get(i).replaceAll("\\$\\(","{").replaceAll("\\)","}");
            rmlcContent.append("\trr:subjectMap [\n");
            rmlcContent.append("\t\ta rr:SubjectMap;\n");
            rmlcContent.append("\t\trr:template \""+template+"\";\n");
            rmlcContent.append("\t];\n");
        }
    }


    private static void translatePredicateObjectMaps(LinkedHashMap<String,Object>  content, StringBuilder rmlcContent){
        ArrayList predicateobjects = (ArrayList) content.get("predicateobjects");


        if(predicateobjects==null){
            predicateobjects = (ArrayList) content.get("po");
        }

        for(int i=0; i<predicateobjects.size();i++){
            rmlcContent.append("\trr:predicateObjectMap[\n");
            Object predicateObject = predicateobjects.get(i);
            try{
                ArrayList po = (ArrayList) predicateObject;
                if(po.get(0).equals("a")){
                    rmlcContent.append("\t\trr:predicate rdf:type;\n");
                }
                else {
                    rmlcContent.append("\t\trr:predicate " + po.get(0) + ";\n");
                }
                rmlcContent.append("\t\trr:objectMap [\n");
                try{
                    String object = (String) po.get(1);
                    translateObject(object,rmlcContent,0);
                }catch (Exception e){
                    //if object is an array
                    ArrayList object = (ArrayList) po.get(1);
                    for(int j=0; j<object.size();j++){
                        translateObject((String)object.get(j),rmlcContent,j);
                    }
                }



            }catch (Exception e){
                //if we have a join or function
                StringBuilder f = new StringBuilder();
                LinkedHashMap<String,Object> functions;
                LinkedHashMap<String,Object> pof = (LinkedHashMap<String,Object>) predicateObject;
                rmlcContent.append("\t\trr:predicate "+pof.get("p")+";\n");
                rmlcContent.append("\t\trr:objectMap [\n");
                try{
                    LinkedHashMap<Object,Object> object = (LinkedHashMap<Object, Object>) pof.get("o");
                    String tripleMapParent = (String) object.get("mapping");
                    rmlcContent.append("\t\t\trr:parentTriplesMap <"+tripleMapParent+">;\n\t\t\trmlc:joinCondition [\n");
                    functions = (LinkedHashMap<String,Object>) object.get("condition");
                    ArrayList parameters=(ArrayList) functions.get("parameters");
                    translateFunctions(parameters.get(0),f,false);
                    rmlcContent.append("\t\t\t\trmlc:child \""+f.toString()+"\";\n");
                    f= new StringBuilder();
                    translateFunctions(parameters.get(1),f,false);
                    rmlcContent.append("\t\t\t\trmlc:parent \""+f.toString()+"\";\n\t\t\t];\n");
                }catch (Exception e2) {
                    ArrayList object = (ArrayList) pof.get("o");
                    functions = (LinkedHashMap<String, Object>) object.get(0);
                    translateFunctions(functions, f, true);
                    rmlcContent.append("\t\t\trmlc:function \""+f.toString()+"\"\n");
                }

            }
            if(i==predicateobjects.size()-1)
                rmlcContent.append("\t\t];\n\t].\n");
            else
                rmlcContent.append("\t\t];\n\t];\n");
        }


    }

    private static void translateObject(String object, StringBuilder rmlcContent, Integer pos){
        if(object.matches(".+\\$\\(.*\\).+") || object.matches("\\$\\(.*\\).*\\$\\(.*\\)")){
            object=object.replaceAll("\\$\\(","{").replaceAll("\\)","}");
            rmlcContent.append("\t\t\trr:template \""+object+"\";\n");
        }
        else if(object.matches("\\$\\(.*\\)")){
            rmlcContent.append("\t\t\trml:reference \""+object.replaceAll("\\$\\(","").replaceAll("\\)","")+"\";\n");
        }

        else if(object.matches(".*~lang")){
            rmlcContent.append("\t\t\trr:language \""+object.replaceAll("~~lang","")+"\";\n");
        }
        else if(object.matches(".*~iri")){
            rmlcContent.append("\t\t\trr:termType rr:IRI");
        }
        else if((object.matches(".*:.*") || object.matches("http[s]?://.*")) && pos!=0){
            rmlcContent.append("\t\t\trr:datatype "+object+";\n");
        }
        else {
            if(object.equals("a")){
                object = "rdf:type";
            }
            rmlcContent.append("\t\t\trr:constant "+object+";\n");
        }

    }

    private static void translateFunctions(Object f1, StringBuilder functionBuilder, Boolean flag){
        ArrayList parameters;
        try{
            LinkedHashMap<String,Object> functions = (LinkedHashMap<String,Object>)f1;
            String function = (String) functions.get("function");
            if(!function.equals("equal")){
                String h2function = RMLCFunctions.translateFunction(function);
                functionBuilder.append(h2function+"(");
            }
            parameters = (ArrayList) functions.get("parameters");
        }catch (Exception e){
            parameters = new ArrayList();
            parameters.add(f1);
        }

        for(int i=0; i<parameters.size();i++){
            try{
                LinkedHashMap<String,Object> f = ((LinkedHashMap<String, Object>)((LinkedHashMap<String, Object>) parameters.get(i)).get("value"));
                translateFunctions(f,functionBuilder,false);
                if(i!=parameters.size()-1)
                    functionBuilder.append("),");
                else
                    functionBuilder.append(")");
            }catch (Exception e){
                String param =(String)((ArrayList)parameters.get(i)).get(1);
                if(i==parameters.size()-1)
                    translateParameter(param,functionBuilder);
                else {
                    translateParameter(param, functionBuilder);
                    functionBuilder.append(",");
                }

            }
        }
        if(flag){
            functionBuilder.append(")");
        }
    }

    public static void translateParameter(String param, StringBuilder functionBuilder){
        functionBuilder.append(param.replaceAll("\\$\\(","{").replaceAll("\\)","}"));
    }





}
