package ar.edu.utn.frbb.tup.Servicio.Cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.Modelo.Cliente;
import ar.edu.utn.frbb.tup.Modelo.CuentaBancaria;
import ar.edu.utn.frbb.tup.Modelo.Prestamo;
import ar.edu.utn.frbb.tup.Persistencia.DatosCliente;
import ar.edu.utn.frbb.tup.Persistencia.DatosCuentaBancaria;
import ar.edu.utn.frbb.tup.Persistencia.DatosMovimiento;
import ar.edu.utn.frbb.tup.Servicio.ServicioCliente;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionClienteNoExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionClienteTienePrestamo;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionClienteTieneSaldo;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionClienteYaExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCuentaBancaria.ExcepcionCuentaBancariaMonedaNoExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCuentaBancaria.ExcepcionCuentaBancariaNoExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCuentaBancaria.ExcepcionCuentaBancariaYaExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesOperacion.ExcepcionSaldoInsuficiente;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestServicioEliminarCliente {
    
    @Mock
    private DatosCliente datosCliente;

    @Mock
    private DatosCuentaBancaria datosCuentaBancaria;

    @Mock
    private DatosMovimiento datosMovimiento;

    @InjectMocks
    private ServicioCliente servicioCliente;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        servicioCliente = new ServicioCliente(datosCliente,datosCuentaBancaria, datosMovimiento);
    }

    @Test
    public void testEliminarClienteExitoso() throws ExcepcionClienteNoExiste, ExcepcionClienteYaExiste, ExcepcionClienteTieneSaldo, ExcepcionClienteTienePrestamo{
        //Se crea un cliente y se le agrega una cuenta bancaria a su lista de cuentas bancarias.
        Cliente cliente=new Cliente(0, "Galo", "Santopietro", 45349054, "2932502274");
        CuentaBancaria cuentaBancaria=new CuentaBancaria(0, 0, LocalDate.now(), 0, "123456", "caja de ahorro", "dolares");
        List<CuentaBancaria> cuentasBancariasCliente=cliente.getCuentasBancarias();
        cuentasBancariasCliente.add(cuentaBancaria);
        cliente.setCuentasBancarias(cuentasBancariasCliente);

        //Se simula la base de datos.
        when(datosCliente.buscarClienteDni(cliente.getDni())).thenReturn(cliente);

        //Se elimina el cliente y se devuelve el cliente eliminado.
        Cliente clienteEliminado=servicioCliente.eliminarCliente("45349054");
        
        //Se verifica que se haya eliminado el cliente.
        verify(datosCliente, times(1)).eliminarCliente(cliente);

        assertEquals(cliente, clienteEliminado);
    }

    @Test
    public void testEliminarClienteNoExiste() throws ExcepcionClienteNoExiste, ExcepcionClienteYaExiste, ExcepcionClienteTieneSaldo, ExcepcionClienteTienePrestamo{
        //Se simula un caso en el que el cliente a eliminar no existe.
        assertThrows(ExcepcionClienteNoExiste.class, () -> servicioCliente.eliminarCliente("45349054"));
    }

    @Test
    public void testEliminarClienteConSaldo() throws ExcepcionClienteNoExiste, ExcepcionClienteYaExiste, ExcepcionClienteTieneSaldo, ExcepcionClienteTienePrestamo, ExcepcionCuentaBancariaYaExiste, ExcepcionCuentaBancariaNoExiste{
        //Se crea un cliente y se le agrega una cuenta bancaria a su lista de cuentas bancarias.
        Cliente cliente=new Cliente(0, "Galo", "Santopietro", 45349054, "2932502274");
        CuentaBancaria cuentaBancaria=new CuentaBancaria(0, 0, LocalDate.now(), 0, "123456", "caja de ahorro", "dolares");
        List<CuentaBancaria> cuentasBancariasCliente=cliente.getCuentasBancarias();
        cuentasBancariasCliente.add(cuentaBancaria);
        cliente.setCuentasBancarias(cuentasBancariasCliente);
        //Se agrega un saldo a la cuenta bancaria.
        cuentaBancaria.setSaldo(12000);

        //Se simula el comportamiento de la base de datos.
        when(datosCliente.buscarClienteDni(cliente.getDni())).thenReturn(cliente);

        //Se intenta eliminar el cliente y se espera que se lance la excepcion por tener saldo en una cuenta bancaria.
        assertThrows(ExcepcionClienteTieneSaldo.class, () -> servicioCliente.eliminarCliente("45349054"));
    }

    @Test
    public void testEliminarClienteConPrestamo() throws ExcepcionClienteNoExiste, ExcepcionClienteYaExiste, ExcepcionCuentaBancariaYaExiste,ExcepcionCuentaBancariaNoExiste, ExcepcionCuentaBancariaMonedaNoExiste, ExcepcionSaldoInsuficiente{
        //Se crea un cliente y se le agrega un prestamo a su lista de prestamos.
        Cliente cliente=new Cliente(0, "Galo", "Santopietro", 45349054, "2932502274");
        Prestamo prestamo=new Prestamo(0, 0, 12000, 10, 0, 12000);
        List<Prestamo> prestamosCliente=cliente.getPrestamos();
        prestamosCliente.add(prestamo);
        cliente.setPrestamos(prestamosCliente);

        //Se simula el comportamiento de la base de datos.
        when(datosCliente.buscarClienteDni(cliente.getDni())).thenReturn(cliente);

        //Se intenta eliminar el cliente y se espera que se lance la excepcion por tener prestamos.
        assertThrows(ExcepcionClienteTienePrestamo.class, () -> servicioCliente.eliminarCliente("45349054"));
    }
}
