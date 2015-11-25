package dao;

import java.sql.Connection;
import java.util.ArrayList;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class MensajesDao 
{
	private static final long timeout=5000;

	/**
	 * Datasource fuente de datos
	 */
	private DataSource ds1;	

	/**
	 * Conexion para la base de datos 1
	 */
	private Connection conDB;

	/**
	 * Conexion a la cola de mensajes
	 */

	private javax.jms.Connection conJMSXA;

	/**
	 * Contexto inicial
	 */
	private InitialContext context;

	/**
	 * Fabrica de conexiones para el envio de mensajes a la cola
	 */
	private ConnectionFactory cf;

	/**
	 * Cola definida para la recepcion de mensajes para el usuario que es servidor
	 */
	private Queue colaA;

	/**
	 * Cola definida para la recepcion de mensajes para el usuario que es servidor
	 */
	private Queue colaB;

	/**
	 * Cola definida para las respuestas por parte de los componentes
	 */
	private Queue colaR;

	/**
	 * Thread para recibir mensajes
	 */
	//private ThreadRecibirMensaje threadRecibirMensaje;

	private ArrayList<Double> idsmensajesActivos;

	public MensajesDao() 
	{
		idsmensajesActivos=new ArrayList<Double>();
		try 
		{	
			context = new InitialContext();

			// Inicializa los datasource por JNDI.
			ds1 = (DataSource) context.lookup("java:jboss/datasources/XADataSource2");

			//Inicializa la fabrica de conexiones JMS.
			cf = (ConnectionFactory) context.lookup("java:/JmsXA");
			conJMSXA = cf.createConnection();
			colaA = (Queue) context.lookup("queue/ColaA");
			colaB = (Queue) context.lookup("queue/ColaB");		
			colaR = (Queue) context.lookup("queue/ColaR");
			conJMSXA = cf.createConnection();
			System.out.println("Todo OK ");

			//threadRecibirMensaje = new ThreadRecibirMensaje( this );
			//threadRecibirMensaje.start( );	

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
	}

	public void enviarRespuesta(Double idReq, Object o, String mensaje )
	{
		UserTransaction utx;
		try 
		{
			utx = (UserTransaction) context.lookup("java:jboss/UserTransaction");
			conJMSXA = cf.createConnection();
			try 
			{
				//Inicializar conexion
				conDB = ds1.getConnection();

				Session s = conJMSXA.createSession(false, Session.AUTO_ACKNOWLEDGE);
				utx.begin();			

				MessageProducer producer = s.createProducer(colaA);
				Message msg = s.createMessage();

				msg.setStringProperty("MENSAJE", mensaje);
				msg.setDoubleProperty("REQUEST_ID", idReq);
				msg.setObjectProperty("DATOS", o);
				producer.send(msg);

				System.out.println("Se envio el mensaje " + msg + "a la cola " + producer.getDestination().toString());

				utx.commit();

				System.out.println("Se realizo commit ");

				//Cerrar conexion
				conDB.close();
				conJMSXA.close();

			}
			catch (Exception e) 
			{					
				e.printStackTrace();
				try 
				{
					utx.rollback();
				} 
				catch (IllegalStateException e1) 
				{
					e1.printStackTrace();
				} 
				catch (SecurityException e1) 
				{
					e1.printStackTrace();
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public void recibirMensaje( )
	{
		UserTransaction utx;
		try
		{
			conJMSXA = cf.createConnection();
			utx = (UserTransaction) context.lookup("java:jboss/UserTransaction");

			try 
			{
				System.out.println("Entra a recibir...");

				//Inicializar conexion
				conDB = ds1.getConnection();
				utx.begin();			

				Session s = conJMSXA.createSession(false, Session.AUTO_ACKNOWLEDGE);

				MessageConsumer consumer = s.createConsumer(colaA);

				conJMSXA.start();

				//Se recibe mensaje

				Message msn = consumer.receive();

				String cont = msn.getStringProperty("MENSAJE");
				String req = cont.split("$")[0];

				// evaluar mensaje para ver que hacer, hacer notify , eliminar
				// rnd+1's, poner respuestas
				s.close();

				utx.commit();
				System.out.println("Se realizo commit ");
				// Cerrar conexion
				conDB.close();
				conJMSXA.close();
			}
			catch (Exception e) 
			{					
				e.printStackTrace();
				try 
				{
					utx.rollback();
				} 
				catch (IllegalStateException e1)
				{
					e1.printStackTrace();
				}
				catch (SecurityException e1) 
				{
					e1.printStackTrace();
				} 
				catch (SystemException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		catch (NamingException e1)
		{
			e1.printStackTrace();
		} 
		catch (JMSException e2) 
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
}