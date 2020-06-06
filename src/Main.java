import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String args[]){
        Scanner user_scanner = new Scanner(System.in);
        ArrayList<Season> curr = new ArrayList<>();

        curr.add(new Season("2010/2011","Milan","Banda","Serie A" , "Italy", 20, 10,2));
        curr.add(new Season("2011/2012","Milan","Banda","Serie A" , "Italy", 20, 10,2));
        curr.add(new Season("2009/2010","Milan","Banda","Serie A" , "Italy", 20, 10,2));

        try{
        System.out.println("Inserisci l'annata da rimuovere: (2013/2014)");
        String yeartr = user_scanner.nextLine();
        Integer num = Integer.parseInt(yeartr.substring(0,4));
        System.out.println(num);
            System.out.println(curr.size());}
        catch(Exception e){
            System.out.println("Formato annata inserita errato");
        }
    }
}
