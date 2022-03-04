// Java implementation for a client 
// Save file as Client.java 

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.plaf.LabelUI;

import java.awt.event.*;
import java.io.*; 
import java.net.*; 
import java.util.Scanner;
import java.util.concurrent.TimeUnit; 

// Client class 
public class Client extends JFrame
{ 
	public static ResultSet rs;
	public static String name="", email ="";
	JFrame f=new JFrame();
	JPanel p=new JPanel(new GridLayout(8,2));
	JButton b1 = new JButton();
	JButton b2 = new JButton();
	JLabel labell = new JLabel("Student Number: ");
	JTextField userinput = new JTextField(3);
	JTextArea clientoutput = new JTextArea(30,30);
   
	public static void main(String[] args) throws IOException 
	{ 
		new Client();

	}


	public Client(){
	   
		b1.setText("Login");
		b2.setText("Exit");
		clientoutput.setEditable(false);
		p.add(labell);
		p.add(userinput);
		p.add(b1);
		p.add(b2);
		p.add(clientoutput);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(600, 250));
		f.add(p);
		f.setVisible(true);
		f.pack();
	
		b1.addActionListener(new LoginListener());
		b2.addActionListener(new ExitListener());
		
	} 


	private class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String received;
				String tosend;

				if(userinput.getText().equals("")) {
					clientoutput.setText("No Student Number Entered! Please Enter and try again!");

				} else {
					try
					{ 
						InetAddress ip = InetAddress.getByName("localhost"); 

						Socket s = new Socket(ip, 5056); 

						DataInputStream dis = new DataInputStream(s.getInputStream()); 
						DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

						while (true) 
						{ 
							if(userinput.getText().equals("")){
								tosend ="Awaiting Client";
							}else{
								tosend = userinput.getText(); 
							}
							
		
							dos.writeUTF("sn:"+tosend);
							TimeUnit.SECONDS.sleep(1);

							received = dis.readUTF(); 

							
							if(received.charAt(0) == 'e'){
								received = received.substring(3);
								clientoutput.setText("Logging In");
								loggedIn();
								clientoutput.setText("Welcome " + received + "... You are now connected to the Server");
							}
							if(received.equals("none")){
								clientoutput.setText("Student Dosent Exist");
								System.out.println("Student Dosent Exist");
							}
										
		
							s.close(); 
							dis.close(); 
							dos.close(); 
							break;
						}
						 
					}catch(Exception x){ 
						x.printStackTrace();
						System.out.println(x);
					} 
					
				}

			} catch(Exception err) {
				System.err.println(err);
			}
		}
	}

	void loggedIn(){
		///https://stackoverflow.com/questions/25100422/change-actionlistener-actionperformed-on-second-click
		
		b1.setText("Answer");
		labell.setText("Area of Circle:");
		userinput.setText("");
		ActionListener[] listeners = b1.getActionListeners();
		for (ActionListener l : listeners) {
			b1.removeActionListener(l);
		}
		b1.addActionListener(new AreaListener());
	}


	private class AreaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String received;
				String tosend;

				if(userinput.getText().equals("")) {
					clientoutput.setText("No Student Number Entered! Please Enter and try again!");

				} else {
					try
					{ 
						InetAddress ip = InetAddress.getByName("localhost"); 

						Socket s = new Socket(ip, 5056); 

						DataInputStream dis = new DataInputStream(s.getInputStream()); 
						DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

						while (true) 
						{ 
							clientoutput.setText("Processing .....");
							if(userinput.getText().equals("")){
								tosend ="Awaiting Client";
							}else{
								tosend = userinput.getText(); 
							}
							
		
							dos.writeUTF("ac:"+tosend);
							TimeUnit.SECONDS.sleep(1);

							double area = dis.readDouble();
							clientoutput.setText("The Area is: " + area); 

							
						
										
		
							s.close(); 
							dis.close(); 
							dos.close(); 
							break;
						}
						 
					}catch(Exception x){ 
						x.printStackTrace();
						System.out.println(x);
					} 
					
				}

			} catch(Exception err) {
				System.err.println(err);
			}
		}
	}

	private class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				InetAddress ip = InetAddress.getByName("localhost"); 

				Socket s = new Socket(ip, 5056); 

				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

				dos.writeUTF("ex:Exit");
				s.close(); 
				dos.close(); 
				System.exit(0);
			} catch(Exception err) {
				System.err.println(err);
			}
		}
	}


} 