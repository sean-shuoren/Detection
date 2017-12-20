public class MainEntry{
   public static void main(String args[]) {
      MammalInt m = new MammalInt();
      m.init(); //init
      m.eat();  //substantive method
      m.travel(); //substantive method

      m.DF = new DocFooter();
      // m.DF.output();

      System.out.println("MainEntry Exits");
   }
}