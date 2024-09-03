package ar.edu.utn.frbb.tup.Controlador.Operacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.Controlador.ControladorOperacion;
import ar.edu.utn.frbb.tup.Controlador.Validaciones.ValidacionDatosOperacion;
import ar.edu.utn.frbb.tup.Modelo.Movimiento;
import ar.edu.utn.frbb.tup.Servicio.ServicioOperacion;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionDatosInvalidos;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesCuentaBancaria.ExcepcionCuentaBancariaNoExiste;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesOperacion.ExcepcionSaldoInsuficiente;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testControladorRetirar {
    
    @Mock
    private ServicioOperacion servicioOperacion;

    @Mock
    private ValidacionDatosOperacion validacionDatosOperacion;

    @InjectMocks
    private ControladorOperacion controladorOperacion;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        controladorOperacion=new ControladorOperacion(servicioOperacion, validacionDatosOperacion);
    }

    @Test
    public void testRetirarExitoso() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancaria", "0");

        //Se crea el movimiento a devolver por el servicio.
        Movimiento movimiento=new Movimiento(0, 0, LocalDate.now(), 12000, "retiro");

        //Se simula el comportamiento del servicio.
        when(servicioOperacion.retirar(datos.get("monto"), datos.get("idCuentaBancaria"))).thenReturn(movimiento);

        //Se llama al controlador y se verifican los resultados.
        assertEquals("201 CREATED", String.valueOf(controladorOperacion.retirar(datos).getStatusCode()));
        assertEquals(movimiento, controladorOperacion.retirar(datos).getBody());
    }

    @Test
    public void testRetirarCuentaBancariaNoExiste() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancaria", "0");

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionCuentaBancariaNoExiste.class).when(servicioOperacion).retirar(datos.get("monto"), datos.get("idCuentaBancaria"));

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionCuentaBancariaNoExiste.class, () -> controladorOperacion.retirar(datos));
    }

    @Test
    public void testRetirarSaldoInsuficiente() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancaria", "0");

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionSaldoInsuficiente.class).when(servicioOperacion).retirar(datos.get("monto"), datos.get("idCuentaBancaria"));

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionSaldoInsuficiente.class, () -> controladorOperacion.retirar(datos));
    }

    @Test
    public void testRetirarDatosInvalidos() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente{
        //Se crea un diccionario con datos invalidos.
        Map<String, String> datos = new HashMap<>();

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionDatosInvalidos.class).when(validacionDatosOperacion).datosOperacionBasica(datos);

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionDatosInvalidos.class, () -> controladorOperacion.retirar(datos));
    }
}
