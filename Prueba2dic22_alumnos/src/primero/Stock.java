package primero;
// Generated 25-feb-2020 15:22:25 by Hibernate Tools 5.2.12.Final

/**
 * Stock generated by hbm2java
 */
public class Stock implements java.io.Serializable {

	private StockId id;
	private Producto producto;
	private Tienda tienda;
	private int unidades;

	public Stock() {
	}

	public Stock(StockId id, Producto producto, Tienda tienda, int unidades) {
		this.id = id;
		this.producto = producto;
		this.tienda = tienda;
		this.unidades = unidades;
	}

	public StockId getId() {
		return this.id;
	}

	public void setId(StockId id) {
		this.id = id;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Tienda getTienda() {
		return this.tienda;
	}

	public void setTienda(Tienda tienda) {
		this.tienda = tienda;
	}

	public int getUnidades() {
		return this.unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

}
