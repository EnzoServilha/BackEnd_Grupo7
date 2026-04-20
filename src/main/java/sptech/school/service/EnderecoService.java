package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.entity.Endereco;
import sptech.school.exception.EnderecoNaoEncontradoException;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.repository.FabricanteRepository;
import sptech.school.repository.FornecedorRepository;

import java.util.List;

@Service
public class EnderecoService {

//    @Autowired
    private EnderecoRepository enderecoRepository;
//    @Autowired
    private FabricanteRepository fabricanteRepository;
//    @Autowired
    private FornecedorRepository fornecedorRepository;
//    @Autowired
    private ClienteRepository clienteRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, FabricanteRepository fabricanteRepository, FornecedorRepository fornecedorRepository, ClienteRepository clienteRepository) {
        this.enderecoRepository = enderecoRepository;
        this.fabricanteRepository = fabricanteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.clienteRepository = clienteRepository;
    }

    // ----------------------------------------------------------------------------------------------------
    // Listar todos os endereços
    // ----------------------------------------------------------------------------------------------------
    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    public Endereco buscarPorId(Integer id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(String.valueOf(id)));
    }

    // ----------------------------------------------------------------------------------------------------
    // TODO: Cadastrar endereço
    // ----------------------------------------------------------------------------------------------------
    // Rascunos por enquanto sla
//    public Item cadastrar(Endereco, List<Integer> codigosAssociadosIds, List<Integer> itensSimilaresIds) {
//
//        return null;
//
//        return enderecoRepository.save(endereco);
//    }
//
//    public Item cadastrar(Item item, List<Integer> codigosAssociadosIds, List<Integer> itensSimilaresIds) {
//        item.setDataCadastro(LocalDateTime.now());
//
//        if (codigosAssociadosIds != null && !codigosAssociadosIds.isEmpty()) {
//            List<CodigoAssociado> codigos = codigoAssociadoRepository.findAllById(codigosAssociadosIds);
//            item.setCodigosAssociados(codigos);
//        }
//
//        if (itensSimilaresIds != null && !itensSimilaresIds.isEmpty()) {
//            List<Item> similares = itemRepository.findAllById(itensSimilaresIds);
//            item.setItensSimilares(similares);
//        }
//
//        return itemRepository.save(item);
//    }
//
//
//    public Curso criar(Curso curso, Integer professorId) {
//
////        Optional<Professor> professorOptional =
////                professorRepository.findById(professorId);
////
////        if (professorOptional.isEmpty()){
////            throw new ProfessorNaoEncontradoException(professorId);
////        }
//
//        Professor professorEntidade = professorRepository.findById(professorId)
//                .orElseThrow(() -> new ProfessorNaoEncontradoException(professorId));
//
////        Professor professorEntidade = professorOptional.get();
//
//        curso.setProfessor(professorEntidade);
//
//        return cursoRepository.save(curso);
//    }


    // ----------------------------------------------------------------------------------------------------
    // TODO: Atualizar endereço por ID
    // ----------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------
    // Deletar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    public void deletar(Integer id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EnderecoNaoEncontradoException(String.valueOf(id));
        }
        enderecoRepository.deleteById(id);
    }


    // mais métodos provavelmente vão ser necessários
}