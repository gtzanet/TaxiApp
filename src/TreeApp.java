import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.lang.Double;
public class TreeApp {
    private static TreeNode lowestFInOpen(LinkedList<TreeNode> openlist, TreeNode myclient) {
        // TODO currently, this is done by going through the whole openList!
        TreeNode cheapest = openlist.get(0);
        for (int i = 0; i < openlist.size(); i++) {
            if (openlist.get(i).g_cost + distance(openlist.get(i),myclient) < (cheapest.g_cost + distance(cheapest,myclient))) {
                cheapest = openlist.get(i);
            }
        }
        return cheapest;
    }

	private static List<TreeNode> adjacent(ArrayList<TreeNode> nodes, LinkedList<TreeNode> closedlist,TreeNode current)
	{	
		List<TreeNode> adj = new LinkedList<TreeNode>();
		TreeNode temp1;
		TreeNode temp2;
		for (TreeNode tr: nodes){
			if((tr.longitude == current.longitude)&&(tr.latitude == current.latitude)){
				if(tr.initial_id >0)
				{
					temp1 = nodes.get(tr.initial_id - 1);
					if(!closedlist.contains(temp1)){
						adj.add(temp1);
					}
				}
				if(tr.initial_id < nodes.size()-1)
				{
					temp2 = nodes.get(tr.initial_id + 1);
					if(!closedlist.contains(temp2)){
						adj.add(temp2);
					}
				}
			}
		
		}
		return adj;
	}
	private static LinkedList<TreeNode> calcpath(TreeNode start, TreeNode end)
	{
		        LinkedList<TreeNode> path = new LinkedList<TreeNode>();

		        TreeNode curr = end;
		        boolean done = false;
		        while (!done) {
		            path.addFirst(curr);
		            curr = (TreeNode) curr.getFather();

		            if (curr.equals(start)) {
		                done = true;
		            }
		        }
		        return path;
	}
	private static double distance (TreeNode node1, TreeNode node2){
		return (Math.sqrt(Math.pow(Math.abs(node1.longitude - node2.longitude), 2) + Math.pow(Math.abs(node1.latitude - node2.latitude), 2)));
	}
	private static  TreeNode findclosest(ArrayList<TreeNode> Nodes, TreeNode taxi){
		TreeApp gb = new TreeApp();
		TreeNode closest = gb.new TreeNode();
		closest = Nodes.get(0);
		double cldist = distance(closest,taxi);
		double temp;
		for(TreeNode tn: Nodes){
			if((temp=distance(tn,taxi)) < cldist){
				closest = tn;
				cldist = temp;
			}
		}
		return closest;
	}
	@SuppressWarnings("unused")
	private static LinkedList<TreeNode> Astar (ArrayList<TreeNode> nodes, TreeNode taxi, TreeNode myclient){
		LinkedList<TreeNode> openlist = new LinkedList<TreeNode>();
		LinkedList<TreeNode> closedlist = new LinkedList<TreeNode>();
		boolean done;
		
		taxi = findclosest(nodes, taxi);
		taxi.g_cost = 0;
		System.out.println("Myclient"+"Longtitue:" + myclient.longitude+" "+"Latitude:"+myclient.latitude+"\n");
		myclient = findclosest(nodes,myclient);
		System.out.println("Closest: Longtitue:" + myclient.longitude+" "+"Latitude:"+myclient.latitude+"\n");
		openlist.add(taxi);
		
		done = false;
		TreeNode current ;
		while(!done) {
			current = lowestFInOpen(openlist,myclient);
			closedlist.add(current);
			openlist.remove(current);
			
			if((current.getLongtitude()== myclient.getLongtitude())&&(current.getLatitude()==myclient.getLatitude())){
				return calcpath(taxi, current);
			}
			List<TreeNode> adjacentNodes = adjacent(nodes, closedlist, current);
			for (int i = 0; i < adjacentNodes.size(); i++) {
                TreeNode currentAdj = adjacentNodes.get(i);
                if (!openlist.contains(currentAdj)) { // node is not in openList
                    currentAdj.setFather(current); // set current node as previous for this node
                    currentAdj.h_cost = distance(current, myclient);
                    currentAdj.g_cost = current.g_cost + distance(current,currentAdj);
                    // set g costs of this node (costs from start to this node)
                    
                    openlist.add(currentAdj); // add node to openList
                } else { // node is in openList
                    if (currentAdj.g_cost > current.g_cost + distance(current,currentAdj)) { // costs from current node are cheaper than previous costs
                        currentAdj.setFather(current); // set current node as previous for this node
                        currentAdj.g_cost = current.g_cost + distance(current,currentAdj); // set g costs of this node (costs from start to this node)
                    }
                }
            }
			if (openlist.isEmpty()) { // no path exists
                return new LinkedList<TreeNode>(); // return empty list
            }	
		}
		return null;
	}

	public class TreeNode{
		private double longitude;
		private double latitude;
		private int id;
		private int initial_id;
		private TreeNode father;
		private double g_cost;
		private double h_cost;
		private String StreetName;
		
		public TreeNode(double longitude, double latitude, int id) {
			this.latitude = latitude;
			this.longitude = longitude;
			 this.id = id;
			 this.StreetName= "N\\A";
		}
		public TreeNode(double longitude, double latitude, int id, String StreetName) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.id = id;
			this.StreetName = StreetName;
		}
		public TreeNode()
		{
			
		}
		public double getLongtitude() {
			return this.longitude;
		}
		public double getLatitude() {
			return this.latitude;
		}
		public void setLongtitude(double longitude) {
			this.longitude = longitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public int getID(){
			return this.id;
		}
		public void setId(int id){
			this.id = id;
		}
		public String getStreetName(){
			return this.StreetName;
		}
		public void setStreetName(String Streetname){
			this.StreetName = Streetname;
		}
		public TreeNode getFather(){
			return this.father;
		}
		public void setFather(TreeNode father){
			this.father = father;
		}
		public void setG_cost(int g_cost)
		{
			this.g_cost = g_cost;
		}
		public double getG_cost(){
			return this.g_cost;
		}
		public void setH_cost(int h_cost)
		{
			this.h_cost = h_cost;
		}
		public double getH_cost(){
			return this.h_cost;
		}
	}
	public class Input{
		@SuppressWarnings("unchecked")
		private <T> ArrayList<T> ReadFile(String path, ArrayList<T> lst, Class<?> cls){
			try(BufferedReader br = new BufferedReader(new FileReader(path))) {
				String line;
				br.readLine();
				while ((line=br.readLine()) != null) {
					String[] data = line.split(",");
					if (cls == TreeNode.class ){
						if(data.length == 4){
							TreeNode inp = new TreeNode(Double.parseDouble(data[0]), Double.parseDouble(data[1]), Integer.parseInt(data[2]), data[3]);
							inp.initial_id = lst.size();
							//System.out.println("ID="+lst.size());
							lst.add((T) inp);
						}
						else{
							TreeNode inp = new TreeNode(Double.parseDouble(data[0]), Double.parseDouble(data[1]), Integer.parseInt(data[2]));
							inp.initial_id = lst.size();
							lst.add((T) inp);
						}
					}
					else
						throw new IOException();
				}
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return TaxiList;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return TaxiList;
			}
			return (ArrayList<T>) lst;
		}
		private void ReadFile(String path, TreeNode cl){
			try(BufferedReader br = new BufferedReader(new FileReader(path))) {
				String line;
				br.readLine();
				while ((line=br.readLine()) != null) {
					String[] data = line.split(",");
					cl.longitude = Double.parseDouble(data[0]);
					cl.latitude = Double.parseDouble(data[1]);
				}
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return TaxiList;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return TaxiList;
			}
		}
	}
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		String taxis_file = "C:\\Users\\Konstantinos\\Google Drive\\Σχολή\\Ροή Λ\\Τεχνητή Νοημοσύνη\\Project\\taxis.csv";
		String nodes_file = "C:\\Users\\Konstantinos\\Google Drive\\Σχολή\\Ροή Λ\\Τεχνητή Νοημοσύνη\\Project\\nodes.csv";
		String client_file = "C:\\Users\\Konstantinos\\Google Drive\\Σχολή\\Ροή Λ\\Τεχνητή Νοημοσύνη\\Project\\client.csv";
		int flag = 0;
		
		//---------INSTANCES-------------//
		TreeApp gb = new TreeApp();
		ArrayList<TreeNode> Taxis = new ArrayList<TreeNode>();
		ArrayList<TreeNode> Nodes = new ArrayList<TreeNode>();
		TreeNode myclient = gb.new TreeNode();
		
		//---------INPUTS----------------//
		Input io = gb.new Input();
		Taxis = io.ReadFile(taxis_file, Taxis, TreeNode.class);
		Nodes = io.ReadFile(nodes_file, Nodes, TreeNode.class);
		io.ReadFile(client_file, myclient);
		//System.out.println("\n\n Following client position. \n\n");
		//System.out.println("Longtitue:" + myclient.longitude+" "+"Latitude:"+myclient.latitude+"\n");
		//System.out.println("Taxi(2) --> Longtitue:" + Taxis.get(3).longitude+" "+"Latitude:"+Taxis.get(2).latitude+"\nGoing to find closest!\n");
		/*TreeNode closest = gb.new TreeNode();
		TreeNode taxind = gb.new TreeNode();
		taxind.longitude = Taxis.get(0).longitude;
		taxind.latitude = Taxis.get(0).latitude;
		closest = findclosest(Nodes,taxind);
		
		System.out.println("Closest Node is :"+closest.id+" Longtitude:"+closest.longitude+" and Latitude:"+closest.latitude+"\n\n");
		System.out.println("going to find path for last taxi \n\n");*/
		
		LinkedList<TreeNode> path = Astar(Nodes, Taxis.get(0), myclient);
		System.out.println("printing for taxi#0\n");
		for(TreeNode tn: path){
			System.out.println(tn.longitude+","+tn.latitude);
		}
		//ArrayList<LinkedList<TreeNode>> final_path= new ArrayList<LinkedList<TreeNode>>();
		
		@SuppressWarnings("unused")
		LinkedList<TreeNode> final_path = new LinkedList<TreeNode>();
		LinkedList<TreeNode> temp;
		double cost = path.get(path.size()-1).g_cost;
		for(int i =1 ; i< Taxis.size(); i++)
		{
			temp = Astar(Nodes,Taxis.get(i),myclient);
			System.out.println("printing for taxi#"+i);
			for(TreeNode tn: temp){
				System.out.println(tn.longitude+","+tn.latitude);
			}
			if(temp.get(temp.size()-1).g_cost < cost)
			{
				cost  = temp.get(temp.size()-1).g_cost;
				final_path= temp;
			}
			//System.out.println("Finished checking Taxi#"+i);
		}
		System.out.println("\n\nLowest cost:"+cost);
		for(TreeNode tn: final_path){
			System.out.println(tn.longitude+","+tn.latitude);
		}
	}
	
}