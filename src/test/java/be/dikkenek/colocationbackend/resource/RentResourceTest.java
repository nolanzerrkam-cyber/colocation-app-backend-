package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.RentEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RentResourceTest {

    @Mock
    private FlatshareDao flatshareDao;

    @InjectMocks
    private RentResource rentResource;

    // --------------------- .../GET only ----------------------- //
    @Test
    void flatshare_noRents() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        Response resp = rentResource.getRent();

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void getRent_Success() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        flatshareEntity.setRent(new RentEntity());
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        Response resp = rentResource.getRent();

        // Then
        Assertions.assertEquals(200, resp.getStatus());
    }

    // ------------------ /PUT only ----------------------- //

    @Test
    void updateRent_dtoNotValid() throws BackendException {
        // Given - when
        Response resp = rentResource.updateRent(null);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void updateRent_rentNull() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RentUpdateDto dto = new RentUpdateDto();
        dto.setAmount(800f);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        Response resp = rentResource.updateRent(dto);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void updateRent_Success() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RentEntity rentEntity = new RentEntity();
        flatshareEntity.setRent(rentEntity);

        RentUpdateDto dto = new RentUpdateDto();
        dto.setAmount(1200f);
        dto.setDeadlineDay(3);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(true);

        // When
        Response resp = rentResource.updateRent(dto);

        // Then
        Assertions.assertEquals(200, resp.getStatus());
    }

    @Test
    void updateRent_Failed() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RentEntity rentEntity = new RentEntity();
        flatshareEntity.setRent(rentEntity);

        RentUpdateDto dto = new RentUpdateDto();
        dto.setAmount(1200f);
        dto.setDeadlineDay(4);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(false);

        // When
        Response resp = rentResource.updateRent(dto);

        // Then
        Assertions.assertEquals(500, resp.getStatus());
    }
}
