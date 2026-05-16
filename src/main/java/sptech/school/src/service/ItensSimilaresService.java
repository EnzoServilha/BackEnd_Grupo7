package sptech.school.src.service;

import org.springframework.stereotype.Service;
import sptech.school.src.entity.Item;
import sptech.school.src.exception.EntidadeNaoEncontradaException;
import sptech.school.src.repository.ItemRepository;

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
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item", itemId));
        return item.getItensSimilares() != null ? item.getItensSimilares() : Collections.emptyList();
    }

    public Item adicionarSimilar(Integer itemId, Integer similarId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item", itemId));
        Item similar = itemRepository.findById(similarId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item Similar", similarId));
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
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item", itemId));
        if (item.getItensSimilares() != null) {
            item.getItensSimilares().removeIf(s -> s.getId().equals(similarId));
        }
        return itemRepository.save(item);
    }
}
