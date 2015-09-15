package controllers;

import java.util.List;

import dao.ConsultaDAO;
import fachada.BancAndes;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import vos.Cliente;
import vos.Usuario;

import static play.libs.Json.toJson;;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("BancAndes 1.0"));
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
			//List<Cliente> clientes=BancAndes.darInstancia().darUsuariosDefault();
			return ok(
					//toJson(clientes)
					);
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
    }

}
