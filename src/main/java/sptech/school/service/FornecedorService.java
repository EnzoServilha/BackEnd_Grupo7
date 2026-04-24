package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.fornecedor.FornecedorRequestDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.entity.Marca;
import sptech.school.entity.Fornecedor;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.CategoriaMapper;
import sptech.school.mapper.FornecedorMapper;
import sptech.school.mapper.MarcaMapper;
import sptech.school.repository.FornecedorRepository;

import java.util.List;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    public List<FornecedorResponseDto> listar() {
        return FornecedorMapper.toResponseDtoList(repository.findAll());
    }

    public FornecedorResponseDto buscarPorId(Integer id) {
        return FornecedorMapper.toResponseDto(repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", id)));
    }

    public List<FornecedorResponseDto> buscarPorNomeContato(String nomeContato) {
        return FornecedorMapper.toResponseDtoList(repository.findByNomeContatoContaining(nomeContato)) ;
    }

    public List<FornecedorResponseDto> buscarPorNomeEmpresa(String nomeEmpresa) {
        return FornecedorMapper.toResponseDtoList(repository.findByNomeEmpresaContaining(nomeEmpresa)) ;
    }

    public List<FornecedorResponseDto> listarPorCategoria(CategoriaRequestDto categoria) {
        return FornecedorMapper.toResponseDtoList(repository.findAllByCategoria(CategoriaMapper.toEntity(categoria))) ;
    }

    public List<MarcaResponseDto> listarFabricantesPorFornecedorId(Integer fornecedorId) {
        Fornecedor fornecedor = repository.findById(fornecedorId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", fornecedorId));

        return MarcaMapper.toResponseDtoList(fornecedor.getMarcas());
    }

    public FornecedorResponseDto criar(FornecedorRequestDto fornecedor) {
        return FornecedorMapper.toResponseDto(repository.save(FornecedorMapper.toEntity(fornecedor)));
    }

    public FornecedorResponseDto atualizar(FornecedorRequestDto fornecedor, Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Fornecedor não encontrado", id);

        Fornecedor fornecedorEntity = FornecedorMapper.toEntity(fornecedor);

        fornecedorEntity.setId(id);
        return FornecedorMapper.toResponseDto(repository.save(fornecedorEntity));
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Fornecedor não encontrado", id);
        repository.deleteById(id);
    }
}
