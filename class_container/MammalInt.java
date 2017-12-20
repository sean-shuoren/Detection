/* File name : MammalInt.java */
public class MammalInt implements Animal {
   DocFooter DF;

   public void init(){
      System.out.println("Mammal init");
   }

   public void eat() {
      System.out.println("Mammal eats");
   }

   public void travel() {
      System.out.println("Mammal travels");
   } 

   public int noOfLegs() {
      return 0;
   }
}