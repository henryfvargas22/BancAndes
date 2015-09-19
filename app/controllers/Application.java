package controllers;

import java.util.List;

import dao.ConsultaDAO;
import fachada.BancAndes;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import vos.Cliente;
import vos.Cuenta;
import vos.Empleado;
import vos.Oficina;
import vos.Prestamo;
import vos.PuntoDeAtencion;
import vos.Usuario;

import static play.libs.Json.toJson;;

public class Application extends Controller {

    public Result index() {
        return ok(main.render("BancAndes en Play!", index.render("BancAndes 1.0")));
    }
    
    public Result getUsuarios()
    {
    	try 
    	{
			List<Usuario> usuarios=BancAndes.darInstancia().darUsuariosDefault();
			return ok(toJson(usuarios));
		} 
    	catch (Exception e) 
    	{
			return internalServerError("Ups: "+e.getMessage());
		}
    }
    
    public Result getClientes()
    {
    	try 
    	{
			List<Cliente> clientes=BancAndes.darInstancia().darClientesDefault();
			return ok(toJson(clientes));
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
    }
    
    public Result getEmpleados()
    {
    	try 
    	{
			List<Empleado> empleados=BancAndes.darInstancia().darEmpleadosDefault();
			return ok(toJson(empleados));
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
    }
    
    public Result getCuentas()
    {
    	try 
    	{
			List<Cuenta> cuentas=BancAndes.darInstancia().darCuentasDefault();
			return ok(toJson(cuentas));
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
    }
    
    public Result getOficinas()
    {
    	try 
    	{
			List<Oficina> oficinas=BancAndes.darInstancia().darOficinasDefault();
			return ok(toJson(oficinas));
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
    }
    
    public Result getPrestamos()
    {
    	try 
    	{
			List<Prestamo> prestamos=BancAndes.darInstancia().darPrestamosDefault();
			return ok(toJson(prestamos));
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
    }
    
    public Result getPuntos()
    {
    	try 
    	{
			List<PuntoDeAtencion> puntos=BancAndes.darInstancia().darPuntosDeAtencionDefault();
			return ok(toJson(puntos));
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
    }
    
    public Result addUsuario()
    {
    	try
    	{
    		DynamicForm dynamicForm = Form.form().bindFromRequest();
    	    Logger.info("Username is: " + dynamicForm.get("username"));
    	    Logger.info("Password is: " + dynamicForm.get("password"));
    	    String usuario=dynamicForm.get("username");
    	    return ok("ok, I recived POST data. That's all...");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return internalServerError("Ups: "+e.getMessage());
    	}
    }
    
    public Result addOficina()
    {
    	try
    	{
    		DynamicForm dynamicForm = Form.form().bindFromRequest();
    	    Logger.info("Username is: " + dynamicForm.get("username"));
    	    Logger.info("Password is: " + dynamicForm.get("password"));
    	    String usuario=dynamicForm.get("username");
    	    return ok("ok, I recived POST data. That's all...");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return internalServerError("Ups: "+e.getMessage());
    	}
    }
}
