import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class accesobd {
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String database = "libros10mayo2022";
	private static String hostname = "localhost";
	private static String port = "3308";
	private static String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database
			+ "?serverTimezone=Europe/Madrid";
	private static String username = "root";
	private static String password = "root";
	private Connection conecta;

	public void conectar() throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		conecta = DriverManager.getConnection(url, username, password);
	}

	public void desconectar() throws SQLException, ClassNotFoundException {

		conecta = null; // = conecta.close();
	}

	public void librosEditorial(String string) throws SQLException {
		// TODO Auto-generated method stub
		String sentenciaSQL="SELECT * from libros where editorial=?";
		PreparedStatement ps=conecta.prepareStatement(sentenciaSQL);
		ps.setString(1, string);
		ResultSet rs= ps.executeQuery();
		while(rs.next()) {
			 System.out.println(rs.getString("isbn")+rs.getString("autor")+rs.getString("titulo"));
		}
		
	}

	public int totalPrestados() throws SQLException {
		int resultado=0;
		String sentenciaSQL="select count(*) from libros where prestados='S'";
		PreparedStatement ps= conecta.prepareStatement(sentenciaSQL);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			resultado=rs.getInt(1);
		}
		return resultado;
	}

	public String insertarUsuario(String string, String string2, String string3, String string4) throws SQLException {
		String sentenciaSQL="select nuevousuario(?,?,?,?)";
		CallableStatement cs= conecta.prepareCall(sentenciaSQL);
		cs.setString(1, string);
		cs.setString(1, string2);
		cs.setString(1, string3);
		cs.setString(1, string4);
		ResultSet rs=cs.executeQuery();
		String resultado = "";
		if(rs.next()) {
			 resultado=rs.getString(1);
		}
		if(resultado.equalsIgnoreCase("OK"))
			return resultado;
		
		return null;
	}
	
	public boolean borrarUsuario(String string) throws SQLException {
		String sentenciaSQL="call borrausuario(?)";
		CallableStatement cs= conecta.prepareCall(sentenciaSQL);
		cs.setString(1, string);
		cs.registerOutParameter(1, Types.BOOLEAN);
		cs.execute();
		return cs.getBoolean(1);
		
	}
	
	public int borraLibro(String string) throws SQLException {
		String sentenciaSQL="delete from libro where isbn=?";
		try {
			conecta.setAutoCommit(false);
			PreparedStatement ps= conecta.prepareStatement(sentenciaSQL);
			int resultado= ps.executeUpdate();
			conecta.commit();
			conecta.setAutoCommit(true);
			return resultado;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				conecta.rollback();
				return -1;
			
		}
		
		
	}
}
/*
 * CRUD:
 * create:"INSERT INTO socio VALUES (((select max(socioID)from socio s)+1),?,?,?,?)"
 * read:"SELECT * FROM socio WHERE socioID=?"
 * update:"UPDATE socio SET nombre=?, estatura=?, edad=?, localidad=? WHERE socioID=?"
 * delete:"DELETE FROM libro WHERE isbn=?"
 * 
 * FUNC:"SELECT totalEmpleados(?) "
 *  	CallableStatement proc = conn.prepareCall(StringSQL);
 * 		proc.setInt(1, 110); 
 *  	ResultSet rs=proc.executeQuery(); 
 *   if (rs.next()) {...}
 *   
 * 		solo parametros de entrada
 * 		
 * PROC:"CALL pesetasAeuros(?) "
 * 		CallableStatement proc = conn.prepareCall(StringSQL);
 * 		 double cantidad = 25000; // Quiero convertir 25000 pesetas a euros
         //Acoplamiento. El par???metro o argumento 1??? es un real, en concreto cantidad
         proc.setDouble(1,cantidad); 
         // El par???metro 1??? tambi???n es a su vez de salida y evidentemente de tipo real
         proc.registerOutParameter(1, Types.DECIMAL);
         // Ejecuto el procedimiento
         proc.execute();            
         System.out.print("Convertir 25.000 pesetas a euros -->");
         // Imprimo el argumento de salida
         System.out.println(proc.getDouble(1));     
 * 
 * 
 */
