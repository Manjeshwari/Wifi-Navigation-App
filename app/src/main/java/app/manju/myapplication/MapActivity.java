package app.manju.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;




public class MapActivity extends AppCompatActivity {

    EditText Source;
    EditText Destination;
    Button Map;
    Button Cancel;
    TextView mapView, seekBarTextView;
    SeekBar seekBar;
    //ProgressBar progressBar;
    int progress;
    Context mContext;


    static List<String> adjacentRoomList = new ArrayList<>();
    static LinkedList<Node> stack;
    static LinkedList<String> nearestneighbor;
    static String output = "";
    /**
     * Node --- class to store each vertex along with its adjacent vertices
     */
    static class Node{
        Node myparentNode;
        private String id;
        private LinkedList<Node> adjacent;

        public Node(String id){
            this.id = id;
            adjacent = new LinkedList<Node>();
            myparentNode=null;
        }

        //Getter method for start vertex
        public String getId(){
            return id;
        }

        //Getter method for end vertex
        public LinkedList<Node> getAdjacent(){
            return adjacent;
        }

        //add node to the adajcent list
        public void addAdjacent(Node vertex){
            adjacent.add(vertex);
        }

        //To print Node
        public String toString(){
            String msg = id + " ";
            for(Node node: adjacent)
                msg = msg  + node.id+" ";
            return msg;
        }


    }

    static String parentidString="";
    /**
     * Create and get node corresponding to a vertex.
     * @param HashMap<String, Node> graph
     * @param id
     * @return Node object.
     */
    private static Node getNode(HashMap<String, Node> graph, String id){
        if(graph.containsKey(id))
            return graph.get(id);
        else{
            Node node = new Node(id);
            graph.put(id, node);
            return node;
        }
    }

    /**
     * Adds edge between source and destination.
     * @param HashMap<String, Node> graph
     * @param source start index
     * @param destinations end index
     * @return No return value.
     */
    public static void add(HashMap<String, Node> graph, String source, String destination){

        //Get nodes corresponding to source and destination vertices.
        Node s = getNode(graph, source);
        Node d = getNode(graph, destination);

        //add nodes to adjacent list
        s.addAdjacent(d);
        d.addAdjacent(s);
    }

    /**
     * Creates a HashMap with string key and Node value
     * @param input list of edges
     * @return HashMap<String,Node>
     */
    public static HashMap<String, Node> createGraph(List<String> adjacentRoomList){
        HashMap<String, Node> graph = new HashMap<String, Node>();
        //Node node;
        for(String s: adjacentRoomList){
            List<String>Room= Arrays.asList(s.split(" "));
            //System.out.println("This is what i want to see: "+Room);
            String first= Room.get(0);
            String second= Room.get(1);
            add(graph, first, second);
        }
        return graph;
    }

    public static void printGraph(HashMap<String,Node> graph){
        HashSet<String> key = new HashSet<String>(graph.keySet());
        for(String e: key)
            System.out.println("node "+e+" connects to "+graph.get(e));
    }
    /**
     * pathExists recursive method to find path between source and destination
     * @param HashMap<String, Node> graph
     * @param source start node
     * @param destinations end node
     * @return true or false
     */

    public static boolean pathExists(HashMap<String, Node> graph, String source, String destination){

        //To store the children of nodes visited
        LinkedList<Node> queue = new LinkedList<Node>();

        //To store the visited nodes
        //HashSet<String> visited = new HashSet<String>();
        LinkedList<String> visited = new LinkedList<>();



        nearestneighbor= new LinkedList<>();

        //to store the adjacent rooms leading to the path:
        stack = new LinkedList<>();

        //adding node of startId in the linkedlist
        System.out.println("Source: "+source);
        queue.add(getNode(graph, source));
        System.out.println("Queue: "+queue);

        parentidString+=source+"--->";

        while(!queue.isEmpty()){

            Node parent = queue.remove();
            System.out.println("Parent-->"+parent);
            System.out.println("parentId "+parent.getId());
            //stack.push(parent);
            //parentidString+=parent.getId();
            String[] parentlist= parent.toString().split(" ");
            String parents= Arrays.toString(parentlist);
            //  System.out.println("Parents: "+parents);




            if(parent.getId().equals(destination)) {

                Node current=parent;
                while(current!=null)
                {
                    //	System.out.println(current.getId());
                    stack.push(current);
                    current=current.myparentNode;

                    do
                    {
                        Node currentparent=stack.pop();
                        System.out.println("CurrentRoom: "+currentparent.getId());
                        //String output = " ";
                        output+= currentparent.getId()+ " <--- ";

//				current=stack.pop();
                    }while(!stack.isEmpty());
                }
                return true;
            }

            visited.add(parent.getId());

            for(Node children: parent.getAdjacent()) {
                if(visited.contains(children.getId())) {

                    continue;
                }
                children.myparentNode=parent;
                queue.add(children);
                String[] childString= children.toString().split(" ");

                String childList= Arrays.toString(childString);
                //System.out.println("Children : "+childList);
                //System.out.println("Contains Destination: "+childList.contains(destination));
                if(childList.contains(destination)==true) {
                    parentidString+=parent.getId()+"--->";

                    nearestneighbor.push(parent.getId());


                } else{
                    //System.out.println("No Appropriate Map found. Sorry for the inconvenience caused");
                    //output="No Appropriate Map found. Sorry for the inconvenience caused!!!";
                }



            }


        }
        return false;
    }
    /**
     * Print HashMap
     * @param HashMap<String, Node> graph
     * @return no return value
     */


    public void distanceCalculation(){

        List<String> dBKeys= new ArrayList(DatabaseActivity.roomdatabasedict.keySet());
        System.out.println("dbkeys:::::"+dBKeys);

        //##############for each room take the individual router and signal values.##########################


        for(int i=0;i<dBKeys.size();i++) {

            HashMap<String, List<String>> roomkeyFrom = DatabaseActivity.roomdatabasedict.get(dBKeys.get(i));

            List<String>roomrouterKeyFrom = new ArrayList(roomkeyFrom.keySet());

            double newDistance;
            double oldDistance=0.0;

            for(int j=0;j<dBKeys.size();j++) {

                HashMap<String, List<String>> roomkeyTo = DatabaseActivity.roomdatabasedict.get(dBKeys.get(j));

                double distance=0;


                for(int k=0;k<roomrouterKeyFrom.size();k++) {


                    if(roomkeyTo.containsKey(roomrouterKeyFrom.get(k))) {

                        List<String> FromSignalValue = roomkeyFrom.get(roomrouterKeyFrom.get(k));
                        double FromSignal = Double.parseDouble(FromSignalValue.get(0));
                        //System.out.println("Roomrouterkey" + roomrouterKeyFrom.get(k));
                        List<String> ToSignalValue = roomkeyTo.get(roomrouterKeyFrom.get(k));

                        double ToSignal = Double.parseDouble(ToSignalValue.get(0));

                        distance += Math.pow((FromSignal - ToSignal), 2);
                    }
                    //System.out.println("Distance from "+roomkeyFrom+" to "+roomkeyTo+" is " +distance);

                }
                System.out.println("Distance from "+dBKeys.get(i)+" to "+dBKeys.get(j)+" is " +distance);

                if(distance==0.0) {

                    //System.out.println("CurrentRoom: "+ dBKeys.get(i));
                }else {

                    newDistance=distance;
                    //System.out.println("NewDistance: "+newDistance);

                    if(oldDistance==0.0) {
                        oldDistance=newDistance;

                    }else if (oldDistance>newDistance)


                    {
                        oldDistance=newDistance;

                    }

                    //System.out.println("Smallest distance is "+oldDistance);
                }

                System.out.println("This is the progress value"+ progress);

                if(distance<7000 && distance!=0.0) {



                    // System.out.println("Room "+dBKeys.get(i)+" is connected to "+dBKeys.get(j));

                    //String adjacentRoomString = dBKeys.get(i)+" "+dBKeys.get(j);
                    adjacentRoomList.add(dBKeys.get(i)+" "+dBKeys.get(j));


                    //System.out.println(Arrays.toString(input));
                }

            }

            // System.out.println("Smallest distance is "+oldDistance);

        }
        System.out.println(adjacentRoomList);


    }
    private void bindViews() {
        seekBar =(SeekBar) findViewById(R.id.seekBar);
        seekBarTextView =(TextView) findViewById(R.id.seekBarTextView);
        //progressBar=(ProgressBar) findViewById(R.id.progressBar);
        seekBar.setMax(15000);
        seekBar.setProgress(1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                seekBarTextView.setText(" " + progress + "  / 15000 ");




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                Toast.makeText(mContext, "Touch SeekBar", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Toast.makeText(mContext, "let go SeekBar", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Source = (EditText) findViewById(R.id.sourceText);
        Source.setText(GuessActivity.ClosestRoom);
        String source = getIntent().getExtras().getString("Source");
        Destination = (EditText) findViewById(R.id.destText);
        Map = (Button) findViewById(R.id.mapButton);
        Cancel=(Button) findViewById(R.id.cancel);
        mapView = (TextView) findViewById(R.id.MapView);
        mContext=MapActivity.this;
        //seekBar =(SeekBar) findViewById(R.id.seekBar);
        //seekBarTextView =(TextView) findViewById(R.id.seekBarTextView);

        //progressBar=(ProgressBar) findViewById(R.id.progressBar);
        //seekBar.setMax(1000);
        //seekBar.setProgress(100);
        //seekBar.animate();

        bindViews();
        distanceCalculation();
        System.out.println("-----------------------------------");
        //seekBar functionality






        /************Calculate the path on the click of the map button **************/


        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create Graph
                HashMap<String, Node> graph = createGraph(adjacentRoomList);
                //Print Graph
                System.out.println("the graph:");
                printGraph(graph);

                pathExists(graph,Source.getText().toString(),Destination.getText().toString());


                mapView.setText(output);
                System.out.println(output);
                output="";


            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.setText(" ");
                Source.setText("");
                Destination.setText("");
            }
        });

    }



}
