package ar.edu.utn.frbb.tup.Controlador.Validacion.DatosEntradas;

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
public class testValidacionDatosIntPositivoValido {
    
    private ValidacionDatos validacionDatos;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        validacionDatos=new ValidacionDatos();
    }

    @Test
    public void testDatosIntPositivoValidoExitoso() throws ExcepcionDatosInvalidos{
        //Se llama al metodo a testear con datos int positivos, si no se lanza una excepcion, el test es exitoso.
        validacionDatos.intPositivoValido("1");
    }

    @Test
    public void testDatosIntPositivoValidoInvalido() throws ExcepcionDatosInvalidos{
        //Se llama al metodo a testear con datos int negativos o una letra en lugar de numeros, si se lanza una excepcion, el test es exitoso.
        assertThrows(ExcepcionDatosInvalidos.class, () -> validacionDatos.intPositivoValido("a"));
        assertThrows(ExcepcionDatosInvalidos.class, () -> validacionDatos.intPositivoValido("-1"));
    }
}
