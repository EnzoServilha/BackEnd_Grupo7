package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.entity.Endereco;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.repository.FabricanteRepository;
import sptech.school.repository.FornecedorRepository;

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
}