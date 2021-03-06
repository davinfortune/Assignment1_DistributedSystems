import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

// Server class 
public class Server extends JFrame
{ 
	public static void main(String[] args) throws IOException 
	{ 
		ArrayList<Student> students = new ArrayList<Student>();
		String userName = null;
		String password = null;
		JFrame f=new JFrame();
		JPanel p = new JPanel(new BorderLayout());
		JTextArea serveroutput = new JTextArea();
		JScrollPane scrollPanel = new JScrollPane(serveroutput);
		p.add(scrollPanel);
		serveroutput.setEditable(false);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(500, 400));
		f.add(p);
		f.setVisible(true);
		f.pack();
        ///////////////////////////////////////////////////////////////////CREATING TABLE/////////
		Connection con = null;


		try {
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////    PLEASE ENTER USERNAME AND PASSWORD TO YOUR SQL SERVER IN ORDER TO USE  //////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
			userName = "root";
			password = "";
////////////////////////////////////////////////////
////////////////////////////////////////////////////			
////////////////////////////////////////////////////	
////////////////////////////////////////////////////			
			
			
			Class.forName("com.mysql.cj.jdbc.Driver");

			String url = "jdbc:mysql://localhost:3306/";

			try{
			Connection startingcon = DriverManager.getConnection(url,userName,password);
			Statement b = startingcon.createStatement ();
			try{
			b.executeUpdate ("CREATE DATABASE IF NOT EXISTS studentsdatabase");
			b.close ();
			} catch(Exception v){
				serveroutput.append("Database Already Exists");
			}
			serveroutput.append ("\nCreated Initial Database");
			} catch(Exception z){
				serveroutput.append("PLEASE ENTER THE CORRECT USERNAME AND PASSWORD FOR SQL");
			}


			url = "jdbc:mysql://localhost:3306/studentsdatabase";

			con = DriverManager.getConnection(url,userName,password);
			if (!con.isClosed())
			serveroutput.append("\nSuccessfully connected to MySQL server...");


			Statement s = con.createStatement ();

			s.executeUpdate ("DROP TABLE IF EXISTS students");
			s.executeUpdate (
			"CREATE TABLE students ("
			+ "SID INT(2) NOT NULL UNIQUE,"
			+ "STUD_ID INT(8),"
			+ "FNAME VARCHAR(20), SNAME VARCHAR(20),"
			+ "TOT_REQ INT(8))");
			s.close ();
			serveroutput.append ("\nAdded Table students");

			Statement a = con.createStatement ();

			a.executeUpdate (
			"INSERT INTO students (SID,STUD_ID,FNAME,SNAME,TOT_REQ)"
			+ "VALUES"
			+ "(54, 20085000, 'Davin', 'Fortune', 0),"
			+ "(64, 20025423, 'George', 'Russell', 0),"
			+ "(69, 20072361, 'Lance', 'Stroll', 0),"
			+ "(77, 20087526, 'Alex', 'Albon', 0)");
			a.close ();

			Statement l = con.createStatement ();
			String studentid;
			String studentname;
			ResultSet result = l.executeQuery("select STUD_ID,FNAME from students");
			while(true){
				if(result.next()){
					studentid=result.getString("STUD_ID");
					studentname=result.getString("FNAME");
					Student student = new Student(studentid, studentname);
					students.add(student);
					serveroutput.append("\nThis is a student id from the database " + studentid);
					} else {
						break;
					}
			}
			l.close ();
			serveroutput.append ("\nStudents Inserted Into Array");

		} 
		
		catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		
		finally {

				try {

					if (con != null)
						con.close();

				} 
				catch(Exception e) {}
			}

		///////////////////////////////////////////////////////////////////////////SERVER////////////
		// server is listening on port 5056 
		
		ServerSocket ss = new ServerSocket(5056);
		while (true) 
		{ 
		// running infinite loop for getting 
		// client request 
			
		
		Socket s = null;
		s = ss.accept(); 
		DataInputStream dis = new DataInputStream(s.getInputStream()); 
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());  
			
			try
			{ 
				// socket object to receive incoming client requests 
				
				
				serveroutput.append("\n\nA new client is connected : " + s); 
				
				// obtaining input and out streams 
				
				
				serveroutput.append("\nAssigning new thread for this client"); 

				// create a new thread object 
				Thread t = new ClientHandler(s, dis, dos,serveroutput,students,userName,password); 

				// Invoking the start() method 
				t.start(); 
			} 
			catch (Exception e){ 
				e.printStackTrace(); 
				System.out.println(e);
			} 		
		} 
	} 
} 