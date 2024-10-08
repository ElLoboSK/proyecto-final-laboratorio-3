package ar.edu.utn.frbb.tup.Controlador.Operacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
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
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesOperacion.ExcepcionMismaCuentaBancaria;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesOperacion.ExcepcionMonedaDiferente;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionesOperacion.ExcepcionSaldoInsuficiente;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testControladorTransferir {
    
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
    public void testTransferirExitoso() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente, ExcepcionMonedaDiferente, ExcepcionMismaCuentaBancaria{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancariaOrigen", "0");
        datos.put("idCuentaBancariaDestino", "1");

        //Se crean los movimientos y se agregan a una lista. Esta es la lista a devolver por el servicio.
        Movimiento movimiento=new Movimiento(0, 0, LocalDate.now(), 12000, "transferencia enviada");
        Movimiento movimiento2=new Movimiento(0, 0, LocalDate.now(), 12000, "transferencia recibida");
        
        List<Movimiento> movimientos=new ArrayList<>();
        movimientos.add(movimiento);
        movimientos.add(movimiento2);

        //Se simula el comportamiento del servicio.
        when(servicioOperacion.transferir(datos.get("monto"), datos.get("idCuentaBancariaOrigen"), datos.get("idCuentaBancariaDestino"))).thenReturn(movimientos);

        //Se llama al controlador y se verifican los resultados.
        assertEquals("201 CREATED", String.valueOf(controladorOperacion.transferir(datos).getStatusCode()));
        assertEquals(movimientos, controladorOperacion.transferir(datos).getBody());
    }

    @Test
    public void testTransferirSaldoInsuficiente() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente, ExcepcionMonedaDiferente, ExcepcionMismaCuentaBancaria{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancariaOrigen", "0");
        datos.put("idCuentaBancariaDestino", "1");

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionSaldoInsuficiente.class).when(servicioOperacion).transferir(datos.get("monto"), datos.get("idCuentaBancariaOrigen"), datos.get("idCuentaBancariaDestino"));

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionSaldoInsuficiente.class, () -> controladorOperacion.transferir(datos));
    }

    @Test
    public void testTransferirCuentaBancariaNoExiste() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente, ExcepcionMonedaDiferente, ExcepcionMismaCuentaBancaria{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancariaOrigen", "0");
        datos.put("idCuentaBancariaDestino", "1");

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionCuentaBancariaNoExiste.class).when(servicioOperacion).transferir(datos.get("monto"), datos.get("idCuentaBancariaOrigen"), datos.get("idCuentaBancariaDestino"));

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionCuentaBancariaNoExiste.class, () -> controladorOperacion.transferir(datos));
    }

    @Test
    public void testTransferirMonedaDiferente() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente, ExcepcionMonedaDiferente, ExcepcionMismaCuentaBancaria{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancariaOrigen", "0");
        datos.put("idCuentaBancariaDestino", "1");

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionMonedaDiferente.class).when(servicioOperacion).transferir(datos.get("monto"), datos.get("idCuentaBancariaOrigen"), datos.get("idCuentaBancariaDestino"));

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionMonedaDiferente.class, () -> controladorOperacion.transferir(datos));
    }

    @Test
    public void testTransferirMismaCuentaBancaria() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente, ExcepcionMonedaDiferente, ExcepcionMismaCuentaBancaria{
        //Se crean los datos de entrada para el controlador y se ponen en un diccionario.
        Map<String, String> datos = new HashMap<>();
        datos.put("monto", "12000");
        datos.put("idCuentaBancariaOrigen", "0");
        datos.put("idCuentaBancariaDestino", "0");

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionMismaCuentaBancaria.class).when(servicioOperacion).transferir(datos.get("monto"), datos.get("idCuentaBancariaOrigen"), datos.get("idCuentaBancariaDestino"));

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionMismaCuentaBancaria.class, () -> controladorOperacion.transferir(datos));
    }

    @Test
    public void testTransferirDatosInvalidos() throws ExcepcionDatosInvalidos, ExcepcionCuentaBancariaNoExiste, ExcepcionSaldoInsuficiente, ExcepcionMonedaDiferente, ExcepcionMismaCuentaBancaria{
        //Se crea un diccionario con datos invalidos.
        Map<String, String> datos = new HashMap<>();

        //Se simula el comportamiento del servicio y se fuerza a que lance una excepcion.
        doThrow(ExcepcionDatosInvalidos.class).when(validacionDatosOperacion).datosOperacionTransferencia(datos);

        //Se llama al controlador y se verifica que se haya lanzado la excepcion.
        assertThrows(ExcepcionDatosInvalidos.class, () -> controladorOperacion.transferir(datos));
    }
}
