package ar.edu.utn.frbb.tup.Controlador.Validacion.DatosCuentaBancaria;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.Controlador.Validaciones.ValidacionDatosCuentaBancaria;
import ar.edu.utn.frbb.tup.Servicio.Excepciones.ExcepcionDatosInvalidos;

import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestValidacionTipoCuentaValido {
    
    private ValidacionDatosCuentaBancaria validacionDatosCuentaBancaria;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        validacionDatosCuentaBancaria=new ValidacionDatosCuentaBancaria();
    }
    
    @Test
    public void testTipoCuentaValidoExitoso() throws ExcepcionDatosInvalidos{
        //Se llama al metodo a testear con tipos de cuenta bancaria correctas, si no se lanza una excepcion, el test es exitoso.
        validacionDatosCuentaBancaria.tipoCuentaValido("caja de ahorro");
        validacionDatosCuentaBancaria.tipoCuentaValido("cuenta corriente");
    }

    @Test
    public void testTipoCuentaValidoInvalido() throws ExcepcionDatosInvalidos{
        //Se llama al metodo a testear con un tipo de cuenta bancaria no esperada, si se lanza una excepcion, el test es exitoso.
        assertThrows(ExcepcionDatosInvalidos.class, () -> validacionDatosCuentaBancaria.tipoCuentaValido("caja corriente"));
    }
}
