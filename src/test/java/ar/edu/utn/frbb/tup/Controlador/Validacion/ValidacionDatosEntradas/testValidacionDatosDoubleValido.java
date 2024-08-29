package ar.edu.utn.frbb.tup.Controlador.Validacion.ValidacionDatosEntradas;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.Controlador.Validaciones.ValidacionDatos;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionDatosInvalidos;

import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testValidacionDatosDoubleValido {
    
    private ValidacionDatos validacionDatos;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        validacionDatos=new ValidacionDatos();
    }

    @Test
    public void testDatosDoubleValidoExitoso() throws ExcepcionDatosInvalidos{
        validacionDatos.doubleValido("10.5");
    }

    @Test
    public void testDatosDoubleValidoInvalido() throws ExcepcionDatosInvalidos{
        assertThrows(ExcepcionDatosInvalidos.class, () -> validacionDatos.doubleValido("a"));
    }
}
