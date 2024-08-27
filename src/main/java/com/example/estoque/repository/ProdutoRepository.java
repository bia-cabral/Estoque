package com.example.estoque.repository;

import com.example.estoque.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
//    Long deleteProdutoById(Long id);

    @Modifying
    @Query ("DELETE FROM Produto prod WHERE :id = prod.id")
    void deleteById(Long id);
    List<Produto> findByNomeLikeIgnoreCase(String nome);
    int countByQntEstoqueIsLessThanEqual(int qnt);
    int deleteByQntEstoqueIsLessThanEqual(int qnt);
}
