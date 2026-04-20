package sptech.school.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sptech.school.dto.endereco.EnderecoRequestDto;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Endereco;
import sptech.school.entity.Item;
import sptech.school.exception.EnderecoNaoEncontradoException;
import sptech.school.mapper.EnderecoMapper;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.repository.FabricanteRepository;
import sptech.school.repository.FornecedorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private FabricanteRepository fabricanteRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, FabricanteRepository fabricanteRepository, FornecedorRepository fornecedorRepository, ClienteRepository clienteRepository) {
        this.enderecoRepository = enderecoRepository;
        this.fabricanteRepository = fabricanteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Endereco buscarPorId(Integer id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(String.valueOf(id)));
    }

    // TODO: cadastrar
    // TODO: atualizar

//    public Item cadastrar(Endereco, List<Integer> codigosAssociadosIds, List<Integer> itensSimilaresIds) {
//        // TODO: fazer  aqui
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


    public void deletar(Integer id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EnderecoNaoEncontradoException(String.valueOf(id));
        }
        enderecoRepository.deleteById(id);
    }
}