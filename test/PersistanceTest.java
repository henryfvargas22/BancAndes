import dao.DaoClientes;
import dao.DaoCuentas;
import dao.DaoEmpleados;
import dao.DaoOficinas;
import dao.DaoOperaciones;
import dao.DaoPrestamos;
import dao.DaoPuntosDeAtencion;
import dao.DaoTrabaja;
import dao.DaoUsuarios;
import junit.framework.TestCase;

import org.junit.*;

public class PersistanceTest extends TestCase
{
	private DaoTrabaja daoTrabaja;
	private DaoOperaciones daoOperaciones;
	private DaoPuntosDeAtencion daoPuntosDeAtencion;
	private DaoClientes daoClientes;
	private DaoCuentas daoCuentas;
	private DaoEmpleados daoEmpleados;
	private DaoOficinas daoOficinas;
	private DaoUsuarios daoUsuarios;
	private DaoPrestamos daoPrestamos;
	
	@Test
	public void unicidadTuplas()
	{
		try
		{
			daoUsuarios.registrarUsuario("nombre", 9000, "usuario", "contrasenia", 30, "male", "ciudad", "direccion", "natural");
			daoUsuarios.registrarUsuario("nombre", 9000, "usuario", "contrasenia", 30, "male", "ciudad", "direccion", "natural");
			fail("Se pudo registrar el usuario");
		}
		catch(Exception e)
		{
			try 
			{
				daoUsuarios.eliminarUsuario(9000);
			} 
			catch (Exception e1) 
			{
				fail("paila");
			}
		}
	}
}
