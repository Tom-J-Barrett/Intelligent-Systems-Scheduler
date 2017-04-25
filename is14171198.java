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
	//				Creates a Schedule object, envokes the createStudentsSchedule method, envokes the generateOrderings method.
	//				When this is complete it calls the selection method which runs the GA.
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
	}
}

//Class UerInput is used to take in parameters from the user 
class UserInput{
	private int generations,population,students,modulePerCourse,modules,d,n,crossover,mutation,reproduction;
	private boolean generationsIsNotValid;
	private boolean studentsIsNotValid;
	private boolean modulesIsNotValid;
	private boolean modulePerCourseIsNotValid;
	private boolean populationIsNotValid;
	private boolean crossoverIsNotValid;
	private boolean mutationIsNotValid;
	
	//UserInput class contructor which calls the getInputFromUser method.
	UserInput(){
		generationsIsNotValid=true;
		studentsIsNotValid=true;
		modulePerCourseIsNotValid=true;
		modulesIsNotValid=true;
		populationIsNotValid=true;
		crossoverIsNotValid=true;
		mutationIsNotValid=true;
		getInputFromUser();
	}
	
	//This method takes in data from the use and sets them to the private data fields for this class.
	//The input from the user id validated to be an integer in the correct range for each variable
	public void getInputFromUser(){
		Scanner in=new Scanner(System.in);
		//Pattern p = Pattern.compile("^\\d+$");
		//Matcher numberMatcher;
		while(generationsIsNotValid){
			System.out.println("Please enter number of generations(greater than 0): ");
			try{
				generations=Integer.parseInt(in.nextLine());
				if(generations>0)
					generationsIsNotValid=false;
			}catch(NumberFormatException ex){	
			}
		}	
		
		while(populationIsNotValid){
			System.out.println("Please enter populationsize(greater than 0): ");
			try{
				population=Integer.parseInt(in.nextLine());
				if(population>0)
					populationIsNotValid=false;
			}catch(NumberFormatException ex){	
			}
		}
		
		while(studentsIsNotValid){
			System.out.println("Please enter number of students(greater than 0): ");
			try{
				students=Integer.parseInt(in.nextLine());
				if(students>0)
					studentsIsNotValid=false;
			}catch(NumberFormatException ex){	
			}
		}
		
		while(modulesIsNotValid){
			System.out.println("Please enter total number of modules(greater than 0 and even): ");
			try{
				modules=Integer.parseInt(in.nextLine());
				if(modules>0 && modules%2==0)
					modulesIsNotValid=false;
			}catch(NumberFormatException ex){	
			}
		}
		
		while(modulePerCourseIsNotValid){
			System.out.println("Please enter number of modules in a course(greater than 0 and less than number of modules: ");
			try{
				modulePerCourse=Integer.parseInt(in.nextLine());
				if(modulePerCourse>0 && modulePerCourse<modules)
					modulePerCourseIsNotValid=false;
			}catch(NumberFormatException ex){	
			}
		}
		
		while(crossoverIsNotValid){
			System.out.println("Please enter percentage number for crossover: ");
			try{
				crossover=Integer.parseInt(in.nextLine());
				if(crossover>0 && crossover<=98){
					crossoverIsNotValid=false;
			}
			}catch(NumberFormatException ex){	
			}
		}
		
		while(mutationIsNotValid){
			System.out.println("Please enter percentage number for mutation: ");
			try{
				mutation=Integer.parseInt(in.nextLine());
				if(mutation>0 && (mutation+crossover<100)){
					mutationIsNotValid=false;
			}
			}catch(NumberFormatException ex){	
			}
		}
		
			
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

//Class Schedule is used to store the schedule
class Schedule{
	
	private int generations,population,students,modulePerCourse,modules,d,n,selectionPopulation,mutation,crossover,reproduction;
	private List<List<Integer>> studentsSchedule;
	private Ordering ordering;
	private Ordering temp;
	private Ordering temp2;
	private Ordering temp3;
	private int[][] mutationArray;
	private List<Ordering> populationOrders;
	private List<Ordering> result;
	private List<Integer> modulesPerStudent;
	private List<Integer> moduleList;
	private int cp;
	private int[][] ord1;
	private int[][] ord2;
	private int[]ord1_1;
	private int[]ord1_2;
	private int[]ord2_1;
	private int[]ord2_2;
	private List<Ordering> crossoverList;
	private List<Ordering> crossoverList2;
	private Map<Integer, Integer> duplicates;
	private Map<Integer, Integer> duplicates2;
	private List<Integer> dup1;
	private List<Integer> dup2;
	private List<Generation> gen;
	
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
	//This method loops through a list of generations and prints out the best ordering that has been calculated earlier
	public void print(String name){
		int [][] orderModules;
		int counter=1;
		try{
			PrintWriter writer = new PrintWriter(name, "UTF-8");

			for(Generation g: gen){
				Ordering order= g.getBest();
				orderModules=order.getOrdering();
				String line1="";
				String line2="                                ";
				writer.print("Generation: "+counter+"  Best Ordering"+" :");
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
		studentsSchedule= new ArrayList<List<Integer>>();
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
	
	public List<List<Integer>> getStudentList(){
		return studentsSchedule;
	}
	
	public List<Ordering> getPopulationOrders(){
		return populationOrders;
	}
	
	//generates an arraylist of all the modules
	public List<Integer> generateModules(List<Integer> moduleList){
		for(int i=1; i< modules+1; i++){
			moduleList.add(i);	
		}	
		//System.out.println("Size: "+ moduleList.size());
		return moduleList;
	}
	
	//This method, for each generation, orders a list of Ordering by cost,
	//and cuts it so that you replace the worst 3rd or orderings with the best 3rd.
	//The ordernumbers are then changed to be in chronilogical order
	//Then, for each popuation, it selects a random number between 1-100 and selects either
	// crossover,mutaion or reporoduction depending on user input percentages
	//the ordering is then saved and added to a generation and the gneration is added to a list of generations
	public void selection(){
		gen=new ArrayList<Generation>();
		for(int c=0; c< generations; c++){
			selectionPopulation=population/3;
			
			populationOrders= populationOrders.stream()
				.sorted(Comparator.comparing(o->o.getCost()))
				.collect(Collectors.toList());
			   
			for(int j=0;j<selectionPopulation;j++){
				temp=populationOrders.get(j);
				populationOrders.set((populationOrders.size()-selectionPopulation+j),temp);
			}
			
			for(int i=0;i<populationOrders.size();i++){
				populationOrders.get(i).setOrderNum(i+1);
			}
			
			for(int i=0;i<populationOrders.size()-1;i++){
				int index= 1 + (int)(Math.random() * 100); 
				temp=populationOrders.get(i);
				if(index<=mutation){
					mutation(populationOrders.get(i));
				}
				else if(index >mutation && index <= mutation + crossover){
					int x=i+1;
					temp2=populationOrders.get(x);
					crossoverList=new ArrayList<Ordering>();
					crossoverList.add(temp);
					crossoverList.add(temp2);
					crossoverList=crossover(crossoverList);
					populationOrders.set(i, crossoverList.get(0));
					populationOrders.set(x, crossoverList.get(1));
					i++;
				}
				
			}	
			Generation g=new Generation();
			g.setList(populationOrders);
			g.generateBestOrdering();
			gen.add(g);
		}
		print("AI17.txt");
	}

	//The crossover function takes a list of Ordering that will contain 2 Ordering objects.
	//It creates a random cutting point in which t0 cut the 2D arrays from each Ordering in the list.
	//Arrays are created in which to pass these now seperated 2D arrays.
	//Depending on if the cutting point reaches past the first row of th 2D array the swapValues method is called with an int parameter.
	//Once swap values is complete, elements in the 2D array have been swapped with the other and we now call the getDoubles function to 
	//check for doubles in each ordering and pass them to a list.
	//The function swapDoubles is now called to swap doubles if any were found and the fitness is then recaculated for each ordering
	public List<Ordering> crossover(List<Ordering> crossoverList){
		Ordering order1=crossoverList.get(0);
		Ordering order2=crossoverList.get(1);
		cp=(int)(Math.random() * ((2*d)-4)) + 2; 
		ord1=order1.getOrdering();
		ord2=order2.getOrdering();
		
		//1D arrays
		ord1_1= new int[cp];
		ord1_2= new int[(2*d)-(cp)];
		ord2_1= new int[cp];
		ord2_2= new int[(2*d)-(cp)];
		
		
		if(cp<ord1[0].length){
			swapValues(0);
		}
		else if(cp>ord1[0].length){
			swapValues(1);
		}
		dup1=new ArrayList<Integer>();
		dup2=new ArrayList<Integer>();
		dup1=getDoubles(dup1,ord1,order1.getOrderNum());
		dup2=getDoubles(dup2,ord2,order2.getOrderNum());
		swapDoubles();
		
		order1.setOrdering(ord1);
		order2.setOrdering(ord2);
		order1.generateFitness();
		order2.generateFitness();
		crossoverList2=new ArrayList<Ordering>();
		crossoverList2.add(order1);
		crossoverList2.add(order2);
		return crossoverList2;
	}
	
	//The swap values function swaps the values from one 2D array to another depending in the cutting point location.
	//It loops through each ordering and replaces the values
	//If the cutting point runs to the second row you perform the same operation but swap the orderings afterwards to account for this
	public void swapValues(int x){
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

	//This function checks for duplicates in a 2D array
	//It first checks by row if any duplicates are in the same row and if so adds the row and column as ints to a list.
	//It then checks if any of the values in row1 appear in row 2 and if so adds the row and column as ints to a list.
	//This list of duplicates is then returned.
	public List<Integer> getDoubles(List<Integer> dup, int[][] ord, int x){
		for(int j=0;j<ord[0].length;j++){
			for(int k=j+1;k<ord[1].length;k++){
				if(ord[0][j]==ord[0][k]&&(!(j==k))){
					dup.add(0);
					dup.add(k);
				}
				else if(ord[1][j]==ord[1][k]&&(!(j==k))){
					dup.add(1);
					dup.add(k);
				}
			}
		}
		for(int i=0;i<ord[0].length;i++){
			for(int c=0;c<ord[1].length;c++){
				if(ord[0][i]==ord[1][c]){
					dup.add(1);
					dup.add(c);
				}
			}
		}
		return dup;
	}
	
	//This function swaps the duplicates found in the previous function.
	//You can assume that there will be the same amount of duplicates in ordering 1 as ordering 2.
	//It loops through the size of the dupliactes list while incrementing by 2 to get the row and column and swaps the corresponding values.
	public void swapDoubles(){
		if(dup1.size()==dup2.size()){
			for(int i=0;i<dup1.size();i=i+2){ 
				int row1=dup1.get(i);
				int col1=dup1.get(i+1);
				int row2=dup2.get(i);
				int col2=dup2.get(i+1);
				int temp= ord1[row1][col1];
				ord1[row1][col1]= ord2[row2][col2];
				ord2[row2][col2]=temp;
			}
		}
	}
	
	//The mutation function swaps random values in the 2 orderings.
	//It random generates a row and column to be swapped and the swaps.
	//It recalculates the new orderings fitness once the swap has happened.
	public void mutation(Ordering order){
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

//An Ordering is contained in Schedule and it has the ordering of exams
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
	private List<List<Integer>> studentsSchedule;
	List<Integer> moduleList;
	
	Ordering(){
		
	}
	
	//Ordering constructor that calls the generateOrder function and the generateFitness function.
	Ordering(int ordernum, int d, int modules,List<List<Integer>> studentsSchedule,int students,int modulePerCourse){
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
				index= 0 + (int)(Math.random() * moduleList.size()); 
				x=moduleList.get(index);
				ordering[j][i]= x;
				moduleList.remove(index);
			}
		}
		
	}
	
	//Generates a list of modules
	public List<Integer> generateModules(List<Integer> moduleList){
		for(int i=1; i< modules+1; i++)
			moduleList.add(i);
		
		return moduleList;
	}
	
	//Generates a fitness value for the object based on the 2D ordering array
	public void generateFitness(){
		cost=0;
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
	
	public void setOrderNum(int ordernum){
		
		this.ordernum=ordernum;
	}
	
	public void setOrdering(int [][] ordering){
		this.ordering=ordering;
	}
};

//A generation conatins a list of orderings
class Generation{
	private List<Ordering> orderings;
	private Ordering bestOrdering;
	
	Generation(){
		orderings=new ArrayList<Ordering>();
	}
	
	public void addOrdering(Ordering x){
		orderings.add(x);
	}
	
	//This method stores the best ordering ie the first ordering in a list after you sort by cost
	public void generateBestOrdering(){
		orderings= orderings.stream()
			.sorted(Comparator.comparing(o->o.getCost()))
			.collect(Collectors.toList());
		bestOrdering=orderings.get(0);
	}
	
	public void setList(List<Ordering> list){
		this.orderings=list;
	}
	
	public int getBestOrderingCost(){
		return bestOrdering.getCost();
	}
	
	public Ordering getBest(){
		return bestOrdering;
	}
	
	public List<Ordering> getList(){
		return orderings;
	}
}













