package vos;

public class Empleado extends Usuario
{
	private int idEmpleado;
	
	public Empleado()
	{
		super();
	}
	
	public int getIdUsuario()
	{
		return cedula;
	}
	
	public void setIdUsuario(int cedula)
	{
		this.cedula=cedula;
	}

	public int getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	
}
