package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main {
	
	private static void createAndShowGUI() {
        JFrame frame = new JFrame("Крошка-Картошка-Сервер");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        JPanel content = packContent();
        frame.getContentPane().add(content);
        frame.pack();
        
        frame.setResizable(false);
        frame.setVisible(true);
	}
	
	private static JPanel packContent() {
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
        JLabel labelHeader = new JLabel("Приём заказов");
        content.add(labelHeader);
        
        JTextArea logs = new JTextArea(10, 20);
        JScrollPane scrollPane = new JScrollPane(logs); 
        logs.setEditable(false);
        content.add(scrollPane);
        
        JButton startButton = new JButton("Запуск!");
        startButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	Thread serverThread = new Thread() {
            	    public void run() {
		        		startButton.setEnabled(false);
		        		try (ServerSocket serverSocket = new ServerSocket(8085))
		            	{
		            		Kitchen kitchen = new Kitchen();
		            		logs.append("Сервер запущен!\n");
		            		while(true)
		            		{
		            			Socket socket = serverSocket.accept();
		            			logs.append("Подключился клиент!\n");
		                    	Thread thread = new Thread() {
		                    	    public void run() {
		                    	    	PrintWriter writer = null;
		                    	    	try {
		                    	    		InputStream input = socket.getInputStream();
		                    	    		OutputStream output = socket.getOutputStream();
		                    	    		BufferedReader reader = new BufferedReader(new InputStreamReader(input));	
		        	            	    	writer = new PrintWriter(output, true);
		        	            	    	
		        	            	    	String line = reader.readLine(); 
		        	            	    	Integer complexity = Integer.parseInt(line);
		        	            	    	
		        	            	    	logs.append(String.format("Клиент добавил заказ сложностью %d%n", complexity));
		        	            	    	kitchen.cook(complexity);
		        	            	    	logs.append(String.format("Заказ сложностью %d выполнен%n", complexity));	            	    	
		
		        	            	    	writer.println("done");
		        	            	    	socket.close();
		                    	    	} catch (Exception e) {
		                    	    		if(writer != null)
		                    	    			writer.println("error");
		                    	    		
		                    	    		logs.append(String.format("Ошибка потока клиента: %s%n", e.getMessage()));
		                    	            e.printStackTrace();
		                    	    	}
		                    	     }
		                    	};
		                    	thread.start();
		            		}
		            	} catch(Exception e) {
		            		logs.append(String.format("Ошибка потока сервера: %s%n", e.getMessage()));
		                    e.printStackTrace();
		            	}
		        		startButton.setEnabled(true);
            	    }
            	};
            	serverThread.start();
        	}
        });
        content.add(startButton);
		
		return content;
	}
	
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });  	
    }
}
