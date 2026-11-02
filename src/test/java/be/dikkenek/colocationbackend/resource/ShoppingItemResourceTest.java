package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ShoppingItemDao;
import be.dikkenek.colocationbackend.dto.ShoppingItemGetDto;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ShoppingItemResourceTest {
    @Mock
    private ShoppingItemDao itemDao;
    @InjectMocks
    private ShoppingItemResource shoppingItemResource;

    private ShoppingItemEntity mockItem(int id, String libelle, int quantity, RoommateEntity roommate) {
        ShoppingItemEntity item = Mockito.mock(ShoppingItemEntity.class);
        Mockito.when(item.getId()).thenReturn(id);
        Mockito.when(item.getLibelle()).thenReturn(libelle);
        Mockito.when(item.getQuantity()).thenReturn(quantity);
        Mockito.when(item.getRoommate()).thenReturn(roommate);
        return item;
    }

    @Test
    void testGetAll_Success() throws BackendException {
        // Given
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com","password","firstname","lastname", "0404040404");
        ShoppingItemEntity item = mockItem(1, "Lait", 2, roommateEntity);
        Mockito.when(itemDao.getAll()).thenReturn(Collections.singletonList(item));

        // When
        List<ShoppingItemGetDto> result = shoppingItemResource.getAll();

        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("Lait", result.get(0).getLibelle());
        Assertions.assertEquals(2, result.get(0).getQuantity());
        Assertions.assertEquals("email@email.com", result.get(0).getRoommateId());
    }

    @Test
    void testGetAll_multiple_items() throws BackendException {
        // Given
        RoommateEntity roommate1 = new RoommateEntity("email1@email.com","password","firstname","lastname", "0404040404");
        RoommateEntity roommate2 = new RoommateEntity("email2@email.com","password","firstname","lastname", "0404040404");
        ShoppingItemEntity item1 = mockItem(1, "Lait", 2, roommate1);
        ShoppingItemEntity item2 = mockItem(2, "Pain", 1, roommate2);
        Mockito.when(itemDao.getAll()).thenReturn(Arrays.asList(item1, item2));

        // When
        List<ShoppingItemGetDto> result = shoppingItemResource.getAll();

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("email1@email.com", result.get(0).getRoommateId());
        Assertions.assertEquals("email2@email.com", result.get(1).getRoommateId());
    }

    @Test
    void testGetAll_empty() throws BackendException {
        // Given
        Mockito.when(itemDao.getAll()).thenReturn(Collections.emptyList());

        // When
        List<ShoppingItemGetDto> result = shoppingItemResource.getAll();

        // Then
        Assertions.assertTrue(result.isEmpty());
    }
}
