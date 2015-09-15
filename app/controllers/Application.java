package controllers;

import java.util.List;

import dao.ConsultaDAO;
import fachada.BancAndes;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import vos.Usuario;

import static play.libs.Json.toJson;;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Andy es marica."));
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
    
    public Result llenarBases()
    {
    	return ok();
    }

}
