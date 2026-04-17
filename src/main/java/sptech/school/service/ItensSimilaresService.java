package sptech.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.entity.Item;
import sptech.school.repository.ItemRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ItensSimilaresService {

    private final ItemRepository itemRepository;

    public ItensSimilaresService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> listarSimilares(Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
        return item.getItensSimilares() != null ? item.getItensSimilares() : Collections.emptyList();
    }

    public Item adicionarSimilar(Integer itemId, Integer similarId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
        Item similar = itemRepository.findById(similarId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item similar não encontrado"));
        if (item.getItensSimilares() == null) {
            item.setItensSimilares(new ArrayList<>());
        }
        if (item.getItensSimilares().stream().noneMatch(s -> s.getId().equals(similarId))) {
            item.getItensSimilares().add(similar);
        }
        return itemRepository.save(item);
    }

    public Item removerSimilar(Integer itemId, Integer similarId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
        if (item.getItensSimilares() != null) {
            item.getItensSimilares().removeIf(s -> s.getId().equals(similarId));
        }
        return itemRepository.save(item);
    }
}
