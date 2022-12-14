package ejemplo1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
//
// Alberto Carrera Mart�n - Abril 2020
//

public class AccesoBdatos {
	private EntityManagerFactory emf;
	private EntityManager em;

	public void conectar() {
		emf = Persistence.createEntityManagerFactory("db/empleados.odb");
		em = emf.createEntityManager();
	}

	public void desconectar() {
		em.close();
		emf.close();
	}

	public DepartamentoEntity buscarDepartamento(int numDepartamento) {
		return em.find(DepartamentoEntity.class, numDepartamento);
	}// de m�todo buscarDepartamento
		//

	@SuppressWarnings("deprecation")
	public void imprimirDepartamento(int numDepartamento) {
		DepartamentoEntity d = buscarDepartamento(numDepartamento);
		if (d == null)
			System.out.println("No existe el Departamento " + numDepartamento);
		else {
			Set<EmpleadoEntity> empleados = d.getEmpleados();
			String datos = "Datos del departamento " + numDepartamento + ": ";
			datos += "Nombre: " + d.getNombre() + " - Localidad: " + d.getLocalidad() + "\n";
			if (empleados.isEmpty())
				datos += "No tiene empleados en este momento";
			else {
				datos += "Lista de empleados" + "\n";
				datos += "*******************";
			}
			for (EmpleadoEntity empleado : empleados) {
				datos += "\nN�mero de empleado: " + empleado.getEmpnoId() + "\n";
				datos += "Nombre: " + empleado.getNombre() + "\n";
				datos += "Oficio: " + empleado.getOficio() + "\n";
				if (empleado.getDirId() == null)
					datos += "Jefe: No tiene" + "\n";
				else
					datos += "Jefe: " + empleado.getDirId().getNombre() + "\n";
				datos += "A�o de alta: " + (empleado.getAlta().getYear() + 1900) + "\n";
				datos += "Salario: " + empleado.getSalario() + "\n";
				if (empleado.getComision() == null)
					datos += "Comisi�n: No tiene" + "\n";
				else
					datos += "Comisi�n: " + empleado.getComision() + "\n";
			}

			System.out.println(datos);
		}
	} // de m�todo imprimirDepartamento

	public boolean insertarDepartamento(DepartamentoEntity d) {
		if (buscarDepartamento(d.getDptoId()) != null)
			return false;
		em.getTransaction().begin();
		em.persist(d);
		em.getTransaction().commit();
		return true;
	} // de insertarDepartamento

	public boolean modificarDepartamento(DepartamentoEntity d) {
		DepartamentoEntity departamentoBuscado = buscarDepartamento(d.getDptoId());
		if (departamentoBuscado == null)
			return false;
		em.getTransaction().begin();
		departamentoBuscado.setNombre(d.getNombre());
		departamentoBuscado.setLocalidad(d.getLocalidad());
		em.persist(departamentoBuscado);
		em.getTransaction().commit();
		return true;
	} // de modificarDepartamento

	// Si tiene empleados lo borrar�a igual, dejando en estos el n�mero de
	// Departamento
	// pero el resto de los atributos del departamento a null. Por eso no dejo
	// borrar

	public boolean borrarDepartamento(int numDepartamento) {
		DepartamentoEntity departamentoBuscado = buscarDepartamento(numDepartamento);
		if (departamentoBuscado == null || !departamentoBuscado.getEmpleados().isEmpty())
			return false;
		em.getTransaction().begin();
		em.remove(departamentoBuscado);
		em.getTransaction().commit();
		return true;
	} // de modificarDepartamento

	public void demoJPQL() {
		/*
		 * Query q1 = em.createQuery("SELECT COUNT(d) FROM DepartamentoEntity d");
		 * System.out.println("Total Departamentos: " + q1.getSingleResult()); //
		 * TypedQuery<Long> tq1 = em.createQuery(
		 * "SELECT COUNT(d) FROM DepartamentoEntity d", Long.class);
		 * System.out.println("Total Departamentos: " + tq1.getSingleResult()); //
		 * TypedQuery<DepartamentoEntity>tq2 =
		 * em.createQuery("SELECT d FROM DepartamentoEntity d",
		 * DepartamentoEntity.class); List<DepartamentoEntity> l2 = tq2.getResultList();
		 * for (DepartamentoEntity r2 : l2) { System.out.println("Nombre :  " +
		 * r2.getNombre()+ ", Localidad: "+ r2.getLocalidad()); } //
		 * TypedQuery<Object[]>tq3 =
		 * em.createQuery("SELECT d.nombre, d.localidad FROM DepartamentoEntity  d",
		 * Object[].class); List<Object[]> l3 = tq3.getResultList(); for (Object[] r3 :
		 * l3) { System.out.println( "Nombre :  " + r3[0] + ", Localidad: " + r3[1]); }
		 * // TypedQuery<Object[]>tq4 =
		 * em.createQuery("SELECT d.nombre, d.localidad FROM DepartamentoEntity d" +
		 * " WHERE d.dptoId != :n", Object[].class); tq4.setParameter("n", 10);
		 * List<Object[]> l4 = tq4.getResultList(); for (Object[] r4 : l4) {
		 * System.out.println( "Nombre :  " + r4[0] + ", Localidad: " + r4[1]); }
		 */
		/**** EJERCICIO 8: **********************************/
		TypedQuery<EmpleadoEntity> TQEmp;
		TQEmp = em.createQuery("SELECT e " + "FROM EmpleadoEntity e", EmpleadoEntity.class);
		List<EmpleadoEntity> listaEmp = TQEmp.getResultList();

		TypedQuery<DepartamentoEntity> TQDep;
		TQDep = em.createQuery("SELECT d " + "FROM DepartamentoEntity d " + "ORDER BY d.nombre",
				DepartamentoEntity.class);
		List<DepartamentoEntity> listaDep = TQDep.getResultList();
		/**** 1 *****/
		for (EmpleadoEntity empleado : listaEmp) {
			System.out.println(empleado.getNombre() + " - " + empleado.getAlta());
		}
		/**** 2 *****/
		System.out.println();
		for (EmpleadoEntity empleado : listaEmp) {
			if (empleado.getNombre().toLowerCase().contains("carrera"))
				System.out.println(empleado.getNombre() + " - " + empleado.getAlta());
		}
		System.out.println();
		/**** 3 *****/
		for (EmpleadoEntity empleado : listaEmp) {
			if (empleado.getDepartamento().getNombre().equalsIgnoreCase("I+D")
					&& empleado.getOficio().equalsIgnoreCase("Empleado"))
				System.out.println(empleado.getNombre() + " - " + empleado.getOficio() + " - "
						+ empleado.getDepartamento().getNombre());
		}
		System.out.println();
		/**** 4 *****/
		Date fecha = new Date("2002/12/31");
		System.out.println(fecha);

		for (EmpleadoEntity empleado : listaEmp) {
			if ((empleado.getAlta().compareTo(fecha)) > 0)
				System.out.println(empleado.getNombre() + " - " + empleado.getAlta());
		}
		System.out.println();
		/**** 5 *****/
		Query query = em.createQuery("SELECT e.departamento.nombre, e.nombre " + "FROM  EmpleadoEntity e "
				+ "ORDER BY e.departamento.nombre");
		List<Object[]> list = query.getResultList();
		for (Object[] o : list) {
			System.out.println(o[0] + " - " + o[1]);
		}
		System.out.println();
		/**** 6 *****/
		Query query2 = em.createQuery("SELECT  d.departamento.nombre , count(d), sum(d.salario), max(d.salario) "
				+ "FROM  EmpleadoEntity d " + "GROUP BY d.departamento.nombre ");
		List<Object[]> list2 = query2.getResultList();
		for (Object[] o : list2) {
			System.out.println(o[0] + " - " + o[1] + " - " + o[2] + " - " + o[3]);
		}
		System.out.println();
		/**** 7 *****/
		Query query3 = em.createQuery("SELECT  d.departamento.nombre , count(d), sum(d.salario), max(d.salario) "
				+ "FROM  EmpleadoEntity d " + "GROUP BY d.departamento.nombre " + "HAVING count(d)>=5");
		List<Object[]> list3 = query3.getResultList();
		for (Object[] o : list3) {
			System.out.println(o[0] + " - " + o[1] + " - " + o[2] + " - " + o[3]);
		}
		System.out.println();
		/**** 8 *****/
		for (EmpleadoEntity empleado : listaEmp) {
			if (empleado.getDirId() != null)
				System.out.println(empleado.getNombre() + " - su jefe es - " + empleado.getDirId().getNombre()
						+ " - departamento - " + empleado.getDepartamento().getDptoId());
		}
		System.out.println();
		/**** 9 *****/
		for (DepartamentoEntity departamento : listaDep) {
			if (departamento.getEmpleados().size() > 0)
				System.out.println(departamento.getNombre() + " - " + departamento.getEmpleados().size());
		}
		System.out.println();
		/**** 10 *****/
		for (DepartamentoEntity departamento : listaDep) {
			System.out.println(departamento.getNombre() + " - " + departamento.getEmpleados().size());
		}
		System.out.println();
		/**** 11 *****/
		TypedQuery<Object[]> tq11 = em.createQuery("SELECT d.departamento.dptoId, d.nombre, d.salario FROM EmpleadoEntity d order by d.departamento.dptoId desc, d.salario asc"
				,
				Object[].class);
		List<Object[]> lista = tq11.getResultList();
		for (Object[] o : lista) {
			for (Object o2 : o) {
				System.out.print(o2 + " - ");
			}
			System.out.println();
		}
		System.out.println();
		/**** 12 *****/
		for (EmpleadoEntity empleado : listaEmp) {
			if (empleado.getDirId() == null)
				System.out.println(empleado.getEmpnoId() + " - " + empleado.getNombre());
		}
		System.out.println();
		/**** 13 *****/
		for (DepartamentoEntity departamento : listaDep) {
			for (EmpleadoEntity empleado : departamento.getEmpleados())
				if (empleado.getEmpnoId() == 1039)
					System.out.println(departamento.getDptoId() + " - " + departamento.getNombre());
		}
		System.out.println();

	}// de demoJPQL
//--------------------------------------------------------------------------------------------------------------
//Ejercicio 9
	public int incrementarSalario(int cantidad) {
		try {
			TypedQuery<EmpleadoEntity> tq = em.createQuery("SELECT e FROM EmpleadoEntity e", EmpleadoEntity.class);
			List<EmpleadoEntity> li = tq.getResultList();
			for (EmpleadoEntity e : li) {
				em.getTransaction().begin();
				e.setSalario(e.getSalario() + cantidad);
				em.getTransaction().commit();
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}

	}
	public int incrementarSalarioV2(int cantidad) {
		Query query =em.createQuery("UPDATE EmpleadoEntity e SET e.salario=(e.salario+ :cant) ");
		query.setParameter("cant",100);
		em.getTransaction().begin();
		int resultado=query.executeUpdate();
		em.getTransaction().commit();
		return resultado;
	}

	public int incrementarSalarioOficio(String oficio, int cantidad) {
		try {
			TypedQuery<EmpleadoEntity> tq = em.createQuery("SELECT e FROM EmpleadoEntity e", EmpleadoEntity.class);
			List<EmpleadoEntity> li = tq.getResultList();
			for (EmpleadoEntity e : li) {
				if (e.getOficio().equalsIgnoreCase(oficio)) {
					em.getTransaction().begin();
					e.setSalario(e.getSalario() + cantidad);
					em.getTransaction().commit();
				}
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	public int incrementarSalarioOficioV2(String oficio,int cantidad) {
		Query query =em.createQuery("UPDATE EmpleadoEntity e SET e.salario=(e.salario+ :cant) WHERE  e.oficio= :ofi");
		query.setParameter("cant",cantidad);
		query.setParameter("ofi",oficio);
		em.getTransaction().begin();
		int resultado=query.executeUpdate();
		em.getTransaction().commit();
		return resultado;
	}

	public int incrementarSalarioDepartamento(int numDepartamento, int cantidad) {
		try {
			DepartamentoEntity d= em.find(DepartamentoEntity.class, numDepartamento);
			if (d ==null) throw new Exception();
			TypedQuery<EmpleadoEntity> tq = em.createQuery("SELECT e FROM EmpleadoEntity e", EmpleadoEntity.class);
			List<EmpleadoEntity> li = tq.getResultList();
			for (EmpleadoEntity e : li) {
				if (e.getDepartamento().getDptoId()==numDepartamento) {
					em.getTransaction().begin();
					e.setSalario(e.getSalario() + cantidad);
					em.getTransaction().commit();
				}
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	public int incrementarSalarioDepartamentoV2(int numdepartamento,int cantidad) {
		Query query =em.createQuery("UPDATE EmpleadoEntity e SET e.salario=(e.salario+ :cant) WHERE e.departamento.getDptoId()= :num");
		query.setParameter("cant",cantidad);
		query.setParameter("num",numdepartamento);
		em.getTransaction().begin();
		int resultado=query.executeUpdate();
		em.getTransaction().commit();
		return resultado;
	}

	public int borrarEmpleado(int numEmpleado) {
		try {
			EmpleadoEntity d= em.find(EmpleadoEntity.class, numEmpleado);
			if (d ==null) throw new Exception();
			TypedQuery<EmpleadoEntity> tq = em.createQuery("SELECT e FROM EmpleadoEntity e", EmpleadoEntity.class);
			List<EmpleadoEntity> li = tq.getResultList();
			for (EmpleadoEntity e : li) {
				if (e.getEmpnoId()==numEmpleado) {
					em.getTransaction().begin();
					em.remove(e);
					em.getTransaction().commit();
				}
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}
		
	}
	public int borrarEmpleadoV2(int numEmpleado) {
		Query query=em.createQuery("DELETE  FROM EmpleadoEntity e WHERE e.empnoId= : num");
		query.setParameter("num", numEmpleado);
		em.getTransaction().begin();
		int resultado=query.executeUpdate();
		em.getTransaction().commit();
		return resultado;
		
	}

	public int borraDepartamento(int numDepartamento) {

		try {
			DepartamentoEntity d= em.find(DepartamentoEntity.class, numDepartamento);
			if (d ==null) throw new Exception();
			TypedQuery<DepartamentoEntity> tq = em.createQuery("SELECT d FROM DepartamentoEntity d", DepartamentoEntity.class);
			List<DepartamentoEntity> li = tq.getResultList();
			
			for (DepartamentoEntity e : li) {
				if (e.getDptoId()==numDepartamento) {
					em.getTransaction().begin();
					em.remove(e);
					em.getTransaction().commit();
				}
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	public int borraDepartamentoV2(int numDepartamento) {
		Query query=em.createQuery("DELETE  FROM DepartamentoEntity e WHERE e.dptoId= : num");
		query.setParameter("num", numDepartamento);
		em.getTransaction().begin();
		int resultado=query.executeUpdate();
		em.getTransaction().commit();
		return resultado;
	}
} // de la clase
