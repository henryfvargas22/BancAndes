package fachada;

import java.util.ArrayList;

import dao.ConsultaDAO;
import dao.DaoUsuarios;
import vos.Usuario;


public class BancAndes 
{
	/**
	 * Conexión con la clase que maneja la base de datos
	 */
	private DaoUsuarios daoUsuarios;
	
    
    // -----------------------------------------------------------------
    // Singleton
    // -----------------------------------------------------------------


    /**
     * Instancia única de la clase
     */
    private static BancAndes instancia;
    
    /**
     * Devuelve la instancia única de la clase
     * @return Instancia única de la clase
     */
    public static BancAndes darInstancia( )
    {
        if( instancia == null )
        {
            instancia = new BancAndes( );
        }
        return instancia;
    }
	
	/**
	 * contructor de la clase. Inicializa el atributo dao.
	 */
	private BancAndes()
	{
		daoUsuarios = new DaoUsuarios();
	}
	
	
    // ---------------------------------------------------
    // Métodos asociados a los casos de uso: Consulta
    // ---------------------------------------------------
    
	/**
	 * método que retorna los videos en orden alfabético.
	 * invoca al DAO para obtener los resultados.
	 * @return ArrayList lista con los videos ordenados alfabeticamente.
	 * @throws Exception pasa la excepción generada por el DAO
	 */
	public ArrayList<Usuario> darUsuariosDefault() throws Exception
	{
	    return daoUsuarios.darUsuariosDefault();
	}
	
}
