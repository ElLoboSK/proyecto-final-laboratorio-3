package ar.edu.utn.frbb.tup.Servicio.Cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;

import ar.edu.utn.frbb.tup.Modelo.Cliente;
import ar.edu.utn.frbb.tup.Persistencia.DatosCliente;
import ar.edu.utn.frbb.tup.Persistencia.DatosCuentaBancaria;
import ar.edu.utn.frbb.tup.Persistencia.DatosMovimiento;
import ar.edu.utn.frbb.tup.Servicio.ServicioCliente;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionClienteNoExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionClienteYaExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionNoHayClientes;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestServicioCrearCliente {
    
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
    public void testCrearClienteExitoso() throws ExcepcionClienteYaExiste, ExcepcionClienteNoExiste, ExcepcionNoHayClientes{
        //Se crea un cliente con el servicio y se devuelve el mismo.
        Cliente cliente=servicioCliente.crearCliente("45349054", "Galo", "Santopietro", "2932502274");

        //Se verifica que se haya agregado el cliente.
        verify(datosCliente, times(1)).agregarCliente(cliente);
        assertEquals(45349054, cliente.getDni());
        assertNotNull(cliente);
    }

    @Test
    public void testCrearClienteYaExiste() throws ExcepcionClienteYaExiste, ExcepcionClienteNoExiste{
        //Se crea un cliente con el servicio y se devuelve el mismo.
        Cliente cliente=servicioCliente.crearCliente("45349054", "Galo", "Santopietro", "2932502274");

        //Se simula el comportamiento de la base de datos.
        when(datosCliente.buscarClienteDni(cliente.getDni())).thenReturn(cliente);

        //Se intenta crear el cliente nuevamente y se espera que se lance una excepcion.
        assertThrows(ExcepcionClienteYaExiste.class, () -> servicioCliente.crearCliente("45349054", "Galo", "Santopietro", "2932502274"));
    }
}
