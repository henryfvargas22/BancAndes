/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static com.google.common.collect.ComparisonChain.start;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
//import sun.org.mozilla.javascript.internal.json.JsonParser;
import com.google.gson.JsonParser;



/**
 *
 * @author jm.rodriguez11
 */
public class Conector extends Thread {
	public final static String NOMBRE = "BANCANDES";

	/**
	 * La direccion del servidor
	 */
	public final static String HOST = "172.24.99.232";

	/**
	 * El puerto a la conexion de pregunta
	 */

	public final static int PUERTO_PREGUNTA = 12345;   // auxiliar

	/**
	 * El puerto a la conexion de respuesta
	 */
	public final static int PUERTO_RESPUESTA = 12346;

	/**
	 * El socket de la conexion
	 */
	private Socket socketPregunta;

	/**
	 * El escritor de la conexion
	 */
	private PrintWriter outPregunta;

	/**
	 * El lector de la linea ingresada
	 */
	private BufferedReader inPregunta;

	/**
	 * El lector del sistema 
	 */
	private BufferedReader stdInPregunta;

	//-----------------------------------------------------------

	/**
	 * El socket de la conexion
	 */
	private Socket socketRespuesta;

	/**
	 * El escritor de la conexion
	 */
	private PrintWriter outRespuesta;

	/**
	 * El lector de la linea ingresada
	 */
	private BufferedReader inRespuesta;

	/**
	 * El lector del sistema 
	 */
	private BufferedReader stdInRespuesta;

	/**
	 * La instancia del conector
	 */
	private static Conector instancia = null;

	/**
	 * Las clases que escuchan el evento
	 */
	private List _listeners = new ArrayList();

	//-----------------------------------------------------------
	// CONSTRUCTOR
	//-----------------------------------------------------------

	/**
	 * Construye un nuevo conector con dos conexion stateless y dos activas
	 * @param params Los parametros del conector
	 * @throws IOException En caso de error
	 * <pos> Las dos conexiones stateless se han podido manejar
	 */
	private Conector() throws Exception {
		System.out.println("=============================INICIANDO CONECTOR===================================");
		openConnectionPregunta();

		outPregunta.write("REGISTRAR-" + NOMBRE);
		outPregunta.flush();
		String resp = inPregunta.readLine();
		String[] resps = resp.split("-");
		if(resps[0].equals("TODO OK")){
			System.out.println("Conexion y registro con la cola de pregunta fue exitosa");
		}else{
			closeConnectionPregunta();
			throw new Exception("No se pudo establecer la conexion con la cola pregunta");
		}

		closeConnectionPregunta();

		//Registrar con respuesta

		openConnectionRespuesta();

		outRespuesta.write("REGISTRAR-" + NOMBRE);
		outRespuesta.flush();
		String resp1 = inRespuesta.readLine();
		String[] resps1 = resp1.split("-");
		if(resps1[0].equals("TODO OK")){
			System.out.println("Conexion y registro con la cola de respuesta fue exitosa");
		}else{
			closeConnectionRespuesta();
			throw new Exception("No se pudo establecer la conexion con la cola respuesta");
		}

		closeConnectionRespuesta();

		start();
	}

	public static Conector getInstance() throws Exception{
		if(instancia == null)
			instancia = new Conector();
		return instancia;
	}


	/**
	 * Inicia la conexion con el socket de pregunta
	 */
	private void openConnectionPregunta(){
		try {

			socketPregunta = new Socket(HOST, PUERTO_PREGUNTA);

			outPregunta = new PrintWriter(socketPregunta.getOutputStream(), true);

			inPregunta = new BufferedReader(new InputStreamReader(
					socketPregunta.getInputStream()));    

			stdInPregunta = new BufferedReader(new InputStreamReader(System.in));


		} catch (UnknownHostException e) {
			System.err.println("Unknown Host.");

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't get I/O for "
					+ "the connection.");
		}
	}

	/**
	 * Cierra las conexiones con el socket de respuesta
	 * @throws IOException
	 */
	private void closeConnectionRespuesta() throws IOException{
		outRespuesta.close();
		inRespuesta.close();
		socketRespuesta.close();
		stdInRespuesta.close();
	}

	/**
	 * Inicia la conexion con el socket de pregunta
	 */
	private void openConnectionRespuesta(){
		try {

			socketRespuesta = new Socket(HOST, PUERTO_RESPUESTA);

			outRespuesta = new PrintWriter(socketRespuesta.getOutputStream(), true);

			inRespuesta = new BufferedReader(new InputStreamReader(
					socketRespuesta.getInputStream()));    

			stdInRespuesta = new BufferedReader(new InputStreamReader(System.in));

		} catch (UnknownHostException e) {
			System.err.println("Unknown Host.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't get I/O for "
					+ "the connection.");
		}
	}

	/**
	 * Cierra las conexiones con el socket de pregunta
	 * @throws IOException
	 */
	private void closeConnectionPregunta() throws IOException{
		outPregunta.close();
		inPregunta.close();
		socketPregunta.close();
		stdInPregunta.close();
	}

	/**
	 * Llamado para terminar todas las conexiones activas que tienen todos los sockets
	 * @throws IOException 
	 */
	public void terminateAll() throws IOException{
		socketPregunta.close();
		outPregunta.close();
		inPregunta.close();
		stdInPregunta.close();

		System.out.println("Conexion a socket pregunta cerrada -stateless-");

		socketRespuesta.close();
		outRespuesta.close();
		inRespuesta.close();
		stdInRespuesta.close(); 

		System.out.println("Conexion a socket respuesta cerrada -stateless-");

		instancia.stop();
	}

	public void run(){
		System.out.println("======================================================================");
		System.out.println("Monitoreando preguntas y respuestas");
		System.out.println("======================================================================");

		while(true)
		{
			try 
			{
				openConnectionPregunta();
				outPregunta.write("PEDIR");
				outPregunta.flush();
				String preg = inPregunta.readLine();
				String[] params = preg.split("-");
				if(params[0].equals("ERROR"))
				{
					System.out.println("No hay preguntas");
				}
				else
				{
					System.out.println("Pregunta recibida: " + params[0]);
					//Atender pregunta!!

					String par = params[0];

					JsonParser jsonParser;
					jsonParser = new JsonParser();
					JsonObject fullJson;
					fullJson = jsonParser.parse(par).getAsJsonObject();

					String tipo = fullJson.get("method").getAsString();



					if(tipo.equals("Top20"))
					{
						System.out.println("Entre a "+tipo);
						//this.retornarTop20(fullJson.get("inicial").getAsString(), fullJson.get("fin").getAsString());
					}
					else if(tipo.equals("darValores"))
					{

						System.out.println("Entre a "+ tipo);
						//this.darConsultaMovimientos(fullJson.get("inicio").getAsString(), fullJson.get("fin").getAsString(),
						//	fullJson.get("start").getAsInt(),fullJson.get("length").getAsInt()  , fullJson.get("columnName").getAsString(),
						//	fullJson.get("tipo").getAsString(), fullJson.get("search").getAsString());
					}
					else if (tipo.equals("darIntermediarios"))
					{
						System.out.println("Entre a "+ tipo);
						//this.darIntermediarios();
					}
					else if(tipo.equals("retirar"))
					{
						System.out.println("Entre a "+ tipo);
						//boolean b =	this.eliminarIntermediario(fullJson.get("id").getAsString());

						JsonObject json = new JsonObject();


						//json.addProperty("resultado", b);


						Gson gson = new GsonBuilder().create();

						String j = gson.toJson(json);


						//this.enviarRespuesta(j);
					}
					else if(tipo.equals("compraVenta"))
					{
						System.out.println("Entre a "+ tipo);
						int num = fullJson.get("cantidad").getAsInt();

						//this.comprarVender( fullJson.get("id").getAsString(),num);
					}


				}
				closeConnectionPregunta();

			} 
			catch (Exception e) 
			{
				try 
				{
					closeConnectionPregunta();
				} 
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			System.out.println("Pidiendo respuestas");

			try 
			{
				openConnectionRespuesta();
				outRespuesta.write("PEDIR");
				outRespuesta.flush();
				String preg = inRespuesta.readLine();
				String[] params = preg.split("-");
				if(params[0].equals("ERROR"))
				{
					//System.out.println("No hay preguntas");
				}
				else
				{
					//Atender respuesta!!
					System.out.println("Respuesta: "+ params[0]);
					//enviarRespuesta("For trying to reach the things that i cant see");
					//fireEvent(params[0]);
				}
				closeConnectionRespuesta();

			} 
			catch (Exception e) 
			{
				try 
				{
					closeConnectionRespuesta();
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	//-----------------------------------------------------------
	// METODOS
	//-----------------------------------------------------------

	public void enviarPregunta(String pregunta) throws IOException{
		openConnectionPregunta();

		outPregunta.write("PUBLICAR-" + pregunta);
		outPregunta.flush();

		closeConnectionPregunta();
	}

	public void enviarRespuesta(String respuesta) throws IOException{


		openConnectionRespuesta();

		outRespuesta.write("PUBLICAR-" + respuesta);
		outRespuesta.flush();

		closeConnectionRespuesta();
	}

}
