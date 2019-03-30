package es.upm.dia.oeg;

public class RMLCFunctions {

    public static String translateFunction(String fnofun){
        String rmlcFun="";
        switch (fnofun){
            case "sql:lower":
                rmlcFun = "LOWER";
                break;
            case "sql:upper":
                rmlcFun = "UPPER";
                break;
            case "sql:concat":
                rmlcFun = "CONCAT";
                break;
            case "sql:ltrim":
                rmlcFun = "LTRIM";
                break;
            case "sql:rtrim":
                rmlcFun = "RTRIM";
                break;
            case "sql:replace":
                rmlcFun = "REPLACE";
                break;
            case "sql:left":
                rmlcFun = "LEFT";
                break;
            case "sql:right":
                rmlcFun = "RIGHT";
                break;
            case "sql:substring":
                rmlcFun = "SUBSTRING";
                break;
            case "sql:trim":
                rmlcFun = "TRIM";
                break;
            case "sql:soundex":
                rmlcFun = "SOUNDEX";
                break;
            case "sql:locate":
                rmlcFun = "LOCATE";
                break;
            case "sql:length":
                rmlcFun = "LENGTH";
                break;
            case "sql:sum":
                rmlcFun = "SUM";
                break;
            case "sql:day":
                rmlcFun = "DAY";
                break;
            case "sql:dayName":
                rmlcFun = "DAYNAME";
                break;
            case "sql:dayOfWeek":
                rmlcFun = "DAYOFWEEK";
                break;
            case "sql:dayOfYear":
                rmlcFun = "DAYOFYEAR";
                break;
            case "sql:month":
                rmlcFun = "MONTH";
                break;
            case "sql:monthName":
                rmlcFun = "MONTHNAME";
                break;
            case "sql:weekOfYear":
                rmlcFun = "WEEKOFYEAR";
                break;
            case "sql:year":
                rmlcFun = "YEAR";
                break;
            case "sql:regexp_replace":
                rmlcFun ="REGEXP_REPLACE";
                break;
        }
        return rmlcFun;

    }


}
