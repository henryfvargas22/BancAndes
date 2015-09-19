package fachada;

import java.util.ArrayList;
import java.util.List;

import dao.ConsultaDAO;
import dao.DaoClientes;
import dao.DaoCuentas;
import dao.DaoEmpleados;
import dao.DaoOficinas;
import dao.DaoPrestamos;
import dao.DaoPuntosDeAtencion;
import dao.DaoUsuarios;
import vos.Cliente;
import vos.Cuenta;
import vos.Empleado;
import vos.Oficina;
import vos.Prestamo;
import vos.PuntoDeAtencion;
import vos.Usuario;


public class BancAndes 
{
	/**
	 * Conexión con la clase que maneja la base de datos
	 */
	private DaoUsuarios daoUsuarios;
	
	private DaoClientes daoClientes;
	
	private DaoEmpleados daoEmpleados;
	
	private DaoCuentas daoCuentas;
	
	private DaoOficinas daoOficinas;
	
	private DaoPrestamos daoPrestamos;
	
	private DaoPuntosDeAtencion	daoPuntosDeAtencion;
    
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
		daoClientes= new DaoClientes();
		daoEmpleados= new DaoEmpleados();
		daoCuentas= new DaoCuentas();
		daoOficinas= new DaoOficinas();
		daoPrestamos= new DaoPrestamos();
		daoPuntosDeAtencion= new DaoPuntosDeAtencion();
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

	public ArrayList<Cliente> darClientesDefault() throws Exception
	{
		return daoClientes.darClientesDefault();
	}
	
	public ArrayList<Empleado> darEmpleadosDefault() throws Exception
	{
		return daoEmpleados.darEmpleadosDefault();
	}
	
	public ArrayList<Cuenta> darCuentasDefault() throws Exception
	{
		return daoCuentas.darCuentasDefault();
	}
	
	public ArrayList<Oficina> darOficinasDefault() throws Exception
	{
		return daoOficinas.darOficinasDefault();
	}
	
	public ArrayList<Prestamo> darPrestamosDefault() throws Exception
	{
		return daoPrestamos.darPrestamosDefault();
	}
	
	public ArrayList<PuntoDeAtencion> darPuntosDeAtencionDefault() throws Exception
	{
		return daoPuntosDeAtencion.darPunto_De_AtencionsDefault();
	}
}
