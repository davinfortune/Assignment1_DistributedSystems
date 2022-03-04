class Student{
    public String id;
    public String name;
  
    // Student class constructor
    Student(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
  
    // display() method to display
    // the student data
    public void display()
    {
        System.out.println("Student id is: " + id + " "
                           + "and Student name is: "
                           + name);
        System.out.println();
    }
}