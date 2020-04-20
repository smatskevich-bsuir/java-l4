package server;

public class Kitchen {
	public synchronized void cook(int complexity) {
		while(complexity > 0) {
			complexity--;
			try {
				Thread.sleep(1000);
			}
			catch (Exception e) {
	            System.out.println("Cook exception: " + e.getMessage());
	            e.printStackTrace();
			}
		}
	}
}
