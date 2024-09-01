package ar.edu.utn.frbb.tup.Controlador.Cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.Controlador.ControladorCliente;
import ar.edu.utn.frbb.tup.Controlador.Validaciones.ValidacionDatosCliente;
import ar.edu.utn.frbb.tup.Modelo.Cliente;
import ar.edu.utn.frbb.tup.Servicio.ServicioCliente;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCliente.ExcepcionNoHayClientes;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testControladorListarClientes {
    
    @Mock
    private ServicioCliente servicioCliente;

    @Mock
    private ValidacionDatosCliente validacionDatosCliente;

    @InjectMocks
    private ControladorCliente controladorCliente;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        controladorCliente=new ControladorCliente(servicioCliente, validacionDatosCliente);
    }

    @Test
    public void testListarClientesExitoso() throws ExcepcionNoHayClientes{
        Cliente cliente=new Cliente(0, "Galo", "Santopietro", 45349054, "2932502274");
        Cliente cliente2=new Cliente(1, "Joaco", "Widmer", 44741717, "2932502274");

        List<Cliente> clientes=new ArrayList<>();
        clientes.add(cliente);
        clientes.add(cliente2);

        when(servicioCliente.listarClientes()).thenReturn(clientes);

        assertEquals("200 OK", String.valueOf(controladorCliente.listarClientes().getStatusCode()));
        assertEquals(clientes, controladorCliente.listarClientes().getBody());
    }

    @Test
    public void testListarClientesNoHayClientes() throws ExcepcionNoHayClientes{
        doThrow(ExcepcionNoHayClientes.class).when(servicioCliente).listarClientes();

        assertThrows(ExcepcionNoHayClientes.class, () -> controladorCliente.listarClientes());
    }
}
