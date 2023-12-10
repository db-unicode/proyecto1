package uniandes.dpoo.proyecto1.rental;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import uniandes.dpoo.proyecto1.queries.CarCategoryQueries;
import uniandes.dpoo.proyecto1.queries.ClientQueries;
import uniandes.dpoo.proyecto1.queries.LocationQueries;


@RunWith(MockitoJUnitRunner.class)
public class RentalIntegrationTest {

    private Rental rental;
    private MockedStatic<ClientQueries> mockedClientQueries;
    private MockedStatic<CarCategoryQueries> mockedCarCategoryQueries;
    private MockedStatic<LocationQueries> mockedLocationQueries;
    


    @Before
    public void setUp() throws Exception { 
        mockedClientQueries = mockStatic(ClientQueries.class);
        mockedCarCategoryQueries = mockStatic(CarCategoryQueries.class);
        mockedLocationQueries = mockStatic(LocationQueries.class);

        mockedClientQueries.when(() -> ClientQueries.checkClientId("clienteIdValido")).thenReturn("clienteIdValido");
        mockedCarCategoryQueries.when(() -> CarCategoryQueries.checkDesiredCarCategory("categoriaValida")).thenReturn(1);
        mockedLocationQueries.when(() -> LocationQueries.checkDeliveryLocationId("ubicacionIdValida")).thenReturn("ubicacionIdValida");
        mockedLocationQueries.when(() -> LocationQueries.checkDeliveryLocation("ubicacionEntregaValida")).thenReturn("ubicacionEntregaValida");


        mockedClientQueries.when(() -> ClientQueries.checkClientId("clienteIdInvalido")).thenThrow(new IllegalArgumentException("Cliente inválido"));
        mockedCarCategoryQueries.when(() -> CarCategoryQueries.checkDesiredCarCategory("categoriaInvalida")).thenThrow(new IllegalArgumentException("Categoría inválida"));
        mockedLocationQueries.when(() -> LocationQueries.checkDeliveryLocationId("ubicacionIdInvalida")).thenThrow(new IllegalArgumentException("Ubicación inválida"));

        rental = new Rental("clienteIdValido", "categoriaValida", "ubicacionEntregaValida");
    }

    @Test
    public void testInitRentalWithValidData() throws Exception {
        rental = new Rental("clienteIdValido", "categoriaValida", "ubicacionEntregaValida");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitRentalWithInvalidClientId() throws Exception {
        rental = new Rental("clienteIdInvalido", "categoriaValida", "ubicacionEntregaValida");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitRentalWithInvalidCarCategory() throws Exception {
        rental = new Rental("clienteIdValido", "categoriaInvalida", "ubicacionEntregaValida");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitRentalWithInvalidLocation() throws Exception {
        rental = new Rental("clienteIdValido", "categoriaValida", "ubicacionIdInvalida");
    }

    @After
    public void tearDown() {
        mockedClientQueries.close();
        mockedCarCategoryQueries.close();
        mockedLocationQueries.close();
    }
}

