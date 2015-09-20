package vos;

public class Cuenta 
{
	private long id;
	private int id_Cliente;
	private String tipo;
	private boolean estaCerrada;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getId_Cliente() {
		return id_Cliente;
	}
	public void setId_Cliente(int id_Cliente) {
		this.id_Cliente = id_Cliente;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public boolean isEstaCerrada() {
		return estaCerrada;
	}
	public void setEstaCerrada(boolean estaCerrada) {
		this.estaCerrada = estaCerrada;
	}
	
	
}
