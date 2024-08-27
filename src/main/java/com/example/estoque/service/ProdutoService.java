package com.example.estoque.service;

import com.example.estoque.models.Produto;
import com.example.estoque.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final Validator validador;

    public ProdutoService(ProdutoRepository produtoRepository, Validator validador) {
        this.produtoRepository = produtoRepository;
        this.validador = validador;
    }

    public List<Produto> buscarTodosProdutos(){
        return produtoRepository.findAll();
    }

    @Transactional
    public ResponseEntity<String> salvarProduto(Produto produto){
        try {
            produtoRepository.save(produto);
            return ResponseEntity.ok("Produto inserido com sucesso! 😄");
        }catch (DataIntegrityViolationException dive){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Produto não inserido! ☹️ Detectamos um campo nulo na requisição");
        }
    }

    public Produto buscarProdutoPorID(Long id){
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado! ☹️"));
    }

    @Transactional
    public ResponseEntity<String> excluirProduto(Long id){
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()){
            produtoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso! 😄");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não excluido! ☹️");
    }

    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado){
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()){
            Produto produto = produtoExistente.get();
            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQntEstoque(produtoAtualizado.getQntEstoque());
            produtoRepository.save(produto);

            return ResponseEntity.ok("Produto atualizado com sucesso! 😄");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()){
            Produto produto = produtoExistente.get();
            int camposAtualizados = 0;

            if (updates.containsKey("nome")){
                produto.setNome((String) updates.get("nome"));
                camposAtualizados ++;
            }

            if (updates.containsKey("descricao")){
                produto.setDescricao((String) updates.get("descricao"));
                camposAtualizados ++;
            }

            if (updates.containsKey("preco")){
                double decPreco = Double.parseDouble(updates.get("preco").toString());
                produto.setPreco(decPreco);
                camposAtualizados ++;
            }

            if (updates.containsKey("qntEstoque")){
                produto.setQntEstoque((Integer) updates.get("qntEstoque"));
                camposAtualizados ++;
            }

            DataBinder binder = new DataBinder(produto);
            binder.setValidator(validador);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()){
                Map erros = validarProduto(result);

                return ResponseEntity.badRequest().body(erros);
            }

            produtoRepository.save(produto);

            if (camposAtualizados > 0){
                return ResponseEntity.ok(camposAtualizados + " campos atualizados com sucesso! 😄");
            }
            return ResponseEntity.ok("Chamada inútil do método! 😠");

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto com ID " + id + " não encontrado ☹️");
        }
    }

    public Map<String, String> validarProduto(BindingResult result){
        Map<String, String> erros = new HashMap<>();
        for (FieldError erro : result.getFieldErrors()) {
            // Coloque o nome do campo e a mensagem de erro no mapa
            erros.put(erro.getField(), erro.getDefaultMessage());
        }

        return erros;
    }

    public List<Produto> procurarProdutosIgnoreCase(String nome){
        return produtoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public int excluirPorQnt(int qnt){
        int qntProdutos = produtoRepository.countByQntEstoqueIsLessThanEqual(qnt);
        if (qntProdutos > 0){
            produtoRepository.deleteByQntEstoqueIsLessThanEqual(qnt);
        }
        return qntProdutos;
    }

    public int contarPorEstoque(int qnt) {
        return produtoRepository.countByQntEstoqueIsLessThanEqual(qnt);
    }
}
