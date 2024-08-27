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
import ar.edu.utn.frbb.tup.Servicio.ServicioCliente;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionDatosInvalidos;
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

    @InjectMocks
    private ServicioCliente servicioCliente;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        servicioCliente = new ServicioCliente(datosCliente,datosCuentaBancaria);
    }

    @Test
    public void testCrearClienteExitoso() throws ExcepcionClienteYaExiste, ExcepcionDatosInvalidos, ExcepcionClienteNoExiste, ExcepcionNoHayClientes{
        Cliente cliente=servicioCliente.crearCliente("45349054", "Galo", "Santopietro", "2932502274");

        verify(datosCliente, times(1)).agregarCliente(cliente);
        verify(datosCliente, times(1)).buscarClienteDni(cliente.getDni());

        assertEquals(45349054, cliente.getDni());
        assertNotNull(cliente);
    }

    @Test
    public void testCrearClienteYaExiste() throws ExcepcionClienteYaExiste, ExcepcionDatosInvalidos, ExcepcionClienteNoExiste{
        Cliente cliente=servicioCliente.crearCliente("45349054", "Galo", "Santopietro", "2932502274");

        when(datosCliente.buscarClienteDni(45349054)).thenReturn(cliente);

        assertThrows(ExcepcionClienteYaExiste.class, () -> servicioCliente.crearCliente("45349054", "Galo", "Santopietro", "2932502274"));
    }

    @Test
    public void testCrear2ClienteExitoso() throws ExcepcionClienteYaExiste, ExcepcionDatosInvalidos, ExcepcionClienteNoExiste, ExcepcionNoHayClientes{
        Cliente cliente=servicioCliente.crearCliente("45349054", "Galo", "Santopietro", "2932502274");
        Cliente cliente2=servicioCliente.crearCliente("44741717", "Joaco", "Widmer", "2932502274");

        verify(datosCliente, times(1)).agregarCliente(cliente);
        verify(datosCliente, times(1)).agregarCliente(cliente2);
        verify(datosCliente, times(1)).buscarClienteDni(cliente.getDni());
        verify(datosCliente, times(1)).buscarClienteDni(cliente2.getDni());

        assertEquals(45349054, cliente.getDni());
        assertEquals(44741717, cliente2.getDni());
        assertNotNull(cliente);
        assertNotNull(cliente2);
    }
}
