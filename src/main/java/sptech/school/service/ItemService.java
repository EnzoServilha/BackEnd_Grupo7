package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Item;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.CodigoAssociadoRepository;
import sptech.school.repository.ItemRepository;
import sptech.school.util.BuscaSanitizer;

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
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item", id));
    }

    public Item buscarPorCodigoInterno(String codigoInterno) {
        return itemRepository.findByCodigoInterno(codigoInterno)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item", codigoInterno));
    }

    public List<Item> listarPorMarca(String marca) {
        return itemRepository.findByMarcaContainingIgnoreCase(BuscaSanitizer.escaparLike(marca));
    }

    public List<Item> pesquisar(String termo) {
        return itemRepository.pesquisarPorTermo(BuscaSanitizer.escaparLike(termo));
    }

    public List<Item> buscarPorCodigoAssociado(String codigo) {
        return itemRepository.buscarPorCodigoAssociado(BuscaSanitizer.escaparLike(codigo));
    }

    public Item cadastrar(Item item, List<Integer> codigosAssociadosIds, List<Integer> itensSimilaresIds) {
        item.setDataCadastro(LocalDateTime.now());

        if (codigosAssociadosIds != null && !codigosAssociadosIds.isEmpty()) {
            List<CodigoAssociado> codigos = codigoAssociadoRepository.findAllById(codigosAssociadosIds);
            item.setCodigosAssociados(codigos);
        }

        if (itensSimilaresIds != null && !itensSimilaresIds.isEmpty()) {
            List<Item> similares = itemRepository.findAllById(itensSimilaresIds);
            item.setItensSimilares(similares);
        }

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
            throw new EntidadeNaoEncontradaException("Item", id);
        }
        itemRepository.deleteById(id);
    }

    public Item adicionarCodigoAssociado(Integer itemId, Integer codigoAssociadoId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item", itemId));
        CodigoAssociado codigo = codigoAssociadoRepository.findById(codigoAssociadoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Código Associado", codigoAssociadoId));
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
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item", itemId));
        if (item.getCodigosAssociados() != null) {
            item.getCodigosAssociados().removeIf(c -> c.getId().equals(codigoAssociadoId));
        }
        return itemRepository.save(item);
    }
}
