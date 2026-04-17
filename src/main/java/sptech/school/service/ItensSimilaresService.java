package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.entity.Item;
import sptech.school.repository.ItemRepository;

import java.util.Collections;
import java.util.List;

@Service
public class ItensSimilaresService {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public ItensSimilaresService(ItemRepository itemRepository, ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    public List<Item> listarSimilares(Integer itemId) {
        Item item = itemService.buscarPorId(itemId);
        return item.getItensSimilares() != null ? item.getItensSimilares() : Collections.emptyList();
    }

    public Item adicionarSimilar(Integer itemId, Integer similarId) {
        return itemService.adicionarItemSimilar(itemId, similarId);
    }

    public Item removerSimilar(Integer itemId, Integer similarId) {
        return itemService.removerItemSimilar(itemId, similarId);
    }
}
