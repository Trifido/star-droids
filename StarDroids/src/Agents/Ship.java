package Agents;

import helpers.Pair;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;

/**
 * Clase que implementa las acciones comunes a todos los agentes.
 *
 *
 * @author Alberto Meana, Andrés Ortiz, Alba Ríos
 */
public class Ship extends SingleAgent {

    private ACLMessage out = new ACLMessage();
    private ACLMessage in = new ACLMessage();

    private JsonObject key, answer, msg;
    private Role role; //role of agent

    private AgentID nextAgent;
    private Token token;
    
    //private Integer[][] worldMap;
    //private int fuelLevel;
    //private Pair<Integer,Integer> myPosition; //my current position
    //private Pair<Integer,Integer>[] dronesPosition; //current position of other drones

    /*
     * @author Alberto Meana,Andrés Ortiz,Alba Ríos
     */
    public Ship(AgentID id, AgentID nextId) throws Exception {
        super(id);
        this.out.setSender(this.getAid());
        this.key = new JsonObject();
        this.answer = new JsonObject();
        this.msg = new JsonObject();
        this.in = null;
        this.nextAgent = nextId;
        
        //this.fuelLevel = 100;
        //this.myPosition = new Pair(-1,-1);
        //this.dronesPosition = new Pair[3];
        /*for (int i = 0; i < 3 ; i++)
            this.dronesPosition[i] = new Pair(-1,-1);
        this.worldMap = new Integer[500][500];
        for(int i = 0; i < 500; i++)
            for(int j = 0; j < 500; j++)
                this.worldMap[i][j] = -1; //unknown 
        */
    }

    /*
     * @author Alberto Meana
     */
    private void subscribe() {
        //Composición de Json de subscripcion.
        this.msg.add("world", "map1");
        // Creación del ACL
        this.out.setPerformative(ACLMessage.SUBSCRIBE);
        this.out.setReceiver(new AgentID("Furud"));
        this.out.setContent(msg.toString());
        this.send(out);
        this.receiveKey();
    }

    /*
     * @author Alberto Meana
     */
    private void receiveKey() {
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        }
        catch(InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(in.getPerformativeInt() == ACLMessage.INFORM) {
            this.key = Json.parse(this.in.getContent()).asObject();
        }
        else {
            System.out.println("ERROR: " + in.getPerformative());
        }
    }

    /*
     * @author Alberto Meana,Andrés Ortiz
     */
    private void register() {
        this.msg = new JsonObject();
        this.msg.add("command", "checkin");
        this.msg.add("key", key.get("result").asString());
        this.out.setPerformative(ACLMessage.REQUEST);
        this.out.setReceiver(new AgentID("Furud"));
        this.out.setContent(this.msg.toString());
        this.send(this.out);
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        }
        catch(InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(in.getPerformativeInt() == ACLMessage.INFORM) {
            this.answer = Json.parse(this.in.getContent()).asObject();
            switch(this.answer.get("rol").asInt()) {
            case(0):
                System.out.println(this.getName() + " dice: Soy un Xwing (mosca)!");
                this.role = new XWing();
                break;
            case(1):
                System.out.println(this.getName() + " dice: Soy un Ywing pajaro!");
                this.role = new YWing();
                break;
            case(2):
                System.out.println(this.getName() + " dice: Soy un Halcon Milenariooo!");
                this.role = new MillenniumFalcon();
                break;
            }
        }
        else {
            System.out.println("ERROR: " + in.getPerformative());
        }
    }

    /**
     * @author Alberto Meana
     */
    private void cancel() {
        System.out.println( "Cancel sent" );
        this.msg = new JsonObject();
        this.msg.add("key", this.key.get("result").asString());
        this.out.setContent(this.msg.toString());
        this.out.setReceiver(new AgentID("Furud"));
        this.out.setPerformative(ACLMessage.CANCEL);
        this.send(this.out);
        // Test del jdialog de ganar.
        // WinnerDialog winrar = new WinnerDialog( new Frame(), true );
    }

    /*
     * @author Alberto Meana,Andrés Ortiz
     */
    private void sendACK(AgentID id) {
        System.out.println( "I'm " + this.getName() + " sending ACK to " + id.getLocalName() );
        this.out.setReceiver(id);
        this.out.setContent("ACK");
        this.out.setPerformative(ACLMessage.INFORM);
        this.send(this.out);
    }

    /*
     * @author Alberto Meana
     */
    private void receiveACK() {
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        }
        catch(InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * clase que abstrae el envio de la key entre agentes
     *
     *
     * @param name Nombre de a quien se envia la key
     * @author Alberto Meana
     */
    private void sendKey(String name) {
        this.sendKey(new AgentID(name));
        
    }
    private void sendKey(AgentID id){
         this.out.setReceiver(id);
        this.out.setContent(this.key.toString());
        this.out.setPerformative(ACLMessage.INFORM);
        this.send(this.out);
    }

    /**
     *
     * @autor Rafael Ruiz
     *
     * @throws InterruptedException
     */
    //COMMENTED UNTIL WORKING
    public void receiveMessage() throws InterruptedException{
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        }
        catch(InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.out.println("Datos " + in.getContent());
        
        if(in.getPerformativeInt() == ACLMessage.INFORM && !in.getContent().equals("ACK")) // DANGER DANGER ACK RARO!!
        {
            System.out.println("PERFORMATIVA " + in.getPerformative());
            this.role.fillSensors(in, role);
        
        }
        else if(in.getPerformativeInt() == ACLMessage.NOT_UNDERSTOOD) {
            System.out.println("PERFORMATIVA " + in.getPerformative());
        }
        else if(in.getPerformativeInt() == ACLMessage.FAILURE) {
            System.out.println("PERFORMATIVA " + in.getPerformative());
        }
        else if(in.getPerformativeInt() == ACLMessage.REFUSE) {
            System.out.println("PERFORMATIVA " + in.getPerformative());
        }
        
    }

    /*
    * @author Alberto Meana
    */
    @Override
    public void execute() {
        if(this.getName().equals(AgentsNames.leaderShip)) {
            this.subscribe();
        }
        else {
            this.receiveKey();
        }
        /* switch( this.getName() ){

         case( AgentsNames.leaderShip ):
         this.sendKey( AgentsNames.ship2 );
         this.receiveACK();
         break;
         case( AgentsNames.ship2 ):
         this.sendKey( AgentsNames.ship3 );
         break;
         case( AgentsNames.ship3 ):
         this.sendKey( AgentsNames.ship4 );
         break;
         case( AgentsNames.ship4 ):
         this.sendACK( AgentsNames.leaderShip);
         break;*/
         if(this.nextAgent.getLocalName().equals( "rojoLider") )
            this.sendACK(this.nextAgent); //same as before, but with store nextAgent
         else{
             this.sendKey(this.nextAgent);
             this.sendACK(this.nextAgent);
         }
        this.register();
        //Enviamos algo de prueba
        /*this.msg = new JsonObject();
        this.msg.add("key", key.get("result").asString());
        this.out.setPerformative(ACLMessage.QUERY_REF);
        this.out.setReceiver(new AgentID("Furud"));
        this.out.setContent(this.msg.toString());
        this.send(this.out);*/

        
        try {
            
            this.receiveMessage();
        }
        catch(InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // Testing lo sensores y envio de mensajes!!!
        this.sendMessage(ActionsEnum.information);
        
        try {
            
            this.receiveMessage();
        }
        catch(InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(this.getName().equals(AgentsNames.leaderShip)) {
            
            this.cancel();
        }
    }
    
    
    
    /*
     * @author Andrés Ortiz
     */
    protected void sendToken() {
        ACLMessage out = new ACLMessage();
        out.setReceiver(nextAgent);
        this.out.setContent(this.token.toJson().toString()); //this should be token
        this.out.setPerformative(ACLMessage.INFORM);
        System.out.println(this.getName() + " send token");
        this.send(out);
    }
    
    /*
     * @author Andrés Ortiz
     */
    protected void waitToken() throws InterruptedException {
        ACLMessage in = new ACLMessage();
        this.in = this.receiveACLMessage();
        if(in.getPerformativeInt() == ACLMessage.INFORM) {
            this.token = new Token(Json.parse(in.getContent()).asObject());
            System.out.println(this.getName() + " received token");
        }
    }
    /**
     * @author Andrés Ortiz
     */
    private void executeAction(){
        ActionsEnum action=role.getAction();
        //execute(action)
    }
    
    
    /**
     * @author Rafael Ruiz
     * 
     * @param msg 
     */
    public void sendMessage(ActionsEnum action)
    {
        if(action.equals(ActionsEnum.battery))
        {
            this.msg = new JsonObject();
            this.msg.add("command", "refuel");
            this.msg.add("key", key.get("result").asString());
            this.out.setPerformative(ACLMessage.REQUEST);
            this.out.setReceiver(new AgentID("Furud"));
            this.out.setContent(this.msg.toString());
            this.send(this.out);
            
        }else if(action == ActionsEnum.information)
        {
            this.msg = new JsonObject();
            this.msg.add("key", key.get("result").asString());
            this.out.setPerformative(ACLMessage.QUERY_REF);
            this.out.setReceiver(new AgentID("Furud"));
            this.out.setContent(this.msg.toString());
            this.send(this.out);
            
        }else
        {
            
            this.msg = new JsonObject();
            this.msg.add("command", msg);
            this.msg.add("key", key.get("result").asString());
            this.out.setPerformative(ACLMessage.REQUEST);
            this.out.setReceiver(new AgentID("Furud"));
            this.out.setContent(this.msg.toString());
            this.send(this.out);
        }
    }
    
}
