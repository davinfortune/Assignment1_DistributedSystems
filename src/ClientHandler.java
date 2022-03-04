import java.io.*; 
import java.util.*;
import java.net.*; 
import java.sql.*;
import javax.swing.JTextArea;

 

// ClientHandler class 
class ClientHandler extends Thread 
{ 
	
	final DataInputStream dis; 
	final DataOutputStream dos; 
	final Socket s; 
	final JTextArea serveroutput;
	final ArrayList<Student> students;
	final String username;
	final String password;
	

	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, JTextArea serveroutput, ArrayList<Student> students, String username, String password) 
	{ 
		this.s = s; 
		this.dis = dis; 
		this.dos = dos; 
		this.serveroutput = serveroutput;
		this.students = students;
		this.username = username;
		this.password = password;

	} 

	@Override
	public void run() 
	{ 
		String received; 
		String toreturn; 
		boolean studentexists = false;

		while (true) 
		{ 
			try { 
				
				received = dis.readUTF();
				if(received.charAt(0) == 's'){
					toreturn = "Awaiting Client"; 
					received = received.substring(3);

					serveroutput.append("\nRECIEVED: " + received);
					for(int i = 0;i<students.size();i++){
						if(received.equals(students.get(i).id)){
							serveroutput.append("\nSTUDENT EXISTS");
							toreturn ="ex:"+students.get(i).name;
							studentexists = true;

							Class.forName("com.mysql.cj.jdbc.Driver");

							String url = "jdbc:mysql://localhost:3306/studentsdatabase";
				
							Connection con = DriverManager.getConnection(url,username,password);
							if (!con.isClosed())
							serveroutput.append("\nSuccessfully connected to MySQL server...");
				
				
							Statement s = con.createStatement ();
							s.executeUpdate (
							"UPDATE students SET TOT_REQ = TOT_REQ+1 WHERE STUD_ID = " + students.get(i).id);
							s.close ();
							serveroutput.append ("\nUpdated TOT_REQ for: " + students.get(i).id);


							break;
						}else {
							studentexists = false;
						}
					}
					
					if(studentexists == false){
						serveroutput.append("\nSTUDENT DOSENT EXIST");
						toreturn = "none";
					}
				
					if(received.equals("Exit")){
						break;
					}

					dos.writeUTF(toreturn); 
					this.dis.close(); 
					this.dos.close(); 
					this.s.close();
					serveroutput.append("\nClient Action Completed....Connection Closed");
					break;
				}
				if(received.charAt(0) == 'a'){
					double radius = 0.00;
					toreturn = "Awaiting Client"; 
					received = received.substring(3);
					try{
					radius = Double.parseDouble(received); 
					} catch(Exception v){ serveroutput.append("Invalid Entry"); received = "Exit";}

					serveroutput.append("\nRECIEVED: " + received);
				
					if(received.equals("Exit")){
						this.dis.close(); 
						this.dos.close(); 
						this.s.close();
						serveroutput.append("\nClient Action Completed....Connection Closed");
						break;
					}

					double area = radius * radius * Math.PI;
					serveroutput.append("\nCalculation Complete Sending Back: " + area);
					dos.writeDouble(area);


					dos.writeUTF(toreturn); 
					this.dis.close(); 
					this.dos.close(); 
					this.s.close();
					serveroutput.append("\nClient Action Completed....Connection Closed");
					break;
				}
				if(received.charAt(0) == 'e'){
					serveroutput.append("\nSession Ended by Client.....");
					this.dis.close(); 
					this.dos.close(); 
					this.s.close();
					serveroutput.append("\nClient Action Completed....Connection Closed");
					break;
				}

			} 
			
			catch (Exception e) { 
				e.printStackTrace(); 
				System.out.println(e);
			} 
		} 
	} 
} 
