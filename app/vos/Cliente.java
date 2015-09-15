package vos;

public class Cliente extends Usuario 
{
	private int idCliente;
	
	public Cliente()
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

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	
}
