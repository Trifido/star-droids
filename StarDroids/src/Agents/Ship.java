package Agents;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa las acciones comunes a todos los agentes. 
 * 
 * 
 * @author Alberto Meana,Andrés Ortiz
 */
public class Ship extends SingleAgent{
    
    private ACLMessage out = new ACLMessage();
    private ACLMessage in = new ACLMessage();
    
    private JsonObject key, answer, msg;
    private Role role; //role of agent
    
    
   /* 
    * @author Alberto Meana
    */
    public Ship( AgentID id ) throws Exception{
    
        super( id );
        this.out.setSender( this.getAid() );
        
        this.key = new JsonObject();
        this.answer = new JsonObject();
        this.msg = new JsonObject();
        
        this.in = null;
    }
        
   /* 
    * @author Alberto Meana
    */
    private void subscribe(){
    
        //Composición de Json de subscripcion.
        this.msg.add( "world", "map1" );
        
        // Creación del ACL
        this.out.setPerformative( ACLMessage.SUBSCRIBE );
        this.out.setReceiver( new AgentID( "Furud" ) );
        this.out.setContent( msg.toString() );
        
        this.send( out );
        
        this.receiveKey();
        
    }
        
   /* 
    * @author Alberto Meana
    */
    private void receiveKey(){
    
        this.in = null;
        
        try {
            
            this.in = this.receiveACLMessage();
            
        } catch (InterruptedException ex) {
            
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        
        }
        
        if( in.getPerformativeInt() == ACLMessage.INFORM ){
            
            this.key = Json.parse( this.in.getContent() ).asObject();
            
        }else{
        
            System.out.println( "ERROR: " + in.getPerformative() );
        
        }
    
    }
        
   /* 
    * @author Alberto Meana,Andrés Ortiz
    */
    private void register(){
        
        this.msg = new JsonObject();
        this.msg.add( "command", "checkin" );
        this.msg.add( "key" , key.get( "result" ).asString() );
        
        this.out.setPerformative( ACLMessage.REQUEST );
        this.out.setReceiver( new AgentID( "Furud" ) );
        this.out.setContent( this.msg.toString() );
        this.send( this.out );
        
        this.in = null;
        try {
            
            this.in = this.receiveACLMessage();
            
        } catch (InterruptedException ex) {
            
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        if( in.getPerformativeInt() == ACLMessage.INFORM ){
            
            this.answer = Json.parse( this.in.getContent() ).asObject();
            
            switch( this.answer.get( "rol" ).asInt() ){
            
                case( 0 ):
                    System.out.println( this.getName() + " dice: Soy un Xwing (mosca)!" ); 
                    this.role=new XWing();
                    break;
                case( 1 ):
                    System.out.println( this.getName() + " dice: Soy un Ywing pajaro!" ); 
                    this.role=new YWing();
                    break;
                case( 2 ):
                    System.out.println( this.getName() + " dice: Soy un Halcon Milenariooo!" ); 
                    this.role=new MillenniumFalcon();
                    break;
            }
            
        }else{
        
            System.out.println( "ERROR: " + in.getPerformative() );
        
        }
        
    }
    
        
   /* 
    * @author Alberto Meana
    */
    private void cancel(){
        
        this.msg = new JsonObject();
        this.msg.add( "key", this.key.get( "result" ).asString() );
        
        this.out.setContent( this.msg.toString() );
        this.out.setReceiver( new AgentID( "Furud" ) );
        this.out.setPerformative( ACLMessage.CANCEL );
        this.send( this.out );
    
    }
    
        
   /* 
    * @author Alberto Meana
    */
    private void sendACK( String name ){
    
        this.out.setReceiver( new AgentID( name ) );
        this.out.setContent( "ACK" );
        this.out.setPerformative( ACLMessage.INFORM );
        this.send( this.out );
    
    }
    
        
   /* 
    * @author Alberto Meana
    */
    private void receiveACK(){
    
        this.in = null;
        
        try {
            
            this.in = this.receiveACLMessage();
            
        } catch (InterruptedException ex) {
            
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    
    }
    
    private void sendKey( String name ){
        
        this.out.setReceiver( new AgentID( name ) );
        this.out.setContent( this.key.toString() );
        this.out.setPerformative( ACLMessage.INFORM );
        this.send( this.out );
    
    }
    
        
   /* 
    * @author Alberto Meana
    */
    @Override
    public void execute(){
    
        if( this.getName().equals( "rojoLider" )){
        
            this.subscribe();
            
        }else{
        
            this.receiveKey();
        
        }
        
        switch( this.getName() ){
        
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
                this.sendACK( AgentsNames.leaderShip );
                break;
        
        }
        
        this.register();
        
        if( this.getName().equals( AgentsNames.leaderShip )){
        
            this.cancel();
            
        }
    }
}
