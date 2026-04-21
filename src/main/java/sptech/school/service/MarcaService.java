package sptech.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.entity.Marca;
import sptech.school.entity.Fornecedor;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.MarcaMapper;
import sptech.school.repository.FornecedorRepository;
import sptech.school.repository.MarcaRepository;

import java.beans.Transient;
import java.util.List;

@Service
public class MarcaService {
    private final MarcaRepository repository;
    private final FornecedorRepository fornecedorRepository;

    public MarcaService(MarcaRepository repository, FornecedorRepository fornecedorRepository) {
        this.repository = repository;
        this.fornecedorRepository = fornecedorRepository;
    }

    public MarcaResponseDto buscarPorNome(String nome) {
        return MarcaMapper.toResponseDto(repository.findByNomeEmpresaContaining(nome));
    }

    public MarcaResponseDto criar(MarcaRequestDto fabricante) {
        return MarcaMapper.toResponseDto(repository.save(MarcaMapper.toEntity(fabricante)));
    }

    @Transactional
    public MarcaResponseDto associarMarcaaFornecedor(Integer marcaId, Integer fornecedorId) {
        Marca marca = repository.findById(marcaId).orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada", marcaId));

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", fornecedorId));

        if (!marca.getFornecedores().contains(fornecedor)) {
            marca.getFornecedores().add(fornecedor);
        }

        return MarcaMapper.toResponseDto(repository.save(marca));
    }

    @Transactional
    public MarcaResponseDto desassociarMarcaaFornecedor(Integer marcaId, Integer fornecedorId) {
        Marca marca = repository.findById(marcaId).orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada", marcaId));

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", fornecedorId));

        marca.getFornecedores().remove(fornecedor);

        fornecedor.getMarcas().remove(marca);

        return MarcaMapper.toResponseDto(repository.save(marca));
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Marca não encontrada", id);
        repository.deleteById(id);
    }
}
