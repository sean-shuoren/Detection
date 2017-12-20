import java.lang.String;
import java.io.*;
import java.util.*;

public class Parse {

   private static Map<String, List<String>> map = new HashMap<>();//the map used to check

   public static void main(String args[]) throws IOException {
      if (args.length != 1) {
         System.err.println("Error: argument not match");
         System.exit(1); 
      }

      List<String> list0 = new ArrayList<>(); //the list of log files
      BufferedReader input = null;
      input = new BufferedReader(new FileReader(args[0]));      
      String line;
      List<String> list_temp = null;
      String classname_temp = "";

      line = input.readLine();
      String[] tokens = line.split("\\s+");
      if (!tokens[0].equals("files")){
         System.err.println("Error: input file does not start with 'files'");
         System.exit(1);          
      }
      while (true){
         line = input.readLine();
         tokens = line.split("\\s+");
         if(!tokens[0].equals("")) break;

         list0.add(tokens[1]);
      }

      if (!tokens[0].equals("classes")){
         System.err.println("Error: input file does not match with 'classes'");
         System.exit(1);
      }
      while (true){
         line = input.readLine();
         if(line == null) { System.out.print("break outer loop because of null\n"); break;}
         tokens = line.split("\\s+");
         
         if(tokens.length == 3){//within one class
            classname_temp = tokens[2];
            list_temp = new ArrayList<>();
            list_temp.add("false");
            while(true){
               line = input.readLine();
               if(line == null) {System.out.print("break inner loop of "+classname_temp+" because of null\n"); break;}
               tokens = line.split("\\s+");
               if(!tokens[1].equals("class")) {System.out.print("break inner loop of "+classname_temp+" because of class\n");break;}

               list_temp.add(tokens[1]);
            }
            map.put(classname_temp, list_temp);
         }//end one class
      }

      // //pre-determine the list of log files
      // List<String> list0 = new ArrayList<>();
      // list0.add("MainEntry");
      // list0.add("MammalInt");
      // list0.add("DocFooter");

      // //pre-determine the map
      // List<String> list1 = new ArrayList<>();
      // list1.add("false"); //bool variable, non-initial methods not invoked
      // list1.add("init");
      // map.put("MammalInt", list1);

      // List<String> list2 = new ArrayList<>();
      // list2.add("false"); //bool variable, non-initial methods not invoked
      // list2.add("init");
      // map.put("DocFooter", list2);

      for(Map.Entry<String, List<String>> entry : map.entrySet()) {
         String key = entry.getKey();
         List<String> value = entry.getValue();
         System.out.print(key+": ");
         for (String element : value) {
            System.out.print(element + " ");
         }
         System.out.println();
      }

      //load the disassemblies of .class files
      BufferedReader in = null;
      FileWriter out = null;

      try {
         for (String filename : list0) {
            System.out.print("Reading from " + filename + "\n");
         
            in = new BufferedReader(new FileReader(filename));
            out = new FileWriter("Parse.txt");
            
            String c;
            while ((c = in.readLine()) != null) {
               if(!c.matches("(.*)invokevirtual(.*)") || !c.matches("(.*)Method(.*)")) continue;

               // out.write(c, 0, c.length());
               // out.write('\n');
               // System.out.println(c);

               // analyzing the string 
               String[] tokensVal = c.split("\\s+|:");

               String Val = tokensVal[7]; //method name
               String[] tokensMeth = Val.split("\\.");
               if (tokensMeth.length == 2){
                  System.out.print(tokensMeth[0] + " " + tokensMeth[1]);
                  System.out.println();

                  if(map.containsKey(tokensMeth[0])){
                     List<String> init_functions = map.get(tokensMeth[0]);
                     if( !init_functions.contains(tokensMeth[1])){
                        // System.out.println("substantive method");
                        init_functions.set(0, "true");
                     }
                  }
               }
            }// end while
         }

         System.out.println("-----------summary-------------");
         for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();

            if (key.equals("MainEntry")) {
               continue;
            }

            System.out.print(key + " is necessary: ");
            System.out.print(value.get(0) + " ");

            System.out.println();
         }

      }finally {
         if (in != null) {
            in.close();
         }
         if (out != null) {
            out.close();
         }
      }
   }
}