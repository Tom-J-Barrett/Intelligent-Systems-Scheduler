import java.util.*;
import java.io.*;
import java.util.*;
import java.util.Random;
import java.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Comparator;
import java.util.Random;

public class is14171198{
	//main method: 	Creates a Userinput object and sets the integer values needed for this program.
	//				Creates a Schedule object, envokes the createStudentsSchedule method, envokes the generateOrderings method and nvokes the print method.
	public static void main(String[]args){
		int generations,population,students,modulePerCourse,modules,d,n,crossover,mutation,reproduction;
		
		UserInput input= new UserInput();
		
		generations=input.getGenerations();
		population=input.getPopulations();
		students=input.getStudents();
		modulePerCourse=input.getModulesPerCourse();
		modules=input.getModules();
		d=input.getDays();
		crossover=input.getCrossover();
		mutation=input.getMutation();
		reproduction=input.getReproduction();
		
		
		Schedule studentsSchedule= new Schedule(generations,population,students,modules,modulePerCourse,crossover,mutation,reproduction);
		studentsSchedule.createStudentsSchedule();
		studentsSchedule.generateOrderings();
		studentsSchedule.selection();
		studentsSchedule.print(studentsSchedule,students, modules, modulePerCourse);
	}
}

class UserInput{
	private int generations,population,students,modulePerCourse,modules,d,n,crossover,mutation,reproduction;
	private boolean generationsIsNotValid;
	private boolean studentsIsNotValid;
	private boolean modulesIsNotValid;
	private boolean modulePerCourseIsNotValid;
	private boolean populationIsNotValid;
	
	//UserInput class contructor which calls the getInputFromUser method.
	UserInput(){
		generationsIsNotValid=true;
		studentsIsNotValid=true;
		modulePerCourseIsNotValid=true;
		modulesIsNotValid=true;
		populationIsNotValid=true;
		getInputFromUser();
	}
	
	//This method takes in data from the use and sets them to the private data fields for this class.
	public void getInputFromUser(){
		Scanner in=new Scanner(System.in);
		while(generationsIsNotValid){
			System.out.println("Please enter number of generations(greater than 0): ");
			generations=Integer.parseInt(in.nextLine());
			if(generations>0)
				generationsIsNotValid=false;
		}	
		
		while(populationIsNotValid){
			System.out.println("Please enter populationsize(greater than 0): ");
			population=Integer.parseInt(in.nextLine());
			if(population>0)
				populationIsNotValid=false;
		}
		
		while(studentsIsNotValid){
			System.out.println("Please enter number of students(greater than 0): ");
			students=Integer.parseInt(in.nextLine());
			if(students>0)
				studentsIsNotValid=false;
		}
		
		while(modulesIsNotValid){
			System.out.println("Please enter total number of modules(greater than 0 and even): ");
			modules=Integer.parseInt(in.nextLine());
			if(modules>0 && modules%2==0)
				modulesIsNotValid=false;
		}
		
		while(modulePerCourseIsNotValid){
			System.out.println("Please enter number of modules in a course(greater than 0 and less than number of modules: ");
			modulePerCourse=Integer.parseInt(in.nextLine());
			if(modulePerCourse>0 && modulePerCourse<modules)
				modulePerCourseIsNotValid=false;
		}
		
		System.out.println("Please enter percentage number for crossover: ");
			crossover=Integer.parseInt(in.nextLine());
			
		System.out.println("Please enter percentage number for mutation: ");
			mutation=Integer.parseInt(in.nextLine());
			
		reproduction=100-mutation-crossover;
		
		d=modules/2;
	}
	
	public int getGenerations(){
		return generations;
	}
	
	public int getPopulations(){
		return population;
	}
	
	public int getStudents(){
		return students;
	}
	
	public int getModules(){
		return modules;
	}
	
	public int getModulesPerCourse(){
		return modulePerCourse;
	}
	
	public int getDays(){
		return d;
	}
	
	public int getMutation(){
		return mutation;
	}
	
	public int getCrossover(){
		return crossover;
	}
	
	public int getReproduction(){
		return reproduction;
	}
	
}


class Schedule{
	
	private int generations,population,students,modulePerCourse,modules,d,n,selectionPopulation,mutation,crossover,reproduction;
	private ArrayList<ArrayList<Integer>> studentsSchedule;
	private Ordering ordering;
	private Ordering temp;
	private Ordering temp2;
	private Ordering temp3;
	private int[][] mutationArray;
	private List<Ordering> populationOrders;
	private ArrayList<Ordering> result;
	private ArrayList<Integer> modulesPerStudent;
	ArrayList<Integer> moduleList;
	private int cp;
	private int[][] ord1;
	private int[][] ord2;
	private int[]ord1_1;
	private int[]ord1_2;
	private int[]ord2_1;
	private int[]ord2_2;
	private List<Ordering> crossoverList;
	private List<Ordering> crossoverList2;
	
	Schedule(){
		
	}
	
	Schedule(int generations,int population,int students,int modules,int modulePerCourse,int crossover,int mutation,int reproduction){
		this.generations=generations;
		this.population=population;
		this.students=students;
		this.modules=modules;
		this.modulePerCourse=modulePerCourse;
		this.d=modules/2;
		this.crossover=crossover;
		this.mutation=mutation;
		this.reproduction=reproduction;
	}
	
	//This method writes the schedule data to a text file.
	//It writes the student data from the multi-dimensional arraylist studentList and the odering data from the 2D array in the Ordering object.
	public void print(Schedule studentsSchedule, int students,int modules, int modulePerCourse){
		ArrayList<ArrayList<Integer>> studentList= studentsSchedule.getStudentList();
		List<Ordering>  populationOrders= studentsSchedule.getPopulationOrders();
		int [][] orderModules;
		int counter=1;
		try{
			PrintWriter writer = new PrintWriter("AI17.txt", "UTF-8");
			for(int i=0;i<studentList.get(0).size();i+=modulePerCourse){
				writer.print("Student "+ studentList.get(0).get(i)+":");
				for(int j=i;j<i+modulePerCourse;j++){
					writer.print(" M" + studentList.get(1).get(j));
				}
				writer.println();
			}
			writer.println();
		
			for(Ordering order: populationOrders){
				orderModules=order.getOrdering();
				String line1="";
				String line2="     ";
				writer.print("Ord"+order.getOrderNum()+":");
				for(int c=0;c<modules/2;c++){
					line1+=" m"+ orderModules[0][c];
					line2+=" m"+ orderModules[1][c];
				}
				line2+="  cost: "+ order.getCost();
				writer.println(line1);
				writer.println(line2);
				writer.println();
				counter++;
			}
			writer.close();
			System.out.println("Output printed to file 'AI17.txt'.");
		} catch (IOException e) {
			System.out.println("Exception!");
		}
	}
	
	//Creates random students modules
	public void createStudentsSchedule(){
		modulesPerStudent=new ArrayList<Integer>();
		moduleList=new ArrayList<Integer>();
		studentsSchedule= new ArrayList<ArrayList<Integer>>();
		studentsSchedule.add(new ArrayList<Integer>());
		studentsSchedule.add(new ArrayList<Integer>());
		
		int x;
		for(int i=1;i<students+1;i++){
			moduleList=new ArrayList<Integer>();
			moduleList=generateModules(moduleList);
			for(int j=0;j<modulePerCourse;j++){
				n=0 + (int)(Math.random() * (moduleList.size())); 
				if(moduleList.size()>0){
					x=moduleList.get(n);	
					studentsSchedule.get(0).add(i);
					studentsSchedule.get(1).add(x);
					moduleList.remove(n);
				}	
			}
		}	
	}
	
	//Generates Ordering objects to be placed in the populationOrders arraylist.
	public void generateOrderings(){
		populationOrders=new ArrayList<Ordering>();
		for(int i=1;i<population+1;i++){
			populationOrders.add(new Ordering(i,d,modules,studentsSchedule,students,modulePerCourse));
		}
	}
	
	public ArrayList<ArrayList<Integer>> getStudentList(){
		return studentsSchedule;
	}
	
	public List<Ordering> getPopulationOrders(){
		return populationOrders;
	}
	
	//generates an arraylist of all the modules
	public ArrayList<Integer> generateModules(ArrayList<Integer> moduleList){
		for(int i=1; i< modules+1; i++){
			moduleList.add(i);	
		}	
		return moduleList;
	}
	
	public void selection(){
		selectionPopulation=population/3;

		
		populationOrders= populationOrders.stream()
			.sorted(Comparator.comparing(o->o.getCost()))
			.collect(Collectors.toList());
		   
		for(int j=0;j<selectionPopulation;j++){
			populationOrders.remove(j*2);
			temp=populationOrders.get(j);
			populationOrders.add(temp);
			temp2=temp=populationOrders.get(j);
		}
		
		for(int i=0;i<populationOrders.size()-1;i+=2){
			int x=i+1;
			temp=populationOrders.get(i);
			temp2=populationOrders.get(x);
			int index= 1 + (int)(Math.random() * 100); 
			/*if(index<=mutation)
				mutation(temp);
			else if(index >mutation && index <= mutation + crossover)
				crossover(temp,temp2);
			else
				System.out.println("Reproduction");*/
			crossoverList=new ArrayList();
			crossoverList.add(temp);
			crossoverList.add(temp2);
			crossoverList=crossover(crossoverList);
			populationOrders.set(i, crossoverList.get(0));
			populationOrders.set(x, crossoverList.get(1));
			System.out.println();
		}	
	}
	
	public List<Ordering> crossover(List<Ordering> crossoverList){
		Ordering order1=crossoverList.get(0);
		Ordering order2=crossoverList.get(1);
		System.out.println("Crossover:"+ order1.getOrderNum()+"   "+ order2.getOrderNum());
		//cp=(int)(Math.random() * ((2*d)-4)) + 2; 
		cp=5;
		//2D arrays
		ord1=order1.getOrdering();
		ord2=order2.getOrdering();
		
		//1D arrays
		ord1_1= new int[cp];
		ord1_2= new int[(2*d)-(cp)];
		ord2_1= new int[cp];
		ord2_2= new int[(2*d)-(cp)];
		
		System.out.println(cp);
		
		if(cp<ord1[0].length){
			System.out.println("Small cp");
			swapValues(0);
		}
		else if(cp>ord1[0].length){
			System.out.println("Big cp");
			swapValues(1);
		}
		swapDoubles();
		order1.setOrdering(ord1);
		order2.setOrdering(ord2);
		crossoverList2=new ArrayList();
		crossoverList2.add(order1);
		crossoverList2.add(order2);
		return crossoverList2;
	}
	
	public void swapValues(int x){

		System.out.println("Yo"+ord1.length+"  "+ord2.length);
		System.out.println("Bo"+ord1_1.length);
		if(x==1){
			for(int i=0;i<ord1_2.length;i++){
				ord1_2[i]=ord1[x][(cp-d)+i];
				ord2_2[i]=ord2[x][(cp-d)+i];
			}
			for(int c=0;c<ord1_2.length;c++){
				ord1[x][c+(cp-d)]=ord2_2[c];
				ord2[x][c+(cp-d)]=ord1_2[c];
			}
			int[][]temp=ord1;
			ord1=ord2;
			ord2=temp;
		}
		else if(x==0){
			for(int i=0;i<ord1_1.length;i++){
				ord1_1[i]=ord1[x][i];
				ord2_1[i]=ord2[x][i];
			}
			for(int c=0;c<ord1_1.length;c++){
				ord1[x][c]=ord2_1[c];
				ord2[x][c]=ord1_1[c];
			}
		}
	}
	
	public void swapDoubles(){
		
	}
	
	public void mutation(Ordering order){
		System.out.println("Mutation");
		
		int day1= 0 + (int)(Math.random() * 1); 
		int day2= 0 + (int)(Math.random() * 1); 
		int exam1= 0 + (int)(Math.random() * d); 
		int exam2= 0 + (int)(Math.random() * d); 
		
		mutationArray=order.getOrdering();
		int temp;
		
		temp=mutationArray[day1][exam1];
		mutationArray[day1][exam1]=mutationArray[day2][exam2];
		mutationArray[day2][exam2]=temp;
		
		order.setOrdering(mutationArray);
		order.generateFitness();
	}
}


class Ordering{
		
	private int ordernum;
	private int d;
	private int [][] ordering;
	private int modules;
	private int index;
	private int x;
	private int cost;
	private int students;
	private int modulePerCourse;
	private ArrayList<ArrayList<Integer>> studentsSchedule;
	ArrayList<Integer> moduleList;
	
	Ordering(){
		
	}
	
	//Ordering constructor that calls the generateOrder function and the generateFitness function.
	Ordering(int ordernum, int d, int modules,ArrayList<ArrayList<Integer>> studentsSchedule,int students,int modulePerCourse){
		this.ordernum=ordernum;
		this.d=d;
		this.modules=modules;
		this.students=students;
		this.studentsSchedule=studentsSchedule;
		this.modulePerCourse=modulePerCourse;
		ordering= new int[2][d];
		generateOrder();
		generateFitness();
	}
	
	//Generates a random ordering and places data into 2D array.
	public void generateOrder(){
		moduleList=new ArrayList<Integer>();
		moduleList=generateModules(moduleList);	
		
		for(int i=0;i<d;i++){
			for(int j=0;j<2;j++){
				index= 0 + (int)(Math.random() * moduleList.size()-1); 
				x=moduleList.get(index);
				ordering[j][i]= x;
				moduleList.remove(index);
			}
		}
		
	}
	
	//Generates a list of modules
	public ArrayList<Integer> generateModules(ArrayList<Integer> moduleList){
		for(int i=1; i< modules+1; i++)
			moduleList.add(i);
		
		return moduleList;
	}
	
	//Generates a fitness value for the object based on the 2D ordering array
	public void generateFitness(){
		int m1;
		int m2;
		ArrayList<Integer> studentsModules=new  ArrayList<Integer>();
		for(int i=0;i<d;i++){
			m1=ordering[0][i];
			m2=ordering[1][i];
	
			for(int c=0;c<studentsSchedule.get(0).size();c+=modulePerCourse){
				studentsModules=new  ArrayList<Integer>();

				for(int j=c;j<c+modulePerCourse;j++){
					studentsModules.add(studentsSchedule.get(1).get(j));
				}

				if(studentsModules.contains(m1) && studentsModules.contains(m2))
					cost++;
			}
		}
	}
	
	public int getCost(){
		return cost;
	}
	
	public int[][] getOrdering(){
		return ordering;
	}
	
	public int getOrderNum(){
		return ordernum;
	}
	
	public void setOrdering(int [][] ordering){
		this.ordering=ordering;
	}
};













