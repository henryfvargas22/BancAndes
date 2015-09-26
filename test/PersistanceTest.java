import fachada.BancAndes;
import static org.junit.Assert.*;

import org.junit.*;

public class PersistanceTest
{
	private BancAndes fachada;
	
	@Test
	public void unicidadTuplas()
	{
		try
		{
			fachada.darInstancia().insertarUsuario("nombre", 9000, "usuario", "contrasenia", 30, "male", "ciudad", "direccion", "natural", null, false, 0);
			fachada.darInstancia().insertarUsuario("nombre", 9000, "usuario", "contrasenia", 30, "male", "ciudad", "direccion", "natural", null, false, 0);
			fail("Tupla duplicada");
		}
		catch(Exception e)
		{
			try 
			{
				fachada.darInstancia().eliminarUsuario(true, 9000);
			} 
			catch (Exception e1) 
			{
				fail("paila");
			}
		}
	}
}
