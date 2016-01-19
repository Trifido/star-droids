package Yoda;

import GUI.YodaPanel;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa a shenron, para nosotros, Yoda.
 *
 * @author Alberto Meana
 */
public class Yoda extends SingleAgent {

    private ACLMessage out = new ACLMessage();
    private ACLMessage in = new ACLMessage();
    private JsonObject msg;
    private YodaPanel yodapanel;
    
    /**
     * Constructor del agente Yoda por defecto
     * 
     * @param id Id del agente
     * @throws Exception 
     */
    public Yoda(AgentID id) throws Exception {
        super(id);
        this.out.setSender(this.getAid());
        this.out.setReceiver(new AgentID("Shenron"));
        this.msg = new JsonObject();
        this.in = null;
        this.yodapanel = new YodaPanel(this);
    }
    
    /**
     * Metodo de ejecucion del agente
     * 
     * @author Alberto Meana
     */
    @Override
    public void execute() {
        this.yodapanel.setVisible(true);
        while(true) {}
    }
    
    /**
     * Método que manda una peticion a shenron de levantar de nuevo el server.
     * 
     * @author Alberto Meana
     */
    public void sendRequest() {
        this.msg.add("user" , "Canmaior");
        this.msg.add("password" , "Ishiguro");
        this.out.setContent(msg.toString());
        this.out.setPerformative(ACLMessage.REQUEST);
        this.send(this.out);
    }
    /**
     * Metodo que manda el query de shenron al server.
     * 
     * @author Alberto Meana
     */
    public void sendQuery() {
        this.msg.add("user" , "Canmaior");
        this.msg.add("password" , "Ishiguro");
        this.out.setContent(msg.toString());
        this.out.setPerformative(ACLMessage.QUERY_REF);
        this.send(this.out);
    }

    /**
     * Método que devuelve lo que manda el servidor y lo enseña en la interfaz.
     * 
     * @author Alberto Meana
     * @return El string conteniendo la cadena del resultado devuelto.
     */
    public String getResult() {
        this.in = null;
        try {
            this.in = this.receiveACLMessage();
        }
        catch(InterruptedException ex) {
            Logger.getLogger(Yoda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return in.getPerformative() + "\n" + in.getContent() ;
    }

}
