package zmq.com.ystlibrary.utility;

import java.util.ArrayList;

public class StringDraw {
   
    /** Creates a new instance of StringDraw */
    public StringDraw() {}
    public static String[] extractWords(String str, char delma) {
        int count = 0;
        String[] s;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == delma) {
                count++;
            }
        }
        int totalQuestion = count;
        s = new String[totalQuestion];
        int start = 0;
        for (int i = 0; i < totalQuestion;) {
            for (int j = start; j < str.length(); j++) {
                if (str.charAt(j) == delma) {
                    s[i] = str.substring(start, j + 1);
                    start = j + 1;
                    i++;
                }
            }
        }
        return s;
    } 
  
  
// for creating multiple lines accor to screen size
    public static String[] no_Of_Line(String[] str, int W)
    {
        int  No_ofLine;
        String[] s;
        String strinng = "abc";
        int j = 0, count = 0;
        while (strinng.charAt(strinng.length() - 2) != '!') 
        {
            strinng = "";
            
            while (strinng.length() + str[j].length() <= (W - W / 4) / 5) 
            {
                strinng += str[j++];
                
                if (j == str.length)
                {
                    break;
                }
            }
            
            count++;
        }
        
        No_ofLine = count;
        
        s = new String[No_ofLine];
        
        int strt = 0;
        
        for (int i = 0; i < No_ofLine; i++) 
        {
            int k = strt;
            strinng = "";
            while (strinng.length() + str[k].length() <= (W - W / 4) / 5)
            {
                strinng += str[k++];
                if (k == str.length) {
                    break;
                }
            } 
            strt = k;
            s[i] = strinng;
        }
        return s;
    }

    public static String[] breakTextIntoMultipleLine(String[] str, int imageWidth){

        ArrayList<String> ls=new ArrayList();

        String line="";
        for (int i = 0; i < str.length; i++) {
            String s = str[i];
            if((GlobalVariables.paint.measureText(line,0,line.length())+
                    GlobalVariables.paint.measureText(s,0,s.length())<=imageWidth)){

                line+=s;
                if(i==str.length-1){
                    ls.add(line);
                }
            }
            else{

                ls.add(line);
                line=s;
                if(i==str.length-1){
                   ls.add(line);
                }
            }

        }
        String[] lines=new String[ls.size()];
        ls.toArray(lines);
        return lines;
    }
    public static String joinStringWithCharacter(String[] s, String joiner){

        String temp="";
        if (s.length==1) {
            temp+=s[0]+joiner;
//            System.out.printf("Tem="+temp);
            return temp.trim();
        }
        for (int i = 0; i <s.length ; i++) {
            if(i==s.length-1){
                temp+=s[i]+joiner;
            }
            else{
           temp+=s[i]+joiner;
            }
        }
//        System.out.printf("Tem="+temp);
        return temp.trim();
    }
}
