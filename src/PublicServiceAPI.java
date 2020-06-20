//Oμάδα 8
//Ψαλτικίδης Θωμάς
//Καραθανάσης Χρήστος
//Τζιουβάκα Ιωάννα
//Χελιδώνη Θωμαή
//Μπότου Μαγδαληνή

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PublicServiceAPI {
	
	private double lowerCost, higherCost;
	
	
	
	//κατασκευαστής αντικειμένων της κλάσης. Χρησιμος ΜΟΝΟ για να τυπωνει μηνύματα στην οθονη και ΟΧΙ στην
	//λειτουργία του ΑΡΙ
	public PublicServiceAPI() {
		
		
		do {
			do {
				System.out.println("Εισάγετε το χαμηλότερο κόστος: ");
				this.lowerCost = getScanner().nextDouble();
			}while(this.lowerCost<0);//το κόστος πρέπει να είναι μη αρνητικός αριθμός
			
			do {
				System.out.println("Εισάγετε το υψηλότερο κόστος (πρέπει να έχει μεγαλύτερη τιμή από το χαμηλότερο κόστος): ");
				this.higherCost = getScanner().nextDouble();
			}while(this.higherCost<0);//το κόστος πρέπει να είναι μη αρνητικός αριθμός
			
		}while(this.higherCost<this.lowerCost);//το ελάχιστο κόστος πρέπει να είναι μικρότερο του μεγίστου
		
	}
	
	
	
	
	
	
	
	
	//μέθοδος που ΜΟΝΟ τυπώνει στην κοσνόλα τα αποτελέσματα. 
	//Καθαρά βοηθητική και ΔΕΝ βοηθάει ουσιαστικά στην λειτουργία του ΑΡΙ.
	public void printPublicServicesWithCosts() throws IOException, SAXException, ParserConfigurationException {
		
		//Url από SPARQL ερώτημα που επιστρέφει όλες τις διαθέσιμες υπηρεσίες με τα κόστη τους.
		URL getURL = new URL(
		"http://data.dai.uom.gr:8890/sparql?default-graph-uri=http%3A%2F%2Fdata.dai.uom.gr%3A8890%2FCPSV-Chatbot&query=prefix+cv%3A%3Chttp%3A%2F%2Fdata.europa.eu%2Fm8g%2F%3E%0D%0Aprefix+dct%3A+%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Fterms%2F%3E%0D%0A%0D%0ASELECT+DISTINCT+%3FPS_name+%3FCost%0D%0A%7B%0D%0A%3Fx+dct%3Atitle+%3FPS_name.%0D%0A%3Fx+cv%3AhasCost+%3Fy.%0D%0A%3Fy+cv%3Avalue+%3FCost.%0D%0A%7D%0D%0A&should-sponge=&format=text%2Fhtml&timeout=0&debug=on");
		
		
		
		//κωδικας που διαχειρίζεται το επιστρεφόμενο αποτέλεσμα
		String readLine = null;
		HttpURLConnection conection = (HttpURLConnection) getURL.openConnection();
		conection.setRequestMethod("GET");
	    int responseCode = conection.getResponseCode();
	    
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        
	    	BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        
	        while ((readLine = in.readLine()) != null) {
	            response.append(readLine);
	        } 
	        in.close();
	        
	        
	        
	        
	        //διαχείρηση απάντησης ερωτήματος
	        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getURL.openStream());
	        
	        
	        //τα δεδομένα του επιστρεφόμενου απο το ερώτημα πίνακα
	        NodeList docList = doc.getElementsByTagName("td");
	        
	        
	        //οι επικεφαλίδες των πεδίων του επιστρεφόμενου απο το ερώτημα πίνακα
	        NodeList docListTitle = doc.getElementsByTagName("th");
	        
	        
	        //εκτύπωση επικεφαλίδων
	        System.out.println("\n-------------------------------------------------------------------------------------------");
	        System.out.println("|      " + docListTitle.item(0).getTextContent() + "       |       " + docListTitle.item(1).getTextContent() + "      |");
 	        System.out.println("-------------------------------------------------------------------------------------------\n");
	        
 	        
 	        
 	        //εκτύπωση περιεχομένων του πίνακα του ερωτήματος
 	        int counter = 0;
 	        for(int n=0; n<docList.getLength(); n+=2) {
	 	     	Node psname = docList.item(n);
	 	     	Node priceString = docList.item(n+1);
	 	     	
 	     		
 	     		//έλεγχος αποτελέσματος για το αν βρίσκεται εντός των ορίων που εισήγαγε ο χρήστης
 	     		if(Double.parseDouble(priceString.getTextContent())>=this.lowerCost  &&  Double.parseDouble(priceString.getTextContent())<=this.higherCost) {
 	     			System.out.println("|  " + psname.getTextContent() + "   |   " + Double.parseDouble(priceString.getTextContent()) + "  |");
 	     			System.out.println("-------------------------------------------------------------------------------------------");
 	     			counter++;
 	     		}
	 	     	
			}
 	        //αριθμός των αποτελεσμάτω που τυπώθηκαν
 	        System.out.println("Σύνολο αποτελεσμάτων: " + counter);
	        
	    } else {
	    	//λάθος URI
	        System.out.println("GET NOT WORKED");
	    }
	}
	
	
	
	//μοναδικό σημείο αναφοράς Scanner. (χρειάζεται σε πάνω απο 1 σημεία στο πρόγραμμα)
	public static Scanner getScanner() {
		return new Scanner(System.in);
	}
	
	
	
	
	
	
	
	
	
	
	//η  μέθοδος του API που επιστρέφει τα αποτελέσματα. 
	//Χρειάζεται 3 ορίσματα: το ελάχιστο κόστος που χρειάζεται μια δημόσια υπηρεσία (double), το μέγιστο (double)
	//Επιστρέφει μια ArrayList<String> με τα ονόματα των υπηρεσιών που ικανοποιούν τα άνω κριτήρια.
	public static ArrayList<String> getPSnamesByCosts(double leastCost, double greatestCost) throws IOException, SAXException, ParserConfigurationException {
		
		ArrayList<String> searchedPublicServices = new ArrayList<>();
		
		
		
		
		//url από SPARQL ερώτημα που επιστρέφει όλες τις διαθέσιμες υπηρεσίες με τα κόστη τους
		//Στο store τα κόστη είναι αποθηκευμένα με μορφή (τύπο δεδομένων) String, συνεπώς
		//δεν συμφέρει η διαχείρηση τους μέσω SPARQL. Πρέπει να φέρουμε όλα τα αποτελέσματα στο
		//πρόγραμμα και να τα επεξεργαστούμε εδώ.
		URL getURL = new URL(
		"http://data.dai.uom.gr:8890/sparql?default-graph-uri=&query=prefix+cv%3A%3Chttp%3A%2F%2Fdata.europa.eu%2Fm8g%2F%3E%0D%0Aprefix+dct%3A+%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Fterms%2F%3E%0D%0A%0D%0ASELECT+DISTINCT+%3FPS_name+%3FCost%0D%0A%7B%0D%0A%3Fx+dct%3Atitle+%3FPS_name.%0D%0A%3Fx+cv%3AhasCost+%3Fy.%0D%0A%3Fy+cv%3Avalue+%3FCost.%0D%0A%7D&should-sponge=&format=text%2Fhtml&timeout=0&debug=on");		
		
		
		
		
		//κωδικας που διαχειρίζεται το επιστρεφόμενο αποτέλεσμα
		String readLine = null;
		HttpURLConnection conection = (HttpURLConnection) getURL.openConnection();
		conection.setRequestMethod("GET");
	    int responseCode = conection.getResponseCode();
	    
	    if (responseCode == HttpURLConnection.HTTP_OK) {
		        
	    	BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        
	        while ((readLine = in.readLine()) != null) {
	            response.append(readLine);
	        } 
	        in.close();
	        
	        
	        
	        
	        
	        
	        //διαχείρηση απάντησης ερωτήματος
	        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getURL.openStream());
	        
	        
	        //τα δεδομένα του επιστρεφόμενου απο το ερώτημα πίνακα
	        NodeList docList = doc.getElementsByTagName("td");
	        
	        
 	        //εκτύπωση περιεχομένων του πίνακα του ερωτήματος
 	        for(int n=0; n<docList.getLength(); n+=2) {
	 	     	Node psname = docList.item(n);
	 	     	Node priceString = docList.item(n+1);
	 	     	
	 	     	
 	     		//έλεγχος αποτελέσματος για το αν βρίσκεται εντός των ορίων που εισήγαγε ο χρήστης
 	     		if(Double.parseDouble(priceString.getTextContent())>=leastCost  &&  Double.parseDouble(priceString.getTextContent())<=greatestCost) {
 	     			searchedPublicServices.add(psname.getTextContent());
 	     		}
			}
	    } else {
	    	//λάθος URI
	        System.out.println("GET NOT WORKED");
	    }
		
		return searchedPublicServices;
	}
	
	
	
	
}
