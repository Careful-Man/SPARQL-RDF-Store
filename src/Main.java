//Oμάδα 8
//Ψαλτικίδης Θωμάς
//Καραθανάσης Χρήστος
//Τζιουβάκα Ιωάννα
//Χελιδώνη Θωμαή
//Μπότου Μαγδαληνή

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;



public class Main{
	
	public static void main (String args[]) throws IOException, SAXException, ParserConfigurationException {
		
		String choice = "y";
		
		
		while(choice.equals("y")) {
			PublicServiceAPI search = new PublicServiceAPI();//εισαγωγή ορίων απο τον χρήστη
			search.printPublicServicesWithCosts();//αναζήτηση βάσει των ορίων
			
			
			System.out.println("\n\n\nΠληκρτολογήστε y και Enter για να πραγματοποιήσετε άλλη αναζήτηση.");
			System.out.println("Οποιαδήποτε άλλη τιμή τερματίζει το πρόγραμμα.");
			
			//αν εισαγει 1 κανει και αλλη αναζήτηση, αλλιώς τερματίζεται το πρόγραμμα.
			choice = PublicServiceAPI.getScanner().next();
			
		}
		
		
		
	}
	
}
