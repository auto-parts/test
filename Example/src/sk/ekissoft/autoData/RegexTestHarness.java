package sk.ekissoft.autoData;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTestHarness {

    public static void main(String[] args){
        String regex = "cat.";
        String input = "cat.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
        	System.out.println("I found the text "+matcher.group()+
        			" starting at index " +matcher.start() +
        			" and ending at index "+matcher.end());
        }
    }
}