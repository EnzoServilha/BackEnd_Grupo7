package sptech.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sptech.school.dto.periodo.PeriodoQtdPecasDTO;
import sptech.school.entity.Item;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Periodo;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.repository.ItemRepository;
import sptech.school.repository.ItensNaMovimentacaoRepository;
import sptech.school.repository.MovimentacaoEstoqueRepository;
import sptech.school.repository.PeriodoRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeriodoService {


    private final PeriodoRepository periodoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final ItensNaMovimentacaoRepository itensNaMovimentacaoRepository;
    private final ItemRepository itemRepository;


    public PeriodoService(PeriodoRepository periodoRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, ItensNaMovimentacaoRepository itensNaMovimentacaoRepository, ItemRepository itemRepository) {
        this.periodoRepository = periodoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.itensNaMovimentacaoRepository = itensNaMovimentacaoRepository;
        this.itemRepository = itemRepository;
    }

    public Periodo buscarUltimoPeriodo() {
        Periodo periodo = periodoRepository.findFirstByOrderByIdDesc();
        if (periodo == null){
            throw new EntidadeConflitanteException("Erro ao buscar o ultimo período");
        }
        return periodo;
    }

    public List<Periodo> buscarTodosPeriodos(){
        List<Periodo> periodo = periodoRepository.findAllByOrderByIdDesc();
        if (periodo == null){
            throw new EntidadeConflitanteException("Erro ao buscar o ultimo período");
        }
        return periodo;
    }

    public Periodo cadastrarPeriodo(String descricao){
        Periodo periodo = new Periodo();

        periodo.setAnotacao(descricao);

        return periodoRepository.save(periodo);
    }

    public Integer contarEstoque (Integer id){
        return periodoRepository.pegarTotalDePecasDoPeriodo(id);
    }

    public Periodo fecharEstoque (Integer id, Integer qtd){
        Periodo periodo = periodoRepository.findById(id).orElseThrow(()-> new EntidadeConflitanteException("Problema na hora de buscar o período a ser fechado"));

        periodo.setQtdPecas(qtd);

        return periodoRepository.save(periodo);
    }


    // PARTE DE ATUALIZAR O NOVO PERÍODO:

    @Transactional
    public List<ItensNaMovimentacao> transferirSaldoParaNovoPeriodo() {


        List<Periodo> todos = periodoRepository.findAllByOrderByIdDesc();
        if (todos.size() < 2) throw new RuntimeException("Não há períodos suficientes");

        List<ItensNaMovimentacao> itensNaMovimentacaoList = new ArrayList<>();


        Periodo atual = todos.get(0);
        Periodo penultimo = todos.get(1);


        //Retorna uma list, com id e qtd para cada peça do pnultimo periodo do banco
        List<PeriodoQtdPecasDTO> saldos = periodoRepository.pegarSaldoPorItemDoPeriodo(penultimo.getId());



        //Faz um unico insert na MovimentacaoEstoque, que vai ter tudo
        MovimentacaoEstoque novaMov = new MovimentacaoEstoque();
        novaMov.setPeriodo(atual);
        novaMov.setObservacoes("Saldo vindo do período anterior: " + penultimo.getAnotacao());
        MovimentacaoEstoque movSalva = movimentacaoEstoqueRepository.save(novaMov);


        for (int i = 0; i < saldos.size(); i++) {
            ItensNaMovimentacao itemMovAtual = new ItensNaMovimentacao();
            itemMovAtual.setMovimentacaoEstoque(movSalva);


            Item itemAtual = itemRepository.findById(saldos.get(i).getId())
                    .orElseThrow(()-> new RuntimeException("Item não encontrado para salvar no novo estoque"));
            itemMovAtual.setItem(itemAtual);

            itemMovAtual.setQtd(saldos.get(i).getQtd());

            itensNaMovimentacaoList.add(itensNaMovimentacaoRepository.save(itemMovAtual));

        }

        return itensNaMovimentacaoList;
    }

    }

