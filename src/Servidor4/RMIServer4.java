import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer4 {
	
	Integer port = 8004;
	
	private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(port);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
        System.out.println("Server ligado!!!");
    }
	
	public static void main(String[] args) {
        RMIServer4 main = new RMIServer4();
        main.startServer();
    }

}
