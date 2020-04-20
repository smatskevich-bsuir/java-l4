package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
	private static void createAndShowGUI() {
        JFrame frame = new JFrame("Крошка-Картошка-Клиент");
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
		
        JLabel labelHeader = new JLabel("Добавить заказ");
        content.add(labelHeader);
        
		JPanel complexityPanel = new JPanel();
        content.add(complexityPanel);
		
        JLabel labelTip = new JLabel("Сложность:");
        complexityPanel.add(labelTip);
        
        JTextField field = new JTextField(8);
        complexityPanel.add(field);
        
        JButton orderButton = new JButton("Добавить!");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	orderButton.setEnabled(false);
            	try {           		
        			Socket socket = new Socket("localhost", 8085);
        			JOptionPane.showMessageDialog(null, "Подключен к серверу Крошки-Картошки!");
        	    	
            		InputStream input = socket.getInputStream();
            		OutputStream output = socket.getOutputStream();
            		BufferedReader reader = new BufferedReader(new InputStreamReader(input));	
            		PrintWriter writer = new PrintWriter(output, true);
            		
        			writer.println(field.getText());
        			JOptionPane.showMessageDialog(null, "Заказ добавлен!");
        			
        			String line = reader.readLine(); 
        			JOptionPane.showMessageDialog(null, "Ответ от сервера: " + line);
        			socket.close();
        		} catch (Exception e) {
        			JOptionPane.showMessageDialog(null, "Произошла ошибка " + e.getMessage());
        			e.printStackTrace();
        		}
            	orderButton.setEnabled(true);
            }
        });
        content.add(orderButton);    
		
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
