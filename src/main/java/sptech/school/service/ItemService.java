package sptech.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Item;
import sptech.school.repository.CodigoAssociadoRepository;
import sptech.school.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CodigoAssociadoRepository codigoAssociadoRepository;

    public ItemService(ItemRepository itemRepository, CodigoAssociadoRepository codigoAssociadoRepository) {
        this.itemRepository = itemRepository;
        this.codigoAssociadoRepository = codigoAssociadoRepository;
    }

    public List<Item> listarTodos() {
        return itemRepository.findAll();
    }

    public Item buscarPorId(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
    }

    public Item buscarPorCodigoInterno(String codigoInterno) {
        return itemRepository.findByCodigoInterno(codigoInterno)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
    }

    public List<Item> listarPorMarca(String marca) {
        return itemRepository.findByMarcaContainingIgnoreCase(marca);
    }

    public List<Item> pesquisar(String termo) {
        return itemRepository.pesquisarPorTermo(termo);
    }

    public List<Item> buscarPorCodigoAssociado(String codigo) {
        return itemRepository.buscarPorCodigoAssociado(codigo);
    }

    public Item cadastrar(Item item) {
        item.setDataCadastro(LocalDateTime.now());
        return itemRepository.save(item);
    }

    public Item atualizar(Integer id, Item itemAtualizado) {
        Item item = buscarPorId(id);
        item.setCodigoInterno(itemAtualizado.getCodigoInterno());
        item.setMarca(itemAtualizado.getMarca());
        item.setAno(itemAtualizado.getAno());
        item.setDescricao(itemAtualizado.getDescricao());
        item.setLocalizacao(itemAtualizado.getLocalizacao());
        return itemRepository.save(item);
    }

    public void deletar(Integer id) {
        if (!itemRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado");
        }
        itemRepository.deleteById(id);
    }

    public Item adicionarCodigoAssociado(Integer itemId, Integer codigoAssociadoId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
        CodigoAssociado codigo = codigoAssociadoRepository.findById(codigoAssociadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código associado não encontrado"));
        if (item.getCodigosAssociados() == null) {
            item.setCodigosAssociados(new ArrayList<>());
        }
        if (item.getCodigosAssociados().stream().noneMatch(c -> c.getId().equals(codigoAssociadoId))) {
            item.getCodigosAssociados().add(codigo);
        }
        return itemRepository.save(item);
    }

    public Item removerCodigoAssociado(Integer itemId, Integer codigoAssociadoId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
        if (item.getCodigosAssociados() != null) {
            item.getCodigosAssociados().removeIf(c -> c.getId().equals(codigoAssociadoId));
        }
        return itemRepository.save(item);
    }
}
